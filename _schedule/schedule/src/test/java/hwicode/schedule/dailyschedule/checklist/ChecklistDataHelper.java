package hwicode.schedule.dailyschedule.checklist;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.application.dto.subtaskchecker.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.application.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;

public class ChecklistDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long DAILY_CHECKLIST_ID = 1L;
    public static final Long TASK_CHECKER_ID = 2L;
    public static final Long SUB_TASK_CHECKER_ID = 3L;


    // 생성 메서드에서 사용됨. 테스트 중간에 사용되면 이미 존재하는 TaskChecker나 SubTaskChecker임
    public static final String TASK_CHECKER_NAME = "taskCheckerName";
    public static final String TASK_CHECKER_NAME2 = "taskCheckerName2";
    public static final String TASK_CHECKER_NAME3 = "taskCheckerName3";

    public static final String SUB_TASK_CHECKER_NAME = "subTaskCheckerName";
    public static final String SUB_TASK_CHECKER_NAME2 = "subTaskCheckerName2";


    // 생성 메서드를 제외하고, TaskChecker나 SubTaskChecker를 생성해야 할 때만 사용
    public static final String NEW_TASK_CHECKER_NAME = "newTaskCheckerName";
    public static final String NEW_SUB_TASK_CHECKER_NAME = "newSubTaskCheckerName";


    // checklist TaskChecker request dto
    public static TaskCheckerSaveRequest createTaskCheckerSaveRequest(Long dailyChecklistId, String taskCheckerName, Difficulty difficulty) {
        return new TaskCheckerSaveRequest(dailyChecklistId, taskCheckerName, difficulty);
    }

    public static TaskStatusModifyRequest createTaskStatusModifyRequest(Long dailyChecklistId, String taskCheckerName, TaskStatus taskStatus) {
        return new TaskStatusModifyRequest(dailyChecklistId, taskCheckerName, taskStatus);
    }

    public static TaskDifficultyModifyRequest createTaskDifficultyModifyRequest(Long dailyChecklistId, String taskCheckerName, Difficulty difficulty) {
        return new TaskDifficultyModifyRequest(dailyChecklistId, taskCheckerName, difficulty);
    }

    public static TaskCheckerNameModifyRequest createTaskCheckerNameModifyRequest(Long dailyChecklistId, String taskCheckerName, String newTaskCheckerName) {
        return new TaskCheckerNameModifyRequest(dailyChecklistId, taskCheckerName, newTaskCheckerName);
    }


    // checklist SubTaskChecker request dto
    public static SubTaskCheckerSaveRequest createSubTaskCheckerSaveRequest(Long dailyChecklistId, String taskCheckerName, String subTaskCheckerName) {
        return new SubTaskCheckerSaveRequest(dailyChecklistId, taskCheckerName, subTaskCheckerName);
    }

    public static SubTaskStatusModifyRequest createSubTaskStatusModifyRequest(Long dailyChecklistId, String taskCheckerName, String subTaskCheckerName, SubTaskStatus subTaskStatus) {
        return new SubTaskStatusModifyRequest(dailyChecklistId, taskCheckerName, subTaskCheckerName, subTaskStatus);
    }

    public static SubTaskCheckerDeleteRequest createSubTaskCheckerDeleteRequest(Long dailyChecklistId, String taskCheckerName) {
        return new SubTaskCheckerDeleteRequest(dailyChecklistId, taskCheckerName);
    }

    public static SubTaskCheckerNameModifyRequest createSubTaskCheckerNameModifyRequest(Long taskCheckId, String subTaskCheckerName, String newSubTaskCheckerName) {
        return new SubTaskCheckerNameModifyRequest(taskCheckId, subTaskCheckerName, newSubTaskCheckerName);
    }


    // checklist TaskChecker response dto
    public static TaskStatusModifyResponse createTaskStatusModifyResponse(String taskCheckerName, TaskStatus taskStatus) {
        return new TaskStatusModifyResponse(taskCheckerName, taskStatus);
    }

    public static TaskDifficultyModifyResponse createTaskDifficultyModifyResponse(String taskCheckerName, Difficulty modifiedDifficulty) {
        return new TaskDifficultyModifyResponse(taskCheckerName, modifiedDifficulty);
    }

    public static TaskCheckerNameModifyResponse createTaskCheckerNameModifyResponse(Long dailyChecklistId, String newTaskCheckerName) {
        return new TaskCheckerNameModifyResponse(dailyChecklistId, newTaskCheckerName);
    }


    // checklist SubTaskChecker response dto
    public static SubTaskStatusModifyResponse createSubTaskStatusModifyResponse(String subTaskCheckerName, TaskStatus taskStatus, SubTaskStatus subTaskStatus) {
        return new SubTaskStatusModifyResponse(subTaskCheckerName, taskStatus, subTaskStatus);
    }

    public static SubTaskCheckerNameModifyResponse createSubTaskCheckerNameModifyResponse(Long TaskCheckerId, String newSubTaskCheckerName) {
        return new SubTaskCheckerNameModifyResponse(TaskCheckerId, newSubTaskCheckerName);
    }
}
