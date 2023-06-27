package hwicode.schedule.timetable.infra.jpa_repository;

import hwicode.schedule.timetable.domain.SubjectOfTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectOfTaskRepository extends JpaRepository<SubjectOfTask, Long> {

}
