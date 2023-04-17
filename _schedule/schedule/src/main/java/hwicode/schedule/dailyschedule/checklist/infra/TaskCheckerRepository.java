package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.domain.TaskCheckerSaveOnlyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskCheckerRepository extends JpaRepository<TaskChecker, Long>, TaskCheckerSaveOnlyRepository {

    @Query("SELECT t FROM TaskChecker t "
            + "LEFT JOIN FETCH t.subTaskCheckers "
            + "WHERE t.id = :id")
    Optional<TaskChecker> findTaskCheckerWithSubTaskCheckers(@Param("id") Long taskCheckerId);
}
