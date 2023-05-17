package hwicode.schedule.dailyschedule.todolist.infra.service_impl;

import hwicode.schedule.common.exception.BusinessException;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.timetable.application.LearningTimeConstraintRemovalService;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskSaveAndDeleteServiceImpl implements TaskSaveAndDeleteService {

    private final TaskCheckerSubService taskCheckerSubService;
    private final TaskRepository taskRepository;
    private final LearningTimeConstraintRemovalService learningTimeConstraintRemovalService;

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
            return taskCheckerSubService.saveTaskChecker(
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
    public Long delete(String taskName, TaskDeleteRequest taskDeleteRequest) {
        learningTimeConstraintRemovalService.deleteSubjectOfTaskBelongingToLearningTime(taskDeleteRequest.getTaskId());
        deleteTaskChecker(taskName, taskDeleteRequest);
        return taskDeleteRequest.getTaskId();
    }

    private void deleteTaskChecker(String taskName, TaskDeleteRequest taskDeleteRequest) {
        try {
            taskCheckerSubService.deleteTaskChecker(
                    taskDeleteRequest.getDailyToDoListId(),
                    taskName
            );
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException(e);
        }
    }
}
