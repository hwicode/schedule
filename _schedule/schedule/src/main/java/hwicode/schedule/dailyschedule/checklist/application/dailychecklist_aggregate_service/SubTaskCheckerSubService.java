package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.SubTaskCheckerSaveRepository;
import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.SubTaskCheckerPrePostService;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubTaskCheckerSubService {

    private final DailyChecklistFindRepository dailyChecklistFindRepository;
    private final SubTaskCheckerSaveRepository subTaskCheckerSaveRepository;
    private final SubTaskCheckerPrePostService subTaskCheckerPrePostService;

    @Transactional
    public Long saveSubTaskChecker(SubTaskSaveRequest subTaskSaveRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(subTaskSaveRequest.getDailyChecklistId());

        SubTaskChecker subTaskChecker = dailyChecklist.createSubTaskChecker(
                subTaskSaveRequest.getTaskName(), subTaskSaveRequest.getSubTaskName()
        );

        return subTaskCheckerSaveRepository.save(subTaskChecker)
                .getId();
    }

    @Transactional
    public Long deleteSubTaskChecker(String subTaskCheckerName, SubTaskDeleteRequest subTaskDeleteRequest) {
        subTaskCheckerPrePostService.performBeforeDelete(subTaskDeleteRequest.getSubTaskId());

        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                subTaskDeleteRequest.getDailyChecklistId());
        dailyChecklist.deleteSubTaskChecker(subTaskDeleteRequest.getTaskName(), subTaskCheckerName);
        return subTaskDeleteRequest.getSubTaskId();
    }

    @Transactional
    public TaskStatus changeSubTaskStatus(String subTaskCheckerName, SubTaskStatusModifyRequest subTaskStatusModifyRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                subTaskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeSubTaskStatus(
                subTaskStatusModifyRequest.getTaskCheckerName(),
                subTaskCheckerName,
                subTaskStatusModifyRequest.getSubTaskStatus());
    }

}
