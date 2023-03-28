package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotFoundException;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.SubTaskRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class SubTaskServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubTaskService subTaskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Autowired
    SubTaskRepository subTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    DailyChecklist createDailyChecklistWithTwoTaskAndSubTask() {
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task(TASK_NAME));
        dailyChecklist.addTask(new Task(TASK_NAME2));
        dailyChecklist.addSubTask(TASK_NAME, new SubTask(SUB_TASK_NAME));
        dailyChecklist.addSubTask(TASK_NAME, new SubTask(SUB_TASK_NAME2));

        return dailyChecklist;
    }

    @Test
    public void 체크리스트에_서브_과제를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME2, NEW_SUB_TASK_NAME);

        // when
        Long subTaskId = subTaskService.saveSubTask(subTaskSaveRequest);

        // then
        assertThat(subTaskRepository.existsById(subTaskId)).isTrue();
    }

    @Test
    public void 체크리스트에_서브_과제를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(dailyChecklist.getId(), TASK_NAME);

        // when
        subTaskService.deleteSubTask(SUB_TASK_NAME, subTaskDeleteRequest);

        // then
        assertThatThrownBy(() -> subTaskService.deleteSubTask(SUB_TASK_NAME, subTaskDeleteRequest))
                .isInstanceOf(SubTaskNotFoundException.class);
    }

    @Test
    public void 체크리스트내에_있는_서브_과제의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklist.makeTaskDone(TASK_NAME);
        dailyChecklist.makeTaskDone(TASK_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(dailyChecklist.getId(), TASK_NAME, Status.PROGRESS);

        // when
        subTaskService.changeSubTaskStatus(SUB_TASK_NAME, subTaskStatusModifyRequest);

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

}
