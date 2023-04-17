package hwicode.schedule.dailyschedule.todolist.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DailyToDoListTest {

    @Test
    void 동일한_리뷰를_작성하면_리뷰에_변경이_없으므로_false가_리턴된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);

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
        DailyToDoList dailyToDoList = new DailyToDoList(Emoji.NOT_BAD);

        String review = "나쁘지 않은데!";
        dailyToDoList.writeReview(review);

        // when
        String newReview = "좋은데!";
        boolean isChange = dailyToDoList.writeReview(newReview);

        // then
        assertThat(isChange).isTrue();
    }

}
