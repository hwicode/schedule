package hwicode.schedule.calendar.domain;

public class CalendarGoal {

    private Calendar calendar;
    private Goal goal;

    CalendarGoal(Calendar calendar, Goal goal) {
        this.calendar = calendar;
        this.goal = goal;
    }

    String changeGoalName(String name) {
        return goal.changeName(name);
    }

    boolean isSameGoal(String name) {
        return goal.isSame(name);
    }
}
