package com.project.chatop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.chatop.model.Message;
import com.project.chatop.repository.MessageRepository;

import lombok.Data;

// Service pour Message
@Data
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Optional<Message> getMessage(final Long id) {
        return messageRepository.findById(id);
    }

    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }

    public void deleteMessage(final Long id) {
    	messageRepository.deleteById(id);
    }

    // Méthode pour enregistrer le message en DB
    public Message saveMessage(Message message) {
    	Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }
}
