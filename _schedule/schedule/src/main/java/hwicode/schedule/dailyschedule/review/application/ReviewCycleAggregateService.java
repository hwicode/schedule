package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.common.login.validator.PermissionValidator;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleDeleteCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleModifyCycleCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleModifyNameCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleSaveCommand;
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
    public Long saveReviewCycle(ReviewCycleSaveCommand command) {
        ReviewCycle reviewCycle = new ReviewCycle(command.getName(), command.getCycle(), command.getUserId());
        reviewCycleRepository.save(reviewCycle);
        return reviewCycle.getId();
    }

    @Transactional
    public String changeReviewCycleName(ReviewCycleModifyNameCommand command) {
        ReviewCycle reviewCycle = findReviewCycle(command.getReviewCycleId());
        PermissionValidator.validateOwnership(command.getUserId(), reviewCycle.getUserId());

        reviewCycle.changeName(command.getNewName());
        return command.getNewName();
    }

    @Transactional
    public List<Integer> changeCycle(ReviewCycleModifyCycleCommand command) {
        ReviewCycle reviewCycle = findReviewCycle(command.getReviewCycleId());
        PermissionValidator.validateOwnership(command.getUserId(), reviewCycle.getUserId());

        return reviewCycle.changeCycle(command.getCycle());
    }

    @Transactional
    public Long deleteReviewCycle(ReviewCycleDeleteCommand command) {
        ReviewCycle reviewCycle = findReviewCycle(command.getReviewCycleId());
        PermissionValidator.validateOwnership(command.getUserId(), reviewCycle.getUserId());

        reviewCycleRepository.delete(reviewCycle);
        return command.getReviewCycleId();
    }

    private ReviewCycle findReviewCycle(Long reviewCycleId) {
        return reviewCycleRepository.findById(reviewCycleId)
                .orElseThrow(ReviewCycleNotFoundException::new);
    }

}
