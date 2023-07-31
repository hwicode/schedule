package hwicode.schedule.dailyschedule.todolist.infra.service_impl;

import hwicode.schedule.common.exception.BusinessException;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.TaskCheckerSaveRequest;
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

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskSaveAndDeleteServiceImpl implements TaskSaveAndDeleteService {

    private final TaskCheckerSubService taskCheckerSubService;
    private final TaskRepository taskRepository;

    private final List<TaskConstraintRemover> taskConstraintRemovers;

    @Override
    @Transactional
    public Long save(TaskSaveRequest taskSaveRequest) {
        Long taskId = saveTaskChecker(taskSaveRequest);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskNotExistException::new);
        task.initialize(taskSaveRequest.getPriority(), taskSaveRequest.getImportance());
        return taskId;
    }

    // Task를 생성하게 되면 Checklist 바운디드 컨텍스트에 영향을 주게 됨
    private Long saveTaskChecker(TaskSaveRequest taskSaveRequest) {
        try {
            TaskCheckerSaveRequest taskCheckerSaveRequest = createTaskCheckerSaveRequest(taskSaveRequest);
            return taskCheckerSubService.saveTaskChecker(taskCheckerSaveRequest);
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException(
                    List.of(e.getMessage())
            );
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
        Long taskId = taskDeleteRequest.getTaskId();
        deleteForeignKeyConstraint(taskId);
        deleteTaskChecker(taskName, taskDeleteRequest);
        return taskId;
    }

    // 하나의 테이블에 여러 개의 엔티티가 매핑되어 있다. 다른 바운디드 컨텍스트에서 Task 테이블과의 매핑을 제거하는 메서드
    private void deleteForeignKeyConstraint(Long taskId) {
        taskConstraintRemovers.forEach(
                taskConstraintRemover -> taskConstraintRemover.delete(taskId)
        );
    }

    private void deleteTaskChecker(String taskName, TaskDeleteRequest taskDeleteRequest) {
        try {
            taskCheckerSubService.deleteTaskChecker(
                    taskDeleteRequest.getDailyToDoListId(),
                    taskName
            );
        } catch (BusinessException e) {
            throw new NotValidExternalRequestException(
                    List.of(e.getMessage())
            );
        }
    }
}
