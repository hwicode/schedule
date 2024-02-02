package hwicode.schedule.dailyschedule.review.presentation.reviewcycle;

import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import hwicode.schedule.dailyschedule.review.application.query.ReviewCycleQueryService;
import hwicode.schedule.dailyschedule.review.application.query.dto.ReviewCycleQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewCycleQueryController {

    private final ReviewCycleQueryService reviewCycleQueryService;

    @GetMapping("/dailyschedule/review-cycles")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewCycleQueryResponse> getReviewCycleQueryResponses(@LoginUser LoginInfo loginInfo) {
        return reviewCycleQueryService.getReviewCycleQueryResponses(loginInfo.getUserId());
    }

}
