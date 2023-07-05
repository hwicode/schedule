package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCycleRepository extends JpaRepository<ReviewCycle, Long> {
}
