package hwicode.schedule.dailyschedule.todolist.application.dto;

import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DailyToDoListInformationChangeRequest {

    @NotNull
    private String review;

    @NotNull
    private Emoji emoji;
}
