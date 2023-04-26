package hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist;

import hwicode.schedule.dailyschedule.todolist.application.DailyToDoListService;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
public class DailyToDoListController {

    private final DailyToDoListService dailyToDoListService;

    @PatchMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}/information")
    @ResponseStatus(value = HttpStatus.OK)
    public DailyToDoListInformationChangeResponse changeDailyToDoListInformation(@PathVariable @NotNull Long dailyToDoListId,
                                                                                 @RequestBody @Valid DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest) {
        dailyToDoListService.changeDailyToDoListInformation(dailyToDoListId, dailyToDoListInformationChangeRequest);
        return new DailyToDoListInformationChangeResponse(
                dailyToDoListId,
                dailyToDoListInformationChangeRequest.getReview(),
                dailyToDoListInformationChangeRequest.getEmoji()
        );
    }

}
