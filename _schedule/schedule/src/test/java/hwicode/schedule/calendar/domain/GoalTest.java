package hwicode.schedule.calendar.domain;

import hwicode.schedule.calendar.exception.domain.goal.SubGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotAllDoneException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotAllTodoException;
import hwicode.schedule.calendar.exception.domain.goal.SubGoalNotFoundException;
import org.junit.jupiter.api.Test;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoalTest {

    private Goal createGoal() {
        return new Goal(GOAL_NAME);
    }

    @Test
    void 목표에_서브_목표를_추가할_수_있다() {
        // given
        Goal goal = createGoal();

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThatThrownBy(() -> goal.createSubGoal(SUB_GOAL_NAME))
                .isInstanceOf(SubGoalDuplicateException.class);
    }

    @Test
    void 목표의_상태가_TODO_일_때_서브_목표_추가시_목표는_TODO_상태가_된다() {
        // given
        Goal goal = createGoal();

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.TODO);
    }

    @Test
    void 목표의_상태가_PROGRESS_일_때_서브_목표_추가시_목표는_PROGRESS_상태를_유지한다() {
        // given
        Goal goal = createGoal();
        goal.changeGoalStatus(GoalStatus.PROGRESS);

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_DONE_일_때_서브_목표_추가시_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.changeGoalStatus(GoalStatus.DONE);

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표에_서브_목표를_삭제할_수_있다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        goal.deleteSubGoal(SUB_GOAL_NAME);

        // then
        assertThatThrownBy(() -> goal.deleteSubGoal(SUB_GOAL_NAME))
                .isInstanceOf(SubGoalNotFoundException.class);
    }

    @Test
    void 목표의_상태가_TODO_일_때_서브_목표_삭제시_목표는_TODO_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        GoalStatus goalStatus = goal.deleteSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.TODO);
    }

    @Test
    void 목표의_상태가_PROGRESS_일_때_서브_목표_삭제시_목표는_PROGRESS_상태를_유지한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);

        // when
        goal.deleteSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_DONE_일_때_서브_목표_삭제시_목표는_DONE_상태를_유지한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goal.changeGoalStatus(GoalStatus.DONE);

        // when
        goal.deleteSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.DONE);
    }

    @Test
    void 목표의_상태가_TODO_일_때_서브_목표가_DONE_상태로_변하면_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_PROGRESS_일_때_서브_목표가_DONE_상태로_변하면_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeGoalStatus(GoalStatus.PROGRESS);

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_DONE_일_때_서브_목표가_DONE_상태로_변하면_목표는_DONE_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goal.changeGoalStatus(GoalStatus.DONE);

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.DONE);
    }

    @Test
    void 목표의_상태가_TODO_일_때_서브_목표가_TODO_상태로_변하면_목표는_TODO_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.TODO);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.TODO);
    }

    @Test
    void 목표의_상태가_PROGRESS_일_때_서브_목표가_TODO_상태로_변하면_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goal.changeGoalStatus(GoalStatus.PROGRESS);

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.TODO);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_DONE_일_때_서브_목표가_TODO_상태로_변하면_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goal.changeGoalStatus(GoalStatus.DONE);

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.TODO);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표가_TODO로_변할_때_서브_목표가_모두_TODO면_목표는_TODO_로_변한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);

        // when
        GoalStatus goalStatus = goal.changeGoalStatus(GoalStatus.TODO);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.TODO);
    }

    @Test
    void 목표가_PROGRESS로_변할_때_서브_목표의_진행_상태에_상관_없이_목표는_PROGRESS_로_변한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);
        goal.changeSubGoalStatus(SUB_GOAL_NAME2, SubGoalStatus.DONE);

        // when
        GoalStatus goalStatus = goal.changeGoalStatus(GoalStatus.PROGRESS);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표가_DONE으로_변할_때_서브_목표가_모두_DONE이면_목표는_DONE으로_변한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goal.changeSubGoalStatus(SUB_GOAL_NAME2, SubGoalStatus.DONE);

        // when
        GoalStatus goalStatus = goal.changeGoalStatus(GoalStatus.DONE);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.DONE);
    }

    @Test
    void 목표가_TODO로_변할_때_서브_목표가_모두_TODO가_아니면_에러가_발생한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);
        goal.changeSubGoalStatus(SUB_GOAL_NAME2, SubGoalStatus.DONE);

        // when then
        assertThatThrownBy(() -> goal.changeGoalStatus(GoalStatus.TODO))
                .isInstanceOf(SubGoalNotAllTodoException.class);
    }

    @Test
    void 목표가_DONE로_변할_때_서브_목표가_모두_DONE이_아니면_에러가_발생한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);
        goal.changeSubGoalStatus(SUB_GOAL_NAME2, SubGoalStatus.DONE);

        // when then
        assertThatThrownBy(() -> goal.changeGoalStatus(GoalStatus.DONE))
                .isInstanceOf(SubGoalNotAllDoneException.class);
    }

    @Test
    void 목표는_서브_목표의_이름을_변경할_수_있다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        String changedName = goal.changeSubGoalName(SUB_GOAL_NAME, SUB_GOAL_NAME2);

        // then
        assertThatThrownBy(() -> goal.createSubGoal(changedName))
                .isInstanceOf(SubGoalDuplicateException.class);
    }

    @Test
    void 목표는_서브_목표의_이름을_변경할_때_이미_존재하는_이름이면_애러가_발생한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);

        // when then
        assertThatThrownBy(() -> goal.changeSubGoalName(SUB_GOAL_NAME, SUB_GOAL_NAME2))
                .isInstanceOf(SubGoalDuplicateException.class);
    }

}
