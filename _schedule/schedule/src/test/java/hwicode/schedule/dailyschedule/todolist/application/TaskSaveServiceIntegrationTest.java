package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TaskSaveServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskSaveService taskSaveService;

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

        TaskSaveRequest taskSaveRequest = new TaskSaveRequest("TASK_NAME", Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        Long taskId = taskSaveService.save(dailyToDoList.getId(), taskSaveRequest);

        // then
        assertThat(taskRepository.existsById(taskId)).isTrue();
    }
}
