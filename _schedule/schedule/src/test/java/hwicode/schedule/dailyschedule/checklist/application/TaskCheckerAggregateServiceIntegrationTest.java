package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.common.login.validator.OwnerForbiddenException;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskNameModifyCommand;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TaskCheckerAggregateServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TaskCheckerAggregateService taskCheckerAggregateService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 과제체커내에_있는_서브_과제체커의_이름을_변경할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        TaskChecker taskChecker = dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME);
        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME2);

        dailyChecklistRepository.save(dailyChecklist);

        SubTaskNameModifyCommand command = new SubTaskNameModifyCommand(userId, taskChecker.getId(), SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        // when
        taskCheckerAggregateService.changeSubTaskCheckerName(command);

        // then
        SubTaskNameModifyCommand duplicatedNameModifyCommand = new SubTaskNameModifyCommand(userId, taskChecker.getId(), SUB_TASK_CHECKER_NAME2, NEW_SUB_TASK_CHECKER_NAME);
        assertThatThrownBy(() -> taskCheckerAggregateService.changeSubTaskCheckerName(duplicatedNameModifyCommand))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

    @Test
    void 존재하지_않는_과제체커를_조회하면_에러가_발생한다() {
        //given
        Long userId = 1L;
        Long noneExistId = 1L;
        SubTaskNameModifyCommand command = new SubTaskNameModifyCommand(userId, noneExistId, SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        // when then
        assertThatThrownBy(() -> taskCheckerAggregateService.changeSubTaskCheckerName(command))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }

    @Test
    void 과제체커내에_있는_서브_과제체커의_이름을_변경할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);
        TaskChecker taskChecker = dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME);
        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME2);

        dailyChecklistRepository.save(dailyChecklist);

        SubTaskNameModifyCommand command = new SubTaskNameModifyCommand(2L, taskChecker.getId(), SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        // when then
        assertThatThrownBy(() -> taskCheckerAggregateService.changeSubTaskCheckerName(command))
                .isInstanceOf(OwnerForbiddenException.class);
    }

}
