package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DailyToDoListTest {

    private final String TASK_NAME = "taskName";
    private final TaskCreateDto taskCreateDto = new TaskCreateDto(TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

    @Test
    void 과제를_생성할_때_새로운_이름이면_과제가_생성된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();

        // when
        dailyToDoList.createTask(taskCreateDto);

        // then
        assertThatThrownBy(() -> dailyToDoList.createTask(taskCreateDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과제를_생성할_때_이름이_중복되면_에러가_발생한다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoList.createTask(taskCreateDto);

        // when then
        assertThatThrownBy(() -> dailyToDoList.createTask(taskCreateDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과제를_생성할_때_이름이_null이면_에러가_발생한다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        TaskCreateDto nullTaskNameDto = new TaskCreateDto(null, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when then
        assertThatThrownBy(() -> dailyToDoList.createTask(nullTaskNameDto))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
