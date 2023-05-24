package hwicode.schedule.calendar.domain;

public class SubGoal {

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
