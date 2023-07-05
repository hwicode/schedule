package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewDateTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDateTaskRepository extends JpaRepository<ReviewDateTask, Long> {
}
