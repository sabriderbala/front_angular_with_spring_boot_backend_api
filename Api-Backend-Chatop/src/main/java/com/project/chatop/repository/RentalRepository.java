package com.project.chatop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.chatop.model.Rental;

@Repository
// Crud Repository pour Rental
public interface RentalRepository extends CrudRepository<Rental, Long> {

}
