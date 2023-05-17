package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.TaskCheckerSaveRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.DailyChecklistFindService.findDailyChecklistWithTaskCheckers;

@RequiredArgsConstructor
@Service
public class TaskCheckerSubService {

    private final DailyChecklistFindRepository dailyChecklistFindRepository;
    private final TaskCheckerSaveRepository taskCheckerSaveRepository;

    @Transactional
    public Long saveTaskChecker(TaskCheckerSaveRequest taskCheckerSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistFindRepository, taskCheckerSaveRequest.getDailyChecklistId());

        TaskChecker taskChecker = taskCheckerSaveRequest.toEntity();
        dailyChecklist.addTaskChecker(taskChecker);

        return taskCheckerSaveRepository.save(taskChecker)
                .getId();
    }

    @Transactional
    public void deleteTaskChecker(Long dailyChecklistId, String taskCheckerName) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(dailyChecklistFindRepository, dailyChecklistId);
        dailyChecklist.deleteTaskChecker(taskCheckerName);
    }

    @Transactional
    public TaskStatus changeTaskStatus(String taskCheckerName, TaskStatusModifyRequest taskStatusModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistFindRepository, taskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskStatus(taskCheckerName, taskStatusModifyRequest.getTaskStatus());
    }

    @Transactional
    public Difficulty changeTaskDifficulty(String taskCheckerName, TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistFindRepository, taskDifficultyModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeDifficulty(taskCheckerName, taskDifficultyModifyRequest.getDifficulty());
    }

    @Transactional
    public String changeTaskCheckerName(String taskCheckerName, TaskCheckerNameModifyRequest taskCheckerNameModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistFindRepository, taskCheckerNameModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskCheckerName(taskCheckerName, taskCheckerNameModifyRequest.getNewTaskCheckerName());
    }

}
