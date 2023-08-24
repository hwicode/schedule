package hwicode.schedule.timetable.infra.jpa_repository;

import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
import hwicode.schedule.timetable.domain.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    @Query("SELECT t FROM TimeTable t "
            + "LEFT JOIN FETCH t.learningTimes "
            + "WHERE t.id = :id")
    Optional<TimeTable> findTimeTableWithLearningTimes(@Param("id") Long timeTableId);

    @Query("SELECT "
            + "new hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse(l.id, l.startTime, l.endTime, l.subject, l.subjectOfTask.id, l.subjectOfSubTask.id) "
            + "FROM TimeTable t "
            + "LEFT JOIN t.learningTimes l "
            + "WHERE t.validator.today = :today")
    List<LearningTimeQueryResponse> findSubGoalQueryResponsesBy(@Param("today") LocalDate date);
}
