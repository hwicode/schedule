package hwicode.schedule.dailyschedule.review.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    public ReviewDate(LocalDate date) {
        this.date = date;
    }

    LocalDate getDate() {
        return this.date;
    }
}
