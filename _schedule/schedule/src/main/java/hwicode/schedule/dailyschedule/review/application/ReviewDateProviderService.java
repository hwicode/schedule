package hwicode.schedule.dailyschedule.review.application;

import hwicode.schedule.dailyschedule.review.domain.ReviewCycle;
import hwicode.schedule.dailyschedule.review.domain.ReviewDate;
import hwicode.schedule.dailyschedule.review.infra.jpa_repository.ReviewDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewDateProviderService {

    private final ReviewDateRepository reviewDateRepository;

    public List<ReviewDate> provideReviewDates(ReviewCycle reviewCycle, LocalDate startDate) {
        List<LocalDate> dates = makeDates(reviewCycle, startDate);
        List<ReviewDate> reviewDates = new ArrayList<>();
        List<ReviewDate> unSavedReviewDates = new ArrayList<>();

        for (LocalDate date : dates) {
            Optional<ReviewDate> reviewDate = reviewDateRepository.findByDate(date);

            reviewDate.ifPresentOrElse(
                    reviewDates::add,
                    () -> unSavedReviewDates.add(new ReviewDate(date, reviewCycle.getUserId()))
            );
        }

        if (!unSavedReviewDates.isEmpty()) {
            reviewDates.addAll(reviewDateRepository.saveAll(unSavedReviewDates));
        }
        return reviewDates;
    }

    private List<LocalDate> makeDates(ReviewCycle reviewCycle, LocalDate startDate) {
        List<Integer> cycle = reviewCycle.getCycle();
        return cycle.stream()
                .map(startDate::plusDays)
                .collect(Collectors.toList());
    }

}
