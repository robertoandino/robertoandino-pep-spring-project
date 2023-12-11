package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService){
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account newAccount){

        Optional<Account> accountOptional = accountService.retrieveAccountByUsername(newAccount.getUsername());

        if(!accountOptional.isEmpty()){
            System.out.println("User already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        if(accountOptional.isEmpty() && newAccount.getPassword().length() >= 4 && !newAccount.getUsername().isEmpty()){

            accountService.addAccount(newAccount);
            System.out.println("Account successfully created");
            return ResponseEntity.status(HttpStatus.OK).body(newAccount);

        }

        System.out.println("Username or password is invalid");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

    }

    @PostMapping("/login")
    public ResponseEntity<Account> userLogin(@RequestBody Account loginAccount){

        Optional<Account> accountOptional = accountService.retrieveAccountByUsername(loginAccount.getUsername());

        if(!accountOptional.isEmpty()){
            
            String expectedUser = accountOptional.get().getUsername();
            String expectedPassword = accountOptional.get().getPassword();
            String actualUser = loginAccount.getUsername();
            String actualPassword = loginAccount.getPassword();

            if(expectedUser.equals(actualUser) && expectedPassword.equals(actualPassword)){
                System.out.println("User login Successful");
                return ResponseEntity.status(HttpStatus.OK).body(accountOptional.get());
            }
        }

        System.out.println("User not logged in, check username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccount(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessageByAccount(@PathVariable int account_id){
        
        List<Message> messageByAccount = messageService.findMessagePerAccountID(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(messageByAccount);

    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage){

        Integer postedBy = newMessage.getPosted_by();

        if(postedBy == null){
            System.out.println("postedBy value is null. " + postedBy);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Account> accountOptional = accountService.retrieveAccountById(postedBy);
        
        if(accountOptional.isEmpty()){
            System.out.println("Account ID for posting user doesn't exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        boolean isMessageEmpty = newMessage.getMessage_text().isEmpty();
        boolean isMessageGreaterthan254chars = newMessage.getMessage_text().length() > 254;

        if(isMessageEmpty){
          
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        
        }else if(isMessageGreaterthan254chars){

            System.out.println("Message can't be greater than 254 chars");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }else if(accountOptional.isEmpty()){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        
        }else{

            messageService.addMessage(newMessage);
            System.out.println("New message added successfully");
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);
        }

    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> findMessageByID(@PathVariable int message_id){

        Optional<Message> messageOptional = messageService.retrieveMessageByID(message_id);
        
        if(!messageOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(messageOptional.get());
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageByID(@PathVariable int message_id){

        Optional<Message> messageOptional = messageService.retrieveMessageByID(message_id);

        if(!messageOptional.isEmpty()){

            Integer rowDeleted = messageService.removeMessageByPerID(message_id);
            System.out.println("Row deleted: " + rowDeleted);
            return ResponseEntity.status(HttpStatus.OK).body(rowDeleted);
        }

        System.out.println("Row deleted: " + 0);
        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageByItsID(@RequestBody Message newMessage, @PathVariable int message_id){

        boolean isMessageEmpty = newMessage.getMessage_text().isEmpty();
        boolean isMessageGreaterthan254chars = newMessage.getMessage_text().length() > 254;

        if(isMessageEmpty){
            
            System.out.println("Text message being sent is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        
        }else if(isMessageGreaterthan254chars){
            
            System.out.println("Message length greater than 254 chars");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }

        Optional<Message> messageOptional = messageService.retrieveMessageByID(message_id);
     
        if(!messageOptional.isEmpty()){

            Message messagetoUpdate = messageOptional.get();
            messagetoUpdate.setMessage_text(newMessage.getMessage_text());

            Integer rowUpdated = messageService.updateMessageByPerID(messagetoUpdate.getMessage_text(), message_id);

            System.out.println("Row updated: " + rowUpdated);
            return ResponseEntity.status(HttpStatus.OK).body(rowUpdated);

        }

        System.out.println("Row updated: " + 0);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
