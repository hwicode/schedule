package hwicode.schedule.calendar.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "goal_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Goal goal;

    @Column(nullable = false)
    private String name;

    @ColumnDefault(value = "TODO")
    @Enumerated(value = EnumType.STRING)
    private SubGoalStatus subGoalStatus;

    SubGoal(Goal goal, String name) {
        this.goal = goal;
        this.name = name;
        this.subGoalStatus = SubGoalStatus.TODO;
    }

    SubGoalStatus changeStatus(SubGoalStatus subGoalStatus) {
        this.subGoalStatus = subGoalStatus;
        return this.subGoalStatus;
    }

    String changeName(String newName) {
        this.name = newName;
        return newName;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    boolean isSameStatus(SubGoalStatus subGoalStatus) {
        return this.subGoalStatus == subGoalStatus;
    }

}
