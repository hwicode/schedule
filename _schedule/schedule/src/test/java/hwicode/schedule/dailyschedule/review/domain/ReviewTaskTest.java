package hwicode.schedule.dailyschedule.review.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_SUB_TASK_NAME;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.START_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReviewTaskTest {

    private ReviewTask createReviewTaskWithReviewSubTasks(int number) {
        ReviewTask reviewTask = new ReviewTask();
        List<ReviewSubTask> reviewSubTasks = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            ReviewSubTask reviewSubTask = new ReviewSubTask(reviewTask, REVIEW_SUB_TASK_NAME + i, 1L);
            reviewSubTasks.add(reviewSubTask);
        }
        reviewTask.addAllToReviewSubTasks(reviewSubTasks);
        return reviewTask;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 6, 7, 20})
    void 과제를_복사할_수_있다(int number) {
        // given
        ReviewTask reviewTask = createReviewTaskWithReviewSubTasks(number);

        // when
        ReviewTask clonedTask = reviewTask.cloneTask(new ReviewList());

        // then
        assertThat(clonedTask).isEqualTo(reviewTask);
    }

    private List<ReviewDate> createReviewDate(LocalDate startDate, List<Integer> cycle, Long userId) {
        return cycle.stream()
                .map(startDate::plusDays)
                .map(date -> new ReviewDate(date, userId))
                .collect(Collectors.toList());
    }

    @Test
    void 과제를_복습하는_날짜들을_생성할_수_있다() {
        // given
        ReviewTask reviewTask = new ReviewTask();
        List<Integer> cycle = List.of(1, 2, 5, 10, 20);
        List<ReviewDate> reviewDates = createReviewDate(START_DATE, cycle, 1L);

        // when
        List<ReviewDateTask> result = reviewTask.review(reviewDates);

        // then
        assertThat(result).hasSize(cycle.size());
        List<ReviewDateTask> emptyList = reviewTask.review(reviewDates);
        assertThat(emptyList).isEmpty();
    }

    private static Stream<Arguments> provideReviewCyclesAndNewSize() {
        return Stream.of(
                arguments(
                        List.of(1, 2, 5),
                        List.of(1, 2, 5, 10, 20),
                        2
                ),
                arguments(
                        List.of(1, 5, 9, 15),
                        List.of(1, 2, 5, 10, 20),
                        3
                ),
                arguments(
                        List.of(10, 20, 30, 40, 50),
                        List.of(1, 2, 5, 10, 20),
                        3
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideReviewCyclesAndNewSize")
    void 과제를_복습할_때_중복되는_날짜들을_제외하고_날짜들을_생성할_수_있다(List<Integer> firstCycle, List<Integer> secondCycle, int newDatesSize) {
        // given
        Long userId = 1L;
        ReviewTask reviewTask = new ReviewTask();

        List<ReviewDate> firstReviewDates = createReviewDate(START_DATE, firstCycle, userId);
        List<ReviewDateTask> firstResult = reviewTask.review(firstReviewDates);

        List<ReviewDate> secondReviewDate = createReviewDate(START_DATE, secondCycle, userId);

        // when
        List<ReviewDateTask> secondResult = reviewTask.review(secondReviewDate);

        // then
        assertThat(firstResult).hasSize(firstCycle.size());
        List<ReviewDateTask> firstEmptyList = reviewTask.review(firstReviewDates);
        assertThat(firstEmptyList).isEmpty();

        assertThat(secondResult).hasSize(newDatesSize);
        List<ReviewDateTask> secondEmptyList = reviewTask.review(secondReviewDate);
        assertThat(secondEmptyList).isEmpty();
    }

}
