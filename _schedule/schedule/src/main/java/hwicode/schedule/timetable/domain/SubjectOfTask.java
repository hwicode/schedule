package hwicode.schedule.timetable.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
@Entity
public class SubjectOfTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long userId;

    // 테스트 코드에서만 사용되는 생성자!
    public SubjectOfTask(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }
}
