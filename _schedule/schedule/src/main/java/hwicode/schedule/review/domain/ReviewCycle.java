package hwicode.schedule.review.domain;

import java.util.ArrayList;
import java.util.List;

public class ReviewCycle {

    private String name;
    private List<Integer> reviewCycleDates;

    public ReviewCycle(String name, List<Integer> reviewCycleDates) {
        validateReviewCycleDates(reviewCycleDates);
        this.name = name;
        this.reviewCycleDates = new ArrayList<>(reviewCycleDates);
    }

    private void validateReviewCycleDates(List<Integer> reviewCycleDates) {
        for (Integer reviewCycleDate : reviewCycleDates) {
            if (reviewCycleDate == null) {
                throw new IllegalArgumentException();
            }

            if (reviewCycleDate < 1 || reviewCycleDate > 60) {
                throw new IllegalArgumentException();
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

}
