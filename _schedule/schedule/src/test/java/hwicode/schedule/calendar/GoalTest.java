package hwicode.schedule.calendar;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoalTest {

    private static final String SUB_GOAL_NAME ="subGoalName";

    @Test
    void 목표에_서브_목표를_추가할_수_있다() {
        // given
        Goal goal = new Goal();

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThatThrownBy(() -> goal.createSubGoal(SUB_GOAL_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 목표의_상태가_TODO_일_때_서브_목표_추가시_목표는_TODO_상태가_된다() {
        // given
        Goal goal = new Goal();

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.TODO);
    }

    @Test
    void 목표의_상태가_PROGRESS_일_때_서브_목표_추가시_목표는_PROGRESS_상태를_유지한다() {
        // given
        Goal goal = new Goal();
        goal.changeToProgress();

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_DONE_일_때_서브_목표_추가시_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = new Goal();
        goal.changeToDone();

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표에_서브_목표를_삭제할_수_있다() {
        // given
        Goal goal = new Goal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        goal.deleteSubGoal(SUB_GOAL_NAME);

        // then
        assertThatThrownBy(() -> goal.deleteSubGoal(SUB_GOAL_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 목표의_상태가_TODO_일_때_서브_목표_삭제시_목표는_TODO_상태가_된다() {
        // given
        Goal goal = new Goal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        GoalStatus goalStatus = goal.deleteSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.TODO);
    }

    @Test
    void 목표의_상태가_PROGRESS_일_때_서브_목표_삭제시_목표는_PROGRESS_상태를_유지한다() {
        // given
        Goal goal = new Goal();
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
        Goal goal = new Goal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goal.changeToDone();

        // when
        goal.deleteSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.DONE);
    }

    @Test
    void 목표의_상태가_TODO_일_때_서브_목표가_DONE_상태로_변하면_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = new Goal();
        goal.createSubGoal(SUB_GOAL_NAME);

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_PROGRESS_일_때_서브_목표가_DONE_상태로_변하면_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = new Goal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeToProgress();

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_DONE_일_때_서브_목표가_DONE_상태로_변하면_목표는_DONE_상태가_된다() {
        // given
        Goal goal = new Goal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);
        goal.changeToDone();

        // when
        GoalStatus goalStatus = goal.changeSubGoalStatus(SUB_GOAL_NAME, SubGoalStatus.DONE);

        // then
        assertThat(goalStatus).isEqualTo(GoalStatus.DONE);
    }

}

class Goal {

    private GoalStatus goalStatus;
    private final List<SubGoal> subGoals = new ArrayList<>();

    public Goal() {
        this.goalStatus = GoalStatus.TODO;
    }

    public SubGoal createSubGoal(String text) {
        validateSubGoal(text);
        SubGoal subGoal = new SubGoal(text);
        subGoals.add(subGoal);

        if (this.goalStatus == GoalStatus.DONE) {
            changeToProgress();
        }
        return subGoal;
    }

    private void validateSubGoal(String text) {
        boolean duplication = subGoals.stream()
                .anyMatch(subGoal -> subGoal.isSame(text));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public GoalStatus deleteSubGoal(String text) {
        subGoals.remove(findSubGoalBy(text));
        return this.goalStatus;
    }

    private SubGoal findSubGoalBy(String text) {
        return subGoals.stream()
                .filter(subGoal -> subGoal.isSame(text))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public GoalStatus changeToProgress() {
        this.goalStatus = GoalStatus.PROGRESS;
        return getGoalStatus();
    }

    public GoalStatus changeToDone() {
        this.goalStatus = GoalStatus.DONE;
        return getGoalStatus();
    }

    public GoalStatus changeSubGoalStatus(String subGoalText, SubGoalStatus subGoalStatus) {
        findSubGoalBy(subGoalText).changeStatus(subGoalStatus);
        if (this.goalStatus == GoalStatus.TODO && subGoalStatus != SubGoalStatus.TODO) {
            changeToProgress();
        }
        return getGoalStatus();
    }

    GoalStatus getGoalStatus() {
        return this.goalStatus;
    }
}

class SubGoal {

    private String text;
    private SubGoalStatus subGoalStatus;

    SubGoal(String text) {
        this.text = text;
        this.subGoalStatus = SubGoalStatus.TODO;
    }

    SubGoalStatus changeStatus(SubGoalStatus subGoalStatus) {
        this.subGoalStatus = subGoalStatus;
        return this.subGoalStatus;
    }

    boolean isSame(String text) {
        return this.text.equals(text);
    }

}

enum GoalStatus {
    TODO, PROGRESS, DONE
}

enum SubGoalStatus {
    TODO, DONE
}
