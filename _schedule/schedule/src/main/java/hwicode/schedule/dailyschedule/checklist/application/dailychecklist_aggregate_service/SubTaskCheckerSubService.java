package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.exception.application.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.subtaskchecker.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.subtaskchecker.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.DailyChecklistFindService.findDailyChecklistWithTaskCheckers;

@RequiredArgsConstructor
@Service
public class SubTaskCheckerSubService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final TaskCheckerRepository taskCheckerRepository;
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

    @Transactional
    public String changeSubTaskCheckerName(String subTaskName, SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest) {
        TaskChecker taskChecker = taskCheckerRepository.findTaskCheckerWithSubTaskCheckers(subTaskCheckerNameModifyRequest.getTaskCheckerId())
                .orElseThrow(TaskCheckerNotFoundException::new);

        return taskChecker.changeSubTaskCheckerName(subTaskName, subTaskCheckerNameModifyRequest.getNewSubTaskCheckerName());
    }
}
