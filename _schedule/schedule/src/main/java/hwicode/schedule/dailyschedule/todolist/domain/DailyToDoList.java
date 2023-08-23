package hwicode.schedule.dailyschedule.todolist.domain;

import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_schedule")
@Entity
public class DailyToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String review;

    @ColumnDefault(value = "NOT_BAD")
    @Enumerated(value = EnumType.STRING)
    private Emoji emoji;

    // 테스트 코드에서만 사용되는 생성자!
    public DailyToDoList(Emoji emoji) {
        this.emoji = emoji;
    }

    public boolean writeReview(String review) {
        if (review.equals(this.review)) {
            return false;
        }
        this.review = review;
        return true;
    }

    public boolean changeTodayEmoji(Emoji emoji) {
        if (this.emoji == emoji) {
            return false;
        }
        this.emoji = emoji;
        return true;
    }

    public Long getId() {
        return id;
    }
}
