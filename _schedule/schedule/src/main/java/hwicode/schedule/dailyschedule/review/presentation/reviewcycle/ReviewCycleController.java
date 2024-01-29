package hwicode.schedule.dailyschedule.review.presentation.reviewcycle;

import hwicode.schedule.dailyschedule.review.application.ReviewCycleAggregateService;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleDeleteCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleModifyCycleCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleModifyNameCommand;
import hwicode.schedule.dailyschedule.review.application.dto.review_cycle.ReviewCycleSaveCommand;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify.ReviewCycleCycleModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify.ReviewCycleCycleModifyResponse;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify.ReviewCycleNameModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify.ReviewCycleNameModifyResponse;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class ReviewCycleController {

    private final ReviewCycleAggregateService reviewCycleAggregateService;

    @PostMapping("/dailyschedule/review-cycles")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ReviewCycleSaveResponse saveReviewCycle(@RequestBody @Valid ReviewCycleSaveRequest request) {
        ReviewCycleSaveCommand command = new ReviewCycleSaveCommand(
                1L, request.getReviewCycleName(), request.getCycle()
        );
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(command);
        return new ReviewCycleSaveResponse(reviewCycleId, command.getName(), command.getCycle());
    }

    @PatchMapping("/dailyschedule/review-cycles/{reviewCycleId}/name")
    @ResponseStatus(HttpStatus.OK)
    public ReviewCycleNameModifyResponse changeReviewCycleName(@PathVariable @Positive Long reviewCycleId,
                                                               @RequestBody @Valid ReviewCycleNameModifyRequest request) {
        ReviewCycleModifyNameCommand command = new ReviewCycleModifyNameCommand(
                1L, reviewCycleId, request.getNewReviewCycleName()
        );
        reviewCycleAggregateService.changeReviewCycleName(command);
        return new ReviewCycleNameModifyResponse(reviewCycleId, command.getNewName());
    }

    @PatchMapping("/dailyschedule/review-cycles/{reviewCycleId}/cycle")
    @ResponseStatus(HttpStatus.OK)
    public ReviewCycleCycleModifyResponse changeCycle(@PathVariable @Positive Long reviewCycleId,
                                                      @RequestBody @Valid ReviewCycleCycleModifyRequest request) {
        ReviewCycleModifyCycleCommand command = new ReviewCycleModifyCycleCommand(
                1L, reviewCycleId, request.getCycle()
        );
        reviewCycleAggregateService.changeCycle(command);
        return new ReviewCycleCycleModifyResponse(reviewCycleId, command.getCycle());
    }

    @DeleteMapping("/dailyschedule/review-cycles/{reviewCycleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReviewCycle(@PathVariable @Positive Long reviewCycleId) {
        ReviewCycleDeleteCommand command = new ReviewCycleDeleteCommand(1L, reviewCycleId);
        reviewCycleAggregateService.deleteReviewCycle(command);
    }

}
