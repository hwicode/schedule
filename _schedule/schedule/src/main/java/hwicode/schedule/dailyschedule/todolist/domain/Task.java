package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_schedule_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyToDoList dailyToDoList;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @ColumnDefault(value = "SECOND")
    @Enumerated(value = EnumType.STRING)
    private Importance importance;

    @Column(nullable = false)
    private Long userId;

    // 테스트 코드에서만 사용되는 생성자!
    public Task(DailyToDoList dailyToDoList, String name, Long userId) {
        this.dailyToDoList = dailyToDoList;
        this.name = name;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }
}
