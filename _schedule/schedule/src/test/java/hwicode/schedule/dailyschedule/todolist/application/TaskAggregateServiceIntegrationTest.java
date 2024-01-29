package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskInformationCommand;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.exception.domain.TaskForbiddenException;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
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
        Long userId = 1L;
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, userId);
        Task task = new Task(dailyToDoList, TASK_NAME, userId);
        dailyToDoListRepository.save(dailyToDoList);
        taskRepository.save(task);

        TaskInformationCommand command = new TaskInformationCommand(userId, task.getId(), Priority.FIRST, Importance.FIRST);

        // when
        taskAggregateService.changeTaskInformation(command);

        // then
        Task savedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(savedTask.changePriority(Priority.FIRST)).isFalse();
        assertThat(savedTask.changeImportance(Importance.FIRST)).isFalse();
    }

    @Test
    void ToDo_리스트에_있는_과제의_정보를_변경할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, userId);
        Task task = new Task(dailyToDoList, TASK_NAME, userId);
        dailyToDoListRepository.save(dailyToDoList);
        taskRepository.save(task);

        TaskInformationCommand command = new TaskInformationCommand(2L, task.getId(), Priority.FIRST, Importance.FIRST);

        // when then
        assertThatThrownBy(() -> taskAggregateService.changeTaskInformation(command))
                .isInstanceOf(TaskForbiddenException.class);
    }

    @Test
    void 존재하지_않는_과제를_조회하면_에러가_발생한다() {
        //given
        Long userId = 1L;
        Long noneExistId = 1L;

        TaskInformationCommand command = new TaskInformationCommand(userId, noneExistId, Priority.FIRST, Importance.FIRST);

        // when then
        assertThatThrownBy(() -> taskAggregateService.changeTaskInformation(command))
                .isInstanceOf(TaskNotExistException.class);
    }
}
