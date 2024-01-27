package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskDeleteCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskSaveCommand;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.sub_task_checker.SubTaskStatusModifyCommand;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.application.ChecklistForbiddenException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.jpa_repository.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SubTaskCheckerSubServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskCheckerSubService subTaskCheckerSubService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskCheckerRepository subTaskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private DailyChecklist createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker(Long userId) {
        DailyChecklist dailyChecklist = new DailyChecklist(userId);

        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME2, Difficulty.NORMAL);

        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME);
        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME2);

        return dailyChecklist;
    }

    @Test
    void 체크리스트에_서브_과제체커를_추가할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskSaveCommand command = new SubTaskSaveCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME2, NEW_SUB_TASK_CHECKER_NAME);

        // when
        Long subTaskCheckerId = subTaskCheckerSubService.saveSubTaskChecker(command);

        // then
        assertThat(subTaskCheckerRepository.existsById(subTaskCheckerId)).isTrue();
    }

    @Test
    void 체크리스트에_서브_과제체커를_추가할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker(userId);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskSaveCommand command = new SubTaskSaveCommand(2L, dailyChecklist.getId(), TASK_CHECKER_NAME2, NEW_SUB_TASK_CHECKER_NAME);

        // when then
        assertThatThrownBy(() -> subTaskCheckerSubService.saveSubTaskChecker(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

    @Test
    void 체크리스트에_서브_과제체커를_삭제할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);

        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME2, Difficulty.NORMAL);

        SubTaskChecker subTaskChecker = dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME);
        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskDeleteCommand command = new SubTaskDeleteCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, subTaskChecker.getId());

        // when
        subTaskCheckerSubService.deleteSubTaskChecker(command);

        // then
        assertThatThrownBy(() -> subTaskCheckerSubService.deleteSubTaskChecker(command))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트에_서브_과제체커를_삭제할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = new DailyChecklist(userId);

        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.createTaskChecker(TASK_CHECKER_NAME2, Difficulty.NORMAL);

        SubTaskChecker subTaskChecker = dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME);
        dailyChecklist.createSubTaskChecker(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskDeleteCommand command = new SubTaskDeleteCommand(2L, dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, subTaskChecker.getId());

        // when then
        assertThatThrownBy(() -> subTaskCheckerSubService.deleteSubTaskChecker(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

    @Test
    void 체크리스트내에_있는_서브_과제체커의_진행상태를_수정할_수_있다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker(userId);
        dailyChecklist.makeTaskCheckerToDone(TASK_CHECKER_NAME);
        dailyChecklist.makeTaskCheckerToDone(TASK_CHECKER_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskStatusModifyCommand command = new SubTaskStatusModifyCommand(userId, dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        // when
        subTaskCheckerSubService.changeSubTaskStatus(command);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

    @Test
    void 체크리스트내에_있는_서브_과제체커의_진행상태를_수정할_때_소유자가_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker(userId);
        dailyChecklist.makeTaskCheckerToDone(TASK_CHECKER_NAME);
        dailyChecklist.makeTaskCheckerToDone(TASK_CHECKER_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskStatusModifyCommand command = new SubTaskStatusModifyCommand(2L, dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        // when then
        assertThatThrownBy(() -> subTaskCheckerSubService.changeSubTaskStatus(command))
                .isInstanceOf(ChecklistForbiddenException.class);
    }

}
