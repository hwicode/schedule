package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sub_task")
@Entity
public class ReviewSubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "task_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewTask reviewTask;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private SubTaskStatus subTaskStatus;

    public ReviewSubTask(ReviewTask reviewTask, String name) {
        this.reviewTask = reviewTask;
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
    }

    // 테스트 코드에서만 사용되는 생성자!
    public ReviewSubTask(String name, SubTaskStatus subTaskStatus) {
        this.name = name;
        this.subTaskStatus = subTaskStatus;
    }

    ReviewSubTask cloneSubTask() {
        return new ReviewSubTask(this.reviewTask, this.name);
    }

    public Long getId() {
        return id;
    }

}
