package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.common.login.validator.PermissionValidator;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskNameModifyCommand;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.TaskCheckerFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskCheckerAggregateService {

    private final TaskCheckerFindRepository taskCheckerFindRepository;

    @Transactional
    public String changeSubTaskCheckerName(SubTaskNameModifyCommand command) {
        TaskChecker taskChecker = taskCheckerFindRepository.findTaskCheckerWithSubTaskCheckers(command.getTaskCheckerId());
        PermissionValidator.validateOwnership(command.getUserId(), taskChecker.getUserId());

        return taskChecker.changeSubTaskCheckerName(command.getSubTaskCheckerName(), command.getNewSubTaskCheckerName());
    }
}
