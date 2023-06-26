package hwicode.schedule.review.domain;

import java.time.LocalDate;

public class ReviewDate {

    private LocalDate date;

    public ReviewDate(LocalDate date) {
        this.date = date;
    }

    boolean isSame(LocalDate date) {
        return this.date.isEqual(date);
    }

    LocalDate getDate() {
        return this.date;
    }
}
