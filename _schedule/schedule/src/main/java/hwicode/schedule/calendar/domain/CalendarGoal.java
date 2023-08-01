package hwicode.schedule.calendar.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.YearMonth;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CalendarGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "calendar_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Calendar calendar;

    @JoinColumn(name = "goal_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Goal goal;

    public CalendarGoal(Calendar calendar, Goal goal) {
        this.calendar = calendar;
        this.goal = goal;
    }

    public String changeGoalName(String name) {
        return goal.changeName(name);
    }

    public boolean isSameGoal(String name) {
        return goal.isSame(name);
    }

    boolean isSameCalendar(YearMonth yearMonth) {
        return calendar.isSame(yearMonth);
    }
}
