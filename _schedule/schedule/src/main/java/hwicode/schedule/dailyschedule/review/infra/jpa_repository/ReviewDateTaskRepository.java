package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewDateTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewDateTaskRepository extends JpaRepository<ReviewDateTask, Long> {

    @Query("Delete ReviewDateTask r "
            + "WHERE r.reviewTask.id = :id")
    @Modifying(clearAutomatically = true)
    void deleteAllReviewDateTasksBy(@Param("id") Long reviewTaskId);
}
