package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.common.exception.BusinessException;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.application.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeService;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
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
    private final LearningTimeService learningTimeService;

    @Override
    @Transactional
    public Long save(TaskSaveRequest taskSaveRequest) {
        Long taskId = saveTaskChecker(taskSaveRequest);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotExistException::new);

        task.initialize(taskSaveRequest.getPriority(), taskSaveRequest.getImportance());
        return taskId;
    }

    private Long saveTaskChecker(TaskSaveRequest taskSaveRequest) {
        try {
            return taskCheckerService.saveTaskChecker(
                    createTaskCheckerSaveRequest(taskSaveRequest)
            );
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException(e);
        }
    }

    private TaskCheckerSaveRequest createTaskCheckerSaveRequest(TaskSaveRequest taskSaveRequest) {
        return new TaskCheckerSaveRequest(
                taskSaveRequest.getDailyToDoListId(),
                taskSaveRequest.getTaskName(),
                taskSaveRequest.getDifficulty());
    }

    @Override
    @Transactional
    public void delete(String taskName, TaskDeleteRequest taskDeleteRequest) {
        learningTimeService.deleteSubjectOfTaskBelongingToLearningTime(taskDeleteRequest.getTaskId());
        deleteTaskChecker(taskName, taskDeleteRequest);
    }

    private void deleteTaskChecker(String taskName, TaskDeleteRequest taskDeleteRequest) {
        try {
            taskCheckerService.deleteTaskChecker(
                    taskDeleteRequest.getDailyToDoListId(),
                    taskName
            );
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException(e);
        }
    }
}
