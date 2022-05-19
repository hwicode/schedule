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

    protected Task() {

    }

    //==연관관계 메서드==//
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void addDetailTask(DetailTask detailTask) {
        detailTasks.add(detailTask);
        detailTask.setTask(this);
    }

    //==생성 메서드==//
    public static Task createTask(Schedule schedule, String name, int priority, int point) {
        Task task = new Task();
        task.setSchedule(schedule);
        task.setName(name);
        task.setPriority(priority);
        task.setTaskPoint(point);
        return task;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setPriority(int priority) {
        this.priority = priority;
    }

    private void setTaskPoint(int point) {
        this.taskPoint = point;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public List<DetailTask> getDetailTasks() {
        return detailTasks;
    }
}
