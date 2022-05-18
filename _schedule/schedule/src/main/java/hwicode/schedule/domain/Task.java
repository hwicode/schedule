package hwicode.schedule.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name")
    private String name;

    @ColumnDefault("2")
    private int taskPoint;

    @ColumnDefault("3")
    private int priority;

    @ColumnDefault("false")
    private boolean taskComplete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<DetailTask> detailTasks = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public Schedule getDaySchedule() {
        return schedule;
    }

    public List<DetailTask> getDetailTasks() {
        return detailTasks;
    }
}
