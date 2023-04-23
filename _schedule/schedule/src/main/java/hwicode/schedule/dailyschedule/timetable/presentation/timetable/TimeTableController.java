package hwicode.schedule.dailyschedule.timetable.presentation.timetable;

import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class TimeTableController {

    private final TimeTableService timeTableService;

    TimeTableController(TimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }

    @PostMapping("/dailyschedule/timetables/{timeTableId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LearningTimeSaveResponse saveLearningTime(@PathVariable @NotBlank Long timeTableId,
                                                     @RequestBody @Valid LearningTimeSaveRequest learningTimeSaveRequest) {
        Long learningTimeId = timeTableService.saveLearningTime(
                timeTableId, learningTimeSaveRequest.getStartTime()
        );
        return new LearningTimeSaveResponse(learningTimeId, learningTimeSaveRequest.getStartTime());
    }
}
