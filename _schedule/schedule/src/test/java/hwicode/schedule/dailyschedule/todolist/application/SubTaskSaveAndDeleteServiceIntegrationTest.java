package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.SubTaskRepository;
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
    SubTaskRepository subTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void ToDo_리스트에_서브_과제를_추가할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoList.createTask(createTaskCreateDto(TASK_NAME));
        dailyToDoListRepository.save(dailyToDoList);

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyToDoList.getId(), TASK_NAME, SUB_TASK_NAME);

        // when
        Long subTaskId = subTaskSaveAndDeleteService.save(subTaskSaveRequest);

        // then
        assertThat(subTaskRepository.existsById(subTaskId)).isTrue();
    }

    @Test
    void ToDo_리스트에_서브_과제를_삭제할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        Task task = dailyToDoList.createTask(createTaskCreateDto(TASK_NAME));
        task.createSubTask(SUB_TASK_NAME);
        dailyToDoListRepository.save(dailyToDoList);

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(dailyToDoList.getId(), TASK_NAME);

        // when
        subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest);

        // then
        assertThatThrownBy(() -> subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
    }
}
