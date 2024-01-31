package hwicode.schedule.dailyschedule.review.infra.jpa_repository;

import hwicode.schedule.dailyschedule.review.application.query.dto.ReviewCycleQueryResponse;
import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewCycleRepository extends JpaRepository<ReviewCycle, Long> {

    @Query("SELECT "
            + "new hwicode.schedule.dailyschedule.review.application.query.dto.ReviewCycleQueryResponse(r.id, r.name, r.reviewCycleDates)"
            + "FROM ReviewCycle r "
            + "WHERE r.userId = :userId "
            + "ORDER BY r.id ASC")
    List<ReviewCycleQueryResponse> getReviewCycleQueryResponses(@Param("userId") Long userId);
}
