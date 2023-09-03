package hwicode.schedule.calendar.infra.jpa_repository.goal;

import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GoalFindRepositoryImpl implements GoalFindRepository {

    private final CalendarGoalRepository calendarGoalRepository;

    @Override
    public List<Goal> findAllByCalendar(Long calendarId) {
        List<CalendarGoal> calendarGoals = calendarGoalRepository.findAllByCalendarWithGoal(calendarId);
        return calendarGoals.stream()
                .map(CalendarGoal::getGoal)
                .collect(Collectors.toList());
    }
}
