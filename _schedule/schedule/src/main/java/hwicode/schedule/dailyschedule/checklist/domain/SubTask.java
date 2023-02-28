package hwicode.schedule.dailyschedule.checklist.domain;

import javax.persistence.*;

@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public SubTask() {}

    public SubTask(String name) {
        this.status = Status.TODO;
        this.name = name;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    void changeStatus(Status status) {
        this.status = status;
    }

    boolean isSameStatus(Status status) {
        return this.status == status;
    }

    String getName() {
        return this.name;
    }
}
