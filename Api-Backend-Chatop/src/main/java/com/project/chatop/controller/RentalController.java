package com.project.chatop.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.chatop.model.Rental;
import com.project.chatop.model.User;
import com.project.chatop.repository.UserRepository;
import com.project.chatop.service.RentalsService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Rentals", description = "API pour les opération de Crud sur les Rentals")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RentalController {

    private final RentalsService rentalService;
    private final UserRepository userRepository;

    public RentalController(RentalsService rentalService, UserRepository userRepository) {
        this.rentalService = rentalService;
        this.userRepository = userRepository;
    }

    // Méthode pour récupérer le current user id
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getId();
        } else {
            throw new RuntimeException("Current user not found");
        }
    }

    // Route pour récupérer toutes les rentals
    @GetMapping("/api/rentals")
    @ResponseBody
    public Iterable<Rental> getRentals() {
        return rentalService.getRentals();
    }

    // Route pour récupéré les détail d'une rental par son id
    @GetMapping("/api/rentals/{id}")
    @ResponseBody
    public Optional<Rental> getRental(@PathVariable Long id) {
        return rentalService.getRental(id);
    }

    // Route pour créer une rental
    @PostMapping(value = "/api/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createRental(
    		@RequestParam("name") String name,
            @RequestParam("surface") int surface,
            @RequestParam("price") int price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture) {

    	 Long ownerId = getCurrentUserId();
    	    Rental rental = new Rental();
    	    rental.setName(name);
    	    rental.setSurface(surface);
    	    rental.setPrice(price);
    	    rental.setDescription(description);
    	    rental.setOwnerId(ownerId);
    	    rental.setCreatedAt(new Date()); // Définir la date de création
    	    rental.setUpdatedAt(new Date()); // Définir la date de mise à jour

        if (picture != null && !picture.isEmpty()) {
            String contentType = picture.getContentType();
            if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                try {
                    byte[] pictureBytes = picture.getBytes();

                    rental.setPicture(pictureBytes);
                } catch (IOException e) {
                    return new ResponseEntity<>(Map.of("message", "Une erreur s'est produite lors du traitement de l'image."), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(Map.of("message", "Le fichier doit être une image JPG ou PNG."), HttpStatus.BAD_REQUEST);
            }
        }

        // On sauvegarde la rental en DB et on envoie une alerte rental créer
        rentalService.saveRental(rental);
        return new ResponseEntity<>(Map.of("message", "Rental Created!"), HttpStatus.OK);
    }

    // Route pour modifier une rental si on est le créateur de cette rental
    @RequestMapping(value = "/api/rentals/{id}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateRental(
        @PathVariable Long id,
        @RequestParam("name") String name,
        @RequestParam("surface") int surface,
        @RequestParam("price") int price,
        @RequestParam("description") String description,
        @RequestParam(value = "picture", required = false) MultipartFile picture
    ) {
        Optional<Rental> existingRental = rentalService.getRental(id);
        if (existingRental.isPresent()) {
            Rental updatedRental = existingRental.get();
            updatedRental.setName(name);
            updatedRental.setSurface(surface);
            updatedRental.setPrice(price);
            updatedRental.setDescription(description);
            
            if (picture != null && !picture.isEmpty()) {
                try {
                    byte[] pictureBytes = picture.getBytes();
                    updatedRental.setPicture(pictureBytes);
                } catch (IOException e) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Error uploading picture"));
                }
            }
            
            updatedRental.setUpdatedAt(new Date());
            updatedRental.setOwnerId(getCurrentUserId());
            rentalService.updateRental(updatedRental, updatedRental.getOwner().getId());
            
            return ResponseEntity.ok(Map.of("message", "Rental Updated"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
