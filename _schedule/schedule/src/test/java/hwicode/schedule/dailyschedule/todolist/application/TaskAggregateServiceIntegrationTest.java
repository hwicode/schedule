package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.todolist.domain.*;
import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TaskAggregateServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskAggregateService taskAggregateService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void ToDo_리스트에_있는_과제의_정보를_변경할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        Task task = new Task(dailyToDoList, TASK_NAME);
        dailyToDoListRepository.save(dailyToDoList);
        taskRepository.save(task);

        TaskInformationModifyRequest taskInformationModifyRequest = new TaskInformationModifyRequest(Priority.FIRST, Importance.FIRST);

        // when
        taskAggregateService.changeTaskInformation(task.getId(), taskInformationModifyRequest);

        // then
        Task savedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(savedTask.changePriority(Priority.FIRST)).isFalse();
        assertThat(savedTask.changeImportance(Importance.FIRST)).isFalse();
    }

    @Test
    void 존재하지_않는_과제를_조회하면_에러가_발생한다() {
        //given
        Long noneExistId = 1L;

        TaskInformationModifyRequest taskInformationModifyRequest = new TaskInformationModifyRequest(Priority.FIRST, Importance.FIRST);

        // when then
        assertThatThrownBy(() -> taskAggregateService.changeTaskInformation(noneExistId, taskInformationModifyRequest))
                .isInstanceOf(TaskNotExistException.class);
    }
}
