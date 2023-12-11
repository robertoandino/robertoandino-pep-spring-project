package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> retrieveMessageByID(int message_id){
        Optional<Message> messageById = messageRepository.findMessageByID(message_id);
        return messageById;
    }

    @Transactional
    public int removeMessageByPerID(int message_id){
        int rowDeleted = messageRepository.deleteMessagePerID(message_id);

        return rowDeleted;
    }

    @Transactional
    public int updateMessageByPerID(String message_text, int message_id){
        int rowUpdated = messageRepository.updateMessagePerItsID(message_text, message_id);

        return rowUpdated;
    }

    public List<Message> findMessagePerAccountID(int account_id){
        List<Message> messageListByAcctId = messageRepository.retrieveMessagesByAccount(account_id);
        return messageListByAcctId;
    }
}
