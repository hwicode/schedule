package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.common.jpa_converter.ReviewCycleDatesAttributeConverter;
import hwicode.schedule.dailyschedule.review.exception.domain.review_cycle.InvalidReviewCycleDateException;
import hwicode.schedule.dailyschedule.review.exception.domain.review_cycle.ReviewCycleNullException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = ReviewCycleDatesAttributeConverter.class)
    private List<Integer> reviewCycleDates;

    public ReviewCycle(String name, List<Integer> reviewCycleDates) {
        validateReviewCycleDates(reviewCycleDates);
        this.name = name;
        this.reviewCycleDates = new ArrayList<>(reviewCycleDates);
    }

    private void validateReviewCycleDates(List<Integer> reviewCycleDates) {
        for (Integer reviewCycleDate : reviewCycleDates) {
            if (reviewCycleDate == null) {
                throw new ReviewCycleNullException();
            }

            if (reviewCycleDate < 1 || reviewCycleDate > 60) {
                throw new InvalidReviewCycleDateException();
            }
        }
    }

    public boolean changeName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    public List<Integer> changeCycle(List<Integer> newReviewCycleDates) {
        validateReviewCycleDates(newReviewCycleDates);
        this.reviewCycleDates = new ArrayList<>(newReviewCycleDates);
        return new ArrayList<>(newReviewCycleDates);
    }

    public List<Integer> getCycle() {
        return new ArrayList<>(reviewCycleDates);
    }

    public Long getId() {
        return id;
    }

}
