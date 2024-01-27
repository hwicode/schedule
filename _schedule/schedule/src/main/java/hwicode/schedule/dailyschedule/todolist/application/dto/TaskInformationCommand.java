package hwicode.schedule.dailyschedule.todolist.application.dto;

import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskInformationCommand {

    private final Long userId;
    private final Long taskId;
    private final Priority priority;
    private final Importance importance;
}
