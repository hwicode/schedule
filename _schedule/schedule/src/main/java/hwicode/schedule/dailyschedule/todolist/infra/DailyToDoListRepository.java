package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DailyToDoListRepository extends JpaRepository<DailyToDoList, Long> {

    @Query("SELECT d FROM DailyToDoList d "
            + "LEFT JOIN FETCH d.tasks "
            + "WHERE d.id = :id")
    Optional<DailyToDoList> findToDoListWithTasks(@Param("id") Long dailyToDoListId);
}
