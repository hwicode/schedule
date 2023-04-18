package hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DailyToDoListInformationChangeResponse {

    @NotNull @Positive
    private Long dailyToDoListId;

    @NotNull
    private String modifiedReview;

    @NotNull
    private Emoji modifiedEmoji;
}
