package hwicode.schedule.dailyschedule.review.presentation.reviewcycle;

import hwicode.schedule.dailyschedule.review.application.ReviewCycleAggregateService;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
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

}
