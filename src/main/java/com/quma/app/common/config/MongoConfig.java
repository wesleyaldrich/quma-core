package com.quma.app.common.config;

import com.mongodb.ConnectionString;
import org.springframework.boot.mongodb.autoconfigure.MongoConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    MongoConnectionDetails mongoConnectionDetails() {
        return () -> new ConnectionString("mongodb://localhost:27017/dquma");
    }
}
