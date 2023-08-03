package com.project.chatop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.chatop.model.Message;

@Repository
// Crud Repository pour Message
public interface MessageRepository extends CrudRepository<Message, Long> {

}
