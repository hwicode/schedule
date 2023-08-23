package hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto;

import hwicode.schedule.dailyschedule.shared_domain.Emoji;
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
