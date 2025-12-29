package com.quma.app.repository;

import com.quma.app.entity.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SessionRepository extends MongoRepository<Session, String> {

    Optional<Session> findBySessionEpoch(String sessionId);

    /* Write custom queries here if needed. */

}
