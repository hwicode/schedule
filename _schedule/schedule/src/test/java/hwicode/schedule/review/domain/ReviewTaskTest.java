package hwicode.schedule.review.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hwicode.schedule.review.ReviewDataHelper.START_DATE;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewTaskTest {

    private List<ReviewDate> createReviewDate(LocalDate startDate, List<Integer> cycle) {
        return cycle.stream()
                .map(startDate::plusDays)
                .map(ReviewDate::new)
                .collect(Collectors.toList());
    }

    @Test
    void 과제를_복습하는_날짜들을_생성할_수_있다() {
        // given
        ReviewTask reviewTask = new ReviewTask();
        List<Integer> cycle = List.of(1, 2, 5, 10, 20);
        List<ReviewDate> reviewDate = createReviewDate(START_DATE, cycle);

        // when
        List<ReviewDateTask> result = reviewTask.review(reviewDate);

        // then
        assertThat(result).hasSize(cycle.size());
        List<ReviewDateTask> emptyList = reviewTask.review(reviewDate);
        assertThat(emptyList).isEmpty();
    }

    @Test
    void 과제를_복습할_때_중복되는_날짜들을_제외하고_날짜들을_생성할_수_있다() {
        // given
        ReviewTask reviewTask = new ReviewTask();

        List<Integer> firstCycle = List.of(1, 2, 5);
        List<ReviewDate> firstReviewDates = createReviewDate(START_DATE, firstCycle);
        List<ReviewDateTask> firstResult = reviewTask.review(firstReviewDates);

        List<Integer> secondCycle = List.of(1, 2, 5, 10, 20);
        List<ReviewDate> secondReviewDate = createReviewDate(START_DATE, secondCycle);

        // when
        List<ReviewDateTask> secondResult = reviewTask.review(secondReviewDate);

        // then
        assertThat(firstResult).hasSize(3);
        List<ReviewDateTask> firstEmptyList = reviewTask.review(firstReviewDates);
        assertThat(firstEmptyList).isEmpty();

        assertThat(secondResult).hasSize(2);
        List<ReviewDateTask> secondEmptyList = reviewTask.review(secondReviewDate);
        assertThat(secondEmptyList).isEmpty();
    }

}

class ReviewTask {

    private final List<ReviewDateTask> reviewDateTasks = new ArrayList<>();

    public List<ReviewDateTask> review(List<ReviewDate> reviewDates) {
        List<ReviewDateTask> uniqueReviewDateTasks = reviewDates.stream()
                .filter(this::isUnique)
                .map(reviewDate -> new ReviewDateTask(this, reviewDate))
                .collect(Collectors.toList());

        reviewDateTasks.addAll(uniqueReviewDateTasks);
        return uniqueReviewDateTasks;
    }

    private boolean isUnique(ReviewDate reviewDate) {
        LocalDate date = reviewDate.getDate();
        boolean isDuplicate = reviewDateTasks.stream()
                .anyMatch(reviewDateTask -> reviewDateTask.isSameDate(date));
        return !isDuplicate;
    }
}

class ReviewDate {

    private LocalDate date;

    public ReviewDate(LocalDate date) {
        this.date = date;
    }

    boolean isSame(LocalDate date) {
        return this.date.isEqual(date);
    }

    LocalDate getDate() {
        return this.date;
    }
}

class ReviewDateTask {

    private ReviewTask reviewTask;
    private ReviewDate reviewDate;

    public ReviewDateTask(ReviewTask reviewTask, ReviewDate reviewDate) {
        this.reviewTask = reviewTask;
        this.reviewDate = reviewDate;
    }

    boolean isSameDate(LocalDate date) {
        return reviewDate.isSame(date);
    }
}
