package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.todolist.domain.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
}
