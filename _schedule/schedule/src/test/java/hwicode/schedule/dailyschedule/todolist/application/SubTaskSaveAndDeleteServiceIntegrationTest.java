package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.SubTaskRepository;
import hwicode.schedule.dailyschedule.todolist.infra.jpa_repository.TaskRepository;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.SUB_TASK_NAME;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
import static hwicode.schedule.timetable.TimeTableDataHelper.NEW_SUBJECT;
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

        Task task = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
        subTaskRepository.save(new SubTask(task, SUB_TASK_NAME));

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyToDoList.getId(), TASK_NAME, NEW_SUBJECT);

        // when
        Long subTaskId = subTaskSaveAndDeleteService.save(subTaskSaveRequest);

        // then
        assertThat(subTaskRepository.existsById(subTaskId)).isTrue();
    }

    @Test
    void ToDo_리스트에_서브_과제를_삭제할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        Task task = taskRepository.save(new Task(dailyToDoList, TASK_NAME));
        SubTask savedSubTask = subTaskRepository.save(new SubTask(task, SUB_TASK_NAME));

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(dailyToDoList.getId(), TASK_NAME, savedSubTask.getId(), SUB_TASK_NAME);

        // when
        subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest);

        // then
        assertThatThrownBy(() -> subTaskSaveAndDeleteService.delete(SUB_TASK_NAME, subTaskDeleteRequest))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
    }

}
