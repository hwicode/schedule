package hwicode.schedule.dailyschedule.todolist.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "daily_to_do_list")
@Entity
public class DailyToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "dailyToDoList", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Task> tasks = new ArrayList<>();
}
