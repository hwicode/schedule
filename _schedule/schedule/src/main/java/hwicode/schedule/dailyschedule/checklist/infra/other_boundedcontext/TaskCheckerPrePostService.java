package hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext;

import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.dto.TaskCheckerAfterSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.TaskAggregateService;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskInformationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskCheckerPrePostService {

    private final TaskAggregateService taskAggregateService;
    private final List<TaskConstraintRemover> taskConstraintRemovers;

    public void performAfterSave(TaskCheckerAfterSaveRequest request) {
        TaskInformationCommand command = new TaskInformationCommand(
                request.getUserId(), request.getTaskCheckerId(), request.getPriority(), request.getImportance()
        );
        taskAggregateService.changeTaskInformation(command);
    }

    public Long performBeforeDelete(Long taskId) {
        deleteForeignKeyConstraint(taskId);
        return taskId;
    }

    // 하나의 테이블에 여러 개의 엔티티가 매핑되어 있다. 다른 바운디드 컨텍스트에서 Task 테이블과의 매핑을 제거하는 메서드
    private void deleteForeignKeyConstraint(Long taskId) {
        taskConstraintRemovers.forEach(
                taskConstraintRemover -> taskConstraintRemover.delete(taskId)
        );
    }

}
