package hwicode.schedule.calendar.infra.jpa_repository.goal;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.Goal;

import java.util.List;

public interface GoalFindRepository {

    List<Goal> findAllByCalendar( Calendar calendar);
}
