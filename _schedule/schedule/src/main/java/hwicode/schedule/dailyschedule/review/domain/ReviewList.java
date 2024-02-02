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

    @Column(nullable = false)
    private Long userId;

    public ReviewList(LocalDate today, Long userId) {
        this.today = today;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getToday() {
        return today;
    }

    public Long getUserId() {
        return userId;
    }
}
