package hwicode.schedule.dailyschedule.todolist.domain;

import org.junit.jupiter.api.Test;

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
}
