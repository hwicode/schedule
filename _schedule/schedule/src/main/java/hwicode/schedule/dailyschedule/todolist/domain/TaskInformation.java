package hwicode.schedule.dailyschedule.todolist.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class TaskInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "taskInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTaskInformation> subTasks = new ArrayList<>();

    public SubTaskInformation createSubTaskInformation(String subTaskName) {
        validateSubTaskDuplication(subTaskName);

        SubTaskInformation subTaskInformation = new SubTaskInformation(this, subTaskName);
        subTasks.add(subTaskInformation);

        return subTaskInformation;
    }

    private void validateSubTaskDuplication(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }
}
