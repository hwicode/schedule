package hwicode.schedule.dailyschedule.checklist.service;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TaskServiceTest {

    @Autowired
    TaskService taskService;

    @Autowired
    DailyChecklistRepository dailyChecklistRepository;

    @Test
    public void 체크리스트에_과제를_추가할_수_있다() {
        // given
        DailyChecklist dailyChecklist = dailyChecklistRepository.save(new DailyChecklist());

        // when
        taskService.saveTask(dailyChecklist.getId(), new Task("name1"));
        taskService.saveTask(dailyChecklist.getId(), new Task("name2"));

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

        // when
        taskService.deleteTask(dailyChecklist.getId(), "name1");

        // then
        DailyChecklist savedDailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklist.getId()).orElseThrow();
        assertThat(savedDailyChecklist.getTotalDifficultyScore()).isEqualTo(2);
    }


}

@Service
class TaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final TaskRepository taskRepository;

    public TaskService(DailyChecklistRepository dailyChecklistRepository, TaskRepository taskRepository) {
        this.dailyChecklistRepository = dailyChecklistRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void saveTask(Long dailyChecklistId, Task task) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findById(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.addTask(task);

        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long dailyChecklistId, String taskName) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findById(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.deleteTask(taskName);
    }

}

interface DailyChecklistRepository extends JpaRepository<DailyChecklist, Long> {

    @Query("SELECT d FROM DailyChecklist d "
    + "JOIN FETCH d.tasks t "
    + "WHERE d.id = :id")
    Optional<DailyChecklist> findDailyChecklistWithTasks(@Param("id") Long id);
}

interface TaskRepository extends JpaRepository<Task, Long> {

}
