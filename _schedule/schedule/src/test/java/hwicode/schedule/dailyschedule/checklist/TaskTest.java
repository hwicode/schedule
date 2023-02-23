package hwicode.schedule.dailyschedule.checklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskTest {

    String NAME = "name";
    String NAME2 = "name2";
    Task task;
    SubTask subTask;
    SubTask subTask2;

    @BeforeEach
    public void beforeEach() {
        task = new Task(NAME);
        subTask = new SubTask(NAME);
        subTask2 = new SubTask(NAME2);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제_추가시_PROGRESS_상태가_된다() {
        // given
        task.changeToDone();

        // when
        Status taskStatus = task.addSubTask(subTask);

        // then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제_삭제시_DONE_상태가_유지된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeToDone();

        // when
        Status taskStatus = task.deleteSubTask(NAME);

        // then
        assertThat(taskStatus).isEqualTo(Status.DONE);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeToDone();

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제가_TODO로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeToDone();

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.TODO);

        //then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_DONE_일_때_서브_과제가_DONE으로_변하면_과제는_DONE을_유지한다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeToDone();

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.DONE);

        //then
        assertThat(taskStatus).isEqualTo(Status.DONE);
    }

    @Test
    public void 과제가_DONE으로_변할_때_서브_과제가_모두_DONE이_아니면_에러가_발생한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);
        task.changeSubTaskStatus(NAME, Status.DONE);

        //when then
        assertThatThrownBy(task::changeToDone)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 과제가_DONE으로_변할_때_서브_과제가_모두_DONE이면_과제는_DONE으로_변한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);
        task.changeSubTaskStatus(NAME, Status.DONE);
        task.changeSubTaskStatus(NAME2, Status.DONE);

        // when
        Status taskStatus = task.changeToDone();

        // then
        assertThat(taskStatus).isEqualTo(Status.DONE);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제_추가시_TODO_상태가_유지된다() {
        // when
        Status taskStatus = task.addSubTask(subTask);

        // then
        assertThat(taskStatus).isEqualTo(Status.TODO);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제_삭제시_TODO_상태가_유지된다() {
        // given
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.deleteSubTask(NAME);

        // then
        assertThat(taskStatus).isEqualTo(Status.TODO);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제가_DONE으로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.DONE);

        //then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_TODO_일_때_서브_과제가_TODO로_변하면_과제는_TODO상태가_유지된다() {
        // given
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.TODO);

        //then
        assertThat(taskStatus).isEqualTo(Status.TODO);
    }

    @Test
    public void 과제가_TODO로_변할_때_서브_과제가_모두_TODO가_아니면_에러가_발생한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);
        task.changeSubTaskStatus(NAME, Status.DONE);

        //when then
        assertThatThrownBy(task::changeToTodo)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 과제가_TODO로_변할_때_서브_과제가_모두_TODO면_과제는_TODO_로_변한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);

        //when
        Status taskStatus = task.changeToTodo();

        // then
        assertThat(taskStatus).isEqualTo(Status.TODO);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제_추가시_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();

        // when
        Status taskStatus = task.addSubTask(subTask);

        // then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제_삭제시_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.deleteSubTask(NAME);

        // then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제가_TODO로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.TODO);

        //then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 과제의_상태가_PROGRESS_일_때_서브_과제가_DONE으로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        Status taskStatus = task.changeSubTaskStatus(NAME, Status.DONE);

        //then
        assertThat(taskStatus).isEqualTo(Status.PROGRESS);
    }

    @Test
    public void 서브_과제의_이름이_중복되면_에러가_발생한다() {
        // given
        Task task = new Task(NAME);
        SubTask subTask = new SubTask(NAME);
        task.addSubTask(subTask);

        SubTask subTask2 = new SubTask(NAME);

        // when then
        assertThatThrownBy(() -> task.addSubTask(subTask2))
                .isInstanceOf(IllegalStateException.class);
    }

}
