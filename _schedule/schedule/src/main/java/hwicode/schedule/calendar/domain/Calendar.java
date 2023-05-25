package hwicode.schedule.calendar.domain;

import hwicode.schedule.calendar.exception.domain.CalendarGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.CalendarGoalNotFoundException;
import hwicode.schedule.calendar.exception.domain.WeeklyDateNotValidException;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Calendar {

    private YearMonth yearMonth;
    private int weeklyStudyDate;
    private final List<CalendarGoal> calendarGoals = new ArrayList<>();

    public Calendar(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
        weeklyStudyDate = 5;
    }

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
            throw new CalendarGoalDuplicateException();
        }
    }

    private CalendarGoal findCalendarGoalBy(String name) {
        return calendarGoals.stream()
                .filter(calendarGoal -> calendarGoal.isSameGoal(name))
                .findFirst()
                .orElseThrow(CalendarGoalNotFoundException::new);
    }

    public boolean changeWeeklyStudyDate(int weeklyDate) {
        validateWeeklyDate(weeklyDate);

        if (this.weeklyStudyDate == weeklyDate) {
            return false;
        }
        this.weeklyStudyDate = weeklyDate;
        return true;
    }

    private void validateWeeklyDate(int weeklyDate) {
        if (0 > weeklyDate || weeklyDate > 7) {
            throw new WeeklyDateNotValidException();
        }
    }

    boolean isSame(YearMonth yearMonth) {
        return this.yearMonth.equals(yearMonth);
    }
}