package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sub_task")
@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private SubTaskStatus subTaskStatus;

    public SubTask(Task task, String name) {
        this.task = task;
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    SubTask cloneSubTask() {
        return new SubTask(this.task, this.name);
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(id, subTask.id)
                && Objects.equals(task, subTask.task)
                && Objects.equals(name, subTask.name)
                && subTaskStatus == subTask.subTaskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, name, subTaskStatus);
    }
}
