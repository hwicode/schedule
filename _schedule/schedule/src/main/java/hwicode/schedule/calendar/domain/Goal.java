package hwicode.schedule.calendar.domain;

import hwicode.schedule.calendar.exception.domain.goal.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private GoalStatus goalStatus;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CalendarGoal> calendarGoals = new ArrayList<>();

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
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
        SubGoal subGoal = new SubGoal(this, name);
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
            throw new SubGoalDuplicateException();
        }
    }

    public GoalStatus deleteSubGoal(String name) {
        subGoals.remove(findSubGoalBy(name));
        return this.goalStatus;
    }

    public GoalStatus changeGoalStatus(GoalStatus goalStatus) {
        switch (goalStatus) {
            case TODO:
                return changeToTodo();
            case PROGRESS:
                return changeToProgress();
            case DONE:
                return changeToDone();
            default:
                throw new GoalStatusNotFoundException();
        }
    }

    private GoalStatus changeToTodo() {
        boolean isAllToDo = isAllSameStatus(SubGoalStatus.TODO);
        if (!isAllToDo) {
            throw new SubGoalNotAllTodoException();
        }

        this.goalStatus = GoalStatus.TODO;
        return getGoalStatus();
    }

    private GoalStatus changeToProgress() {
        this.goalStatus = GoalStatus.PROGRESS;
        return getGoalStatus();
    }

    private GoalStatus changeToDone() {
        boolean isAllDone = isAllSameStatus(SubGoalStatus.DONE);
        if (!isAllDone) {
            throw new SubGoalNotAllDoneException();
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
                .orElseThrow(SubGoalNotFoundException::new);
    }

    private void checkGoalStatusConditions(SubGoalStatus subGoalStatus) {
        if (this.goalStatus == GoalStatus.TODO && subGoalStatus != SubGoalStatus.TODO) {
            changeToProgress();
        }

        if (this.goalStatus == GoalStatus.DONE && subGoalStatus != SubGoalStatus.DONE) {
            changeToProgress();
        }
    }

    String changeName(String name) {
        this.name = name;
        return name;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public GoalStatus getGoalStatus() {
        return this.goalStatus;
    }

}
