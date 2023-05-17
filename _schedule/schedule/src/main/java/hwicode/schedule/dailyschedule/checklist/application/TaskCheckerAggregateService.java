package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.application.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskCheckerAggregateService {

    private final TaskCheckerRepository taskCheckerRepository;

    @Transactional
    public String changeSubTaskCheckerName(String subTaskName, SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest) {
        TaskChecker taskChecker = taskCheckerRepository.findTaskCheckerWithSubTaskCheckers(subTaskCheckerNameModifyRequest.getTaskCheckerId())
                .orElseThrow(TaskCheckerNotFoundException::new);

        return taskChecker.changeSubTaskCheckerName(subTaskName, subTaskCheckerNameModifyRequest.getNewSubTaskCheckerName());
    }
}
