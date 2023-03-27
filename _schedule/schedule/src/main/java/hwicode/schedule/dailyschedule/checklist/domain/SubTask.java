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
    private Status status;

    public SubTask(String name) {
        this.status = Status.TODO;
        this.name = name;
    }

    void savedInTask(Task task) {
        this.task = task;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    void changeStatus(Status status) {
        this.status = status;
    }

    String getName() {
        return this.name;
    }

    public Long getId() {
        return id;
    }

    public boolean isSameStatus(Status status) {
        return this.status == status;
    }
}
