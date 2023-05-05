package hwicode.schedule.dailyschedule.todolist;

import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveResponse;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyResponse;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveResponse;

public class ToDoListDataHelper {

    // 단순히 id값으로 숫자가 필요할 때만 사용
    public static final Long DAILY_TO_DO_LIST_ID = 1L;
    public static final Long TASK_ID = 2L;
    public static final Long SUB_TASK_ID = 3L;

    // given절에서 사용됨, 테스트 중간에 사용되면 이미 존재하는 Task나 SubTask임
    public static final String TASK_NAME = "taskName";
    public static final String SUB_TASK_NAME = "subTaskName";


    // todolist DailyToDoList request dto
    public static DailyToDoListInformationChangeRequest createDailyToDoListInformationChangeRequest(String review, Emoji emoji) {
        return new DailyToDoListInformationChangeRequest(review, emoji);
    }


    // todolist Task request dto
    public static TaskSaveRequest createTaskSaveRequest(Long dailyToDoListId, String taskName) {
        return new TaskSaveRequest(dailyToDoListId, taskName, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);
    }

    public static TaskInformationModifyRequest createTaskInformationModifyRequest(Priority priority, Importance importance) {
        return new TaskInformationModifyRequest(priority, importance);
    }

    public static TaskDeleteRequest createTaskDeleteRequest(Long dailyToDoListId, Long taskId) {
        return new TaskDeleteRequest(dailyToDoListId, taskId);
    }


    // todolist SubTask request dto
    public static SubTaskSaveRequest createSubTaskSaveRequest(Long dailyToDoListId, String taskName, String subTaskName) {
        return new SubTaskSaveRequest(dailyToDoListId, taskName, subTaskName);
    }

    public static SubTaskDeleteRequest createSubTaskDeleteRequest(Long dailyToDoListId, String taskName) {
        return new SubTaskDeleteRequest(dailyToDoListId, taskName);
    }


    // todolist Task response dto
    public static TaskSaveResponse createTaskSaveResponse(Long taskId, String taskName) {
        return new TaskSaveResponse(taskId, taskName);
    }

    public static TaskInformationModifyResponse createTaskInformationModifyResponse(Long taskId, Priority priority, Importance importance) {
        return new TaskInformationModifyResponse(taskId, priority, importance);
    }


    // todolist SubTask response dto
    public static SubTaskSaveResponse createSubTaskSaveResponse(Long subTaskId, String subTaskName) {
        return new SubTaskSaveResponse(subTaskId, subTaskName);
    }
}
