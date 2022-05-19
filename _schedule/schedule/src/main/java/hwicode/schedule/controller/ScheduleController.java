package hwicode.schedule.controller;

import hwicode.schedule.controller.request.DateRequest;
import hwicode.schedule.controller.request.FeedbackRequest;
import hwicode.schedule.service.ScheduleService;
import hwicode.schedule.service.response.ScheduleIdResponse;
import hwicode.schedule.service.response.ScheduleResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("calendar/schedule")
    public ScheduleIdResponse createSchedule(@RequestBody DateRequest dateRequest) {
        String date = dateRequest.getDate();
        return scheduleService.makeSchedule(date);
    }

    @PutMapping("calendar/schedule")
    public void modifySchedule(@RequestBody FeedbackRequest feedbackRequest) {
        Long scheduleId = feedbackRequest.getScheduleId();
        String feedback = feedbackRequest.getFeedback();
        scheduleService.changeFeedback(scheduleId, feedback);
    }

    @GetMapping("calendar/schedule/{scheduleId}")
    public ScheduleResponse showSchedule(@PathVariable Long scheduleId) {
        return scheduleService.findById(scheduleId);
    }
}
