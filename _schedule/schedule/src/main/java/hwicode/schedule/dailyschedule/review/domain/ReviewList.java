package hwicode.schedule.dailyschedule.review.domain;

import javax.persistence.*;

@Table(name = "daily_schedule")
@Entity
public class ReviewList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }
}
