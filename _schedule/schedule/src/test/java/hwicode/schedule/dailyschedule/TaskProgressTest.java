package hwicode.schedule.dailyschedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskProgressTest {

    public static final String NAME = "name";
    Task task;
    SubTask subTask;

    @BeforeEach
    public void beforeEach() {
        task = new Task();
        subTask = new SubTask(NAME);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제_추가시_PROGRESS_상태가_된다() {
        // given
        task.changeToDone();

        // when
        task.addSubTask(subTask);

        // then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제_삭제시_DONE_상태가_유지된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeToDone();

        // when
        task.deleteSubTask(NAME);

        // then
        assertThat(task.getStatus()).isEqualTo(Status.DONE);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeToDone();

        // when
        task.changeSubTaskStatus(NAME, Status.PROGRESS);

        //then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제가_TODO로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeToDone();

        // when
        task.changeSubTaskStatus(NAME, Status.TODO);

        //then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제가_DONE으로_변할_때_서브_과제가_모두_DONE이_아니면_에러가_발생한다() {
        //given
        task.addSubTask(subTask);

        //when then
        assertThatThrownBy(task::changeToDone)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제_추가시_TODO_상태가_유지된다() {
        // when
        task.addSubTask(subTask);

        // then
        assertThat(task.getStatus()).isEqualTo(Status.TODO);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제_삭제시_TODO_상태가_유지된다() {
        // given
        task.addSubTask(subTask);

        // when
        task.deleteSubTask(NAME);

        // then
        assertThat(task.getStatus()).isEqualTo(Status.TODO);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);

        // when
        task.changeSubTaskStatus(NAME, Status.PROGRESS);

        //then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제가_DONE으로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);

        // when
        task.changeSubTaskStatus(NAME, Status.DONE);

        //then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제가_TODO로_변할_때_서브_과제가_모두_TODO가_아니면_에러가_발생한다() {
        //given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);

        //when then
        assertThatThrownBy(task::changeToTodo)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제_추가시_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();

        // when
        task.addSubTask(subTask);

        // then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제_삭제시_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        task.deleteSubTask(NAME);

        // then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제가_TODO로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        task.changeSubTaskStatus(NAME, Status.TODO);

        //then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        task.changeSubTaskStatus(NAME, Status.PROGRESS);

        //then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제가_DONE으로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        task.changeSubTaskStatus(NAME, Status.DONE);

        //then
        assertThat(task.getStatus()).isEqualTo(Status.PROGRESS);
    }

}
