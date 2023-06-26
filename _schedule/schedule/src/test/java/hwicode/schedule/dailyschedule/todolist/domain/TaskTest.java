package hwicode.schedule.dailyschedule.todolist.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.START_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskTest {

    @Test
    void 동일한_우선순위로_변경하면_우선순위에_변경이_없으므로_false가_리턴된다() {
        // given
        Task task = new Task();
        task.initialize(Priority.SECOND, Importance.SECOND);

        // when
        boolean isChange = task.changePriority(Priority.SECOND);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void 새로운_우선순위로_변경하면_우선순위에_변경이_있으므로_true가_리턴된다() {
        // given
        Task task = new Task();
        task.initialize(Priority.SECOND, Importance.SECOND);

        // when
        boolean isChange = task.changePriority(Priority.FIRST);

        // then
        assertThat(isChange).isTrue();
    }

    @Test
    void 동일한_중요도로_변경하면_중요도에_변경이_없으므로_false가_리턴된다() {
        // given
        Task task = new Task();
        task.initialize(Priority.SECOND, Importance.SECOND);

        // when
        boolean isChange = task.changeImportance(Importance.SECOND);

        // then
        assertThat(isChange).isFalse();
    }

    @Test
    void 새로운_중요도로_변경하면_중요도에_변경이_있으므로_true가_리턴된다() {
        // given
        Task task = new Task();
        task.initialize(Priority.SECOND, Importance.SECOND);

        // when
        boolean isChange = task.changeImportance(Importance.FIRST);

        // then
        assertThat(isChange).isTrue();
    }

    private List<ReviewDate> createReviewDate(LocalDate startDate, List<Integer> cycle) {
        return cycle.stream()
                .map(startDate::plusDays)
                .map(ReviewDate::new)
                .collect(Collectors.toList());
    }

    @Test
    void 과제를_복습하는_날짜들을_생성할_수_있다() {
        // given
        Task task = new Task();
        List<Integer> cycle = List.of(1, 2, 5, 10, 20);
        List<ReviewDate> reviewDate = createReviewDate(START_DATE, cycle);

        // when
        List<ReviewDateTask> result = task.review(reviewDate);

        // then
        Assertions.assertThat(result).hasSize(cycle.size());
        List<ReviewDateTask> emptyList = task.review(reviewDate);
        Assertions.assertThat(emptyList).isEmpty();
    }

    @Test
    void 과제를_복습할_때_중복되는_날짜들을_제외하고_날짜들을_생성할_수_있다() {
        // given
        Task task = new Task();

        List<Integer> firstCycle = List.of(1, 2, 5);
        List<ReviewDate> firstReviewDates = createReviewDate(START_DATE, firstCycle);
        List<ReviewDateTask> firstResult = task.review(firstReviewDates);

        List<Integer> secondCycle = List.of(1, 2, 5, 10, 20);
        List<ReviewDate> secondReviewDate = createReviewDate(START_DATE, secondCycle);

        // when
        List<ReviewDateTask> secondResult = task.review(secondReviewDate);

        // then
        Assertions.assertThat(firstResult).hasSize(3);
        List<ReviewDateTask> firstEmptyList = task.review(firstReviewDates);
        Assertions.assertThat(firstEmptyList).isEmpty();

        Assertions.assertThat(secondResult).hasSize(2);
        List<ReviewDateTask> secondEmptyList = task.review(secondReviewDate);
        Assertions.assertThat(secondEmptyList).isEmpty();
    }

}
