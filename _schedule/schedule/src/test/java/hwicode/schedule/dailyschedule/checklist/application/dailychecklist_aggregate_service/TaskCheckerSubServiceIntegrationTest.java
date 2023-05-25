package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TaskCheckerSubServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerSubService taskCheckerSubService;

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

        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME2, Difficulty.NORMAL);

        return dailyChecklist;
    }

    @Test
    void 체크리스트에_과제체커를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        TaskCheckerSaveRequest taskCheckerSaveRequest = new TaskCheckerSaveRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL);

        // when
        Long taskCheckerId = taskCheckerSubService.saveTaskChecker(taskCheckerSaveRequest);

        // then
        assertThat(taskCheckerRepository.existsById(taskCheckerId)).isTrue();
    }

    @Test
    void 체크리스트에_과제체커를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        // when
        taskCheckerSubService.deleteTaskChecker(dailyChecklist.getId(), TASK_CHECKER_NAME2);

        // then
        assertThatThrownBy(() -> taskCheckerSubService.deleteTaskChecker(dailyChecklist.getId(), TASK_CHECKER_NAME2))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_어려움_점수를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        TaskDifficultyModifyRequest taskDifficultyModifyRequest = new TaskDifficultyModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME2, Difficulty.HARD);

        // when
        taskCheckerSubService.changeTaskDifficulty(TASK_CHECKER_NAME2, taskDifficultyModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        TaskStatusModifyRequest taskStatusModifyRequest = new TaskStatusModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, TaskStatus.DONE);

        // when
        taskCheckerSubService.changeTaskStatus(TASK_CHECKER_NAME, taskStatusModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_이름을_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        TaskCheckerNameModifyRequest taskCheckerNameModifyRequest = new TaskCheckerNameModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_TASK_CHECKER_NAME);

        // when
        taskCheckerSubService.changeTaskCheckerName(TASK_CHECKER_NAME, taskCheckerNameModifyRequest);

        // then
        TaskCheckerSaveRequest taskCheckerSaveRequest = new TaskCheckerSaveRequest(dailyChecklist.getId(), NEW_TASK_CHECKER_NAME, Difficulty.NORMAL);
        assertThatThrownBy(() -> taskCheckerSubService.saveTaskChecker(taskCheckerSaveRequest))
                .isInstanceOf(TaskCheckerNameDuplicationException.class);
    }

}