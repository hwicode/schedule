package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.domain.TaskSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskSaveServiceImpl implements TaskSaveService {

    private final TaskCheckerService taskCheckerService;
    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        Task savedTask = taskRepository.save(task);
        taskCheckerService.saveTask(
                createTaskCheckerSaveRequest(task)
        );
        return savedTask;
    }

    public TaskCheckerSaveRequest createTaskCheckerSaveRequest(Task task) {
        return new TaskCheckerSaveRequest(
                task.getDailyToDoListId(),
                task.getName()
        );
    }
}
