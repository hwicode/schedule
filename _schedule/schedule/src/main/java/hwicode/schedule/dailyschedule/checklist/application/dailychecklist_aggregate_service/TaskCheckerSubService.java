package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.TaskCheckerSaveRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskCheckerSubService {

    private final DailyChecklistFindRepository dailyChecklistFindRepository;
    private final TaskCheckerSaveRepository taskCheckerSaveRepository;

    @Transactional
    public Long saveTaskChecker(TaskCheckerSaveRequest taskCheckerSaveRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskCheckerSaveRequest.getDailyChecklistId());

        TaskChecker taskChecker = dailyChecklist.createTaskChecker(
                taskCheckerSaveRequest.getTaskCheckerName(), taskCheckerSaveRequest.getDifficulty()
        );

        return taskCheckerSaveRepository.save(taskChecker)
                .getId();
    }

    @Transactional
    public void deleteTaskChecker(Long dailyChecklistId, String taskCheckerName) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(dailyChecklistId);
        dailyChecklist.deleteTaskChecker(taskCheckerName);
    }

    @Transactional
    public TaskStatus changeTaskStatus(String taskCheckerName, TaskStatusModifyRequest taskStatusModifyRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskStatus(taskCheckerName, taskStatusModifyRequest.getTaskStatus());
    }

    @Transactional
    public Difficulty changeTaskDifficulty(String taskCheckerName, TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskDifficultyModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeDifficulty(taskCheckerName, taskDifficultyModifyRequest.getDifficulty());
    }

    @Transactional
    public String changeTaskCheckerName(String taskCheckerName, TaskCheckerNameModifyRequest taskCheckerNameModifyRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskCheckerNameModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskCheckerName(taskCheckerName, taskCheckerNameModifyRequest.getNewTaskCheckerName());
    }

}
