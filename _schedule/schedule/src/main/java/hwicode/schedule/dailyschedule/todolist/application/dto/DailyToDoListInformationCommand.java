package hwicode.schedule.dailyschedule.todolist.application.dto;

import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DailyToDoListInformationCommand {

    private final Long userId;
    private final Long dailyToDoListId;
    private final String review;
    private final Emoji emoji;
}
