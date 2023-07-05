package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

//todo: LocalDate에 대한 인덱스를 어떻게 할 지 생각해보기 -> 일단 LocalDate에 unique 제약 조건으로 논-클러스터링 인덱스 생성
public interface ReviewDateRepository extends JpaRepository<ReviewDate, Long> {

    Optional<ReviewDate> findByDate(LocalDate date);
}
