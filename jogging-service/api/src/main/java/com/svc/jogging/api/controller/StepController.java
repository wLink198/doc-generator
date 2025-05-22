package com.svc.jogging.api.controller;

import com.svc.jogging.model.constant.ZoneIds;
import com.svc.jogging.model.dto.StepDto;
import com.svc.jogging.model.req.StepCountReq;
import com.svc.jogging.model.res.ResponseData;
import com.svc.jogging.service.facade.StepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "steps")
public class StepController {

    private final StepService stepService;

    @Operation(summary = "API for recording the number of steps taken by users in the current day from the application",
            description = "First time start recording user steps for current day, the steps must be 0")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return StepDto with the ID of the newly created entity",
                    content = @Content(schema = @Schema(implementation = StepDto.class))),
            @ApiResponse(responseCode = "400", description = "Return error message based on situations such as:<br>" +
                    "Can not record new steps which are less than previous steps<br>" +
                    "Steps can not be greater than 0 for the first time recorded<br>" +
                    "Steps cannot be negative")
    })
    @PostMapping("")
    public ResponseData recordUserStepCountToday(
            @RequestHeader String userId,
            @RequestParam(required = false, defaultValue = ZoneIds.DEFAULT_ZONE_ID) String zoneId,
            @Valid @RequestBody StepCountReq req) {
        req.setUserId(userId);
        req.setZoneId(zoneId);
        return ResponseData.builder().data(stepService.recordUserStepCountToday(req)).build();
    }

    @Operation(summary = "API for retrieving the leaderboard to display in the application")
    @GetMapping("/top/{limit}")
    public ResponseData findTopUsersByDate(
            @Parameter(description = "Top ranking of walkers according to the limit parameter", schema = @Schema(type = "integer"))
            @PathVariable int limit,
            @Parameter(description = "Ranking by date with pattern: yyyy-MM-dd", schema = @Schema(type = "string"))
            @RequestParam(required = false) String date,
            @RequestParam(required = false, defaultValue = ZoneIds.DEFAULT_ZONE_ID) String zoneId) {
        return ResponseData.builder().data(stepService.findTopUsersByDate(limit, date, zoneId)).build();
    }

    @Operation(summary = "API for retrieving the total number of steps taken by a user in the current week")
    @GetMapping("/count/week")
    public ResponseData countStepsByUserCurrentWeek(
            @RequestHeader String userId,
            @RequestParam(required = false, defaultValue = ZoneIds.DEFAULT_ZONE_ID) String zoneId) {
        return ResponseData.builder().data(stepService.countStepsByUserCurrentWeek(userId, zoneId)).build();
    }

    @Operation(summary = "API for retrieving the total number of steps taken by a user in the current month")
    @GetMapping("/count/month")
    public ResponseData countStepsByUserCurrentMonth(
            @RequestHeader String userId,
            @RequestParam(required = false, defaultValue = ZoneIds.DEFAULT_ZONE_ID) String zoneId) {
        return ResponseData.builder().data(stepService.countStepsByUserCurrentMonth(userId, zoneId)).build();
    }
}
