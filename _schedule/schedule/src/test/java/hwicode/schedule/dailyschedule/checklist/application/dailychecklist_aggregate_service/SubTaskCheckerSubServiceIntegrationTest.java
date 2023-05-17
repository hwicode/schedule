package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.subtaskchecker.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.subtaskchecker.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
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

    private DailyChecklist createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker() {
        DailyChecklist dailyChecklist = new DailyChecklist();

        dailyChecklist.addTaskChecker(new TaskChecker(TASK_CHECKER_NAME, TaskStatus.TODO, Difficulty.NORMAL));
        dailyChecklist.addTaskChecker(new TaskChecker(TASK_CHECKER_NAME2, TaskStatus.TODO, Difficulty.NORMAL));

        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME, new SubTaskChecker(SUB_TASK_CHECKER_NAME, SubTaskStatus.TODO));
        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME, new SubTaskChecker(SUB_TASK_CHECKER_NAME2, SubTaskStatus.TODO));

        return dailyChecklist;
    }

    @Test
    void 체크리스트에_서브_과제체커를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = new SubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME2, NEW_SUB_TASK_CHECKER_NAME);

        // when
        Long subTaskCheckerId = subTaskCheckerSubService.saveSubTaskChecker(subTaskCheckerSaveRequest);

        // then
        assertThat(subTaskCheckerRepository.existsById(subTaskCheckerId)).isTrue();
    }

    @Test
    void 체크리스트에_서브_과제체커를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest = new SubTaskCheckerDeleteRequest(dailyChecklist.getId(), TASK_CHECKER_NAME);

        // when
        subTaskCheckerSubService.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME, subTaskCheckerDeleteRequest);

        // then
        assertThatThrownBy(() -> subTaskCheckerSubService.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME, subTaskCheckerDeleteRequest))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트내에_있는_서브_과제체커의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskCheckerAndSubTaskChecker();
        dailyChecklist.makeTaskCheckerToDone(TASK_CHECKER_NAME);
        dailyChecklist.makeTaskCheckerToDone(TASK_CHECKER_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        // when
        subTaskCheckerSubService.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, subTaskStatusModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTaskCheckers(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

    @Test
    void 과제체커내에_있는_서브_과제체커의_이름을_변경할_수_있다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        TaskChecker taskChecker = new TaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        SubTaskChecker subTaskChecker = new SubTaskChecker(SUB_TASK_CHECKER_NAME);

        dailyChecklist.addTaskChecker(taskChecker);
        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME, subTaskChecker);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = new SubTaskCheckerNameModifyRequest(taskChecker.getId(), SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        // when
        subTaskCheckerSubService.changeSubTaskCheckerName(SUB_TASK_CHECKER_NAME, subTaskCheckerNameModifyRequest);

        // then
        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = new SubTaskCheckerSaveRequest(dailyChecklist.getId(), TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);
        assertThatThrownBy(() -> subTaskCheckerSubService.saveSubTaskChecker(subTaskCheckerSaveRequest))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}
