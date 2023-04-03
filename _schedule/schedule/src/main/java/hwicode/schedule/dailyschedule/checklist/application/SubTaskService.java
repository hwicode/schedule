package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskSaveOnlyRepository;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTasks;

@RequiredArgsConstructor
@Service
public class SubTaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final SubTaskSaveOnlyRepository subTaskSaveOnlyRepository;

    @Transactional
    public Long saveSubTask(SubTaskSaveRequest subTaskSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, subTaskSaveRequest.getDailyChecklistId());

        SubTask subTask = subTaskSaveRequest.toEntity();
        dailyChecklist.addSubTask(subTaskSaveRequest.getTaskName(), subTask);

        return subTaskSaveOnlyRepository.save(subTask)
                .getId();
    }

    @Transactional
    public void deleteSubTask(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, subTaskDeleteRequest.getDailyChecklistId());

        dailyChecklist.deleteSubTask(subTaskDeleteRequest.getTaskName(), subTaskName);
    }

    @Transactional
    public TaskStatus changeSubTaskStatus(String subTaskName, SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, subTaskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeSubTaskStatus(
                subTaskStatusModifyRequest.getTaskName(),
                subTaskName,
                subTaskStatusModifyRequest.getSubTaskStatus());
    }
}
