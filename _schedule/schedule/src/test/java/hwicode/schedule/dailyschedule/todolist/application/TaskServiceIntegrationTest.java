package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.domain.*;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class TaskServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskService taskService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SubTaskRepository subTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void ToDo_리스트에_있는_과제의_정보를_변경할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        Task task = dailyToDoList.createTask(createTaskCreateDto(TASK_NAME, Priority.SECOND, Importance.SECOND));
        dailyToDoListRepository.save(dailyToDoList);

        TaskInformationChangeRequest taskInformationChangeRequest = createTaskInformationChangeRequest(Priority.FIRST, Importance.FIRST);

        // when
        taskService.changeTaskInformation(task.getId(), taskInformationChangeRequest);

        // then
        Task savedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(savedTask.changePriority(Priority.FIRST)).isFalse();
        assertThat(savedTask.changeImportance(Importance.FIRST)).isFalse();
    }

    @Test
    void ToDo_리스트에_있는_서브_과제의_이름을_변경할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        Task task = dailyToDoList.createTask(createTaskCreateDto(TASK_NAME));
        SubTask subTask = task.createSubTask(SUB_TASK_NAME);
        dailyToDoListRepository.save(dailyToDoList);

        // when
        String newSubTaskName = taskService.changeSubTaskName(task.getId(), SUB_TASK_NAME, NEW_SUB_TASK_NAME);

        // then
        SubTask savedSubTask = subTaskRepository.findById(subTask.getId()).orElseThrow();
        assertThat(savedSubTask.getName()).isEqualTo(newSubTaskName);
    }
}
