package com.svc.jogging.service.facade;

import com.svc.jogging.model.dto.StepDto;
import com.svc.jogging.model.req.StepCountReq;

import java.util.List;

public interface StepService {
    StepDto recordUserStepCountToday(StepCountReq req);

    List<StepDto> findTopUsersByDate(int limit, String date, String zoneId);

    long countStepsByUserCurrentWeek(String userId, String zoneId);

    long countStepsByUserCurrentMonth(String userId, String zoneId);
}
