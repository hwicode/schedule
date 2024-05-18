package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DailyToDoListTest {

    @Test
    void 동일한_리뷰를_작성하면_리뷰에_변경이_없으므로_false가_리턴된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, 1L);

        String review = "나쁘지 않은데!";
        dailyToDoList.writeReview(review);

        // when
        boolean isChange = dailyToDoList.writeReview(review);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void 새로운_리뷰를_작성하면_리뷰에_변경이_있으므로_true가_리턴된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, 1L);

        String review = "나쁘지 않은데!";
        dailyToDoList.writeReview(review);

        // when
        String newReview = "좋은데!";
        boolean isChange = dailyToDoList.writeReview(newReview);

        // then
        assertThat(isChange).isTrue();
    }

    @Test
    void 리뷰가_null일_때_null인_리뷰를_작성하면_리뷰에_변경이_없으므로_false가_리턴된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, 1L);

        // when
        boolean isChange = dailyToDoList.writeReview(null);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void 리뷰가_null일_때_새로운_리뷰를_작성하면_리뷰에_변경이_있으므로_true가_리턴된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, 1L);
        String review = "나쁘지 않은데!";

        // when
        boolean isChange = dailyToDoList.writeReview(review);

        // then
        assertThat(isChange).isTrue();
    }

    @Test
    void 동일한_이모지로_변경하면_이모지에_변경이_없으므로_false가_리턴된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, 1L);

        // when
        boolean isChange = dailyToDoList.changeTodayEmoji(Emoji.NOT_BAD);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void 새로운_이모지로_변경하면_이모지에_변경이_있으므로_true가_리턴된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD, 1L);

        // when
        boolean isChange = dailyToDoList.changeTodayEmoji(Emoji.GOOD);

        // then
        assertThat(isChange).isTrue();
    }
}
