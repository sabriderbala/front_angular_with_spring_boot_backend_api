package com.project.chatop.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.project.chatop.model.Message;
import com.project.chatop.model.Rental;
import com.project.chatop.repository.RentalRepository;
import com.project.chatop.service.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Message", description = "API pour les opération de Crud sur les Messages")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    private final MessageService messageService;
    
    @Autowired
    private RentalRepository rentalRepository;


    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    

    // Route pour envoyer un message sur une Rental
    @PostMapping(value = "/api/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody Message message) {
    	Long rentalId = message.getRental() != null ? message.getRental().getId() : null;

        if (rentalId != null) {
            Optional<Rental> rentalOpt = rentalRepository.findById(rentalId); // Utilisez l'instance injectée
            if (rentalOpt.isPresent()) {
                message.setRental(rentalOpt.get());
            } else {
                throw new RuntimeException("Rental not found");
            }
        }

        message.setCreatedAt(new Date()); // Définir la date de création
        message.setUpdatedAt(new Date()); // Définir la date de mise à jour

        messageService.saveMessage(message); // Enregistrer le message

        return ResponseEntity.ok(Map.of("message", "Message send with success"));
    }

}
