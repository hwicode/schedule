package hwicode.schedule.calendar.presentation.daily_schedule;

import hwicode.schedule.calendar.application.DailyScheduleService;
import hwicode.schedule.calendar.application.dto.daily_schedule.DailyScheduleSaveCommand;
import hwicode.schedule.calendar.presentation.daily_schedule.dto.DailyScheduleSaveRequest;
import hwicode.schedule.calendar.presentation.daily_schedule.dto.DailyScheduleSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class DailyScheduleController {

    private final DailyScheduleService dailyScheduleService;

    @PostMapping("/daily-todo-lists")
    @ResponseStatus(HttpStatus.CREATED)
    public DailyScheduleSaveResponse saveDailySchedule(@RequestBody @Valid DailyScheduleSaveRequest request) {
        DailyScheduleSaveCommand command = new DailyScheduleSaveCommand(
                1L, LocalDate.now(), request.getDate()
        );
        Long dailyScheduleId = dailyScheduleService.saveDailySchedule(command);
        return new DailyScheduleSaveResponse(dailyScheduleId, command.getDate());
    }

}
