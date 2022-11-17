//package ar.edu.itba.paw.webapp.controllerOld;
//
//import ar.edu.itba.paw.interfaces.services.*;
//import ar.edu.itba.paw.model.enums.DebateCategory;
//import ar.edu.itba.paw.model.enums.DebateOrder;
//import ar.edu.itba.paw.model.enums.DebateStatus;
//import ar.edu.itba.paw.model.enums.DebateVote;
//import ar.edu.itba.paw.model.exceptions.CategoryNotFoundException;
//import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
//import ar.edu.itba.paw.model.exceptions.InvalidPageException;
//import ar.edu.itba.paw.webapp.form.ArgumentForm;
//import ar.edu.itba.paw.webapp.form.ChatForm;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//
//// TODO: Delete
//@Deprecated
//@Controller
//@RequestMapping("/debates")
//public class DebateController {
//
//    class ModelAndView {
//        public ModelAndView(String hola) {
//        }
//
//        public void addObject(String string, Object object) {
//
//        }
//    }
//
//    class RedirectAttributes {
//        public RedirectAttributes() {
//        }
//
//        public void addFlashAttribute(String string, Object object) {
//
//        }
//    }
//
//    private final DebateService debateService;
//    private final SubscribedService subscribedService;
//    private final VoteService voteService;
//    private final ArgumentService argumentService;
//    private final LikeService likeService;
//    private final ChatService chatService;
//    private static final Logger LOGGER = LoggerFactory.getLogger(DebateController.class);
//
//    @Autowired
//    public DebateController(DebateService debateService, ArgumentService argumentService, LikeService likeService, SubscribedService subscribedService, VoteService voteService, ChatService chatService) {
//        this.debateService = debateService;
//        this.argumentService = argumentService;
//        this.likeService = likeService;
//        this.subscribedService = subscribedService;
//        this.voteService = voteService;
//        this.chatService = chatService;
//    }
//
//    @RequestMapping(method = { RequestMethod.GET, RequestMethod.HEAD })
//    public ModelAndView debatesList(
//            @RequestParam(value = "category", required = false) String category,
//            @RequestParam(value = "search", required = false) String search,
//            @RequestParam(value = "page", defaultValue = "0") String page,
//            @RequestParam(value = "order", required = false) String order,
//            @RequestParam(value = "status", required = false) String status,
//            @RequestParam(value = "date", required = false) String date
//    ) {
//        if (!page.matches("-?\\d+")) {
//            LOGGER.error("/debates : Invalid page number {}", page);
//            throw new InvalidPageException();
//        }
//        String auxOrder = order;
//        if (order != null && Arrays.stream(DebateOrder.values()).noneMatch((o) -> o.getName().equals(auxOrder))) order = null;
//        if (status != null && !status.equals("open") && !status.equals("closed")) status = null;
//        if (category != null && Arrays.stream(DebateCategory.values()).noneMatch((c) -> c.getName().equals(category))) {
//            LOGGER.error("/debates : Invalid category {}", category);
//            throw new CategoryNotFoundException();
//        }
//        if (date != null && !date.matches("\\d{2}-\\d{2}-\\d{4}")) date = null;
//
//        final ModelAndView mav = new ModelAndView("pages/debates-list");
//        mav.addObject("categories", DebateCategory.values());
//        mav.addObject("orders", DebateOrder.values());
//
//        DebateCategory finalCategory = category == null ? null : DebateCategory.valueOf(category.toUpperCase());
//        DebateOrder finalOrder = order == null ? null : DebateOrder.valueOf(order.toUpperCase());
//        DebateStatus finalStatus = status == null ? null : DebateStatus.valueOf(status.toUpperCase());
//        LocalDate finalDate = date == null ? null : LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//
//        mav.addObject("total_pages", debateService.getPages(search, finalCategory, finalStatus, finalDate));
//        mav.addObject("debates", debateService.get(Integer.parseInt(page), search, finalCategory, finalOrder, finalStatus, finalDate));
//        return mav;
//    }
//
//    @ModelAttribute("chatForm")
//    public ChatForm chatForm() {
//        return new ChatForm();
//    }
//
//    @ModelAttribute("argumentForm")
//    public ArgumentForm argumentForm() {
//        return new ArgumentForm();
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}", method = { RequestMethod.GET, RequestMethod.HEAD })
//    public ModelAndView debate(
//            @PathVariable("debateId") final String debateId,
//            @RequestParam(value = "page", defaultValue = "0") String page,
//            Authentication auth,
//            @RequestParam(value = "chatPage", defaultValue = "0") String chatPage
//    ) {
//
//        if (!page.matches("-?\\d+") || !chatPage.matches("-?\\d+")) {
//            LOGGER.error("/debates/{debateId} : Invalid page number {}", page);
//            throw new InvalidPageException();
//        }
//        long debateIdNum = Long.parseLong(debateId);
//        int pageNum = Integer.parseInt(page);
//
//        final ModelAndView mav = new ModelAndView("pages/debate");
//        mav.addObject("debate", debateService.getDebateById(debateIdNum).orElseThrow(() -> {
//            LOGGER.error("/debates/{debateId} : Debate not found {}", debateId);
//            return new DebateNotFoundException();
//        }));
//
//        String username = null;
//        if(auth != null && auth.getPrincipal() != null) {
//            username = auth.getName();
//            mav.addObject("isSubscribed", subscribedService.isUserSubscribed(auth.getName(), debateIdNum));
//            voteService.getVote(debateIdNum, auth.getName()).ifPresent(v -> mav.addObject("userVote", v.getVote()));
//            argumentService.getLastArgument(debateIdNum).ifPresent(lastArgument -> mav.addObject("lastArgument", lastArgument));
//            mav.addObject("recommendedDebates", debateService.getRecommendedDebates(debateIdNum, username));
//        } else {
//            mav.addObject("recommendedDebates", debateService.getRecommendedDebates(debateIdNum));
//        }
//        mav.addObject("arguments", argumentService.getArgumentsByDebate(debateIdNum, username, pageNum));
//        mav.addObject("total_pages", argumentService.getArgumentByDebatePageCount(debateIdNum));
//        mav.addObject("chats", chatService.getDebateChat(debateIdNum, Integer.parseInt(chatPage)));
//        mav.addObject("total_chat_pages", chatService.getDebateChatPageCount(debateIdNum));
//
//        return mav;
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/close", method = { RequestMethod.POST })
//    public ModelAndView closeDebate(@PathVariable("debateId") final String debateId, Authentication auth) {
//        debateService.startConclusion(Long.parseLong(debateId), auth.getName());
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/argument", method = { RequestMethod.POST })
//    public ModelAndView createArgument(
//            @PathVariable("debateId") final String debateId,
//            @Valid @ModelAttribute("argumentForm") final ArgumentForm argumentForm,
//            BindingResult errors,
//            RedirectAttributes redirectAttributes,
//            Authentication auth
//    ) throws IOException {
//
//        if (errors.hasErrors()) {
//            LOGGER.warn("Create argument form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.argumentForm", errors);
//            redirectAttributes.addFlashAttribute("argumentForm", argumentForm);
//            return new ModelAndView("redirect:/debates/" + debateId);
//        }
//
//        argumentService.create(auth.getName(), Long.parseLong(debateId), argumentForm.getContent(), argumentForm.getFile().getBytes());
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/vote/for", method = {RequestMethod.POST})
//    public ModelAndView voteFor(@PathVariable("debateId") final String debateId, Authentication auth) {
//        voteService.addVote(Long.parseLong(debateId), auth.getName(), DebateVote.FOR);
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/vote/against", method = {RequestMethod.POST})
//    public ModelAndView voteAgainst(@PathVariable("debateId") final String debateId, Authentication auth) {
//        voteService.addVote(Long.parseLong(debateId), auth.getName(), DebateVote.AGAINST);
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/unvote", method = {RequestMethod.POST, RequestMethod.DELETE})
//    public ModelAndView unvote(@PathVariable("debateId") final String debateId, Authentication auth) {
//        voteService.removeVote(Long.parseLong(debateId), auth.getName());
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/subscribe", method = { RequestMethod.POST })
//    public ModelAndView subscribe(@PathVariable("debateId") final String debateId, Authentication auth) {
//        subscribedService.subscribeToDebate(auth.getName(), Long.parseLong(debateId));
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/unsubscribe", method = { RequestMethod.POST, RequestMethod.DELETE })
//    public ModelAndView unsubscribe(@PathVariable("debateId") final String debateId, Authentication auth) {
//        subscribedService.unsubscribeToDebate(auth.getName(), Long.parseLong(debateId));
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/like/{argumentId:\\d+}", method = { RequestMethod.POST })
//    public ModelAndView like(@PathVariable("argumentId") final String argumentId, @PathVariable("debateId") final String debateId ,Authentication auth) {
//        likeService.likeArgument(Long.parseLong(argumentId), auth.getName());
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/unlike/{argumentId:\\d+}", method = { RequestMethod.POST, RequestMethod.DELETE })
//    public ModelAndView unlike(@PathVariable("argumentId") final String argumentId, @PathVariable("debateId") final String debateId , Authentication auth) {
//        likeService.unlikeArgument(Long.parseLong(argumentId), auth.getName());
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/chat", method = { RequestMethod.POST })
//    public ModelAndView chat(
//            @PathVariable("debateId") final String debateId,
//            Authentication auth,
//            @Valid @ModelAttribute("chatForm") ChatForm chatForm,
//            BindingResult errors,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (errors.hasErrors()) {
//            LOGGER.warn("Create chat form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.chatForm", errors);
//            redirectAttributes.addFlashAttribute("chatForm", chatForm);
//            return new ModelAndView("redirect:/debates/" + debateId);
//        }
//
//        chatService.create(auth.getName(), Long.parseLong(debateId), chatForm.getMessage());
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/delete", method = {RequestMethod.POST, RequestMethod.DELETE})
//    public ModelAndView deleteDebate(@PathVariable("debateId") final String debateId, Authentication auth) {
//        debateService.deleteDebate(Long.parseLong(debateId), auth.getName());
//        return new ModelAndView("redirect:/debates");
//    }
//
//    @RequestMapping(value = "/{debateId:\\d+}/delete/{argumentId:\\d+}", method = {RequestMethod.POST, RequestMethod.DELETE})
//    public ModelAndView deleteArgument(@PathVariable("argumentId") final String argumentId, @PathVariable("debateId") final String debateId, Authentication auth) {
//        argumentService.deleteArgument(Long.parseLong(argumentId), auth.getName());
//        return new ModelAndView("redirect:/debates/" + debateId);
//    }
//}
