package hwicode.schedule.dailyschedule.todolist.domain;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReviewDate {

    private LocalDate date;
    private final Set<ReviewDateTask> reviewDateTasks = new HashSet<>();

    public ReviewDate(LocalDate date) {
        this.date = date;
    }

    void addReviewDateTask(ReviewDateTask reviewDateTask) {
        this.reviewDateTasks.add(reviewDateTask);
    }

    public List<Task> createTodayReviewTasks(DailyToDoList dailyToDoList) {
        return reviewDateTasks.stream()
                .map(reviewDateTask -> reviewDateTask.cloneTask(dailyToDoList))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDate that = (ReviewDate) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
