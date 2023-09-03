package hwicode.schedule.calendar.domain;

import hwicode.schedule.calendar.exception.domain.calendar.WeeklyDateNotValidException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.YearMonth;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private YearMonth yearAndMonth;

    private int weeklyStudyDate;

    public Calendar(YearMonth yearMonth) {
        this.yearAndMonth = yearMonth;
        weeklyStudyDate = 5;
    }

    public boolean changeWeeklyStudyDate(int weeklyDate) {
        validateWeeklyDate(weeklyDate);

        if (this.weeklyStudyDate == weeklyDate) {
            return false;
        }
        this.weeklyStudyDate = weeklyDate;
        return true;
    }

    private void validateWeeklyDate(int weeklyDate) {
        if (0 > weeklyDate || weeklyDate > 7) {
            throw new WeeklyDateNotValidException();
        }
    }

    public Long getId() {
        return this.id;
    }

}
