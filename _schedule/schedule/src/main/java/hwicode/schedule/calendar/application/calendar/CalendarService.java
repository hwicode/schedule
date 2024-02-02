package hwicode.schedule.calendar.application.calendar;

import hwicode.schedule.calendar.application.calendar.dto.*;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.infra.GoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.goal.GoalRepository;
import hwicode.schedule.calendar.infra.limited_repository.CalendarGoalSaveAllRepository;
import hwicode.schedule.common.login.validator.PermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final CalendarGoalDomainService calendarGoalDomainService;
    private final CalendarProviderService calendarProviderService;

    private final GoalRepository goalRepository;
    private final CalendarGoalSaveAllRepository calendarGoalSaveAllRepository;

    @Transactional
    public Long saveCalendar(CalendarSaveCommand command) {
        Calendar calendar = calendarProviderService.provideCalendar(command.getUserId(), command.getYearMonth());
        return calendar.getId();
    }

    @Transactional
    public Long saveGoal(GoalSaveCommand command) {
        Goal goal = new Goal(command.getName(), command.getUserId());
        goalRepository.save(goal);

        List<Calendar> calendars = calendarProviderService.provideCalendars(command.getUserId(), command.getYearMonths());
        List<CalendarGoal> calendarGoals = addGoal(calendars, goal);

        calendarGoalSaveAllRepository.saveAll(calendarGoals);
        return goal.getId();
    }

    @Transactional
    public Long addGoalToCalendars(GoalAddToCalendersCommand command) {
        Goal goal = goalRepository.findById(command.getGoalId())
                .orElseThrow(GoalNotFoundException::new);
        PermissionValidator.validateOwnership(command.getUserId(), goal.getUserId());

        List<Calendar> calendars = calendarProviderService.provideCalendars(command.getUserId(), command.getYearMonths());
        List<CalendarGoal> calendarGoals = addGoal(calendars, goal);

        calendarGoalSaveAllRepository.saveAll(calendarGoals);
        return goal.getId();
    }

    private List<CalendarGoal> addGoal(List<Calendar> calendars, Goal goal) {
        List<CalendarGoal> calendarGoals = new ArrayList<>();
        for (Calendar calendar : calendars) {
            List<Goal> foundCalendarGoals = goalRepository.findAllByCalendar(calendar.getId());
            CalendarGoal calendarGoal = calendarGoalDomainService.addGoalToCalendar(calendar, goal, foundCalendarGoals);
            calendarGoals.add(calendarGoal);
        }
        return calendarGoals;
    }

    @Transactional
    public String changeGoalName(GoalModifyNameCommand command) {
        Calendar calendar = calendarProviderService.provideCalendar(command.getUserId(), command.getYearMonth());

        List<Goal> foundCalendarGoals = goalRepository.findAllByCalendar(calendar.getId());
        return calendarGoalDomainService.changeGoalName(command.getName(), command.getNewName(), foundCalendarGoals);
    }

    @Transactional
    public int changeWeeklyStudyDate(CalendarModifyStudyDateCommand command) {
        Calendar calendar = calendarProviderService.provideCalendar(command.getUserId(), command.getYearMonth());

        calendar.changeWeeklyStudyDate(command.getWeeklyStudyDate());
        return command.getWeeklyStudyDate();
    }

}
