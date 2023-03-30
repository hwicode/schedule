package hwicode.schedule.dailyschedule.checklist.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
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

    @Enumerated(value = EnumType.STRING)
    private SubTaskStatus subTaskStatus;

    public SubTask(String name) {
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    public SubTask(String name, SubTaskStatus subTaskStatus) {
        this.name = name;
        this.subTaskStatus = subTaskStatus;
    }

    void savedInTask(Task task) {
        this.task = task;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    void changeStatus(SubTaskStatus subTaskStatus) {
        this.subTaskStatus = subTaskStatus;
    }

    String getName() {
        return this.name;
    }

    public Long getId() {
        return id;
    }

    public boolean isSameStatus(SubTaskStatus subTaskStatus) {
        return this.subTaskStatus == subTaskStatus;
    }
}
