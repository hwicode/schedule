package hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyToDoListInformationChangeResponse {

    private Long dailyToDoListId;
    private String modifiedReview;
    private Emoji modifiedEmoji;
}
