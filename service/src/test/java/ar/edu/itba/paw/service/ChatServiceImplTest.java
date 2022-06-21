package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ChatDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenChatException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatServiceImplTest {

    private final static String MESSAGE = "message";
    private final static long DEBATE_ID = 1;
    private final static String CHAT_USERNAME = "chatUsername";

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";

    private final static String USER_USERNAME_2 = "username2";
    private final static String USER_PASSWORD_2 = "password";
    private final static String USER_EMAIL_2 = "test2@test.com";

    private final static String DEBATE_NAME = "Debate Name Test";
    private final static String DEBATE_DESCRIPTION = "Debate Description Test";

    private User user;
    private User user2;
    private Debate debate;

    private final static int VALID_PAGE = 0;
    private final static int INVALID_PAGE = -1;

    @InjectMocks
    private ChatServiceImpl chatService = new ChatServiceImpl();
    @Mock
    private ChatDao chatDao;
    @Mock
    private UserService userService;
    @Mock
    private DebateService debateService;

    @Before
    public void setUp() {
        user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        user2 = new User(USER_EMAIL_2, USER_USERNAME_2, USER_PASSWORD_2, Locale.ENGLISH);
        debate = new Debate(DEBATE_NAME, DEBATE_DESCRIPTION, user, true, user2, null, DebateCategory.OTHER);
    }

    @Test(expected = DebateNotFoundException.class)
    public void testCreateChatNoDebate() {
        chatService.create(CHAT_USERNAME, DEBATE_ID, MESSAGE);
    }

    @Test(expected = UserNotFoundException.class)
    public void testCreateChatNoUser() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));

        chatService.create(CHAT_USERNAME, DEBATE_ID, MESSAGE);
    }

    @Test(expected = ForbiddenChatException.class)
    public void testCreateChatClosedDebate() {
        debate.setStatus(DebateStatus.CLOSED);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        chatService.create(CHAT_USERNAME, DEBATE_ID, MESSAGE);
    }

    @Test(expected = ForbiddenChatException.class)
    public void testCreateChatUserIsParticipant() {
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user2));

        chatService.create(USER_USERNAME, DEBATE_ID, MESSAGE);
    }

    @Test
    public void testCreateChat() {
        Chat chat = new Chat(user, debate, MESSAGE);
        when(chatDao.create(any(User.class), any(Debate.class), anyString())).thenReturn(chat);
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        Chat c = chatService.create(CHAT_USERNAME, DEBATE_ID, MESSAGE);

        assertEquals(chat.getMessage(), c.getMessage());
        assertEquals(chat.getUser().getUsername(), c.getUser().getUsername());
        assertEquals(chat.getDebate().getName(), c.getDebate().getName());
    }

    @Test(expected = DebateNotFoundException.class)
    public void testGetDebateChatNoDebate() {
        chatService.getDebateChat(DEBATE_ID, VALID_PAGE);
    }

    @Test
    public void testGetDebateChatInvalidPage() {
        List<Chat> c = chatService.getDebateChat(DEBATE_ID, INVALID_PAGE);

        assertTrue(c.isEmpty());
    }

    @Test
    public void testGetDebateChat() {
        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat(user, debate, MESSAGE));
        when(debateService.getDebateById(anyLong())).thenReturn(Optional.of(debate));
        when(chatDao.getDebateChat(any(Debate.class), anyInt())).thenReturn(chats);

        List<Chat> c = chatService.getDebateChat(DEBATE_ID, VALID_PAGE);

        assertFalse(c.isEmpty());

        assertEquals(chats.get(0).getMessage(), c.get(0).getMessage());
    }

    @Test
    public void testGetPostsByDebatePageCount() {
        int postCount = 47;
        int expectedPageCount = 4;
        when(chatDao.getDebateChatsCount(anyLong())).thenReturn(postCount);

        int pc = chatService.getDebateChatPageCount(DEBATE_ID);

        assertEquals(expectedPageCount, pc);
    }

    @Test
    public void testGetPostsByDebatePageCountNoPosts() {
        int postCount = 0;
        int expectedPageCount = 0;
        when(chatDao.getDebateChatsCount(anyLong())).thenReturn(postCount);

        int pc = chatService.getDebateChatPageCount(DEBATE_ID);

        assertEquals(expectedPageCount, pc);
    }
}
