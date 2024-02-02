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

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long userId;

    public ReviewDate(LocalDate date, Long userId) {
        this.date = date;
        this.userId = userId;
    }

    LocalDate getDate() {
        return this.date;
    }

    public Long getId() {
        return id;
    }

}
