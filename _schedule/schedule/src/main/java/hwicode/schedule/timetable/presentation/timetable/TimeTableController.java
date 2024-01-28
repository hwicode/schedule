package hwicode.schedule.timetable.presentation.timetable;

import hwicode.schedule.timetable.application.TimeTableAggregateService;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeDeleteCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeModifyEndTimeCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeModifyStartTimeCommand;
import hwicode.schedule.timetable.application.dto.time_table.LearningTimeSaveCommand;
import hwicode.schedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyResponse;
import hwicode.schedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import hwicode.schedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import hwicode.schedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
                                                     @RequestBody @Valid LearningTimeSaveRequest request) {
        LearningTimeSaveCommand command = new LearningTimeSaveCommand(
                1L, timeTableId, request.getStartTime()
        );
        Long learningTimeId = timeTableAggregateService.saveLearningTime(command);
        return new LearningTimeSaveResponse(learningTimeId, command.getStartTime());
    }

    @PatchMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time")
    @ResponseStatus(value = HttpStatus.OK)
    public StartTimeModifyResponse changeLearningTimeStartTime(@PathVariable @Positive Long timeTableId,
                                                               @PathVariable @Positive Long learningTimeId,
                                                               @RequestBody @Valid StartTimeModifyRequest request) {
        LearningTimeModifyStartTimeCommand command = new LearningTimeModifyStartTimeCommand(
                1L, timeTableId, request.getStartTime(), request.getNewStartTime()
        );
        LocalDateTime newStartTime = timeTableAggregateService.changeLearningTimeStartTime(command);
        return new StartTimeModifyResponse(newStartTime);
    }

    @PatchMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/end-time")
    @ResponseStatus(value = HttpStatus.OK)
    public EndTimeModifyResponse changeLearningTimeEndTime(@PathVariable @Positive Long timeTableId,
                                                           @PathVariable @Positive Long learningTimeId,
                                                           @RequestBody @Valid EndTimeModifyRequest request) {
        LearningTimeModifyEndTimeCommand command = new LearningTimeModifyEndTimeCommand(
                1L, timeTableId, request.getStartTime(), request.getEndTime()
        );
        LocalDateTime endTime = timeTableAggregateService.changeLearningTimeEndTime(command);
        return new EndTimeModifyResponse(endTime);
    }

    @DeleteMapping("/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteLearningTime(@PathVariable @Positive Long timeTableId,
                                   @PathVariable @Positive Long learningTimeId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @NotNull LocalDateTime startTime) {
        LearningTimeDeleteCommand command = new LearningTimeDeleteCommand(1L, timeTableId, startTime);
        timeTableAggregateService.deleteLearningTime(command);
    }

}
