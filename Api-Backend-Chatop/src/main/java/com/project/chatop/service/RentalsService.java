package com.project.chatop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.chatop.model.Rental;
import com.project.chatop.repository.RentalRepository;

import lombok.Data;

// Service pour Rentals
@Data
@Service
public class RentalsService {
    private final RentalRepository rentalRepository;

    @Autowired
    public RentalsService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Optional<Rental> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public void deleteRental(final Long id) {
        rentalRepository.deleteById(id);
    }

    public void saveRental(Rental rental) {
        rentalRepository.save(rental);
    }


    public Rental updateRental(Rental rental, Long ownerId) {
        rental.setOwnerId(ownerId);
        Rental updatedRental = rentalRepository.save(rental);
        return updatedRental;
    }

}
