package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskCheckerRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SubTaskCheckerServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskCheckerService subTaskCheckerService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskCheckerRepository subTaskCheckerRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private DailyChecklist createDailyChecklistWithTwoTaskAndSubTask() {
        DailyChecklist dailyChecklist = new DailyChecklist();

        dailyChecklist.addTask(new Task(TASK_NAME, TaskStatus.TODO, Difficulty.NORMAL));
        dailyChecklist.addTask(new Task(TASK_NAME2, TaskStatus.TODO, Difficulty.NORMAL));

        dailyChecklist.addSubTask(TASK_NAME, new SubTaskChecker(SUB_TASK_NAME, SubTaskStatus.TODO));
        dailyChecklist.addSubTask(TASK_NAME, new SubTaskChecker(SUB_TASK_NAME2, SubTaskStatus.TODO));

        return dailyChecklist;
    }

    @Test
    void 체크리스트에_서브_과제를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskSaveRequest subTaskSaveRequest = createSubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME2, NEW_SUB_TASK_NAME);

        // when
        Long subTaskId = subTaskCheckerService.saveSubTask(subTaskSaveRequest);

        // then
        assertThat(subTaskCheckerRepository.existsById(subTaskId)).isTrue();
    }

    @Test
    void 체크리스트에_서브_과제를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskDeleteRequest subTaskDeleteRequest = createSubTaskDeleteRequest(dailyChecklist.getId(), TASK_NAME);

        // when
        subTaskCheckerService.deleteSubTask(SUB_TASK_NAME, subTaskDeleteRequest);

        // then
        assertThatThrownBy(() -> subTaskCheckerService.deleteSubTask(SUB_TASK_NAME, subTaskDeleteRequest))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트내에_있는_서브_과제의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklist.makeTaskDone(TASK_NAME);
        dailyChecklist.makeTaskDone(TASK_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = createSubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_NAME, SubTaskStatus.PROGRESS);

        // when
        subTaskCheckerService.changeSubTaskStatus(SUB_TASK_NAME, subTaskStatusModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

}
