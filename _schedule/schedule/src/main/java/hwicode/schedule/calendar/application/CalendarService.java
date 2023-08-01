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
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final CalendarProviderService calendarProviderService;
    private final GoalRepository goalRepository;
    private final CalendarGoalRepository calendarGoalRepository;

    @Transactional
    public Long saveGoal(String name, List<YearMonth> yearMonths) {
        Goal goal = new Goal(name);
        goalRepository.save(goal);

        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);
        List<CalendarGoal> calendarGoals = calendars.stream()
                .map(calendar -> calendar.addGoal(goal))
                .collect(Collectors.toList());

        calendarGoalRepository.saveAll(calendarGoals);
        return goal.getId();
    }

    @Transactional
    public Long addGoalToCalendars(Long goalId, List<YearMonth> yearMonths) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(GoalNotFoundException::new);

        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);
        List<CalendarGoal> calendarGoals = calendars.stream()
                .map(calendar -> calendar.addGoal(goal))
                .collect(Collectors.toList());

        calendarGoalRepository.saveAll(calendarGoals);
        return goal.getId();
    }

    @Transactional
    public String changeGoalName(YearMonth yearMonth, String goalName, String newGoalName) {
        Calendar calendar = calendarProviderService.provideCalendar(yearMonth);
        return calendar.changeGoalName(goalName, newGoalName);
    }

    @Transactional
    public int changeWeeklyStudyDate(YearMonth yearMonth, int weeklyStudyDate) {
        Calendar calendar = calendarProviderService.provideCalendar(yearMonth);
        calendar.changeWeeklyStudyDate(weeklyStudyDate);
        return weeklyStudyDate;
    }

}
