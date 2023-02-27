package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
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
public class TaskServiceIntegrationTest {

    @Autowired
    TaskService taskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void 체크리스트에_과제를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task("name1"));
        dailyChecklistRepository.save(dailyChecklist);

        entityManager.clear();

        // when
        taskService.saveTask(dailyChecklist.getId(), new Task("name2"));

        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(4);
    }

    @Test
    public void 체크리스트에_과제를_삭제할_수_있다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task("name1"));
        dailyChecklist.addTask(new Task("name2"));
        dailyChecklistRepository.save(dailyChecklist);

        entityManager.clear();

        // when
        taskService.deleteTask(dailyChecklist.getId(), "name1");

        entityManager.flush();
        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(2);
    }

    @Test
    public void 체크리스트내에_있는_과제의_어려움_점수를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task("name1"));
        dailyChecklist.addTask(new Task("name2"));
        dailyChecklistRepository.save(dailyChecklist);

        entityManager.clear();

        // when
        taskService.changeTaskDifficulty(dailyChecklist.getId(), "name1", Difficulty.HARD);

        entityManager.flush();
        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    public void 체크리스트내에_있는_과제의_진행상태를_수정할_수_있다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        dailyChecklist.addTask(new Task("name1"));
        dailyChecklist.addTask(new Task("name2"));
        dailyChecklistRepository.save(dailyChecklist);

        entityManager.clear();

        // when
        taskService.changeTaskStatus(dailyChecklist.getId(), "name1", Status.DONE);

        entityManager.flush();
        entityManager.clear();

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTodayDonePercent()).isEqualTo(50);
    }

}
