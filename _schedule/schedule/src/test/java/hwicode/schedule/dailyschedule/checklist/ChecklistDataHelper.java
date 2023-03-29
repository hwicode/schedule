package hwicode.schedule.dailyschedule.checklist;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify.TaskStatusModifyRequest;

public class ChecklistDataHelper {

    public static final Long DAILY_CHECKLIST_ID = 1L;
    public static final Long TASK_ID = 2L;
    public static final Long SUB_TASK_ID = 3L;

    public static final String TASK_NAME = "taskName";
    public static final String TASK_NAME2 = "taskName2";
    public static final String TASK_NAME3 = "taskName3";

    public static final String SUB_TASK_NAME = "subTaskName";
    public static final String SUB_TASK_NAME2 = "subTaskName2";

    public static final String NEW_TASK_NAME = "newTaskName";
    public static final String NEW_SUB_TASK_NAME = "newSubTaskName";

    // checklist request dto
    public static TaskSaveRequest createTaskSaveRequest(Long dailyChecklistId, String taskName) {
        return new TaskSaveRequest(dailyChecklistId, taskName);
    }

    public static TaskStatusModifyRequest createTaskStatusModifyRequest(Long dailyChecklistId, Status status) {
        return new TaskStatusModifyRequest(dailyChecklistId, status);
    }

    public static TaskDifficultyModifyRequest createTaskDifficultyModifyRequest(Long dailyChecklistId, Difficulty difficulty) {
        return new TaskDifficultyModifyRequest(dailyChecklistId, difficulty);
    }

    public static TaskDeleteRequest createTaskDeleteRequest(Long dailyChecklistId) {
        return new TaskDeleteRequest(dailyChecklistId);
    }

    public static SubTaskSaveRequest createSubTaskSaveRequest(Long dailyChecklistId, String taskName, String subTaskName) {
        return new SubTaskSaveRequest(dailyChecklistId, taskName, subTaskName);
    }

    public static SubTaskStatusModifyRequest createSubTaskStatusModifyRequest(Long dailyChecklistId, String taskName, Status status) {
        return new SubTaskStatusModifyRequest(dailyChecklistId, taskName, status);
    }

    public static SubTaskDeleteRequest createSubTaskDeleteRequest(Long dailyChecklistId, String taskName) {
        return new SubTaskDeleteRequest(dailyChecklistId, taskName);
    }

}
