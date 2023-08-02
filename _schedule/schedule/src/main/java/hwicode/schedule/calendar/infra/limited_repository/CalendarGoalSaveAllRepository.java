package hwicode.schedule.calendar.infra.limited_repository;

import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarGoalSaveAllRepository {

    private final CalendarGoalRepository calendarGoalRepository;

    public List<CalendarGoal> saveAll(List<CalendarGoal> calendarGoals) {
        return calendarGoalRepository.saveAll(calendarGoals);
    }
}
