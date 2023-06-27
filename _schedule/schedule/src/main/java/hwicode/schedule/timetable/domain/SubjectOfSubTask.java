package hwicode.schedule.timetable.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sub_task")
@Entity
public class SubjectOfSubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public SubjectOfSubTask(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
