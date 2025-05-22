package com.svc.jogging.repository.facade;

import com.svc.jogging.repository.entity.StepEntity;

import java.time.Instant;
import java.util.List;

public interface StepRepositoryCustom {
    List<StepEntity> findTopUsersByTimeRange(int limit, Instant startTime, Instant endTime);

    long countStepsByUserAndTimeRange(String userId, Instant startTime, Instant endTime);
}
