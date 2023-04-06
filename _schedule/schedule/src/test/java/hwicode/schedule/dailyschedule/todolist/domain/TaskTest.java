package hwicode.schedule.dailyschedule.todolist.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TaskTest {

    @Test
    void 서브_과제의_이름이_중복되면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask("SUB_TASK_NAME");

        // when then
        assertThatThrownBy(() -> task.createSubTask("SUB_TASK_NAME"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제의_이름_변경_요청을_할_때_이름이_중복되면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask("SUB_TASK_NAME");
        task.createSubTask("SUB_TASK_NAME2");

        // when then
        assertThatThrownBy(() -> task.changeSubTaskName("SUB_TASK_NAME", "SUB_TASK_NAME2"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제의_이름_변경_요청을_할_때_이름이_존재하지_않으면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask("SUB_TASK_NAME");

        // when then
        assertThatThrownBy(() -> task.changeSubTaskName("NEW_SUB_TASK_NAME", "NEW_SUB_TASK_NAME2"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제의_이름_변경_요청을_할_때_이름이_null이면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask("SUB_TASK_NAME");

        // when then
        assertThatThrownBy(() -> task.changeSubTaskName(null, "NEW_SUB_TASK_NAME"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}