package hwicode.schedule.dailyschedule.checklist;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskTest {

    final String NAME = "name";

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
