package hwicode.schedule.dailyschedule.review.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_schedule")
@Entity
public class ReviewList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate today;

    public ReviewList(LocalDate today) {
        this.today = today;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getToday() {
        return today;
    }

}
