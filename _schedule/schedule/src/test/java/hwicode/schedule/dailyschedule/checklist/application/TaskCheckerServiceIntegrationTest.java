package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TaskCheckerServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerService taskCheckerService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    TaskCheckerRepository taskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private DailyChecklist createDailyChecklistWithTwoTask() {
        DailyChecklist dailyChecklist = new DailyChecklist();

        dailyChecklist.addTask(new TaskChecker(TASK_NAME, TaskStatus.TODO, Difficulty.NORMAL));
        dailyChecklist.addTask(new TaskChecker(TASK_NAME2, TaskStatus.TODO, Difficulty.NORMAL));

        return dailyChecklist;
    }

    @Test
    void 체크리스트에_과제를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveRequest taskSaveRequest = createTaskSaveRequest(dailyChecklist.getId(), NEW_TASK_NAME);

        // when
        Long taskId = taskCheckerService.saveTask(taskSaveRequest);

        // then
        assertThat(taskCheckerRepository.existsById(taskId)).isTrue();
    }

    @Test
    void 체크리스트에_과제를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        // when
        taskCheckerService.deleteTask(dailyChecklist.getId(), TASK_NAME2);

        // then
        assertThatThrownBy(() -> taskCheckerService.deleteTask(dailyChecklist.getId(), TASK_NAME2))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트내에_있는_과제의_어려움_점수를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = createTaskDifficultyModifyRequest(dailyChecklist.getId(), Difficulty.HARD);

        // when
        taskCheckerService.changeTaskDifficulty(TASK_NAME2, taskDifficultyModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    void 체크리스트내에_있는_과제의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTask();
        dailyChecklistRepository.save(dailyChecklist);

        TaskStatusModifyRequest taskStatusModifyRequest = createTaskStatusModifyRequest(dailyChecklist.getId(), TaskStatus.DONE);

        // when
        taskCheckerService.changeTaskStatus(TASK_NAME, taskStatusModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

}
