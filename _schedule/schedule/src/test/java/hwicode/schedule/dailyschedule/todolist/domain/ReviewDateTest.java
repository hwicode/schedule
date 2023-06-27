package hwicode.schedule.dailyschedule.todolist.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewDateTest {

    private List<Task> addTasksToReviewDate(ReviewDate reviewDate, int number) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Task task = new Task(null, TASK_NAME + i, null, null, null, createSubTasks(i));
            tasks.add(task);
            task.review(List.of(reviewDate));
        }
        return tasks;
    }

    private List<SubTask> createSubTasks(int number) {
        List<SubTask> subTasks = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            SubTask subTask = new SubTask(null, SUB_TASK_NAME + i);
            subTasks.add(subTask);
        }
        return subTasks;
    }

    @ValueSource(ints = {1, 3, 5, 6, 9})
    @ParameterizedTest
    void 복습_날짜에서_오늘_복습하기로한_과제들을_복사할_수_있다(int number) {
        // given
        ReviewDate reviewDate = new ReviewDate(START_DATE);
        List<Task> tasks = addTasksToReviewDate(reviewDate, number);

        // when
        List<Task> clonedTasks = reviewDate.createTodayReviewTasks(new DailyToDoList());

        // then
        assertThat(clonedTasks)
                .hasSize(tasks.size())
                .containsAll(tasks);
    }

}
