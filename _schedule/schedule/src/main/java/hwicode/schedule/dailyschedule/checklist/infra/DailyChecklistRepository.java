package hwicode.schedule.dailyschedule.checklist.infra;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DailyChecklistRepository extends JpaRepository<DailyChecklist, Long> {

    @Query("SELECT d FROM DailyChecklist d "
            + "LEFT JOIN FETCH d.taskCheckers "
            + "WHERE d.id = :id")
    Optional<DailyChecklist> findDailyChecklistWithTaskCheckers(@Param("id") Long dailyCheckListId);
}
