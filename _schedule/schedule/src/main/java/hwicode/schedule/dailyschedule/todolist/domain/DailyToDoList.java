package hwicode.schedule.dailyschedule.todolist.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "daily_to_do_list")
@Entity
public class DailyToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String review;

    @Enumerated(value = EnumType.STRING)
    private Emoji emoji;

    @OneToMany(mappedBy = "dailyToDoList", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Task> tasks = new ArrayList<>();

    public String writeReview(String review) {
        this.review = review;
        return this.review;
    }

    public Emoji changeTodayEmoji(Emoji emoji) {
        this.emoji = emoji;
        return this.emoji;
    }

    public Task createTask(TaskCreateDto taskCreateDto) {
        validateTaskName(taskCreateDto.getTaskName());

        Task task = new Task(this, taskCreateDto);
        tasks.add(task);

        return task;
    }

    public String changeTaskName(String taskName, String newTaskName) {
        validateTaskName(newTaskName);
        return findTaskBy(taskName).changeTaskName(newTaskName);
    }

    private void validateTaskName(String name) {
        boolean duplication = tasks.stream()
                .anyMatch(task -> task.isSame(name));

        if (duplication || name == null) {
            throw new IllegalArgumentException();
        }
    }

    private Task findTaskBy(String name) {
        return tasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
