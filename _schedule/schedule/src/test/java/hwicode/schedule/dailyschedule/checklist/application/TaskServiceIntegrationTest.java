package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify.TaskStatusModifyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TaskServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskService taskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    final String NAME = "name";
    final String NAME2 = "name2";
    final String NEW = "new";

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    DailyChecklist createDailyChecklistWithTwoTask() {
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task(NAME));
        dailyChecklist.addTask(new Task(NAME2));

        return dailyChecklist;
    }

    @Test
    public void 체크리스트에_과제를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(dailyChecklist.getId(), NEW);

        // when
        taskService.saveTask(taskSaveRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(6);
    }

    @Test
    public void 체크리스트에_과제를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        // when
        taskService.deleteTask(dailyChecklist.getId(), NAME2);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(2);
    }

    @Test
    public void 체크리스트내에_있는_과제의_어려움_점수를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = new TaskDifficultyModifyRequest(dailyChecklist.getId(), Difficulty.HARD);

        // when
        taskService.changeTaskDifficulty(NAME2, taskDifficultyModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    public void 체크리스트내에_있는_과제의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        TaskStatusModifyRequest taskStatusModifyRequest = new TaskStatusModifyRequest(dailyChecklist.getId(), Status.DONE);

        // when
        taskService.changeTaskStatus(NAME, taskStatusModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

}
