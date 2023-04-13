package hwicode.schedule.dailyschedule.todolist.infra;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyToDoListRepository extends JpaRepository<DailyToDoList, Long> {
}
