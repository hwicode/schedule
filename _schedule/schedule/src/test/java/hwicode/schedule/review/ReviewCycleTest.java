package hwicode.schedule.review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewCycleTest {

    private static final String REVIEW_CYCLE_NAME = "name";
    private static final String NEW_REVIEW_CYCLE_NAME = "newName";

    @Test
    void 복습_주기를_생성할_수_있다() {
        // given
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60);

        // when
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        // then
        assertThat(reviewCycle).isInstanceOf(ReviewCycle.class);
    }

    @Test
    void 복습_주기를_생성할_때_null값은_없어야_한다() {
        // given
        List<Integer> reviewCycleDates = new ArrayList<>();
        reviewCycleDates.add(null);

        // when then
        assertThatThrownBy(() -> new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 61})
    void 복습_주기를_생성할_때_날짜는_1과_60_사이의_값이여야_한다(int noneValidDate) {
        // given
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60, noneValidDate);

        // when then
        assertThatThrownBy(() -> new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 복습_주기의_이름을_변경하면_true가_리턴된다() {
        // given
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        // when
        boolean isChanged = reviewCycle.changeName(NEW_REVIEW_CYCLE_NAME);

        // then
        assertThat(isChanged).isTrue();
    }

    @Test
    void 복습_주기의_이름을_변경하지_못하면_false가_리턴된다() {
        // given
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        // when
        boolean isChanged = reviewCycle.changeName(REVIEW_CYCLE_NAME);

        // then
        assertThat(isChanged).isFalse();
    }

}

class ReviewCycle {

    private String name;
    private final List<Integer> reviewCycleDates;

    public ReviewCycle(String name, List<Integer> reviewCycleDates) {
        validateReviewCycleDates(reviewCycleDates);
        this.name = name;
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

    public boolean changeName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

}
