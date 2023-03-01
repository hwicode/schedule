package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.domain.TaskSaveOnlyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskSaveOnlyRepository {

}
