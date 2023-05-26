package hwicode.schedule.calendar.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_schedule")
@Entity
public class DailySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "calendar_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Calendar calendar;

    @Column(nullable = false)
    private LocalDate today;

    public DailySchedule(Calendar calendar, LocalDate today) {
        this.calendar = calendar;
        this.today = today;
    }

}
