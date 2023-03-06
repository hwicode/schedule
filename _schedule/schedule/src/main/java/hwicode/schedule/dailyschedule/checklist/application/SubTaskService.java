package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskSaveOnlyRepository;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTasks;

@Service
public class SubTaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final SubTaskSaveOnlyRepository subTaskSaveOnlyRepository;

    public SubTaskService(DailyChecklistRepository dailyChecklistRepository, SubTaskSaveOnlyRepository subTaskSaveOnlyRepository) {
        this.dailyChecklistRepository = dailyChecklistRepository;
        this.subTaskSaveOnlyRepository = subTaskSaveOnlyRepository;
    }

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
    public void changeSubTaskStatus(Long dailyChecklistId, String taskName, String subTaskName, Status status) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.changeSubTaskStatus(taskName, subTaskName, status);
    }
}
