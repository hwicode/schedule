package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewCycleNotFoundException;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewCycleAggregateService {

    private final ReviewCycleRepository reviewCycleRepository;

    @Transactional
    public Long saveReviewCycle(String reviewCycleName, List<Integer> cycle) {
        ReviewCycle reviewCycle = new ReviewCycle(reviewCycleName, cycle);
        reviewCycleRepository.save(reviewCycle);
        return reviewCycle.getId();
    }

    @Transactional
    public String changeReviewCycleName(Long reviewCycleId, String newName) {
        ReviewCycle reviewCycle = findReviewCycle(reviewCycleId);

        reviewCycle.changeName(newName);
        return newName;
    }

    @Transactional
    public List<Integer> changeCycle(Long reviewCycleId, List<Integer> cycle) {
        ReviewCycle reviewCycle = findReviewCycle(reviewCycleId);

        return reviewCycle.changeCycle(cycle);
    }

    @Transactional
    public void deleteReviewCycle(Long reviewCycleId) {
        ReviewCycle reviewCycle = findReviewCycle(reviewCycleId);
        reviewCycleRepository.delete(reviewCycle);
    }

    private ReviewCycle findReviewCycle(Long reviewCycleId) {
        return reviewCycleRepository.findById(reviewCycleId)
                .orElseThrow(ReviewCycleNotFoundException::new);
    }

}
