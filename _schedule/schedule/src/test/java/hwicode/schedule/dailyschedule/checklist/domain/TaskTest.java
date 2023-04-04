package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotAllTodoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskTest {

    private Task task;
    private SubTask subTask;
    private SubTask subTask2;

    @BeforeEach
    void beforeEach() {
        task = new Task(TASK_NAME, TaskStatus.TODO, Difficulty.NORMAL);
        subTask = new SubTask(SUB_TASK_NAME, SubTaskStatus.TODO);
        subTask2 = new SubTask(SUB_TASK_NAME2, SubTaskStatus.TODO);
    }

    @Test
    void 과제의_상태가_DONE_일_때_서브_과제_추가시_PROGRESS_상태가_된다() {
        // given
        task.changeToDone();

        // when
        TaskStatus taskStatus = task.addSubTask(subTask);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_DONE_일_때_서브_과제_삭제시_DONE_상태가_유지된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);
        task.changeToDone();

        // when
        TaskStatus taskStatus = task.deleteSubTask(SUB_TASK_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void 과제의_상태가_DONE_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);
        task.changeToDone();

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_DONE_일_때_서브_과제가_TODO로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);
        task.changeToDone();

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_DONE_일_때_서브_과제가_DONE으로_변하면_과제는_DONE을_유지한다() {
        // given
        task.addSubTask(subTask);
        task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);
        task.changeToDone();

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void 과제가_DONE으로_변할_때_서브_과제가_모두_DONE이_아니면_에러가_발생한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);
        task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);

        //when then
        assertThatThrownBy(task::changeToDone)
                .isInstanceOf(SubTaskNotAllDoneException.class);
    }

    @Test
    void 과제가_DONE으로_변할_때_서브_과제가_모두_DONE이면_과제는_DONE으로_변한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);
        task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);
        task.changeSubTaskStatus(SUB_TASK_NAME2, SubTaskStatus.DONE);

        // when
        TaskStatus taskStatus = task.changeToDone();

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void 과제의_상태가_TODO_일_때_서브_과제_추가시_TODO_상태가_유지된다() {
        // when
        TaskStatus taskStatus = task.addSubTask(subTask);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제의_상태가_TODO_일_때_서브_과제_삭제시_TODO_상태가_유지된다() {
        // given
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.deleteSubTask(SUB_TASK_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제의_상태가_TODO_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_TODO_일_때_서브_과제가_DONE으로_변하면_과제는_PROGRESS가_된다() {
        // given
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_TODO_일_때_서브_과제가_TODO로_변하면_과제는_TODO상태가_유지된다() {
        // given
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제가_TODO로_변할_때_서브_과제가_모두_TODO가_아니면_에러가_발생한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);
        task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);

        //when then
        assertThatThrownBy(task::changeToTodo)
                .isInstanceOf(SubTaskNotAllTodoException.class);
    }

    @Test
    void 과제가_TODO로_변할_때_서브_과제가_모두_TODO면_과제는_TODO_로_변한다() {
        //given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);

        //when
        TaskStatus taskStatus = task.changeToTodo();

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제의_상태가_PROGRESS_일_때_서브_과제_추가시_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();

        // when
        TaskStatus taskStatus = task.addSubTask(subTask);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_PROGRESS_일_때_서브_과제_삭제시_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.deleteSubTask(SUB_TASK_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_PROGRESS_일_때_서브_과제가_TODO로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_PROGRESS_일_때_서브_과제가_PROGRESS로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제의_상태가_PROGRESS_일_때_서브_과제가_DONE으로_변하면_과제는_PROGRESS_상태가_유지된다() {
        // given
        task.changeToProgress();
        task.addSubTask(subTask);

        // when
        TaskStatus taskStatus = task.changeSubTaskStatus(SUB_TASK_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void makeDone_메서드를_사용하면_TODO상태인_과제와_서브과제가_모두_DONE상태가_된다() {
        // given
        task.addSubTask(subTask);
        task.addSubTask(subTask2);

        // when
        task.makeDone();

        //then
        assertThat(task.isDone()).isTrue();
    }

    @Test
    void 서브_과제의_이름이_중복되면_에러가_발생한다() {
        // given
        SubTask subTask = new SubTask(NEW_SUB_TASK_NAME);
        task.addSubTask(subTask);

        SubTask duplicatedSubTask = new SubTask(NEW_SUB_TASK_NAME);

        // when then
        assertThatThrownBy(() -> task.addSubTask(duplicatedSubTask))
                .isInstanceOf(SubTaskNameDuplicationException.class);
    }

}
