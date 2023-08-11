package hwicode.schedule.dailyschedule.review.presentation.reviewcycle;

import hwicode.schedule.dailyschedule.review.application.ReviewCycleAggregateService;
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
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class ReviewCycleController {

    private final ReviewCycleAggregateService reviewCycleAggregateService;

    @PostMapping("/dailyschedule/review-cycles")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ReviewCycleSaveResponse saveReviewCycle(@RequestBody @Valid ReviewCycleSaveRequest reviewCycleSaveRequest) {
        String reviewCycleName = reviewCycleSaveRequest.getReviewCycleName();
        List<Integer> cycle = reviewCycleSaveRequest.getCycle();
        Long reviewCycleId = reviewCycleAggregateService.saveReviewCycle(
                reviewCycleName, cycle
        );
        return new ReviewCycleSaveResponse(reviewCycleId, reviewCycleName, cycle);
    }

    @PatchMapping("/dailyschedule/review-cycles/{reviewCycleId}/name")
    @ResponseStatus(HttpStatus.OK)
    public ReviewCycleNameModifyResponse changeReviewCycleName(@PathVariable @Positive Long reviewCycleId,
                                                               @RequestBody @Valid ReviewCycleNameModifyRequest reviewCycleNameModifyRequest) {
        String newName = reviewCycleNameModifyRequest.getNewReviewCycleName();
        reviewCycleAggregateService.changeReviewCycleName(reviewCycleId, newName);
        return new ReviewCycleNameModifyResponse(reviewCycleId, newName);
    }

    @PatchMapping("/dailyschedule/review-cycles/{reviewCycleId}/cycle")
    @ResponseStatus(HttpStatus.OK)
    public ReviewCycleCycleModifyResponse changeCycle(@PathVariable @Positive Long reviewCycleId,
                                                      @RequestBody @Valid ReviewCycleCycleModifyRequest reviewCycleCycleModifyRequest) {
        List<Integer> cycle = reviewCycleCycleModifyRequest.getCycle();
        reviewCycleAggregateService.changeCycle(reviewCycleId, cycle);
        return new ReviewCycleCycleModifyResponse(reviewCycleId, cycle);
    }

    @DeleteMapping("/dailyschedule/review-cycles/{reviewCycleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReviewCycle(@PathVariable @Positive Long reviewCycleId) {
        reviewCycleAggregateService.deleteReviewCycle(reviewCycleId);
    }

}
