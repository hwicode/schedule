package hwicode.schedule.dailyschedule.review.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.START_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
        List<ReviewDate> reviewDates = createReviewDate(START_DATE, cycle);

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
        ReviewTask reviewTask = new ReviewTask();

        List<ReviewDate> firstReviewDates = createReviewDate(START_DATE, firstCycle);
        List<ReviewDateTask> firstResult = reviewTask.review(firstReviewDates);

        List<ReviewDate> secondReviewDate = createReviewDate(START_DATE, secondCycle);

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
