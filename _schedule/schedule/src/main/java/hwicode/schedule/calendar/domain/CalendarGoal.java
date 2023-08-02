package hwicode.schedule.calendar.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public Goal getGoal() {
        return this.goal;
    }

}
