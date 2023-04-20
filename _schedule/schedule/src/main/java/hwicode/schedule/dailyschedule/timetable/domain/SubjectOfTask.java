package hwicode.schedule.dailyschedule.timetable.domain;

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

    public SubjectOfTask(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
