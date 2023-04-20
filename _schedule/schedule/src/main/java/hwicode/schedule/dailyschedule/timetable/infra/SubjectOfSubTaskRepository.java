package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectOfSubTaskRepository extends JpaRepository<SubjectOfSubTask, Long> {

}
