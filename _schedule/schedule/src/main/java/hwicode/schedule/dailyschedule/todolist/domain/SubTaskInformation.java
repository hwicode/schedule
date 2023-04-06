package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sub_task")
@Entity
public class SubTaskInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private SubTaskStatus subTaskStatus;

    SubTaskInformation(Task task, String name) {
        this.task = task;
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    public String changeSubTaskName(String name) {
        this.name = name;
        return name;
    }
}
