package com.svc.jogging.service.impl;

import com.svc.jogging.model.dto.StepDto;
import com.svc.jogging.model.exception.BusinessException;
import com.svc.jogging.model.req.StepCountReq;
import com.svc.jogging.repository.entity.StepEntity;
import com.svc.jogging.repository.facade.StepRepository;
import com.svc.jogging.service.converter.StepConverter;
import com.svc.jogging.service.facade.StepService;
import com.svc.jogging.service.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepServiceImpl implements StepService {

    private final StepRepository stepRepository;

    private final StepConverter stepConverter;

    @Override
    public StepDto recordUserStepCountToday(StepCountReq req) {
        String userId = req.getUserId();
        req.setRecordedDate(TimeUtil.getDateOfTime(Instant.now(), req.getZoneId()));
        Optional<StepEntity> optional = stepRepository.findByUserIdAndRecordedDateEquals(userId, req.getRecordedDate());
        // chưa có thì record mới
        if (optional.isEmpty()) {
            validateInitStepReq(req);
            return stepConverter.toDto(stepRepository.save(stepConverter.fromReq(req)));
        }

        var stepEntity = optional.get();
        validateUpdateStepReq(req, stepEntity);
        stepConverter.updateStep(req, stepEntity);

        return stepConverter.toDto(stepRepository.save(stepEntity));
    }

    private void validateUpdateStepReq(StepCountReq req, StepEntity entity) {
        if (req.getSteps() < entity.getSteps()) {
            throw new BusinessException("Can not record new steps which are less than previous steps");
        }
        // tùy vào yêu cầu nghiệp vụ mà sẽ check thêm steps trong khoảng thời gian và địa điểm/timezone có hợp lý hay không
    }

    private void validateInitStepReq(StepCountReq req) {
        /*
        validate initial data dựa vào yêu cầu nghiệp vụ
        lần đầu record số bước chân luôn bằng 0
        */
        if (req.getSteps() > 0) {
            throw new BusinessException("Steps can not be greater than 0 for the first time recorded");
        }
    }

    @Override
    public List<StepDto> findTopUsersByDate(int limit, String date, String zoneId) {
        TimeUtil.TimeOfDay timeOfDay;
        // tùy vào yêu cầu nghiệp vụ mà sẽ validate date có hợp lệ không
        if (StringUtils.hasLength(date)) {
            timeOfDay = TimeUtil.getTimeOfDay(date, zoneId);
        } else {
            timeOfDay = TimeUtil.getTimeOfDay(Instant.now(), zoneId);
        }

        return stepConverter.toDto(stepRepository.findTopUsersByTimeRange(limit, timeOfDay.getStart(), timeOfDay.getEnd()));
    }

    @Override
    public long countStepsByUserCurrentWeek(String userId, String zoneId) {
        Instant now = Instant.now();
        return stepRepository.countStepsByUserAndTimeRange(userId, TimeUtil.getFirstDayOfWeek(now, zoneId), now);
    }

    @Override
    public long countStepsByUserCurrentMonth(String userId, String zoneId) {
        Instant now = Instant.now();
        return stepRepository.countStepsByUserAndTimeRange(userId, TimeUtil.getFirstDayOfMonth(now, zoneId), now);
    }
}
