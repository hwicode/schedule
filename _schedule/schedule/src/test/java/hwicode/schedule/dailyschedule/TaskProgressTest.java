package hwicode.schedule.dailyschedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

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

}

class Task {

    private Status status;
    private final List<SubTask> subTasks = new ArrayList<>();

    public Task() {
        this.status = Status.TODO;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);

        if (this.status == Status.DONE) {
            this.status = Status.PROGRESS;
        }
    }

    public void changeSubTaskStatus(String name, Status status) {
        findSubTaskBy(name).changeStatus(status);

        if (this.status == Status.DONE) {
            this.status = Status.PROGRESS;
        }

        if (this.status == Status.TODO && status != Status.TODO) {
            this.status = Status.PROGRESS;
        }
    }

    public void deleteSubTask(String name) {
        subTasks.remove(findSubTaskBy(name));
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void changeToDone() {
        if (!isAllDone()) {
            throw new IllegalStateException();
        }
        this.status = Status.DONE;
    }

    private boolean isAllDone() {
        int count = (int) subTasks.stream()
                .filter(SubTask::isNotDone)
                .count();

        return count == 0;
    }

    public void changeToTodo() {
        if (!isAllTodo()) {
            throw new IllegalStateException();
        }
        this.status = Status.TODO;
    }

    private boolean isAllTodo() {
        int count = (int) subTasks.stream()
                .filter(SubTask::isNotTodo)
                .count();

        return count == 0;
    }

    public Status getStatus() {
        return this.status;
    }
}

class SubTask {

    private Status status;
    private String name;

    public SubTask(String name) {
        this.status = Status.TODO;
        this.name = name;
    }

    public boolean isNotDone() {
        return status != Status.DONE;
    }

    public boolean isNotTodo() {
        return status != Status.TODO;
    }

    public boolean isSame(String name) {
        return this.name.equals(name);
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}

enum Status {
    TODO, PROGRESS, DONE
}
