package hwicode.schedule.timetable.presentation.timetable;

import hwicode.schedule.timetable.application.query.TimeTableQueryService;
import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TimeTableQueryController {

    private final TimeTableQueryService timeTableQueryService;

    @GetMapping("/dailyschedule/timetables")
    @ResponseStatus(value = HttpStatus.OK)
    public List<LearningTimeQueryResponse> getLearningTimeQueryResponses(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return timeTableQueryService.getLearningTimeQueryResponses(date);
    }

}
