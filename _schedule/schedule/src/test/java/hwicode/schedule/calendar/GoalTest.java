package hwicode.schedule.calendar;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoalTest {

    private static final String GOAL_NAME = "goalName";
    private static final String SUB_GOAL_NAME ="subGoalName";
    private static final String SUB_GOAL_NAME2 ="subGoalName2";

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
                .isInstanceOf(IllegalArgumentException.class);
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
        goal.changeToProgress();

        // when
        goal.createSubGoal(SUB_GOAL_NAME);

        // then
        assertThat(goal.getGoalStatus()).isEqualTo(GoalStatus.PROGRESS);
    }

    @Test
    void 목표의_상태가_DONE_일_때_서브_목표_추가시_목표는_PROGRESS_상태가_된다() {
        // given
        Goal goal = createGoal();
        goal.changeToDone();

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
                .isInstanceOf(IllegalArgumentException.class);
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
        goal.changeToDone();

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
        goal.changeToProgress();

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
        goal.changeToDone();

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
        goal.changeToProgress();

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
        goal.changeToDone();

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
        GoalStatus goalStatus = goal.changeToTodo();

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
        GoalStatus goalStatus = goal.changeToProgress();

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
        GoalStatus goalStatus = goal.changeToDone();

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
        assertThatThrownBy(goal::changeToTodo)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 목표가_DONE로_변할_때_서브_목표가_모두_DONE이_아니면_에러가_발생한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);
        goal.changeSubGoalStatus(SUB_GOAL_NAME2, SubGoalStatus.DONE);

        // when then
        assertThatThrownBy(goal::changeToDone)
                .isInstanceOf(IllegalArgumentException.class);
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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 목표는_서브_목표의_이름을_변경할_때_이미_존재하는_이름이면_애러가_발생한다() {
        // given
        Goal goal = createGoal();
        goal.createSubGoal(SUB_GOAL_NAME);
        goal.createSubGoal(SUB_GOAL_NAME2);

        // when then
        assertThatThrownBy(() -> goal.changeSubGoalName(SUB_GOAL_NAME, SUB_GOAL_NAME2))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

class Goal {

    private String name;
    private GoalStatus goalStatus;
    private final List<SubGoal> subGoals = new ArrayList<>();

    public Goal(String name) {
        this.name = name;
        this.goalStatus = GoalStatus.TODO;
    }

    public String changeSubGoalName(String subGoalName, String newSubGoalName) {
        validateSubGoal(newSubGoalName);
        return findSubGoalBy(subGoalName).changeName(newSubGoalName);
    }

    public SubGoal createSubGoal(String name) {
        validateSubGoal(name);
        SubGoal subGoal = new SubGoal(name);
        subGoals.add(subGoal);

        if (this.goalStatus == GoalStatus.DONE) {
            changeToProgress();
        }
        return subGoal;
    }

    private void validateSubGoal(String name) {
        boolean duplication = subGoals.stream()
                .anyMatch(subGoal -> subGoal.isSame(name));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public GoalStatus deleteSubGoal(String name) {
        subGoals.remove(findSubGoalBy(name));
        return this.goalStatus;
    }

    public GoalStatus changeToTodo() {
        boolean isAllToDo = isAllSameStatus(SubGoalStatus.TODO);
        if (!isAllToDo) {
            throw new IllegalArgumentException();
        }

        this.goalStatus = GoalStatus.TODO;
        return getGoalStatus();
    }

    public GoalStatus changeToProgress() {
        this.goalStatus = GoalStatus.PROGRESS;
        return getGoalStatus();
    }

    public GoalStatus changeToDone() {
        boolean isAllDone = isAllSameStatus(SubGoalStatus.DONE);
        if (!isAllDone) {
            throw new IllegalArgumentException();
        }

        this.goalStatus = GoalStatus.DONE;
        return getGoalStatus();
    }

    private boolean isAllSameStatus(SubGoalStatus subGoalStatus) {
        int count = (int) subGoals.stream()
                .filter(s -> s.isSameStatus(subGoalStatus))
                .count();

        return count == subGoals.size();
    }

    public GoalStatus changeSubGoalStatus(String subGoalName, SubGoalStatus subGoalStatus) {
        findSubGoalBy(subGoalName).changeStatus(subGoalStatus);
        checkGoalStatusConditions(subGoalStatus);

        return getGoalStatus();
    }

    private SubGoal findSubGoalBy(String name) {
        return subGoals.stream()
                .filter(subGoal -> subGoal.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void checkGoalStatusConditions(SubGoalStatus subGoalStatus) {
        if (this.goalStatus == GoalStatus.TODO && subGoalStatus != SubGoalStatus.TODO) {
            changeToProgress();
        }

        if (this.goalStatus == GoalStatus.DONE && subGoalStatus != SubGoalStatus.DONE) {
            changeToProgress();
        }
    }

    GoalStatus getGoalStatus() {
        return this.goalStatus;
    }
}

class SubGoal {

    private String name;
    private SubGoalStatus subGoalStatus;

    SubGoal(String name) {
        this.name = name;
        this.subGoalStatus = SubGoalStatus.TODO;
    }

    SubGoalStatus changeStatus(SubGoalStatus subGoalStatus) {
        this.subGoalStatus = subGoalStatus;
        return this.subGoalStatus;
    }

    String changeName(String newName) {
        this.name = newName;
        return newName;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    boolean isSameStatus(SubGoalStatus subGoalStatus) {
        return this.subGoalStatus == subGoalStatus;
    }

}

enum GoalStatus {
    TODO, PROGRESS, DONE
}

enum SubGoalStatus {
    TODO, DONE
}
