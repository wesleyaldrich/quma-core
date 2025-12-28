package com.quma.app.repository;

import com.quma.app.entity.QrResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QrResultRepository extends MongoRepository<QrResult, String> {

}
