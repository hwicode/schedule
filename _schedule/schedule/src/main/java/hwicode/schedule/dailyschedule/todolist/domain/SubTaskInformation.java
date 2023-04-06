package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.SubTaskStatus;
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
    private TaskInformation taskInformation;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private SubTaskStatus subTaskStatus;

    SubTaskInformation(TaskInformation taskInformation, String name) {
        this.taskInformation = taskInformation;
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    String changeSubTaskName(String name) {
        this.name = name;
        return name;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }
}
