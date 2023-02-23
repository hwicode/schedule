package hwicode.schedule.dailyschedule.checklist;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScheduleTest {

    final String NAME = "name";

    @Test
    public void 과제의_이름이_중복되면_에러가_발생한다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        Task task = new Task(NAME);
        dailyChecklist.addTask(task);

        Task task2 = new Task(NAME);

        // when then
        assertThatThrownBy(() -> dailyChecklist.addTask(task2))
                .isInstanceOf(IllegalStateException.class);
    }
}
