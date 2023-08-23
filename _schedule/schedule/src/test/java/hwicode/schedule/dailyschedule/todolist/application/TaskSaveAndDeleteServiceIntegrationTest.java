package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.todolist.domain.*;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
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
    void 과제를_추가할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(dailyToDoList.getId(), TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        Long taskId = taskSaveAndDeleteService.save(taskSaveRequest);

        // then
        assertThat(taskRepository.existsById(taskId)).isTrue();
    }

    @Test
    void 과제를_삭제할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        Task savedTask = taskRepository.save(new Task(dailyToDoList, TASK_NAME));

        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(dailyToDoList.getId(), savedTask.getId(), TASK_NAME);

        // when
        taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest);

        // then
        assertThatThrownBy(() -> taskSaveAndDeleteService.delete(TASK_NAME, taskDeleteRequest))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }

}
