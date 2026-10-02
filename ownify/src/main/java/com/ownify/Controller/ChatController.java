package com.ownify.Controller;

import com.ownify.Model.ChatMessage;
import com.ownify.Entity.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
public class ChatController {

    @GetMapping("/messaging")
    public String messagingPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/signin";
        }
        model.addAttribute("user", user);
        return "messaging";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/public")
    public ChatMessage send(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // Take the sender from the logged-in session user, never from the client payload
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        User user = sessionAttributes != null ? (User) sessionAttributes.get("user") : null;
        if (user == null) {
            throw new AccessDeniedException("User not logged in");
        }
        message.setSender(user.getFirstName());
        return message;
    }
}
