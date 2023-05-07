package hwicode.schedule.dailyschedule.timetable.presentation.timetable;

import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.delete.LearningTimeDeleteRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectOfSubTaskTotalLearningTimeResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Validated
@RestController
public class TimeTableController {

    private final TimeTableService timeTableService;

    @PostMapping("/dailyschedule/timetables/{timeTableId}/learning-times")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LearningTimeSaveResponse saveLearningTime(@PathVariable @Positive Long timeTableId,
                                                     @RequestBody @Valid LearningTimeSaveRequest learningTimeSaveRequest) {
        Long learningTimeId = timeTableService.saveLearningTime(
                timeTableId, learningTimeSaveRequest.getStartTime()
        );
        return new LearningTimeSaveResponse(learningTimeId, learningTimeSaveRequest.getStartTime());
    }

    @PatchMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time")
    @ResponseStatus(value = HttpStatus.OK)
    public StartTimeModifyResponse changeLearningTimeStartTime(@PathVariable @Positive Long timeTableId,
                                                               @PathVariable @Positive Long learningTimeId,
                                                               @RequestBody @Valid StartTimeModifyRequest startTimeModifyRequest) {
        LocalDateTime newStartTime = timeTableService.changeLearningTimeStartTime(
                timeTableId, startTimeModifyRequest.getStartTime(), startTimeModifyRequest.getNewStartTime()
        );
        return new StartTimeModifyResponse(newStartTime);
    }

    @PatchMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/end-time")
    @ResponseStatus(value = HttpStatus.OK)
    public EndTimeModifyResponse changeLearningTimeEndTime(@PathVariable @Positive Long timeTableId,
                                                           @PathVariable @Positive Long learningTimeId,
                                                           @RequestBody @Valid EndTimeModifyRequest endTimeModifyRequest) {
        LocalDateTime endTime = timeTableService.changeLearningTimeEndTime(
                timeTableId, endTimeModifyRequest.getStartTime(), endTimeModifyRequest.getEndTime()
        );
        return new EndTimeModifyResponse(endTime);
    }

    @DeleteMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLearningTime(@PathVariable @Positive Long timeTableId,
                                   @PathVariable @Positive Long learningTimeId,
                                   @RequestBody @Valid LearningTimeDeleteRequest learningTimeDeleteRequest) {
        timeTableService.deleteLearningTime(timeTableId, learningTimeDeleteRequest.getStartTime());
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/subject-total-time")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectTotalLearningTimeResponse getSubjectTotalLearningTime(@PathVariable @Positive Long timeTableId,
                                                                        @RequestParam @NotBlank String subject) {
        int subjectTotalLearningTime = timeTableService.calculateSubjectTotalLearningTime(timeTableId, subject);
        return new SubjectTotalLearningTimeResponse(subjectTotalLearningTime);
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/task-total-time")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectOfTaskTotalLearningTimeResponse getSubjectOfTaskTotalLearningTime(@PathVariable @Positive Long timeTableId,
                                                                                    @RequestParam("subject_of_task_id") @Positive Long subjectOfTaskId) {
        int totalLearningTime = timeTableService.calculateSubjectOfTaskTotalLearningTime(timeTableId, subjectOfTaskId);
        return new SubjectOfTaskTotalLearningTimeResponse(totalLearningTime);
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/subtask-total-time")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectOfSubTaskTotalLearningTimeResponse getSubjectOfSubTaskTotalLearningTime(@PathVariable @Positive Long timeTableId,
                                                                                          @RequestParam("subject_of_subtask_id") @Positive Long subjectOfSubTaskId) {
        int totalLearningTime = timeTableService.calculateSubjectOfSubTaskTotalLearningTime(timeTableId, subjectOfSubTaskId);
        return new SubjectOfSubTaskTotalLearningTimeResponse(totalLearningTime);
    }

}
