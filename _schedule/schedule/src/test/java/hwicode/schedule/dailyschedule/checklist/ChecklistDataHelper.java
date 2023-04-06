package hwicode.schedule.dailyschedule.checklist;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyResponse;

public class ChecklistDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long DAILY_CHECKLIST_ID = 1L;
    public static final Long TASK_ID = 2L;
    public static final Long SUB_TASK_ID = 3L;


    // 생성 메서드에서 사용됨. 테스트 중간에 사용되면 이미 존재하는 Task나 SubTask임
    public static final String TASK_NAME = "taskName";
    public static final String TASK_NAME2 = "taskName2";
    public static final String TASK_NAME3 = "taskName3";

    public static final String SUB_TASK_NAME = "subTaskName";
    public static final String SUB_TASK_NAME2 = "subTaskName2";


    // 생성 메서드를 제외하고, Task나 SubTask를 생성해야 할 때만 사용
    public static final String NEW_TASK_NAME = "newTaskName";
    public static final String NEW_SUB_TASK_NAME = "newSubTaskName";


    // checklist Task request dto
    public static TaskSaveRequest createTaskSaveRequest(Long dailyChecklistId, String taskName) {
        return new TaskSaveRequest(dailyChecklistId, taskName);
    }

    public static TaskStatusModifyRequest createTaskStatusModifyRequest(Long dailyChecklistId, TaskStatus taskStatus) {
        return new TaskStatusModifyRequest(dailyChecklistId, taskStatus);
    }

    public static TaskDifficultyModifyRequest createTaskDifficultyModifyRequest(Long dailyChecklistId, Difficulty difficulty) {
        return new TaskDifficultyModifyRequest(dailyChecklistId, difficulty);
    }

    public static TaskDeleteRequest createTaskDeleteRequest(Long dailyChecklistId) {
        return new TaskDeleteRequest(dailyChecklistId);
    }


    // checklist SubTask request dto
    public static SubTaskSaveRequest createSubTaskSaveRequest(Long dailyChecklistId, String taskName, String subTaskName) {
        return new SubTaskSaveRequest(dailyChecklistId, taskName, subTaskName);
    }

    public static SubTaskStatusModifyRequest createSubTaskStatusModifyRequest(Long dailyChecklistId, String taskName, SubTaskStatus subTaskStatus) {
        return new SubTaskStatusModifyRequest(dailyChecklistId, taskName, subTaskStatus);
    }

    public static SubTaskDeleteRequest createSubTaskDeleteRequest(Long dailyChecklistId, String taskName) {
        return new SubTaskDeleteRequest(dailyChecklistId, taskName);
    }


    // checklist Task response dto
    public static TaskSaveResponse createTaskSaveResponse(Long taskId, String taskName) {
        return new TaskSaveResponse(taskId, taskName);
    }

    public static TaskStatusModifyResponse createTaskStatusModifyResponse(String taskName, TaskStatus taskStatus) {
        return new TaskStatusModifyResponse(taskName, taskStatus);
    }

    public static TaskDifficultyModifyResponse createTaskDifficultyModifyResponse(String taskName, Difficulty modifiedDifficulty) {
        return new TaskDifficultyModifyResponse(taskName, modifiedDifficulty);
    }


    // checklist SubTask response dto
    public static SubTaskSaveResponse createSubTaskSaveResponse(Long subTaskId, String subtaskName) {
        return new SubTaskSaveResponse(subTaskId, subtaskName);
    }

    public static SubTaskStatusModifyResponse createSubTaskStatusModifyResponse(String subTaskName, TaskStatus taskStatus, SubTaskStatus subTaskStatus) {
        return new SubTaskStatusModifyResponse(subTaskName, taskStatus, subTaskStatus);
    }
}
