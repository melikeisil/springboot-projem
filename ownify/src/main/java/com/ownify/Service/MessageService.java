package com.ownify.Service;

import com.ownify.Entity.Message;
import com.ownify.Entity.User;
import com.ownify.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    public Message sendMessage(User sender, User receiver, String content) {
        Message message = new Message(sender, receiver, content);
        return messageRepository.save(message);
    }
    
    public List<Message> getUserMessages(Long userId) {
        return messageRepository.findMessagesByUserId(userId);
    }
    
    public List<Message> getConversation(Long senderId, Long receiverId) {
        return messageRepository.findConversation(senderId, receiverId);
    }
    
    public List<Message> getUnreadMessages(User user) {
        return messageRepository.findByReceiverAndIsReadFalse(user);
    }
    
    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setIsRead(true);
            return messageRepository.save(message);
        }
        return null;
    }
}