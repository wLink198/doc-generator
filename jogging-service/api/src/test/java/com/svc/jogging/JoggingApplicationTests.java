package com.svc.jogging;

import com.svc.jogging.api.JoggingApplication;
import com.svc.jogging.config.MongoTestConfiguration;
import com.svc.jogging.model.dto.StepDto;
import com.svc.jogging.model.req.StepCountReq;
import com.svc.jogging.service.facade.StepService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@SpringBootTest(classes = {JoggingApplication.class})
@Import(MongoTestConfiguration.class)
class JoggingApplicationTests {

    private final StepService stepService;

    private static StepCountReq stepCountReq;

    @Autowired
    public JoggingApplicationTests(StepService stepService) {
        this.stepService = stepService;
    }

    @BeforeAll
    static void init() {
        log.info("TEST startup");
        stepCountReq = new StepCountReq();
        stepCountReq.setUserId("user123");
        stepCountReq.setSteps(0);
        stepCountReq.setRecordedDate("2023-07-30");
        stepCountReq.setFromLocation("Location A");
        stepCountReq.setToLocation("Location B");
        stepCountReq.setZoneId("Asia/Ho_Chi_Minh");
    }

    @Test
    public void recordUserStepCountTodayTest() {
        StepDto result = stepService.recordUserStepCountToday(stepCountReq);
        verifyRecordedSteps(stepCountReq, result);

        // update steps for this user
        stepCountReq.setSteps(10);
        StepDto updatedResult = stepService.recordUserStepCountToday(stepCountReq);
        verifyRecordedSteps(stepCountReq, updatedResult);
    }

    private void verifyRecordedSteps(StepCountReq req, StepDto result) {
        assertNotNull(result.get_id());
        assertEquals(req.getUserId(), result.getUserId());
        assertEquals(req.getSteps(), result.getSteps());
        assertEquals(req.getFromLocation(), result.getFromLocation());
        assertEquals(req.getToLocation(), result.getToLocation());
        assertEquals(req.getRecordedDate(), result.getRecordedDate());
        assertEquals(req.getZoneId(), result.getZoneId());
        assertNotNull(result.getStartedAt());
        assertNotNull(result.getEndedAt());
    }

    @AfterAll
    static void teardown() {
        log.info("TEST teardown");
        stepCountReq = null;
    }
}
