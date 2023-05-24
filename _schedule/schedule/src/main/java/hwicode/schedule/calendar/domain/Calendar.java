package hwicode.schedule.calendar.domain;

import java.util.ArrayList;
import java.util.List;

public class Calendar {

    private final List<CalendarGoal> calendarGoals = new ArrayList<>();

    public CalendarGoal addGoal(Goal goal) {
        validateCalendarGoal(goal.getName());
        CalendarGoal calendarGoal = new CalendarGoal(this, goal);
        calendarGoals.add(calendarGoal);
        return calendarGoal;
    }

    public String changeGoalName(String goalName, String newGoalName) {
        validateCalendarGoal(newGoalName);
        return findCalendarGoalBy(goalName).changeGoalName(newGoalName);
    }

    private void validateCalendarGoal(String name) {
        boolean duplication = calendarGoals.stream()
                .anyMatch(calendarGoal -> calendarGoal.isSameGoal(name));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    private CalendarGoal findCalendarGoalBy(String name) {
        return calendarGoals.stream()
                .filter(calendarGoal -> calendarGoal.isSameGoal(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
