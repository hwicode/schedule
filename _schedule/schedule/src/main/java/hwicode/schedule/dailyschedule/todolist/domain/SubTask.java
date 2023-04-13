package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.common.domain.SubTaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

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

    SubTask(Task task, String name) {
        this.task = task;
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return id;
    }
}
