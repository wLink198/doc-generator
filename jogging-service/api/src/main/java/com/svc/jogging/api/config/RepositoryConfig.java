package com.svc.jogging.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.svc.jogging.repository")
@EnableMongoAuditing
public class RepositoryConfig {
}
