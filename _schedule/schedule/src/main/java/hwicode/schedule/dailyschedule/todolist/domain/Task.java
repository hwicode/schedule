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

    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTask> subTasks = new ArrayList<>();

    public Priority changePriority(Priority priority) {
        this.priority = priority;
        return this.priority;
    }

    public SubTask createSubTask(String subTaskName) {
        validateSubTaskName(subTaskName);

        SubTask subTask = new SubTask(this, subTaskName);
        subTasks.add(subTask);

        return subTask;
    }

    public String changeSubTaskName(String subTaskName, String newSubTaskName) {
        validateSubTaskName(newSubTaskName);
        return findSubTaskBy(subTaskName).changeSubTaskName(newSubTaskName);
    }

    private void validateSubTaskName(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication || name == null) {
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
