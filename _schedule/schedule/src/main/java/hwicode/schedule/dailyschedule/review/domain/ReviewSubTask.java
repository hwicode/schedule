package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@EqualsAndHashCode(of = {"id", "name"})
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

    @Column(nullable = false)
    private Long userId;

    public ReviewSubTask(ReviewTask reviewTask, String name, Long userId) {
        this.reviewTask = reviewTask;
        this.name = name;
        this.subTaskStatus = SubTaskStatus.TODO;
        this.userId = userId;
    }

    ReviewSubTask cloneSubTask(ReviewTask reviewTask) {
        return new ReviewSubTask(reviewTask, this.name, this.userId);
    }

    public Long getId() {
        return id;
    }

}
