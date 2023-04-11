package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.todolist.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t "
            + "LEFT JOIN FETCH t.subTasks "
            + "WHERE t.id = :id")
    Optional<Task> findTaskWithSubtasks(@Param("id") Long taskId);
}
