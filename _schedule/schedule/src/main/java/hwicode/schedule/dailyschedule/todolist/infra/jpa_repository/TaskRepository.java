package hwicode.schedule.dailyschedule.todolist.infra.jpa_repository;

import hwicode.schedule.dailyschedule.todolist.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
