package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewTaskRepository extends JpaRepository<ReviewTask, Long> {
}
