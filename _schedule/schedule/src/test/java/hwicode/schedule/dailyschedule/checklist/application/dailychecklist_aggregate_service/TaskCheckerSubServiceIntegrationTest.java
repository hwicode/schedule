package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker.*;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.application.ChecklistForbiddenException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.TaskCheckerRepository;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
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

    private DailyChecklist createDailyChecklistWithTwoTaskChecker(Long userId) {
        DailyChecklist dailyChecklist = new DailyChecklist(userId);

        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME2, Difficulty.NORMAL);

        return dailyChecklist;
    }

    @Test
    void 체크리스트에_과제체커를_추가할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveCommand command = new TaskSaveCommand(
                userId, dailyChecklist.getId(), NEW_TASK_CHECKER_NAME,
                Difficulty.NORMAL, Priority.SECOND, Importance.SECOND
        );

        // when
        Long taskCheckerId = taskCheckerSubService.saveTaskChecker(command);

        // then
        assertThat(taskCheckerRepository.existsById(taskCheckerId)).isTrue();
    }

    @Test
    void 체크리스트에_과제체커를_추가할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskSaveCommand command = new TaskSaveCommand(
                2L, dailyChecklist.getId(), NEW_TASK_CHECKER_NAME,
                Difficulty.NORMAL, Priority.SECOND, Importance.SECOND
        );

        // when then
        assertThatThrownBy(() -> taskCheckerSubService.saveTaskChecker(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

    @Test
    void 체크리스트에_과제체커를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        TaskChecker taskChecker2 = dailyChecklist.createTaskChecker(TASK_CHECKER_NAME2, Difficulty.NORMAL);

        dailyChecklistRepository.save(dailyChecklist);

        TaskDeleteCommand command = new TaskDeleteCommand(userId, dailyChecklist.getId(), taskChecker2.getId(), TASK_CHECKER_NAME2);

        // when
        taskCheckerSubService.deleteTaskChecker(command);

        // then
        assertThatThrownBy(() -> taskCheckerSubService.deleteTaskChecker(command))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트에_과제체커를_삭제할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        TaskChecker taskChecker2 = dailyChecklist.createTaskChecker(TASK_CHECKER_NAME2, Difficulty.NORMAL);

        dailyChecklistRepository.save(dailyChecklist);

        TaskDeleteCommand command = new TaskDeleteCommand(2L, dailyChecklist.getId(), taskChecker2.getId(), TASK_CHECKER_NAME2);

        // when then
        assertThatThrownBy(() -> taskCheckerSubService.deleteTaskChecker(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_어려움_점수를_수정할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskDifficultyModifyCommand command = new TaskDifficultyModifyCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME2, Difficulty.HARD);

        // when
        taskCheckerSubService.changeTaskDifficulty(command);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_어려움_점수를_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskDifficultyModifyCommand command = new TaskDifficultyModifyCommand(2L, dailyChecklist.getId(), TASK_CHECKER_NAME2, Difficulty.HARD);

        // when then
        assertThatThrownBy(() -> taskCheckerSubService.changeTaskDifficulty(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_진행상태를_수정할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskStatusModifyCommand command = new TaskStatusModifyCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, TaskStatus.DONE);

        // when
        taskCheckerSubService.changeTaskStatus(command);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_진행상태를_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskStatusModifyCommand command = new TaskStatusModifyCommand(2L, dailyChecklist.getId(), TASK_CHECKER_NAME, TaskStatus.DONE);

        // when then
        assertThatThrownBy(() -> taskCheckerSubService.changeTaskStatus(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_이름을_수정할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskCheckerNameModifyCommand taskCheckerNameModifyCommand = new TaskCheckerNameModifyCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_TASK_CHECKER_NAME);

        // when
        taskCheckerSubService.changeTaskCheckerName(taskCheckerNameModifyCommand);

        // then
        TaskSaveCommand taskSaveCommand = new TaskSaveCommand(
                userId, dailyChecklist.getId(), NEW_TASK_CHECKER_NAME,
                Difficulty.NORMAL, Priority.SECOND, Importance.SECOND
        );
        assertThatThrownBy(() -> taskCheckerSubService.saveTaskChecker(taskSaveCommand))
                .isInstanceOf(TaskCheckerNameDuplicationException.class);
    }

    @Test
    void 체크리스트내에_있는_과제체커의_이름을_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        TaskCheckerNameModifyCommand command = new TaskCheckerNameModifyCommand(2L, dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_TASK_CHECKER_NAME);

        // when then
        assertThatThrownBy(() -> taskCheckerSubService.changeTaskCheckerName(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

}
