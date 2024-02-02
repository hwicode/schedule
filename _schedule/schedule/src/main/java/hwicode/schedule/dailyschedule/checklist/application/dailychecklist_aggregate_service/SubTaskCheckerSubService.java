package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.common.login.validator.PermissionValidator;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskDeleteCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskSaveCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskStatusModifyCommand;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.SubTaskCheckerSaveRepository;
import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.SubTaskCheckerPrePostService;
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
    public Long saveSubTaskChecker(SubTaskSaveCommand command) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        SubTaskChecker subTaskChecker = dailyChecklist.createSubTaskChecker(command.getTaskCheckerName(), command.getSubTaskCheckerName());
        return subTaskCheckerSaveRepository.save(subTaskChecker)
                .getId();
    }

    @Transactional
    public Long deleteSubTaskChecker(SubTaskDeleteCommand command) {
        subTaskCheckerPrePostService.performBeforeDelete(command.getSubTaskCheckerId());

        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        dailyChecklist.deleteSubTaskChecker(command.getTaskCheckerName(), command.getSubTaskCheckerName());
        return command.getSubTaskCheckerId();
    }

    @Transactional
    public TaskStatus changeSubTaskStatus(SubTaskStatusModifyCommand command) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        return dailyChecklist.changeSubTaskStatus(
                command.getTaskCheckerName(),
                command.getSubTaskCheckerName(),
                command.getSubTaskStatus());
    }

}
