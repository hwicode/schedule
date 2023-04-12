package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.taskchecker.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.taskchecker.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskCheckerTest {

    private TaskChecker taskChecker;
    private SubTaskChecker subTaskChecker;
    private SubTaskChecker subTaskChecker2;

    @BeforeEach
    void beforeEach() {
        taskChecker = new TaskChecker(TASK_CHECKER_NAME, TaskStatus.TODO, Difficulty.NORMAL);
        subTaskChecker = new SubTaskChecker(SUB_TASK_CHECKER_NAME, SubTaskStatus.TODO);
        subTaskChecker2 = new SubTaskChecker(SUB_TASK_CHECKER_NAME2, SubTaskStatus.TODO);
    }

    @Test
    void 과제체커의_상태가_DONE_일_때_서브_과제체커_추가시_PROGRESS_상태가_된다() {
        // given
        taskChecker.changeToDone();

        // when
        TaskStatus taskStatus = taskChecker.addSubTaskChecker(subTaskChecker);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_DONE_일_때_서브_과제체커_삭제시_DONE_상태가_유지된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        taskChecker.changeToDone();

        // when
        TaskStatus taskStatus = taskChecker.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void 과제체커의_상태가_DONE_일_때_서브_과제체커가_PROGRESS로_변하면_과제체커는_PROGRESS가_된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        taskChecker.changeToDone();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_DONE_일_때_서브_과제체커가_TODO로_변하면_과제체커는_PROGRESS가_된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        taskChecker.changeToDone();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_DONE_일_때_서브_과제체커가_DONE으로_변하면_과제체커는_DONE을_유지한다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        taskChecker.changeToDone();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void 과제체커가_DONE으로_변할_때_서브_과제체커가_모두_DONE이_아니면_에러가_발생한다() {
        //given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.addSubTaskChecker(subTaskChecker2);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //when then
        assertThatThrownBy(taskChecker::changeToDone)
                .isInstanceOf(SubTaskCheckerNotAllDoneException.class);
    }

    @Test
    void 과제체커가_DONE으로_변할_때_서브_과제체커가_모두_DONE이면_과제체커는_DONE으로_변한다() {
        //given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.addSubTaskChecker(subTaskChecker2);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME2, SubTaskStatus.DONE);

        // when
        TaskStatus taskStatus = taskChecker.changeToDone();

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커_추가시_TODO_상태가_유지된다() {
        // when
        TaskStatus taskStatus = taskChecker.addSubTaskChecker(subTaskChecker);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커_삭제시_TODO_상태가_유지된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커가_PROGRESS로_변하면_과제체커는_PROGRESS가_된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커가_DONE으로_변하면_과제체커는_PROGRESS가_된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커가_TODO로_변하면_과제체커는_TODO상태가_유지된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커가_TODO로_변할_때_서브_과제체커가_모두_TODO가_아니면_에러가_발생한다() {
        //given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.addSubTaskChecker(subTaskChecker2);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //when then
        assertThatThrownBy(taskChecker::changeToTodo)
                .isInstanceOf(SubTaskCheckerNotAllTodoException.class);
    }

    @Test
    void 과제체커가_TODO로_변할_때_서브_과제체커가_모두_TODO면_과제체커는_TODO_로_변한다() {
        //given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.addSubTaskChecker(subTaskChecker2);

        //when
        TaskStatus taskStatus = taskChecker.changeToTodo();

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커_추가시_PROGRESS_상태가_유지된다() {
        // given
        taskChecker.changeToProgress();

        // when
        TaskStatus taskStatus = taskChecker.addSubTaskChecker(subTaskChecker);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커_삭제시_PROGRESS_상태가_유지된다() {
        // given
        taskChecker.changeToProgress();
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커가_TODO로_변하면_과제체커는_PROGRESS_상태가_유지된다() {
        // given
        taskChecker.changeToProgress();
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커가_PROGRESS로_변하면_과제체커는_PROGRESS_상태가_유지된다() {
        // given
        taskChecker.changeToProgress();
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커가_DONE으로_변하면_과제체커는_PROGRESS_상태가_유지된다() {
        // given
        taskChecker.changeToProgress();
        taskChecker.addSubTaskChecker(subTaskChecker);

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void makeDone_메서드를_사용하면_TODO상태인_과제체커와_서브과제체커가_모두_DONE상태가_된다() {
        // given
        taskChecker.addSubTaskChecker(subTaskChecker);
        taskChecker.addSubTaskChecker(subTaskChecker2);

        // when
        taskChecker.makeDone();

        //then
        assertThat(taskChecker.isDone()).isTrue();
    }

    @Test
    void 서브_과제체커의_이름이_중복되면_에러가_발생한다() {
        // given
        SubTaskChecker subTaskChecker = new SubTaskChecker(NEW_SUB_TASK_CHECKER_NAME);
        taskChecker.addSubTaskChecker(subTaskChecker);

        SubTaskChecker duplicatedSubTaskChecker = new SubTaskChecker(NEW_SUB_TASK_CHECKER_NAME);

        // when then
        assertThatThrownBy(() -> taskChecker.addSubTaskChecker(duplicatedSubTaskChecker))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}
