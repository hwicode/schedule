package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.TaskInformationCommand;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.exception.application.ToDoListForbiddenException;
import hwicode.schedule.dailyschedule.todolist.infra.limited_repository.TaskFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskAggregateService {

    private final TaskFindRepository taskFindRepository;

    @Transactional
    public Long changeTaskInformation(TaskInformationCommand command) {
        Task task = taskFindRepository.findById(command.getTaskId());

        if (!task.isOwner(command.getUserId())) {
            throw new ToDoListForbiddenException();
        }

        task.changePriority(command.getPriority());
        task.changeImportance(command.getImportance());
        return command.getTaskId();
    }
}
