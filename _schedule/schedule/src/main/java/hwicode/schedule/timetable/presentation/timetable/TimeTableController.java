package hwicode.schedule.timetable.presentation.timetable;

import hwicode.schedule.timetable.application.TimeTableAggregateService;
import hwicode.schedule.timetable.presentation.timetable.dto.delete.LearningTimeDeleteRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyResponse;
import hwicode.schedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import hwicode.schedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Validated
@RestController
public class TimeTableController {

    private final TimeTableAggregateService timeTableAggregateService;

    @PostMapping("/dailyschedule/timetables/{timeTableId}/learning-times")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LearningTimeSaveResponse saveLearningTime(@PathVariable @Positive Long timeTableId,
                                                     @RequestBody @Valid LearningTimeSaveRequest learningTimeSaveRequest) {
        Long learningTimeId = timeTableAggregateService.saveLearningTime(
                timeTableId, learningTimeSaveRequest.getStartTime()
        );
        return new LearningTimeSaveResponse(learningTimeId, learningTimeSaveRequest.getStartTime());
    }

    @PatchMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time")
    @ResponseStatus(value = HttpStatus.OK)
    public StartTimeModifyResponse changeLearningTimeStartTime(@PathVariable @Positive Long timeTableId,
                                                               @PathVariable @Positive Long learningTimeId,
                                                               @RequestBody @Valid StartTimeModifyRequest startTimeModifyRequest) {
        LocalDateTime newStartTime = timeTableAggregateService.changeLearningTimeStartTime(
                timeTableId, startTimeModifyRequest.getStartTime(), startTimeModifyRequest.getNewStartTime()
        );
        return new StartTimeModifyResponse(newStartTime);
    }

    @PatchMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/end-time")
    @ResponseStatus(value = HttpStatus.OK)
    public EndTimeModifyResponse changeLearningTimeEndTime(@PathVariable @Positive Long timeTableId,
                                                           @PathVariable @Positive Long learningTimeId,
                                                           @RequestBody @Valid EndTimeModifyRequest endTimeModifyRequest) {
        LocalDateTime endTime = timeTableAggregateService.changeLearningTimeEndTime(
                timeTableId, endTimeModifyRequest.getStartTime(), endTimeModifyRequest.getEndTime()
        );
        return new EndTimeModifyResponse(endTime);
    }

    @DeleteMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLearningTime(@PathVariable @Positive Long timeTableId,
                                   @PathVariable @Positive Long learningTimeId,
                                   @RequestBody @Valid LearningTimeDeleteRequest learningTimeDeleteRequest) {
        timeTableAggregateService.deleteLearningTime(timeTableId, learningTimeDeleteRequest.getStartTime());
    }

}
