package hwicode.schedule.dailyschedule.timetable.infra;

import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    @Query("SELECT t FROM TimeTable t "
            + "LEFT JOIN FETCH t.learningTimes "
            + "WHERE t.id = :id")
    Optional<TimeTable> findTimeTableWithLearningTimes(@Param("id") Long timeTableId);
}
