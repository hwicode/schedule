package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskSaveAndDeleteServiceImpl implements TaskSaveAndDeleteService {

    private final TaskCheckerService taskCheckerService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Long save(TaskSaveRequest taskSaveRequest) {
        Long taskId = taskCheckerService.saveTaskChecker(
                createTaskCheckerSaveRequest(taskSaveRequest)
        );

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotExistException::new);

        task.initialize(taskSaveRequest.getPriority(), taskSaveRequest.getImportance());
        return taskId;
    }

    private TaskCheckerSaveRequest createTaskCheckerSaveRequest(TaskSaveRequest taskSaveRequest) {
        return new TaskCheckerSaveRequest(
                taskSaveRequest.getDailyChecklistId(),
                taskSaveRequest.getTaskName(),
                taskSaveRequest.getDifficulty());
    }

    @Override
    @Transactional
    public void delete(String taskName, TaskDeleteRequest taskDeleteRequest) {
        taskCheckerService.deleteTaskChecker(
                taskDeleteRequest.getDailyToDoListId(),
                taskName
        );
    }
}
