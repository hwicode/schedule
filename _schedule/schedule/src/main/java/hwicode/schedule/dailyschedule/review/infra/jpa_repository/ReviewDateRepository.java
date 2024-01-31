package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ReviewDateRepository extends JpaRepository<ReviewDate, Long> {

    @Query("SELECT r FROM ReviewDate r "
            + "WHERE r.userId = :userId and r.date = :date")
    Optional<ReviewDate> findByDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
