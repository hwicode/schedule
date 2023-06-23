package hwicode.schedule.review.domain;

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

    @Test
    void 복습_주기의_주기를_변경할_수_있다() {
        // given
        List<Integer> reviewCycleDates = List.of(1, 2, 4, 7, 14, 60);
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        List<Integer> newReviewCycleDates = List.of(1, 5, 10, 20);

        // when
        List<Integer> result = reviewCycle.changeCycle(newReviewCycleDates);

        // then
        assertThat(result)
                .hasSize(newReviewCycleDates.size())
                .isEqualTo(newReviewCycleDates);
    }

    @Test
    void 복습_주기를_생성할_때_입력된_리스트가_변해도_복습_주기는_변화가_없다() {
        // given
        List<Integer> reviewCycleDates = new ArrayList<>(List.of(1, 2, 4));
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        // when
        reviewCycleDates.add(10);

        // then
        assertThat(reviewCycle.getCycle()).hasSize(3);
    }

    @Test
    void 복습_주기의_주기를_변경할_때_입력된_리스트가_변해도_복습_주기는_변화가_없다() {
        // given
        List<Integer> reviewCycleDates = new ArrayList<>(List.of(1, 2, 4));
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        List<Integer> newReviewCycleDates = new ArrayList<>(List.of(1, 5, 10, 20));
        reviewCycle.changeCycle(newReviewCycleDates);

        // when
        newReviewCycleDates.add(30);

        // then
        assertThat(reviewCycle.getCycle()).hasSize(4);
    }

    @Test
    void 복습_주기의_주기를_변경할_때_리턴된_리스트가_변해도_복습_주기는_변화가_없다() {
        // given
        List<Integer> reviewCycleDates = new ArrayList<>(List.of(1, 2, 4));
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        List<Integer> newReviewCycleDates = new ArrayList<>(List.of(1, 5, 10, 20));
        List<Integer> result = reviewCycle.changeCycle(newReviewCycleDates);

        // when
        result.add(30);

        // then
        assertThat(reviewCycle.getCycle()).hasSize(4);
    }

    @Test
    void 복습_주기의_주기를_가져올_때_가져온_리스트가_변해도_복습_주기는_변화가_없다() {
        // given
        List<Integer> reviewCycleDates = new ArrayList<>(List.of(1, 2, 4));
        ReviewCycle reviewCycle = new ReviewCycle(REVIEW_CYCLE_NAME, reviewCycleDates);

        List<Integer> cycle = reviewCycle.getCycle();

        // when
        cycle.add(30);

        // then
        assertThat(reviewCycle.getCycle()).hasSize(3);
    }

}
