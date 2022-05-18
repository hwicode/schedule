package hwicode.schedule.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
public class DetailTask {

    @Id
    @GeneratedValue
    @Column(name = "detail_task_id")
    private Long id;

    @ColumnDefault("false")
    private boolean detailTaskComplete;

    @Column(name = "detail_task_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Task getTask() {
        return task;
    }
}
