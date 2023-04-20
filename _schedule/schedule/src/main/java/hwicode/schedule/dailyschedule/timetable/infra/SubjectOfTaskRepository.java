package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectOfTaskRepository extends JpaRepository<SubjectOfTask, Long> {

}
