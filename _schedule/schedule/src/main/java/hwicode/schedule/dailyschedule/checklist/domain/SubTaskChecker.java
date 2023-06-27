package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
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

    public SubTaskChecker(TaskChecker taskChecker, String name) {
        this.taskChecker = taskChecker;
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    String changeName(String name) {
        this.name = name;
        return name;
    }

    void changeStatus(SubTaskStatus subTaskStatus) {
        this.subTaskStatus = subTaskStatus;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return id;
    }

    public boolean isSameStatus(SubTaskStatus subTaskStatus) {
        return this.subTaskStatus == subTaskStatus;
    }
}
