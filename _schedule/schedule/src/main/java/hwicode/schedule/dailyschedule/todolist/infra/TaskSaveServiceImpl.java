package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveService;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskSaveServiceImpl implements TaskSaveService {

    private final TaskCheckerService taskCheckerService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Long save(Long dailyToDoListId, TaskSaveRequest taskSaveRequest) {
        Long taskId = taskCheckerService.saveTask(
                createTaskCheckerSaveRequest(dailyToDoListId, taskSaveRequest)
        );

        Task task = taskRepository.findById(taskId)
                .orElseThrow(IllegalArgumentException::new);

        task.initialize(taskSaveRequest.getPriority(), taskSaveRequest.getImportance());
        return taskId;
    }

    private TaskCheckerSaveRequest createTaskCheckerSaveRequest(Long dailyToDoListId, TaskSaveRequest taskSaveRequest) {
        return new TaskCheckerSaveRequest(
                dailyToDoListId,
                taskSaveRequest.getTaskName(),
                taskSaveRequest.getDifficulty());
    }
}
