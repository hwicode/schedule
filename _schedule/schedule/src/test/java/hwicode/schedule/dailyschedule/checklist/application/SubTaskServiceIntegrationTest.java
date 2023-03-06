package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_dto.save.SubTaskSaveRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class SubTaskServiceIntegrationTest {

    @Autowired
    SubTaskService subTaskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @PersistenceContext
    EntityManager entityManager;

    final String TASK_NAME = "taskName";
    final String TASK_NAME2 = "taskName2";
    final String SUB_TASK_NAME = "subTaskName";
    final String SUB_TASK_NAME2 = "subTaskName2";
    final String NEW_SUB_TASK = "newSubTaskName";

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

        entityManager.clear();

        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(dailyChecklist.getId(), TASK_NAME2, NEW_SUB_TASK);

        // when
        subTaskService.saveSubTask(subTaskSaveRequest);

        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.changeSubTaskStatus(TASK_NAME2, NEW_SUB_TASK, Status.PROGRESS)).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 체크리스트에_서브_과제를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklistRepository.save(dailyChecklist);

        entityManager.clear();

        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(dailyChecklist.getId(), TASK_NAME);

        // when
        subTaskService.deleteSubTask(SUB_TASK_NAME, subTaskDeleteRequest);

        entityManager.flush();
        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThatThrownBy(() -> savedDailyChecklist.deleteSubTask(TASK_NAME, SUB_TASK_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 체크리스트내에_있는_서브_과제의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklist.makeTaskDone(TASK_NAME);
        dailyChecklist.makeTaskDone(TASK_NAME2);
        dailyChecklistRepository.save(dailyChecklist);

        entityManager.clear();

        // when
        subTaskService.changeSubTaskStatus(dailyChecklist.getId(), TASK_NAME, SUB_TASK_NAME, Status.PROGRESS);

        entityManager.flush();
        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

}
