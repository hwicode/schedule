package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewSubTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSubTaskRepository extends JpaRepository<ReviewSubTask, Long> {

}
