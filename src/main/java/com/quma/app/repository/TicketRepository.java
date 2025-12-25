package com.quma.app.repository;

import com.quma.app.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    /* Type custom queries here if needed. */

}
