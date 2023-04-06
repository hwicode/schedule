package hwicode.schedule.dailyschedule.todolist.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TaskInformationTest {

    @Test
    void 서브_과제의_이름이_중복되면_에러가_발생한다() {
        // given
        TaskInformation taskInformation = new TaskInformation();
        taskInformation.createSubTaskInformation("SUB_TASK_NAME");

        // when then
        assertThatThrownBy(() -> taskInformation.createSubTaskInformation("SUB_TASK_NAME"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
