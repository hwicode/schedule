package hwicode.schedule.dailyschedule.timetable.infra.jpa_repository;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LearningTimeRepository extends JpaRepository<LearningTime, Long> {

    @Query("UPDATE LearningTime l "
            + "SET l.subjectOfTask = null "
            + "WHERE l.subjectOfTask.id = :id")
    @Modifying(clearAutomatically = true)
    void deleteSubjectOfTaskBelongingToLearningTime(@Param("id") Long subjectOfTaskId);

    @Query("UPDATE LearningTime l "
            + "SET l.subjectOfSubTask = null "
            + "WHERE l.subjectOfSubTask.id = :id")
    @Modifying(clearAutomatically = true)
    void deleteSubjectOfSubTaskBelongingToLearningTime(@Param("id") Long subjectOfSubTaskId);
}
