package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskNameChangeRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskInformationChangeRequest;
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
    public void changeTaskInformation(Long taskId, TaskInformationChangeRequest taskInformationChangeRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(IllegalArgumentException::new);

        task.changePriority(taskInformationChangeRequest.getPriority());
        task.changeImportance(taskInformationChangeRequest.getImportance());
    }

    @Transactional
    public String changeSubTaskName(String subTaskName, SubTaskNameChangeRequest subTaskNameChangeRequest) {
        Task task = taskRepository.findTaskWithSubtasks(subTaskNameChangeRequest.getTaskId())
                .orElseThrow(IllegalArgumentException::new);

        return task.changeSubTaskName(subTaskName, subTaskNameChangeRequest.getNewSubTaskName());
    }
}
