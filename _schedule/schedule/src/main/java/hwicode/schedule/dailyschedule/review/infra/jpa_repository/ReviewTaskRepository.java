package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewTaskRepository extends JpaRepository<ReviewTask, Long> {

    @Query("SELECT r FROM ReviewTask r "
            + "LEFT JOIN FETCH r.reviewDateTasks "
            + "WHERE r.id = :id")
    Optional<ReviewTask> findReviewTaskWithReviewDateTasks(@Param("id") Long reviewTaskId);
}
