package hwicode.schedule.calendar.application.calendar;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.domain.calendar.CalendarGoalDuplicateException;
import hwicode.schedule.calendar.exception.infra.GoalNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarGoalDomainService {

    public CalendarGoal addGoalToCalendar(Calendar calendar, Goal goal, List<Goal> calendarGoals) {
        validateCalendarGoal(goal.getName(), calendarGoals);
        calendarGoals.add(goal);
        return new CalendarGoal(calendar, goal);
    }

    public String changeGoalName(String goalName, String newGoalName, List<Goal> calendarGoals) {
        validateCalendarGoal(newGoalName, calendarGoals);
        return findCalendarGoal(goalName, calendarGoals).changeName(newGoalName);
    }

    private void validateCalendarGoal(String goalName, List<Goal> goals) {
        boolean duplication = goals.stream()
                .anyMatch(goal -> goal.isSame(goalName));

        if (duplication) {
            throw new CalendarGoalDuplicateException();
        }
    }

    private Goal findCalendarGoal(String goalName, List<Goal> goals) {
        return goals.stream()
                .filter(goal -> goal.isSame(goalName))
                .findFirst()
                .orElseThrow(GoalNotFoundException::new);
    }

}
