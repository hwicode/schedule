package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewListRepository extends JpaRepository<ReviewList, Long> {
}
