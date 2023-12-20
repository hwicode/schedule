package hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext;

import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.TaskAggregateService;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskCheckerPostService {

    private final TaskAggregateService taskAggregateService;

    public void perform(Long taskId, TaskSaveRequest taskSaveRequest) {
        TaskInformationModifyRequest taskInformationModifyRequest = new TaskInformationModifyRequest(taskSaveRequest.getPriority(), taskSaveRequest.getImportance());
        taskAggregateService.changeTaskInformation(taskId, taskInformationModifyRequest);
    }

}
