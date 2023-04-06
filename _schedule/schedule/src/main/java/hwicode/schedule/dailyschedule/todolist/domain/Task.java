package hwicode.schedule.dailyschedule.todolist.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTask> subTasks = new ArrayList<>();

    public SubTask createSubTask(String subTaskName) {
        validateSubTaskDuplication(subTaskName);

        SubTask subTask = new SubTask(this, subTaskName);
        subTasks.add(subTask);

        return subTask;
    }

    public String changeSubTaskName(String subTaskName, String newSubTaskName) {
        validateSubTaskDuplication(newSubTaskName);
        return findSubTaskBy(subTaskName).changeSubTaskName(newSubTaskName);
    }

    private void validateSubTaskDuplication(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}