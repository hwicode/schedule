package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.domain.CalendarGoalNotFoundException;
import hwicode.schedule.calendar.exception.domain.calendar.CalendarGoalDuplicateException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarGoalDomainService {

    public CalendarGoal addGoalToCalendar(Calendar calendar, Goal goal, List<CalendarGoal> calendarGoals) {
        validateCalendarGoal(goal.getName(), calendarGoals);
        CalendarGoal calendarGoal = new CalendarGoal(calendar, goal);
        calendarGoals.add(calendarGoal);
        return calendarGoal;
    }

    public String changeGoalName(String goalName, String newGoalName, List<CalendarGoal> calendarGoals) {
        validateCalendarGoal(newGoalName, calendarGoals);
        return findCalendarGoal(goalName, calendarGoals).changeGoalName(newGoalName);
    }

    private void validateCalendarGoal(String goalName, List<CalendarGoal> calendarGoals) {
        boolean duplication = calendarGoals.stream()
                .anyMatch(calendarGoal -> calendarGoal.isSameGoal(goalName));

        if (duplication) {
            throw new CalendarGoalDuplicateException();
        }
    }

    private CalendarGoal findCalendarGoal(String goalName, List<CalendarGoal> calendarGoals) {
        return calendarGoals.stream()
                .filter(calendarGoal -> calendarGoal.isSameGoal(goalName))
                .findFirst()
                .orElseThrow(CalendarGoalNotFoundException::new);
    }

}
