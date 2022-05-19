package hwicode.schedule.domain;

import hwicode.schedule.repository.ScheduleRepository;
import hwicode.schedule.service.response.ScheduleResponse;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
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

    private String scheduleDate;

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
        checkPoint();
        checkPercentage();
    }

    //==생성 메서드==//
    public static Schedule createSchedule(User user, String date) {
        Schedule schedule = new Schedule();
//        schedule.setUser(user);
        schedule.setScheduleDateTime(date);

        return schedule;
    }

    private void setScheduleDateTime(String localDate) {
        this.scheduleDate = localDate;
    }

    //==비즈니스 메서드==//
    public void checkPoint() {
        int totalPoint = 0;
        for (Task task : tasks) {
            totalPoint += task.getTaskPoint();
        }
        this.dayPoint = totalPoint;
    }

    //size = 0이면 큰일남
    public void checkPercentage() {
        int size = tasks.size();
        int completeNumber = 0;

        for (Task task : tasks) {
            if (task.isTaskComplete()) {
                completeNumber++;
            }
        }

        this.dayPercentage = (completeNumber / size) * 100;
    }

    public void setDayFeedback(String feedback) {
        this.dayFeedback = feedback;
    }

    public String getScheduleDateTime() {
        return scheduleDate;
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

    public String getScheduleDate() {
        return scheduleDate;
    }

    public ScheduleResponse toDto() {
        return new ScheduleResponse(dayPoint, dayPercentage, dayFeedback, scheduleDate, tasks);
    }
}
