package hwicode.schedule.dailyschedule.review.domain;

import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Objects;

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

    ReviewSubTask cloneSubTask(ReviewTask reviewTask) {
        return new ReviewSubTask(reviewTask, this.name);
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewSubTask that = (ReviewSubTask) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
