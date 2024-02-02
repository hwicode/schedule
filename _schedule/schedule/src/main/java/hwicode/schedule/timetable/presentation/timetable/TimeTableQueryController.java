package hwicode.schedule.timetable.presentation.timetable;

import hwicode.schedule.common.config.auth.LoginInfo;
import hwicode.schedule.common.config.auth.LoginUser;
import hwicode.schedule.timetable.application.query.TimeTableQueryService;
import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.timetable.application.query.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class TimeTableQueryController {

    private final TimeTableQueryService timeTableQueryService;

    @GetMapping("/dailyschedule/timetables")
    @ResponseStatus(value = HttpStatus.OK)
    public List<LearningTimeQueryResponse> getLearningTimeQueryResponses(@LoginUser LoginInfo loginInfo,
                                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return timeTableQueryService.getLearningTimeQueryResponses(loginInfo.getUserId(), date);
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/subject-total-time")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectTotalLearningTimeResponse getSubjectTotalLearningTime(@LoginUser LoginInfo loginInfo,
                                                                        @PathVariable @Positive Long timeTableId,
                                                                        @RequestParam @NotBlank String subject) {
        return timeTableQueryService.calculateSubjectTotalLearningTime(timeTableId, subject);
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/task-total-time")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectOfTaskTotalLearningTimeResponse getSubjectOfTaskTotalLearningTime(@LoginUser LoginInfo loginInfo,
                                                                                    @PathVariable @Positive Long timeTableId,
                                                                                    @RequestParam("subject_of_task_id") @Positive Long subjectOfTaskId) {
        return timeTableQueryService.calculateSubjectOfTaskTotalLearningTime(timeTableId, subjectOfTaskId);
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/subtask-total-time")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectOfSubTaskTotalLearningTimeResponse getSubjectOfSubTaskTotalLearningTime(@LoginUser LoginInfo loginInfo,
                                                                                          @PathVariable @Positive Long timeTableId,
                                                                                          @RequestParam("subject_of_subtask_id") @Positive Long subjectOfSubTaskId) {
        return timeTableQueryService.calculateSubjectOfSubTaskTotalLearningTime(timeTableId, subjectOfSubTaskId);
    }

}
