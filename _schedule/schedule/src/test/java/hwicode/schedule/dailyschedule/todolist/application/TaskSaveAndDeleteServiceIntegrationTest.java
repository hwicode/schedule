package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TaskSaveAndDeleteServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void ToDo_리스트에_과제를_추가할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoListRepository.save(dailyToDoList);

        TaskSaveRequest taskSaveRequest = createTaskSaveRequest(dailyToDoList.getId(), TASK_NAME);

        // when
        Long taskId = taskSaveAndDeleteService.save(taskSaveRequest);

        // then
        assertThat(taskRepository.existsById(taskId)).isTrue();
    }

    @Test
    void ToDo_리스트에_과제를_삭제할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoListRepository.save(dailyToDoList);
        taskRepository.save(new Task(dailyToDoList, TASK_NAME));

        TaskDeleteRequest taskDeleteRequest = createTaskDeleteRequest(dailyToDoList.getId());

        // when
        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);

        // then
        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }
}
