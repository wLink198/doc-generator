package com.svc.jogging.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document("steps")
public class StepEntity {
    public static final String COLLECTION_NAME = "steps";

    @Id
    private String _id;
    private String userId;
    private long steps;
    private String fromLocation;
    private String toLocation;
    private String recordedDate;
    private String zoneId;

    @CreatedDate
    private Instant startedAt;
    @LastModifiedDate
    private Instant endedAt;
}
