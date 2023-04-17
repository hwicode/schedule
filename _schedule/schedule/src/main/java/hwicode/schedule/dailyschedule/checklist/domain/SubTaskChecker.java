package hwicode.schedule.dailyschedule.checklist.domain;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor
@Table(name = "sub_task")
@Entity
public class SubTaskChecker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TaskChecker taskChecker;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private SubTaskStatus subTaskStatus;

    public SubTaskChecker(String name) {
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    public SubTaskChecker(String name, SubTaskStatus subTaskStatus) {
        this.name = name;
        this.subTaskStatus = subTaskStatus;
    }

    void savedInTaskChecker(TaskChecker taskChecker) {
        this.taskChecker = taskChecker;
    }

    String changeSubTaskCheckerName(String name) {
        this.name = name;
        return name;
    }

    void changeStatus(SubTaskStatus subTaskStatus) {
        this.subTaskStatus = subTaskStatus;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    String getName() {
        return this.name;
    }

    public Long getId() {
        return id;
    }

    public boolean isSameStatus(SubTaskStatus subTaskStatus) {
        return this.subTaskStatus == subTaskStatus;
    }
}
