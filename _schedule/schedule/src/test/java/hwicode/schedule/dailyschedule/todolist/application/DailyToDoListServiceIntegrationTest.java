package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.todolist.application.dto.DailyToDoListInformationChangeRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.name_modify.TaskNameModifyRequest;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import hwicode.schedule.dailyschedule.todolist.infra.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DailyToDoListServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyToDoListService dailyToDoListService;

    @Autowired
    DailyToDoListRepository dailyToDoListRepository;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void ToDo_리스트의_정보를_변경할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);
        dailyToDoListRepository.save(dailyToDoList);

        String review = "좋았다!";
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = createDailyToDoListInformationChangeRequest(review, Emoji.GOOD);

        // when
        dailyToDoListService.changeDailyToDoListInformation(dailyToDoList.getId(), dailyToDoListInformationChangeRequest);

        // then
        DailyToDoList savedDailyToDoList = dailyToDoListRepository.findById(dailyToDoList.getId()).orElseThrow();
        assertThat(savedDailyToDoList.writeReview(review)).isFalse();
        assertThat(savedDailyToDoList.changeTodayEmoji(Emoji.GOOD)).isFalse();
    }

    @Test
    void ToDo_리스트에_있는_과제의_이름을_변경할_수_있다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        Task task = dailyToDoList.createTask(createTaskCreateDto(TASK_NAME));
        dailyToDoListRepository.save(dailyToDoList);

        TaskNameModifyRequest taskNameModifyRequest = createTaskNameModifyRequest(dailyToDoList.getId(), NEW_TASK_NAME);

        // when
        dailyToDoListService.changeTaskName(TASK_NAME, taskNameModifyRequest);

        // then
        Task savedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(savedTask.getName()).isEqualTo(NEW_TASK_NAME);
    }
}
