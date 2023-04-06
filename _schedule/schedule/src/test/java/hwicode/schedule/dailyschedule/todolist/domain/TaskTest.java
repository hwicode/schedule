package hwicode.schedule.dailyschedule.todolist.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TaskTest {

    private final String SUB_TASK_NAME = "subTaskName";
    private final String SUB_TASK_NAME2 = "subTaskName2";

    private final String NEW_SUB_TASK_NAME = "newSubTaskName";
    private final String NEW_SUB_TASK_NAME2 = "newSubTaskName2";

    @Test
    void 서브_과제를_생성할_때_새로운_이름이면_서브_과제가_생성된다() {
        // given
        Task task = new Task();

        // when
        task.createSubTask(SUB_TASK_NAME);

        // then
        assertThatThrownBy(() -> task.createSubTask(SUB_TASK_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제를_생성할_때_이름이_중복되면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask(SUB_TASK_NAME);

        // when then
        assertThatThrownBy(() -> task.createSubTask(SUB_TASK_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제를_생성할_때_이름이_null이면_에러가_발생한다() {
        // given
        Task task = new Task();

        // when then
        assertThatThrownBy(() -> task.createSubTask(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제의_이름_변경_요청을_할_때_새로운_이름이면_이름이_변경된다() {
        // given
        Task task = new Task();
        task.createSubTask(SUB_TASK_NAME);

        // when
        task.changeSubTaskName(SUB_TASK_NAME, NEW_SUB_TASK_NAME);

        // then
        assertThatThrownBy(() -> task.createSubTask(NEW_SUB_TASK_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제의_이름_변경_요청을_할_때_이름이_중복되면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask(SUB_TASK_NAME);
        task.createSubTask(SUB_TASK_NAME2);

        // when then
        assertThatThrownBy(() -> task.changeSubTaskName(SUB_TASK_NAME, SUB_TASK_NAME2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제의_이름_변경_요청을_할_때_이름이_존재하지_않으면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask(SUB_TASK_NAME);

        // when then
        assertThatThrownBy(() -> task.changeSubTaskName(NEW_SUB_TASK_NAME, NEW_SUB_TASK_NAME2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 서브_과제의_이름_변경_요청을_할_때_이름이_null이면_에러가_발생한다() {
        // given
        Task task = new Task();
        task.createSubTask(SUB_TASK_NAME);

        // when then
        assertThatThrownBy(() -> task.changeSubTaskName(null, NEW_SUB_TASK_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
