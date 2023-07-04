package hwicode.schedule.dailyschedule.todolist.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sub_task")
@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;

    @Column(nullable = false)
    private String name;

    // 테스트 코드에서만 사용되는 생성자!
    public SubTask(Task task, String name) {
        this.task = task;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

}
