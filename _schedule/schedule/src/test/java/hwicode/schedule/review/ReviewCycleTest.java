package hwicode.schedule.review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewCycleTest {

    @Test
    void 복습_주기를_생성할_수_있다() {
        // given
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60);

        // when
        ReviewCycle reviewCycle = new ReviewCycle(reviewCycleDates);

        // then
        assertThat(reviewCycle).isInstanceOf(ReviewCycle.class);
    }

    @Test
    void 복습_주기를_생성할_때_null값은_없어야_한다() {
        // given
        List<Integer> reviewCycleDates = new ArrayList<>();
        reviewCycleDates.add(null);

        // when then
        assertThatThrownBy(() -> new ReviewCycle(reviewCycleDates))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 61})
    void 복습_주기를_생성할_때_날짜는_1과_60_사이의_값이여야_한다(int noneValidDate) {
        // given
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60, noneValidDate);

        // when then
        assertThatThrownBy(() -> new ReviewCycle(reviewCycleDates))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

class ReviewCycle {

    private final List<Integer> reviewCycleDates;

    public ReviewCycle(List<Integer> reviewCycleDates) {
        validateReviewCycleDates(reviewCycleDates);
        this.reviewCycleDates = reviewCycleDates;
    }

    private void validateReviewCycleDates(List<Integer> reviewCycleDates) {
        for (Integer reviewCycleDate : reviewCycleDates) {
            if (reviewCycleDate == null) {
                throw new IllegalArgumentException();
            }

            if (reviewCycleDate < 1 || reviewCycleDate > 60) {
                throw new IllegalArgumentException();
            }
        }
    }

}
