package com.svc.jogging.config;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@TestConfiguration
public class MongoTestConfiguration {

    @Bean(destroyMethod = "stop")
    public MongodProcess mongodProcess() throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodConfig config = ImmutableMongodConfig.builder()
                .version(Version.Main.PRODUCTION)
                .net(new de.flapdoodle.embed.mongo.config.Net("localhost", 27016, false))
                .build();
        MongodExecutable executable = starter.prepare(config);
        return executable.start();
    }

    @Bean(destroyMethod = "close")
    public com.mongodb.client.MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27016");
    }
}
