package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.dailyschedule_domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
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

    private DailyChecklist createDailyChecklistWithTwoTaskChecker() {
        DailyChecklist dailyChecklist = new DailyChecklist();

        dailyChecklist.addTaskChecker(new TaskChecker(TASK_CHECKER_NAME, TaskStatus.TODO, Difficulty.NORMAL));
        dailyChecklist.addTaskChecker(new TaskChecker(TASK_CHECKER_NAME2, TaskStatus.TODO, Difficulty.NORMAL));

        return dailyChecklist;
    }

    @Test
    void 체크리스트에_과제체커를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        TaskCheckerSaveRequest taskCheckerSaveRequest = createTaskCheckerSaveRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL);

        // when
        Long taskCheckerId = taskCheckerService.saveTaskChecker(taskCheckerSaveRequest);

        // then
        assertThat(taskCheckerRepository.existsById(taskCheckerId)).isTrue();
    }

    @Test
    void 체크리스트에_과제체커를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        // when
        taskCheckerService.deleteTaskChecker(dailyChecklist.getId(), TASK_CHECKER_NAME2);

        // then
        assertThatThrownBy(() -> taskCheckerService.deleteTaskChecker(dailyChecklist.getId(), TASK_CHECKER_NAME2))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_어려움_점수를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = createTaskDifficultyModifyRequest(dailyChecklist.getId(), Difficulty.HARD);

        // when
        taskCheckerService.changeTaskDifficulty(TASK_CHECKER_NAME2, taskDifficultyModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        TaskStatusModifyRequest taskStatusModifyRequest = createTaskStatusModifyRequest(dailyChecklist.getId(), TaskStatus.DONE);

        // when
        taskCheckerService.changeTaskStatus(TASK_CHECKER_NAME, taskStatusModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_이름을_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        // when
        taskCheckerService.changeTaskName(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_TASK_CHECKER_NAME);

        // then
        TaskCheckerSaveRequest taskCheckerSaveRequest = createTaskCheckerSaveRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL);
        assertThatThrownBy(() -> taskCheckerService.saveTaskChecker(taskCheckerSaveRequest))
                .isInstanceOf(TaskCheckerNameDuplicationException.class);
    }

}
