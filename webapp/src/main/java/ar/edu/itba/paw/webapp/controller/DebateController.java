package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.LikeService;
import ar.edu.itba.paw.interfaces.services.ArgumentService;
import ar.edu.itba.paw.interfaces.services.SubscribedService;
import ar.edu.itba.paw.interfaces.services.VoteService;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.webapp.form.ArgumentForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Controller
@RequestMapping("/debates")
public class DebateController {

    private final DebateService debateService;
    private final SubscribedService subscribedService;
    private final VoteService voteService;
    private final ArgumentService argumentService;
    private final LikeService likeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DebateController.class);

    @Autowired
    public DebateController(DebateService debateService, ArgumentService argumentService, LikeService likeService, SubscribedService subscribedService, VoteService voteService) {
        this.debateService = debateService;
        this.argumentService = argumentService;
        this.likeService = likeService;
        this.subscribedService = subscribedService;
        this.voteService = voteService;
    }

    @RequestMapping(method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debatesList(@RequestParam(value = "category", required = false) String category,
                                    @RequestParam(value = "search", required = false) String search,
                                    @RequestParam(value = "page", defaultValue = "0") String page,
                                    @RequestParam(value = "order", required = false) String order,
                                    @RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "date", required = false) String date) {

        if (!page.matches("-?\\d+")) {
            LOGGER.error("/debates : Invalid page number {}", page);
            throw new InvalidPageException();
        }
        String auxOrder = order;
        if (order != null && Arrays.stream(DebateOrder.values()).noneMatch((o) -> o.getName().equals(auxOrder))) order = null;
        if (status != null && !status.equals("open") && !status.equals("closed")) status = null;
        if (category != null && Arrays.stream(DebateCategory.values()).noneMatch((c) -> c.getName().equals(category))) {
            LOGGER.error("/debates : Invalid category {}", category);
            throw new CategoryNotFoundException();
        }
        if (date != null && !date.matches("\\d{2}-\\d{2}-\\d{4}")) date = null;

        final ModelAndView mav = new ModelAndView("pages/debates-list");
        mav.addObject("categories", DebateCategory.values());
        mav.addObject("orders", DebateOrder.values());

        DebateCategory finalCategory = category == null ? null : DebateCategory.valueOf(category.toUpperCase());
        DebateOrder finalOrder = order == null ? null : DebateOrder.valueOf(order.toUpperCase());
        DebateStatus finalStatus = status == null ? null : DebateStatus.valueOf(status.toUpperCase());
        LocalDate finalDate = date == null ? null : LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        mav.addObject("total_pages", debateService.getPages(search, finalCategory, finalStatus, finalDate));
        mav.addObject("debates", debateService.get(Integer.parseInt(page), search, finalCategory, finalOrder, finalStatus, finalDate));
        return mav;
    }

    @RequestMapping(value = "/{debateId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debate(@PathVariable("debateId") final String debateId, @ModelAttribute("argumentForm") final ArgumentForm form,
                               @RequestParam(value = "page", defaultValue = "0") String page, Authentication auth) {

        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId} : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (!page.matches("-?\\d+")) {
            LOGGER.error("/debates/{debateId} : Invalid page number {}", page);
            throw new InvalidPageException();
        }
        long debateIdNum = Long.parseLong(debateId);
        int pageNum = Integer.parseInt(page);

        final ModelAndView mav = new ModelAndView("pages/debate");
        mav.addObject("debate", debateService.getDebateById(debateIdNum).orElseThrow(() -> {
            LOGGER.error("/debates/{debateId} : Debate not found {}", debateId);
            return new DebateNotFoundException();
        }));

        String username = null;
        if(auth != null && auth.getPrincipal() != null) {
            username = auth.getName();
            mav.addObject("isSubscribed", subscribedService.isUserSubscribed(auth.getName(), debateIdNum));
            voteService.getVote(debateIdNum, auth.getName()).ifPresent(v -> mav.addObject("userVote", v.getVote()));
            argumentService.getLastArgument(debateIdNum).ifPresent(lastArgument -> mav.addObject("lastArgument", lastArgument));
        }
        mav.addObject("arguments", argumentService.getArgumentsByDebate(debateIdNum, username, pageNum));
        mav.addObject("total_pages", argumentService.getArgumentByDebatePageCount(debateIdNum));

        return mav;
    }

    @RequestMapping(value = "/{debateId}/close", method = { RequestMethod.POST })
    public ModelAndView closeDebate(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/close : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/close : User not logged in");
            throw new UnauthorizedUserException();
        }

        debateService.startConclusion(Long.parseLong(debateId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}", method = { RequestMethod.POST })
    public ModelAndView createArgument(@PathVariable("debateId") final String debateId,
                                   @Valid @ModelAttribute("argumentForm") final ArgumentForm form, BindingResult errors, Authentication auth) throws IOException {

        if (errors.hasErrors()) {
            LOGGER.warn("Create argument form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return debate(debateId, form, "0", auth);
        }
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId} : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }

        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId} : User not logged in");
            throw new UnauthorizedUserException();
        }

        argumentService.create(auth.getName(), Long.parseLong(debateId), form.getContent(), form.getFile().getBytes());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/vote/for", method = {RequestMethod.POST})
    public ModelAndView voteFor(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/vote/for : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/vote/for : User not logged in");
            throw new UnauthorizedUserException();
        }

        voteService.addVote(Long.parseLong(debateId), auth.getName(), DebateVote.FOR);
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/vote/against", method = {RequestMethod.POST})
    public ModelAndView voteAgainst(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/vote/against : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/vote/against : User not logged in");
            throw new UnauthorizedUserException();
        }

        voteService.addVote(Long.parseLong(debateId), auth.getName(), DebateVote.AGAINST);
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/unvote", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ModelAndView unvote(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/unvote : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/unvote : User not logged in");
            throw new UnauthorizedUserException();
        }

        voteService.removeVote(Long.parseLong(debateId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }


    @RequestMapping(value = "/{debateId}/subscribe", method = { RequestMethod.POST })
    public ModelAndView subscribe(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/subscribe : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/subscribe : User not logged in");
            throw new UnauthorizedUserException();
        }

        subscribedService.subscribeToDebate(auth.getName(), Long.parseLong(debateId));
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/unsubscribe", method = { RequestMethod.POST, RequestMethod.DELETE })
    public ModelAndView unsubscribe(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/unsubscribe : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/unsubscribe : User not logged in");
            throw new UnauthorizedUserException();
        }

        subscribedService.unsubscribeToDebate(auth.getName(), Long.parseLong(debateId));
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/like/{argumentId}", method = { RequestMethod.POST })
    public ModelAndView like(@PathVariable("argumentId") final String argumentId, @PathVariable("debateId") final String debateId ,Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/like/{argumentId} : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (!argumentId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/like/{argumentId} : ArgumentId {} not a valid id number", argumentId);
            throw new ArgumentNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/like/{argumentId} : User not logged in");
            throw new UnauthorizedUserException();
        }

        likeService.likeArgument(Long.parseLong(argumentId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/unlike/{argumentId}", method = { RequestMethod.POST, RequestMethod.DELETE })
    public ModelAndView unlike(@PathVariable("argumentId") final String argumentId, @PathVariable("debateId") final String debateId , Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/unlike/{argumentId} : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (!argumentId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/unlike/{argumentId} : ArgumentId {} not a valid id number", argumentId);
            throw new ArgumentNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/unlike/{argumentId} : User not logged in");
            throw new UnauthorizedUserException();
        }

        likeService.unlikeArgument(Long.parseLong(argumentId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/delete", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ModelAndView deleteDebate(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.deleteDebate(Long.parseLong(debateId), auth.getName());
        return new ModelAndView("redirect:/debates");
    }

    @RequestMapping(value = "/{debateId}/delete/{postId}", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ModelAndView deleteArgument(@PathVariable("postId") final String argumentId, @PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/delete/{argumentId} : DebateId {} not a valid id number", debateId);
            throw new DebateNotFoundException();
        }
        if (!argumentId.matches("\\d+")) {
            LOGGER.error("/debates/{debateId}/delete/{argumentId} : ArgumentId {} not a valid id number", argumentId);
            throw new ArgumentNotFoundException();
        }
        if (auth == null || auth.getPrincipal() == null) {
            LOGGER.error("/debates/{debateId}/delete/{argumentId} : User not logged in");
            throw new UnauthorizedUserException();
        }
        argumentService.deleteArgument(Long.parseLong(argumentId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }
}
