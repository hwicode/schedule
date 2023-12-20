package hwicode.schedule.dailyschedule.todolist.infra.service_impl;

import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
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
    public Long save(hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest taskSaveRequest) {
        return null;
    }
//        Long taskId = saveTaskChecker(taskSaveRequest);
//
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(TaskNotExistException::new);
//        task.initialize(taskSaveRequest.getPriority(), taskSaveRequest.getImportance());
//        return taskId;
//    }
//
//    // Task를 생성하게 되면 Checklist 바운디드 컨텍스트에 영향을 주게 됨
//    private Long saveTaskChecker(hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest taskSaveRequest) {
//        TaskSaveRequest taskCheckerSaveRequest = createTaskCheckerSaveRequest(taskSaveRequest);
//        return taskCheckerSubService.saveTaskChecker(taskCheckerSaveRequest);
//    }
//
//    private TaskSaveRequest createTaskCheckerSaveRequest(hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest taskSaveRequest) {
//        return new TaskSaveRequest(
//                taskSaveRequest.getDailyToDoListId(),
//                taskSaveRequest.getTaskName(),
//                taskSaveRequest.getDifficulty());
//    }

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
        taskCheckerSubService.deleteTaskChecker(
                taskDeleteRequest.getDailyToDoListId(), taskName
        );
    }

}
