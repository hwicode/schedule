package hwicode.schedule.timetable.domain;

import hwicode.schedule.timetable.exception.domain.subject_of_task.SubjectOfTaskForbiddenException;
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

    public void checkOwnership(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new SubjectOfTaskForbiddenException();
        }
    }

    String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
