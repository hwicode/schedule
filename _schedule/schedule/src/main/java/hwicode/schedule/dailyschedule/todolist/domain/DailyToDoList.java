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

    @OneToMany(mappedBy = "dailyToDoList", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Task> tasks = new ArrayList<>();

    public Task createTask(TaskCreateDto taskCreateDto) {
        validateTaskName(taskCreateDto.getTaskName());

        Task task = new Task(this, taskCreateDto);
        tasks.add(task);

        return task;
    }

    private void validateTaskName(String name) {
        boolean duplication = tasks.stream()
                .anyMatch(task -> task.isSame(name));

        if (duplication || name == null) {
            throw new IllegalArgumentException();
        }
    }

}
