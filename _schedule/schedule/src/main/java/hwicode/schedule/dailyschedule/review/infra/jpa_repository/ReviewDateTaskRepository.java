package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.domain.ReviewDateTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReviewDateTaskRepository extends JpaRepository<ReviewDateTask, Long> {

    @Query("Delete ReviewDateTask r "
            + "WHERE r.reviewTask.id = :id")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllReviewDateTasksBy(@Param("id") Long reviewTaskId);

    @Query("SELECT r FROM ReviewDateTask r "
            + "JOIN FETCH r.reviewTask "
            + "JOIN FETCH r.reviewDate "
            + "WHERE r.reviewDate.date = :date")
    List<ReviewDateTask> findAllByDateWithReviewTask(@Param("date") LocalDate date);
}
