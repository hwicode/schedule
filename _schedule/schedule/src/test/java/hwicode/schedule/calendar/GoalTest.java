package hwicode.schedule.calendar;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

}

class Goal {

    private final List<SubGoal> subGoals = new ArrayList<>();

    public SubGoal createSubGoal(String text) {
        validateSubGoal(text);
        SubGoal subGoal = new SubGoal(text);
        subGoals.add(subGoal);
        return subGoal;
    }

    private void validateSubGoal(String text) {
        boolean duplication = subGoals.stream()
                .anyMatch(subGoal -> subGoal.isSame(text));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }
}

class SubGoal {

    private String text;

    SubGoal(String text) {
        this.text = text;
    }

    boolean isSame(String text) {
        return this.text.equals(text);
    }
}
