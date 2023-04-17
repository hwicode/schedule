package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskCheckerSaveOnlyRepository;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTaskCheckers;

@RequiredArgsConstructor
@Service
public class SubTaskCheckerService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final SubTaskCheckerSaveOnlyRepository subTaskCheckerSaveOnlyRepository;

    @Transactional
    public Long saveSubTaskChecker(SubTaskCheckerSaveRequest subTaskCheckerSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, subTaskCheckerSaveRequest.getDailyChecklistId());

        SubTaskChecker subTaskChecker = subTaskCheckerSaveRequest.toEntity();
        dailyChecklist.addSubTaskChecker(subTaskCheckerSaveRequest.getTaskCheckerName(), subTaskChecker);

        return subTaskCheckerSaveOnlyRepository.save(subTaskChecker)
                .getId();
    }

    @Transactional
    public void deleteSubTaskChecker(String subTaskCheckerName, SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, subTaskCheckerDeleteRequest.getDailyChecklistId());

        dailyChecklist.deleteSubTaskChecker(subTaskCheckerDeleteRequest.getTaskCheckerName(), subTaskCheckerName);
    }

    @Transactional
    public TaskStatus changeSubTaskStatus(String subTaskCheckerName, SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, subTaskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeSubTaskStatus(
                subTaskStatusModifyRequest.getTaskCheckerName(),
                subTaskCheckerName,
                subTaskStatusModifyRequest.getSubTaskStatus());
    }
}
