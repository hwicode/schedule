package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
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

        TaskSaveRequest taskSaveRequest = createTaskSaveRequest(TASK_NAME);

        // when
        Long taskId = taskSaveAndDeleteService.save(dailyToDoList.getId(), taskSaveRequest);

        // then
        assertThat(taskRepository.existsById(taskId)).isTrue();
    }

    @Test
    void ToDo_리스트에_과제를_삭제할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoList.createTask(createTaskCreateDto(TASK_NAME));
        dailyToDoListRepository.save(dailyToDoList);

        // when
        taskSaveAndDeleteService.delete(dailyToDoList.getId(), TASK_NAME);

        // then
        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(dailyToDoList.getId(), TASK_NAME))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }
}
