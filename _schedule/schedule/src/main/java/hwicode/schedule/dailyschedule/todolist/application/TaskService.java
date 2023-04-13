package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.SubTaskNameChangeRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Long changeTaskInformation(Long taskId, TaskInformationModifyRequest taskInformationModifyRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotExistException::new);

        task.changePriority(taskInformationModifyRequest.getPriority());
        task.changeImportance(taskInformationModifyRequest.getImportance());
        return taskId;
    }

    @Transactional
    public String changeSubTaskName(String subTaskName, SubTaskNameChangeRequest subTaskNameChangeRequest) {
        Task task = taskRepository.findTaskWithSubtasks(subTaskNameChangeRequest.getTaskId())
                .orElseThrow(TaskNotExistException::new);

        return task.changeSubTaskName(subTaskName, subTaskNameChangeRequest.getNewSubTaskName());
    }
}
