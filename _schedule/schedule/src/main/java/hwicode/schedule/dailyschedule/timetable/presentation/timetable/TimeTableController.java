package hwicode.schedule.dailyschedule.timetable.presentation.timetable;

import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.delete.LearningTimeDeleteRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectOfTaskTotalLearningTimeResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.subject_totaltime_response.SubjectTotalLearningTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class TimeTableController {

    private final TimeTableService timeTableService;

    @PostMapping("/dailyschedule/timetables/{timeTableId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LearningTimeSaveResponse saveLearningTime(@PathVariable @NotBlank Long timeTableId,
                                                     @RequestBody @Valid LearningTimeSaveRequest learningTimeSaveRequest) {
        Long learningTimeId = timeTableService.saveLearningTime(
                timeTableId, learningTimeSaveRequest.getStartTime()
        );
        return new LearningTimeSaveResponse(learningTimeId, learningTimeSaveRequest.getStartTime());
    }

    @PatchMapping("/dailyschedule/timetable/{startTime}/starttime")
    @ResponseStatus(value = HttpStatus.OK)
    public StartTimeModifyResponse changeLearningTimeStartTime(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                                                               @RequestBody @Valid StartTimeModifyRequest startTimeModifyRequest) {
        LocalDateTime newStartTime = timeTableService.changeLearningTimeStartTime(
                startTimeModifyRequest.getTimeTableId(), startTime, startTimeModifyRequest.getNewStartTime()
        );
        return new StartTimeModifyResponse(newStartTime);
    }

    @PatchMapping("/dailyschedule/timetable/{startTime}/endtime")
    @ResponseStatus(value = HttpStatus.OK)
    public EndTimeModifyResponse changeLearningTimeEndTime(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                                                           @RequestBody @Valid EndTimeModifyRequest endTimeModifyRequest) {
        LocalDateTime endTime = timeTableService.changeLearningTimeEndTime(
                endTimeModifyRequest.getTimeTableId(), startTime, endTimeModifyRequest.getEndTime()
        );
        return new EndTimeModifyResponse(endTime);
    }

    @DeleteMapping("/dailyschedule/timetable/{startTime}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLearningTime(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                                   @RequestBody @Valid LearningTimeDeleteRequest learningTimeDeleteRequest) {
        timeTableService.deleteLearningTime(
                learningTimeDeleteRequest.getTimeTableId(), startTime
        );
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/{subject}")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectTotalLearningTimeResponse getSubjectTotalLearningTime(@PathVariable @NotBlank Long timeTableId,
                                                                        @PathVariable @NotBlank String subject) {
        int subjectTotalLearningTime = timeTableService.calculateSubjectTotalLearningTime(timeTableId, subject);
        return new SubjectTotalLearningTimeResponse(subjectTotalLearningTime);
    }

    @GetMapping("/dailyschedule/timetables/{timeTableId}/subjectoftask/{subjectOfTaskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectOfTaskTotalLearningTimeResponse getSubjectOfTaskTotalLearningTime(@PathVariable @NotBlank Long timeTableId,
                                                                                    @PathVariable @NotBlank Long subjectOfTaskId) {
        int totalLearningTime = timeTableService.calculateSubjectOfTaskTotalLearningTime(timeTableId, subjectOfTaskId);
        return new SubjectOfTaskTotalLearningTimeResponse(totalLearningTime);
    }
}
