package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.domain.DailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyScheduleRepository extends JpaRepository<DailySchedule, Long> {

    @Query("SELECT d FROM DailySchedule d "
            + "WHERE d.userId = :userId and d.today = :today")
    Optional<DailySchedule> findByDate(@Param("userId") Long userId, @Param("today") LocalDate date);
}
