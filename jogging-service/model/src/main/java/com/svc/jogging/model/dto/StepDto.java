package com.svc.jogging.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StepDto {
    @JsonProperty("id")
    private String _id;
    private String userId;
    private long steps;
    private String fromLocation;
    private String toLocation;
    private String recordedDate;
    private String zoneId;

    private Instant startedAt;
    private Instant endedAt;
}
