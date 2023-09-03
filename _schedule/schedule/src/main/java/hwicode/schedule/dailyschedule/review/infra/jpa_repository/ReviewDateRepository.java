package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
// ReviewDate의 date에 세컨더리 인덱스 존재함
public interface ReviewDateRepository extends JpaRepository<ReviewDate, Long> {

    Optional<ReviewDate> findByDate(LocalDate date);
}
