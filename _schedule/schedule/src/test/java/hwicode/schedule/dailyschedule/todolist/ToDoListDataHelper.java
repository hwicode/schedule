package hwicode.schedule.dailyschedule.todolist;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.domain.TaskCreateDto;

public class ToDoListDataHelper {

    // given절에서 사용됨, 테스트 중간에 사용되면 이미 존재하는 Task나 SubTask임
    public static final String TASK_NAME = "taskName";
    public static final String TASK_NAME2 = "taskName2";

    public static final String SUB_TASK_NAME = "subTaskName";
    public static final String SUB_TASK_NAME2 = "subTaskName2";


    // given절을 제외하고, Task나 SubTask를 생성해야 할 때만 사용
    public static final String NEW_TASK_NAME = "newTaskName";
    public static final String NEW_TASK_NAME2 = "newTaskName2";

    public static final String NEW_SUB_TASK_NAME = "newSubTaskName";
    public static final String NEW_SUB_TASK_NAME2 = "newSubTaskName2";


    // todolist Task create dto
    public static TaskCreateDto createTaskCreateDto(String taskName) {
        return new TaskCreateDto(taskName, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);
    }

    public static TaskCreateDto createTaskCreateDto(String taskName, Priority priority, Importance importance) {
        return new TaskCreateDto(taskName, Difficulty.NORMAL, priority, importance);
    }


    // todolist Task request dto
    public static TaskSaveRequest createTaskSaveRequest(String taskName) {
        return new TaskSaveRequest(taskName, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);
    }

    public static TaskInformationChangeRequest createTaskInformationChangeRequest(Priority priority, Importance importance) {
        return new TaskInformationChangeRequest(priority, importance);
    }


    // todolist SubTask request dto
    public static SubTaskSaveRequest createSubTaskSaveRequest(Long dailyToDoListId, String taskName, String subTaskName) {
        return new SubTaskSaveRequest(dailyToDoListId, taskName, subTaskName);
    }

    public static SubTaskDeleteRequest createSubTaskDeleteRequest(Long dailyToDoListId, String taskName) {
        return new SubTaskDeleteRequest(dailyToDoListId, taskName);
    }

}
