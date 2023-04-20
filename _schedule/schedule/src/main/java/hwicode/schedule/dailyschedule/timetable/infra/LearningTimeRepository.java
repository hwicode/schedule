package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningTimeRepository extends JpaRepository<LearningTime, Long> {

}
