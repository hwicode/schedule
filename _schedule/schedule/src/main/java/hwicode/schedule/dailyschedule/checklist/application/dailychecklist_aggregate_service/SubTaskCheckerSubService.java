package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.subtaskchecker.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.subtaskchecker.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistFindRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskCheckerSaveRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.DailyChecklistFindService.findDailyChecklistWithTaskCheckers;

@RequiredArgsConstructor
@Service
public class SubTaskCheckerSubService {

    private final DailyChecklistFindRepository dailyChecklistFindRepository;
    private final SubTaskCheckerSaveRepository subTaskCheckerSaveRepository;

    @Transactional
    public Long saveSubTaskChecker(SubTaskCheckerSaveRequest subTaskCheckerSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistFindRepository, subTaskCheckerSaveRequest.getDailyChecklistId());

        SubTaskChecker subTaskChecker = subTaskCheckerSaveRequest.toEntity();
        dailyChecklist.addSubTaskChecker(subTaskCheckerSaveRequest.getTaskCheckerName(), subTaskChecker);

        return subTaskCheckerSaveRepository.save(subTaskChecker)
                .getId();
    }

    @Transactional
    public void deleteSubTaskChecker(String subTaskCheckerName, SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistFindRepository, subTaskCheckerDeleteRequest.getDailyChecklistId());

        dailyChecklist.deleteSubTaskChecker(subTaskCheckerDeleteRequest.getTaskCheckerName(), subTaskCheckerName);
    }

    @Transactional
    public TaskStatus changeSubTaskStatus(String subTaskCheckerName, SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistFindRepository, subTaskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeSubTaskStatus(
                subTaskStatusModifyRequest.getTaskCheckerName(),
                subTaskCheckerName,
                subTaskStatusModifyRequest.getSubTaskStatus());
    }

}
