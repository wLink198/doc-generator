package com.svc.jogging.model.req;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class StepCountReq {
    private String userId;
    @Min(value = 0, message = "Steps cannot be negative")
    private int steps;
    private String recordedDate;
    private String fromLocation;
    private String toLocation;
    private String zoneId;
}
