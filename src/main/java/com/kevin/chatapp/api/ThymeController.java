package com.kevin.chatapp.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.view.ChatVO;
import com.kevin.chatapp.domain.view.MessageGroupVO;
import com.kevin.chatapp.domain.view.MessageVO;

@Controller
public class ThymeController {

    private static final String LAST_POST_MSG = "%s: %s";
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<ChatVO> chatViews =
                getChats().stream()
                        .map(
                                chat ->
                                        new ChatVO(
                                                chat.id(),
                                                chat.name(),
                                                "https://images.unsplash.com/photo-1550745165-9bc0b252726f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2940&q=80",
                                                String.format(
                                                        LAST_POST_MSG, "Kevin Callanan", "Hey!"),
                                                chat.lastPostDate().format(dateTimeFormatter)))
                        .collect(Collectors.toList());
        model.addAttribute("chats", chatViews);
        return "homePage";
    }

    @GetMapping("/chat")
    public String viewChatPage(Model model) {
        List<MessageGroupVO> messageGroups =
                getMessageGroups(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        ChatVO chat =
                new ChatVO(
                        UUID.randomUUID(),
                        "The Dev Group Chat",
                        "https://images.unsplash.com/photo-1487058792275-0ad4aaf24ca7?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2940&q=80",
                        null,
                        LocalDateTime.now().format(dateTimeFormatter));

        model.addAttribute("chat", chat);
        model.addAttribute("messageGroups", messageGroups);
        return "chatView";
    }

    @PostMapping("/send")
    public String send(Model model, @RequestParam String value) {
        // TODO - this should send a fragment that is the last msg in the chat
        List<MessageGroupVO> messageGroups =
                getMessageGroups(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<MessageGroupVO> messageGroupsFinal = List.of(messageGroups.get(0));

        model.addAttribute("messageGroups", messageGroupsFinal);
        return "fragments/chatBubbles";
    }

    private List<Chat> getChats() {
        return List.of(
                new Chat(UUID.randomUUID(), "test Chat 1", new ArrayList<>(), LocalDateTime.now()),
                new Chat(UUID.randomUUID(), "test Chat 2", new ArrayList<>(), LocalDateTime.now()));
    }

    // TODO - this is all a bit messy
    private List<MessageGroupVO> getMessageGroups(UUID loggedInUserId, UUID author1, UUID author2) {
        String author1thumbnail =
                "https://images.unsplash.com/photo-1550745165-9bc0b252726f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2940&q=80"; // TODO - default value like anon would be good
        String author2thumbnail =
                "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=3087&q=80"; // TODO - default value like anon would be good
        String author3thumbnail =
                "https://images.unsplash.com/photo-1531123897727-8f129e1688ce?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=3087&q=80"; // TODO - default value like anon would be good

        List<MessageGroupVO> messageGroups = new ArrayList<>();
        String thumbnailUrl =
                "https://images.unsplash.com/photo-1550745165-9bc0b252726f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2940&q=80"; // TODO - default value like anon would be good
        UUID authorId = null;
        boolean isLoggedInUser = false;
        List<MessageVO> messages = new ArrayList<>();

        for (Message msg : getMessages(loggedInUserId, author1, author2)) {
            if (!msg.author().equals(authorId)) {
                // add previous messageGroup
                if (authorId != null)
                    messageGroups.add(
                            new MessageGroupVO(thumbnailUrl, authorId, isLoggedInUser, messages));

                authorId = msg.author();
                isLoggedInUser = msg.author().equals(loggedInUserId);
                messages = new ArrayList<>();
                thumbnailUrl =
                        (authorId.equals(loggedInUserId))
                                ? author1thumbnail
                                : (authorId.equals(author1)) ? author2thumbnail : author3thumbnail;
            }
            messages.add(new MessageVO(msg.text(), msg.datePosted().format(dateTimeFormatter)));
        }
        if (authorId != null)
            messageGroups.add(new MessageGroupVO(thumbnailUrl, authorId, isLoggedInUser, messages));
        return messageGroups;
    }

    private List<Message> getMessages(UUID loggedInUser, UUID author1, UUID author2) {
        UUID chatId = UUID.randomUUID();

        return List.of(
                new Message(UUID.randomUUID(), "Hey", chatId, LocalDateTime.now(), author1),
                new Message(UUID.randomUUID(), "How are u?", chatId, LocalDateTime.now(), author1),
                new Message(
                        UUID.randomUUID(),
                        "I'm good! How's tricks?",
                        chatId,
                        LocalDateTime.now(),
                        loggedInUser),
                new Message(
                        UUID.randomUUID(),
                        "Gerrup, ye daisy!",
                        chatId,
                        LocalDateTime.now(),
                        author2),
                new Message(
                        UUID.randomUUID(),
                        "I wouldn't event mind but haven't chatted in yonks.",
                        chatId,
                        LocalDateTime.now(),
                        author2),
                new Message(UUID.randomUUID(), "Pints?", chatId, LocalDateTime.now(), author2),
                new Message(
                        UUID.randomUUID(), "You know it!", chatId, LocalDateTime.now(), author1),
                new Message(UUID.randomUUID(), "Where?", chatId, LocalDateTime.now(), author1));
    }
}
