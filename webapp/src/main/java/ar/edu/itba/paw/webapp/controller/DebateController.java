package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.webapp.form.PostForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/debates")
public class DebateController {

    private final DebateService debateService;
    private final PostService postService;

    @Autowired
    public DebateController(DebateService debateService, PostService postService) {
        this.debateService = debateService;
        this.postService = postService;
    }

    @RequestMapping(method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debatesList(@RequestParam(value = "category", required = false) String category,
                                    @RequestParam(value = "search", required = false) String search,
                                    @RequestParam(value = "page", defaultValue = "0") String page,
                                    @RequestParam(value = "order", required = false) String order,
                                    @RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "date", required = false) String date) {

        if (!page.matches("-?\\d+")) throw new InvalidPageException();
        String finalOrder = order;
        if (order != null && Arrays.stream(DebateOrder.values()).noneMatch((o) -> o.getName().equals(finalOrder))) order = null;
        if (status != null && !status.equals("open") && !status.equals("closed")) status = null;
        if (category != null && Arrays.stream(DebateCategory.values()).noneMatch((c) -> c.getName().equals(category)))
            throw new CategoryNotFoundException();
        if (date != null && !date.matches("\\d{2}-\\d{2}-\\d{4}")) date = null;

        final ModelAndView mav = new ModelAndView("pages/debates-list");
        mav.addObject("categories", DebateCategory.values());
        mav.addObject("orders", DebateOrder.values());
        mav.addObject("total_pages", debateService.getPages(search, category, status, date));
        mav.addObject("debates", debateService.get(Integer.parseInt(page), search, category, order, status, date));
        return mav;
    }

    @RequestMapping(value = "/{debateId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debate(@PathVariable("debateId") final String debateId, @ModelAttribute("postForm") final PostForm form,
                               @RequestParam(value = "page", defaultValue = "0") String page, Authentication auth) {

        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (!page.matches("-?\\d+")) throw new InvalidPageException();
        long debateIdNum = Long.parseLong(debateId);
        int pageNum = Integer.parseInt(page);

        final ModelAndView mav = new ModelAndView("pages/debate");
        mav.addObject("debate", debateService.getPublicDebateById(debateIdNum).orElseThrow(DebateNotFoundException::new));
        mav.addObject("total_pages", postService.getPostsByDebatePageCount(debateIdNum));

        if(auth != null && auth.getPrincipal() != null) {
            mav.addObject("isSubscribed", debateService.isUserSubscribed(auth.getName(), debateIdNum));
            mav.addObject("posts", postService.getPublicPostsByDebateWithIsLiked(debateIdNum, auth.getName(), pageNum));
            mav.addObject("userVote", debateService.getUserVote(debateIdNum, auth.getName()));
            postService.getLastArgument(debateIdNum).ifPresent(lastArgument -> mav.addObject("lastArgument", lastArgument));
        } else {
            mav.addObject("posts", postService.getPublicPostsByDebate(debateIdNum, pageNum));
        }

        return mav;
    }

    @RequestMapping(value = "/{debateId}/close", method = { RequestMethod.POST })
    public ModelAndView closeDebate(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.startConclusion(Long.parseLong(debateId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/argument", method = { RequestMethod.POST })
    public ModelAndView createPost(@PathVariable("debateId") final String debateId,
                                   @Valid @ModelAttribute("postForm") final PostForm form, BindingResult errors, Authentication auth) throws IOException {

        if (errors.hasErrors()) {
            return debate(debateId, form, "0", auth);
        }
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();

        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        postService.create(auth.getName(), Long.parseLong(debateId), form.getContent(), form.getFile().getBytes());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/vote/for", method = {RequestMethod.POST})
    public ModelAndView voteFor(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.addVote(Long.parseLong(debateId), auth.getName(), DebateVote.FOR);
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/vote/against", method = {RequestMethod.POST})
    public ModelAndView voteAgainst(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.addVote(Long.parseLong(debateId), auth.getName(), DebateVote.AGAINST);
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/unvote", method = {RequestMethod.POST})
    public ModelAndView unvoteAgainst(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.removeVote(Long.parseLong(debateId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }


    @RequestMapping(value = "/{debateId}/subscribe", method = { RequestMethod.POST })
    public ModelAndView subscribe(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.subscribeToDebate(auth.getName(), Long.parseLong(debateId));
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/unsubscribe", method = { RequestMethod.POST, RequestMethod.DELETE })
    public ModelAndView unsubscribe(@PathVariable("debateId") final String debateId, Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.unsubscribeToDebate(auth.getName(), Long.parseLong(debateId));
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/like/{postId}", method = { RequestMethod.POST })
    public ModelAndView like(@PathVariable("postId") final String postId, @PathVariable("debateId") final String debateId ,Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (!postId.matches("\\d+")) throw new PostNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        postService.likePost(Long.parseLong(postId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/{debateId}/unlike/{postId}", method = { RequestMethod.POST, RequestMethod.DELETE })
    public ModelAndView unlike(@PathVariable("postId") final String postId, @PathVariable("debateId") final String debateId ,Authentication auth) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (!postId.matches("\\d+")) throw new PostNotFoundException();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        postService.unlikePost(Long.parseLong(postId), auth.getName());
        return new ModelAndView("redirect:/debates/" + debateId);
    }
}
