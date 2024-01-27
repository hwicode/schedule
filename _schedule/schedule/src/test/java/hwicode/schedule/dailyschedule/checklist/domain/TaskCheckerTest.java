package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import org.junit.jupiter.api.Test;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskCheckerTest {

    private TaskChecker createTaskCheckerWithSubTaskChecker() {
        DailyChecklist dailyChecklist = new DailyChecklist(1L);
        TaskChecker taskChecker = new TaskChecker(dailyChecklist, TASK_CHECKER_NAME, Difficulty.NORMAL, 1L);
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME);
        return taskChecker;
    }

    @Test
    void 과제체커의_상태가_DONE_일_때_서브_과제체커_추가시_PROGRESS_상태가_된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        taskChecker.changeToDone();

        // when
        taskChecker.createSubTaskChecker(NEW_SUB_TASK_CHECKER_NAME);

        // then
        assertThat(taskChecker.getTaskStatus()).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_DONE_일_때_서브_과제체커_삭제시_DONE_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
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
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
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
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
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
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
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
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME2);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //when then
        assertThatThrownBy(taskChecker::changeToDone)
                .isInstanceOf(SubTaskCheckerNotAllDoneException.class);
    }

    @Test
    void 과제체커가_DONE으로_변할_때_서브_과제체커가_모두_DONE이면_과제체커는_DONE으로_변한다() {
        //given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME2);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME2, SubTaskStatus.DONE);

        // when
        TaskStatus taskStatus = taskChecker.changeToDone();

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커_추가시_TODO_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();

        // when
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME2);

        // then
        assertThat(taskChecker.getTaskStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커_삭제시_TODO_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();

        // when
        TaskStatus taskStatus = taskChecker.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커가_PROGRESS로_변하면_과제체커는_PROGRESS가_된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커가_DONE으로_변하면_과제체커는_PROGRESS가_된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_TODO_일_때_서브_과제체커가_TODO로_변하면_과제체커는_TODO상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커가_TODO로_변할_때_서브_과제체커가_모두_TODO가_아니면_에러가_발생한다() {
        //given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME2);
        taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //when then
        assertThatThrownBy(taskChecker::changeToTodo)
                .isInstanceOf(SubTaskCheckerNotAllTodoException.class);
    }

    @Test
    void 과제체커가_TODO로_변할_때_서브_과제체커가_모두_TODO면_과제체커는_TODO_로_변한다() {
        //given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME2);

        //when
        TaskStatus taskStatus = taskChecker.changeToTodo();

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커_추가시_PROGRESS_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.changeToProgress();

        // when
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME2);

        // then
        assertThat(taskChecker.getTaskStatus()).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커_삭제시_PROGRESS_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.changeToProgress();

        // when
        TaskStatus taskStatus = taskChecker.deleteSubTaskChecker(SUB_TASK_CHECKER_NAME);

        // then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커가_TODO로_변하면_과제체커는_PROGRESS_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.changeToProgress();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.TODO);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커가_PROGRESS로_변하면_과제체커는_PROGRESS_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.changeToProgress();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void 과제체커의_상태가_PROGRESS_일_때_서브_과제체커가_DONE으로_변하면_과제체커는_PROGRESS_상태가_유지된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.changeToProgress();

        // when
        TaskStatus taskStatus = taskChecker.changeSubTaskStatus(SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);

        //then
        assertThat(taskStatus).isEqualTo(TaskStatus.PROGRESS);
    }

    @Test
    void makeDone_메서드를_사용하면_TODO상태인_과제체커와_서브과제체커가_모두_DONE상태가_된다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.createSubTaskChecker(SUB_TASK_CHECKER_NAME2);

        // when
        taskChecker.makeDone();

        //then
        assertThat(taskChecker.isDone()).isTrue();
    }

    @Test
    void 서브_과제체커의_이름이_중복되면_에러가_발생한다() {
        // given
        TaskChecker taskChecker = createTaskCheckerWithSubTaskChecker();
        taskChecker.createSubTaskChecker(NEW_SUB_TASK_CHECKER_NAME);

        // when then
        assertThatThrownBy(() -> taskChecker.createSubTaskChecker(NEW_SUB_TASK_CHECKER_NAME))
                .isInstanceOf(SubTaskCheckerNameDuplicationException.class);
    }

}
