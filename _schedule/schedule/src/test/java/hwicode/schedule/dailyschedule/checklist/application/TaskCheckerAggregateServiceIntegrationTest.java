package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
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
        DailyChecklist dailyChecklist = new DailyChecklist();
        TaskChecker taskChecker = new TaskChecker(TASK_CHECKER_NAME, Difficulty.NORMAL);
        SubTaskChecker subTaskChecker = new SubTaskChecker(SUB_TASK_CHECKER_NAME);
        SubTaskChecker subTaskChecker2 = new SubTaskChecker(SUB_TASK_CHECKER_NAME2);

        dailyChecklist.addTaskChecker(taskChecker);
        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME, subTaskChecker);
        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME, subTaskChecker2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = new SubTaskCheckerNameModifyRequest(taskChecker.getId(), SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        // when
        taskCheckerAggregateService.changeSubTaskCheckerName(SUB_TASK_CHECKER_NAME, subTaskCheckerNameModifyRequest);

        // then
        SubTaskCheckerNameModifyRequest duplicatedNewNameRequest = new SubTaskCheckerNameModifyRequest(taskChecker.getId(), SUB_TASK_CHECKER_NAME2, NEW_SUB_TASK_CHECKER_NAME);
        assertThatThrownBy(() -> taskCheckerAggregateService.changeSubTaskCheckerName(SUB_TASK_CHECKER_NAME2, duplicatedNewNameRequest))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}
