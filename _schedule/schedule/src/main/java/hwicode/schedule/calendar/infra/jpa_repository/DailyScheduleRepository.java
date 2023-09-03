package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.domain.DailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

// DailySchedule의 today에 세컨더리 인덱스 존재함
public interface DailyScheduleRepository extends JpaRepository<DailySchedule, Long> {

    @Query("SELECT d FROM DailySchedule d "
            + "WHERE d.today = :today")
    Optional<DailySchedule> findByDate(@Param("today") LocalDate date);
}
