package hwicode.schedule.dailyschedule.checklist.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "daily_checklist_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DailyChecklist dailyChecklist;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubTask> subTasks = new ArrayList<>();

    public Task(){}

    public Task(String name) {
        this.name = name;
        this.difficulty = Difficulty.NORMAL;
        this.status = Status.TODO;
    }

    public Task(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
        this.status = Status.TODO;
    }

    Status addSubTask(SubTask subTask) {
        validateSubTaskDuplication(subTask.getName());
        subTasks.add(subTask);
        subTask.savedInTask(this);

        if (this.status.isDone()) {
            changeToProgress();
        }
        return this.status;
    }

    private void validateSubTaskDuplication(String name) {
        boolean duplication = subTasks.stream()
                .anyMatch(subTask -> subTask.isSame(name));

        if (duplication) {
            throw new IllegalStateException();
        }
    }

    Status changeSubTaskStatus(String name, Status subTaskStatus) {
        findSubTaskBy(name).changeStatus(subTaskStatus);
        checkTaskStatusConditions(subTaskStatus);

        return this.status;
    }

    private void checkTaskStatusConditions(Status subTaskStatus) {
        if (this.status.isDone() && !subTaskStatus.isDone()) {
            changeToProgress();
        }

        else if (this.status.isTodo() && !subTaskStatus.isTodo()) {
            changeToProgress();
        }
    }

    Status deleteSubTask(String name) {
        subTasks.remove(findSubTaskBy(name));
        return this.status;
    }

    private SubTask findSubTaskBy(String name) {
        return subTasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    Status changeToDone() {
        boolean isAllDone = isAllSameStatus(Status.DONE);
        if (!isAllDone) {
            throw new IllegalStateException();
        }

        this.status = Status.DONE;
        return this.status;
    }

    Status changeToTodo() {
        boolean isAllTodo = isAllSameStatus(Status.TODO);
        if (!isAllTodo) {
            throw new IllegalStateException();
        }

        this.status = Status.TODO;
        return this.status;
    }

    private boolean isAllSameStatus(Status status) {
        int count = (int) subTasks.stream()
                .filter(subTask -> subTask.isSameStatus(status))
                .count();

        return count == subTasks.size();
    }

    Status changeToProgress() {
        this.status = Status.PROGRESS;
        return this.status;
    }

    void savedInChecklist(DailyChecklist dailyChecklist) {
        this.dailyChecklist = dailyChecklist;
    }

    void changeDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    boolean isDone() {
        return this.status.isDone();
    }

    int getDifficultyScore() {
        return difficulty.getValue();
    }

    String getName() {
        return this.name;
    }
}
