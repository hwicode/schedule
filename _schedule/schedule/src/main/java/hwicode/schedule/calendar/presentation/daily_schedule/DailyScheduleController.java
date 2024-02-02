package hwicode.schedule.calendar.presentation.daily_schedule;

import hwicode.schedule.calendar.application.daily_schedule.DailyScheduleService;
import hwicode.schedule.calendar.application.daily_schedule.DailyScheduleSaveCommand;
import hwicode.schedule.calendar.presentation.daily_schedule.dto.DailyScheduleSaveRequest;
import hwicode.schedule.calendar.presentation.daily_schedule.dto.DailyScheduleSaveResponse;
import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class DailyScheduleController {

    private final DailyScheduleService dailyScheduleService;

    @PostMapping("/daily-todo-lists")
    @ResponseStatus(HttpStatus.CREATED)
    public DailyScheduleSaveResponse saveDailySchedule(@LoginUser LoginInfo loginInfo,
                                                       @RequestBody @Valid DailyScheduleSaveRequest request) {
        DailyScheduleSaveCommand command = new DailyScheduleSaveCommand(loginInfo.getUserId(), request.getDate());
        Long dailyScheduleId = dailyScheduleService.saveDailySchedule(command);
        return new DailyScheduleSaveResponse(dailyScheduleId, command.getDate());
    }

}
