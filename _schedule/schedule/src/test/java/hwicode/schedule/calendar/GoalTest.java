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

    public GoalStatus changeToProgress() {
        this.goalStatus = GoalStatus.PROGRESS;
        return getGoalStatus();
    }

    public GoalStatus changeToDone() {
        this.goalStatus = GoalStatus.DONE;
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

    boolean isSame(String text) {
        return this.text.equals(text);
    }
}

enum GoalStatus {
    TODO, PROGRESS, DONE
}

enum SubGoalStatus {
    TODO
}
