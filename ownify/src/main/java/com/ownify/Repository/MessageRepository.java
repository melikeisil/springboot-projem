package com.ownify.Repository;

import com.ownify.Entity.Message;
import com.ownify.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId OR m.receiver.id = :userId) " +
           "ORDER BY m.createdAt DESC")
    List<Message> findMessagesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :senderId AND m.receiver.id = :receiverId) OR " +
           "(m.sender.id = :receiverId AND m.receiver.id = :senderId) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    
    List<Message> findByReceiverAndIsReadFalse(User receiver);
}