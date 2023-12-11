package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{

    @Query(value = "SELECT * FROM message WHERE message_id = ?1", nativeQuery = true)
    Optional<Message> findMessageByID(int id);
    
    @Modifying
    @Query(value="delete from message m WHERE m.message_id = ?1", nativeQuery=true)
    int deleteMessagePerID(int id);

    @Modifying
    @Query(value="update message m set m.message_text = ?1 where message_id = ?2", nativeQuery=true)
    int updateMessagePerItsID(String message_text, int id);

    @Query(value="select * from message where posted_by = ?1", nativeQuery = true)
    List<Message> retrieveMessagesByAccount(int account_id);
}
