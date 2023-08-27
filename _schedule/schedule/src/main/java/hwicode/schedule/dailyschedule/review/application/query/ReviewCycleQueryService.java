package hwicode.schedule.dailyschedule.review.application.query;

import hwicode.schedule.dailyschedule.review.application.query.dto.ReviewCycleQueryResponse;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewCycleQueryService {

    private final ReviewCycleRepository reviewCycleRepository;

    @Transactional(readOnly = true)
    public List<ReviewCycleQueryResponse> getReviewCycleQueryResponses() {
        return reviewCycleRepository.getReviewCycleQueryResponses();
    }

}
