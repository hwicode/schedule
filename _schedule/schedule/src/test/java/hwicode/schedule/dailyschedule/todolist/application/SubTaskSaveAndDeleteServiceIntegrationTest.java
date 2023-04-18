package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.exception.application.NotValidExternalRequestException;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SubTaskSaveAndDeleteServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

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
    void ToDo_리스트에_서브_과제를_추가할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);
        taskRepository.save(new Task(dailyToDoList, TASK_NAME));

        SubTaskSaveRequest subTaskSaveRequest = createSubTaskSaveRequest(dailyToDoList.getId(), TASK_NAME, SUB_TASK_NAME);

        // when
        Long subTaskId = subTaskSaveAndDeleteService.save(subTaskSaveRequest);

        // then
        assertThat(subTaskRepository.existsById(subTaskId)).isTrue();
    }

    @Test
    void ToDo_리스트에_서브_과제를_삭제할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        Task task = new Task(dailyToDoList, TASK_NAME);
        SubTask subTask = new SubTask(task, SUB_TASK_NAME);

        dailyToDoListRepository.save(dailyToDoList);
        taskRepository.save(task);
        subTaskRepository.save(subTask);

        SubTaskDeleteRequest subTaskDeleteRequest = createSubTaskDeleteRequest(dailyToDoList.getId(), TASK_NAME);

        // when
        subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest);

        // then
        assertThatThrownBy(() -> subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest))
                .isInstanceOf(NotValidExternalRequestException.class);
    }
}
