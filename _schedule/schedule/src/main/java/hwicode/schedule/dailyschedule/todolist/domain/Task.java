package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_to_do_list_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyToDoList dailyToDoList;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "NORMAL")
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Importance importance;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTask> subTasks = new ArrayList<>();

    public Task(DailyToDoList dailyToDoList, String name) {
        this.dailyToDoList = dailyToDoList;
        this.name = name;
    }

    public void initialize(Priority priority, Importance importance) {
        this.priority = priority;
        this.importance = importance;
    }

    public boolean changePriority(Priority priority) {
        if (this.priority == priority) {
            return false;
        }
        this.priority = priority;
        return true;
    }

    public boolean changeImportance(Importance importance) {
       if (this.importance == importance) {
           return false;
       }
       this.importance = importance;
       return true;
    }

    public Long getId() {
        return this.id;
    }
}
