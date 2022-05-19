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

    protected DetailTask() {

    }

    //==연관관계 메서드==//
    public void setTask(Task task) {
        this.task = task;
    }

    //==생성 메서드==//
    public static DetailTask createDetailTask(Task task, String name) {
        DetailTask detailTask = new DetailTask();
        detailTask.setTask(task);
        detailTask.setName(name);
        return detailTask;
    }

    public void setName(String name) {
        this.name = name;
    }

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
