package hwicode.schedule.dailyschedule.checklist.infra.jpa_repository;

import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskCheckerRepository extends JpaRepository<TaskChecker, Long> {

    @Query("SELECT t FROM TaskChecker t "
            + "LEFT JOIN FETCH t.subTaskCheckers "
            + "WHERE t.id = :id")
    Optional<TaskChecker> findTaskCheckerWithSubTaskCheckers(@Param("id") Long taskCheckerId);
}
