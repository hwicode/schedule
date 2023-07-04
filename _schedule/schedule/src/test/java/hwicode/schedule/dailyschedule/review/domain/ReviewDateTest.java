package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewDateTest {

    private List<ReviewTask> addTasksToReviewDate(ReviewDate reviewDate, int number) {
        List<ReviewTask> reviewTasks = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            ReviewTask reviewTask = new ReviewTask(TASK_NAME + i, TaskStatus.PROGRESS, createReviewSubTasks(i));
            reviewTasks.add(reviewTask);
            reviewTask.review(List.of(reviewDate));
        }
        return reviewTasks;
    }

    private List<ReviewSubTask> createReviewSubTasks(int number) {
        List<ReviewSubTask> reviewSubTasks = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            ReviewSubTask reviewSubTask = new ReviewSubTask(SUB_TASK_NAME + i, SubTaskStatus.DONE);
            reviewSubTasks.add(reviewSubTask);
        }
        return reviewSubTasks;
    }

//    @ValueSource(ints = {1, 3, 5, 6, 9})
//    @ParameterizedTest
//    void 복습_날짜에서_오늘_복습하기로한_과제들을_복사할_수_있다(int number) {
//        // given
//        ReviewDate reviewDate = new ReviewDate(START_DATE);
//        List<ReviewTask> reviewTasks = addTasksToReviewDate(reviewDate, number);
//
//        // when
//        List<Task> clonedTasks = reviewDate.createTodayReviewTasks(new DailyToDoList());
//
//        // then
//        assertThat(clonedTasks)
//                .hasSize(tasks.size())
//                .containsAll(tasks);
//    }

}
