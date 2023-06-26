package hwicode.schedule.review.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewTask {

    private final List<ReviewDateTask> reviewDateTasks = new ArrayList<>();

    public List<ReviewDateTask> review(List<ReviewDate> reviewDates) {
        List<ReviewDateTask> uniqueReviewDateTasks = reviewDates.stream()
                .filter(this::isUnique)
                .map(reviewDate -> new ReviewDateTask(this, reviewDate))
                .collect(Collectors.toList());

        reviewDateTasks.addAll(uniqueReviewDateTasks);
        return uniqueReviewDateTasks;
    }

    private boolean isUnique(ReviewDate reviewDate) {
        LocalDate date = reviewDate.getDate();
        boolean isDuplicate = reviewDateTasks.stream()
                .anyMatch(reviewDateTask -> reviewDateTask.isSameDate(date));
        return !isDuplicate;
    }
}
