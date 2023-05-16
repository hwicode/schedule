package hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DailyToDoListInformationChangeResponse {

    private Long dailyToDoListId;
    private String modifiedReview;
    private Emoji modifiedEmoji;
}
