package hwicode.schedule.dailyschedule.timetable.presentation.timetable;

import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyResponse;
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

    @PatchMapping("dailyschedule/timetable/{startTime}")
    @ResponseStatus(value = HttpStatus.OK)
    public StartTimeModifyResponse changeLearningTimeStartTime(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                                                               @RequestBody @Valid StartTimeModifyRequest startTimeModifyRequest) {
        LocalDateTime newStartTime = timeTableService.changeLearningTimeStartTime(
                startTimeModifyRequest.getTimeTableId(), startTime, startTimeModifyRequest.getNewStartTime()
        );
        return new StartTimeModifyResponse(newStartTime);
    }
}
