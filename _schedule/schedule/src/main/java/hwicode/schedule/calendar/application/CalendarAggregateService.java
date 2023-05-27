package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.infra.limited_repository.CalendarGoalSaveAllRepository;
import hwicode.schedule.calendar.infra.limited_repository.GoalSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CalendarAggregateService {

    private final CalendarProviderService calendarProviderService;
    private final GoalSaveRepository goalSaveRepository;
    private final CalendarGoalSaveAllRepository calendarGoalSaveAllRepository;

    @Transactional
    public Long saveGoal(String name, List<YearMonth> yearMonths) {
        Goal goal = new Goal(name);
        goalSaveRepository.save(goal);

        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);
        List<CalendarGoal> calendarGoals = calendars.stream()
                .map(calendar -> calendar.addGoal(goal))
                .collect(Collectors.toList());

        calendarGoalSaveAllRepository.saveAll(calendarGoals);
        return goal.getId();
    }

    @Transactional
    public String changeGoalName(YearMonth yearMonth, String goalName, String newGoalName) {
        Calendar calendar = calendarProviderService.provideCalendar(yearMonth);
        return calendar.changeGoalName(goalName, newGoalName);
    }

}
