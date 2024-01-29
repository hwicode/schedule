package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.review.exception.domain.review_list.ReviewListForbiddenException;
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

    public void checkOwnership(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new ReviewListForbiddenException();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getToday() {
        return today;
    }

}
