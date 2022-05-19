package hwicode.schedule.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Schedule {

    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @ColumnDefault("0")
    private int dayPoint;

    @ColumnDefault("0")
    private int dayPercentage;

    @Column(columnDefinition = "TEXT")
    private String dayFeedback;

    private LocalDateTime scheduleDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id")
    private User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    protected Schedule() {

    }

    //==연관관계 메서드==//
    private void setUser(User user) {
        this.user = user;
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setSchedule(this);
    }

    //==생성 메서드==//
    public static Schedule createSchedule(User user) {
        Schedule schedule = new Schedule();
        schedule.setUser(user);
        schedule.setScheduleDateTime(LocalDateTime.now());
        return schedule;
    }

    private void setScheduleDateTime(LocalDateTime localDateTime) {
        this.scheduleDateTime = localDateTime;
    }

    public LocalDateTime getScheduleDateTime() {
        return scheduleDateTime;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public int getDayPoint() {
        return dayPoint;
    }

    public int getDayPercentage() {
        return dayPercentage;
    }

    public String getDayFeedback() {
        return dayFeedback;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
