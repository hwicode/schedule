package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskCheckerSaveOnlyRepository;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTasks;

@RequiredArgsConstructor
@Service
public class SubTaskCheckerService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final SubTaskCheckerSaveOnlyRepository subTaskCheckerSaveOnlyRepository;

    @Transactional
    public Long saveSubTask(SubTaskCheckerSaveRequest subTaskCheckerSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, subTaskCheckerSaveRequest.getDailyChecklistId());

        SubTaskChecker subTaskChecker = subTaskCheckerSaveRequest.toEntity();
        dailyChecklist.addSubTask(subTaskCheckerSaveRequest.getTaskName(), subTaskChecker);

        return subTaskCheckerSaveOnlyRepository.save(subTaskChecker)
                .getId();
    }

    @Transactional
    public void deleteSubTask(String subTaskName, SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, subTaskCheckerDeleteRequest.getDailyChecklistId());

        dailyChecklist.deleteSubTask(subTaskCheckerDeleteRequest.getTaskName(), subTaskName);
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
