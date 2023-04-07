package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DailyToDoListTest {

    private final String TASK_NAME = "taskName";
    private final String TASK_NAME2 = "taskName2";
    private final String NEW_TASK_NAME = "newTaskName";
    private final String NEW_TASK_NAME2 = "newTaskName2";

    private TaskCreateDto createDto(String taskName) {
        return new TaskCreateDto(taskName, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);
    }

    @Test
    void 과제를_생성할_때_새로운_이름이면_과제가_생성된다() {
        // given
        TaskCreateDto taskCreateDto = createDto(TASK_NAME);
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
        TaskCreateDto taskCreateDto = createDto(TASK_NAME);

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
        TaskCreateDto nullTaskNameDto = createDto(null);

        // when then
        assertThatThrownBy(() -> dailyToDoList.createTask(nullTaskNameDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과제의_이름_변경_요청을_할_때_새로운_이름이면_이름이_변경된다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoList.createTask(createDto(TASK_NAME));

        // when
        dailyToDoList.changeTaskName(TASK_NAME, NEW_TASK_NAME);

        // then
        assertThatThrownBy(() -> dailyToDoList.createTask(createDto(NEW_TASK_NAME)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과제의_이름_변경_요청을_할_때_이름이_중복되면_에러가_발생한다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoList.createTask(createDto(TASK_NAME));
        dailyToDoList.createTask(createDto(TASK_NAME2));

        // when then
        assertThatThrownBy(() -> dailyToDoList.changeTaskName(TASK_NAME, TASK_NAME2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과제의_이름_변경_요청을_할_때_이름이_존재하지_않으면_에러가_발생한다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoList.createTask(createDto(TASK_NAME));

        // when then
        assertThatThrownBy(() -> dailyToDoList.changeTaskName(NEW_TASK_NAME, NEW_TASK_NAME2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 과제의_이름_변경_요청을_할_때_이름이_null이면_에러가_발생한다() {
        // given
        DailyToDoList dailyToDoList = new DailyToDoList();
        dailyToDoList.createTask(createDto(TASK_NAME));

        // when then
        assertThatThrownBy(() -> dailyToDoList.changeTaskName(null, NEW_TASK_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
