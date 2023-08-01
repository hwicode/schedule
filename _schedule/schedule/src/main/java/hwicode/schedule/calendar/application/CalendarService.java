package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.infra.GoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final CalendarGoalDomainService calendarGoalDomainService;
    private final CalendarProviderService calendarProviderService;

    private final GoalRepository goalRepository;
    private final CalendarGoalRepository calendarGoalRepository;

    @Transactional
    public Long saveGoal(String name, List<YearMonth> yearMonths) {
        Goal goal = new Goal(name);
        goalRepository.save(goal);

        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);
        List<CalendarGoal> calendarGoals = createCalendarGoals(calendars, goal);

        calendarGoalRepository.saveAll(calendarGoals);
        return goal.getId();
    }

    @Transactional
    public Long addGoalToCalendars(Long goalId, List<YearMonth> yearMonths) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(GoalNotFoundException::new);

        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);
        List<CalendarGoal> calendarGoals = createCalendarGoals(calendars, goal);

        calendarGoalRepository.saveAll(calendarGoals);
        return goal.getId();
    }

    private List<CalendarGoal> createCalendarGoals(List<Calendar> calendars, Goal goal) {
        List<CalendarGoal> calendarGoals = new ArrayList<>();
        for (Calendar calendar : calendars) {
            List<CalendarGoal> foundCalendarGoals = calendarGoalRepository.findAllByCalendar(calendar);
            CalendarGoal calendarGoal = calendarGoalDomainService.addGoalToCalendar(calendar, goal, foundCalendarGoals);
            calendarGoals.add(calendarGoal);
        }
        return calendarGoals;
    }

    @Transactional
    public String changeGoalName(YearMonth yearMonth, String goalName, String newGoalName) {
        Calendar calendar = calendarProviderService.provideCalendar(yearMonth);
        List<CalendarGoal> foundCalendarGoals = calendarGoalRepository.findAllByCalendar(calendar);
        return calendarGoalDomainService.changeGoalName(goalName, newGoalName, foundCalendarGoals);
    }

    @Transactional
    public int changeWeeklyStudyDate(YearMonth yearMonth, int weeklyStudyDate) {
        Calendar calendar = calendarProviderService.provideCalendar(yearMonth);
        calendar.changeWeeklyStudyDate(weeklyStudyDate);
        return weeklyStudyDate;
    }

}
