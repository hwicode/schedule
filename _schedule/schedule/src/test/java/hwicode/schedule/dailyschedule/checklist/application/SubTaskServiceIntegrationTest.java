package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.SubTask;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SubTaskServiceIntegrationTest {

    @Autowired
    SubTaskService subTaskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @PersistenceContext
    EntityManager entityManager;

    final String NAME = "name";
    final String NAME2 = "name2";
    final String SUB_TASK_NAME = "subTaskName";
    final String SUB_TASK_NAME2 = "subTaskName2";

    DailyChecklist createDailyChecklistWithTwoTaskAndSubTask() {
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task(NAME));
        dailyChecklist.addTask(new Task(NAME2));
        dailyChecklist.addSubTask(NAME, new SubTask(SUB_TASK_NAME));
        dailyChecklist.addSubTask(NAME, new SubTask(SUB_TASK_NAME2));

        return dailyChecklist;
    }

    @Test
    public void 체크리스트에_서브_과제를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithTwoTaskAndSubTask();
        dailyChecklistRepository.save(dailyChecklist);

        entityManager.clear();

        // when
        subTaskService.saveSubTask(dailyChecklist.getId(), NAME, new SubTask(NAME2));

        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId())
                .orElseThrow();
        assertThat(savedDailyChecklist.changeSubTaskStatus(NAME, NAME2, Status.PROGRESS)).isEqualTo(Status.PROGRESS);
    }

}
