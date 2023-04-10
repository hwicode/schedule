package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import hwicode.schedule.dailyschedule.todolist.domain.SubTaskSaveService;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubTaskSaveService subTaskSaveService;

    @Transactional
    public Long saveSubTask(Long taskId, String subTaskName) {
        Task task = taskRepository.findTaskWithSubtasks(taskId)
                .orElseThrow(IllegalArgumentException::new);

        SubTask subTask = task.createSubTask(subTaskName);
        return subTaskSaveService.save(subTask)
                .getId();
    }
}
