package hwicode.schedule.dailyschedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduleProgressPointTest {

    Schedule schedule;

    String NAME = "name";
    String NAME2 = "name2";
    String NAME3 = "name3";

    @BeforeEach
    public void beforeEach() {
        schedule = new Schedule();
    }

    @ParameterizedTest
    @MethodSource("provideTasksAndTotalScore")
    public void 계획표에_과제가_생기면_총_점수를_계산할_수_있다(List<Task> tasks, int totalScore) {
        // given
        schedule.addTask(tasks.get(0));
        schedule.addTask(tasks.get(1));
        schedule.addTask(tasks.get(2));

        // then
        assertThat(schedule.getTotalDifficultyScore()).isEqualTo(totalScore);
    }

    private Stream<Arguments> provideTasksAndTotalScore() {
        return Stream.of(
                arguments(makeTasksWithDifficulty(
                        Difficulty.NORMAL, Difficulty.NORMAL, Difficulty.NORMAL
                ), 6),
                arguments(makeTasksWithDifficulty(
                        Difficulty.HARD, Difficulty.HARD, Difficulty.HARD
                ), 9),
                arguments(makeTasksWithDifficulty(
                        Difficulty.NORMAL, Difficulty.HARD, Difficulty.EASY
                ), 6),
                arguments(makeTasksWithDifficulty(
                        Difficulty.EASY, Difficulty.NORMAL, Difficulty.EASY
                ), 4)
        );
    }

    private List<Task> makeTasksWithDifficulty(Difficulty difficulty, Difficulty difficulty2, Difficulty difficulty3) {
        return Arrays.asList(
                new Task(NAME, difficulty),
                new Task(NAME2, difficulty2),
                new Task(NAME3, difficulty3)
        );
    }

    @Test
    public void 계획표에_보통_난이도의_과제가_어려움으로_바뀌면_총_점수가_1점_증가한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.NORMAL, Difficulty.NORMAL, Difficulty.NORMAL);
        schedule.addTask(tasks.get(0));
        schedule.addTask(tasks.get(1));
        schedule.addTask(tasks.get(2));

        // when
        schedule.changeTaskDifficulty(NAME, Difficulty.HARD);

        //then
        assertThat(schedule.getTotalDifficultyScore()).isEqualTo(7);
    }

    @Test
    public void 계획표에_쉬움_난이도의_과제가_어려움으로_바뀌면_총_점수가_2점_증가한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.NORMAL, Difficulty.EASY, Difficulty.NORMAL);
        schedule.addTask(tasks.get(0));
        schedule.addTask(tasks.get(1));
        schedule.addTask(tasks.get(2));

        // when
        schedule.changeTaskDifficulty(NAME2, Difficulty.HARD);

        //then
        assertThat(schedule.getTotalDifficultyScore()).isEqualTo(7);
    }

    @Test
    public void 계획표에_어려운_난이도의_과제가_쉬움으로_바뀌면_총_점수가_2점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        schedule.addTask(tasks.get(0));
        schedule.addTask(tasks.get(1));
        schedule.addTask(tasks.get(2));

        // when
        schedule.changeTaskDifficulty(NAME, Difficulty.EASY);

        //then
        assertThat(schedule.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    public void 계획표에_보통_난이도의_과제가_쉬움으로_바뀌면_총_점수가_1점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        schedule.addTask(tasks.get(0));
        schedule.addTask(tasks.get(1));
        schedule.addTask(tasks.get(2));

        // when
        schedule.changeTaskDifficulty(NAME3, Difficulty.EASY);

        //then
        assertThat(schedule.getTotalDifficultyScore()).isEqualTo(6);
    }

    @Test
    public void 계획표에_보통_난이도의_과제를_쉬움으로_바꾸고_어려움_과제를_보통으로_바꾸면_총_점수가_3점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        schedule.addTask(tasks.get(0));
        schedule.addTask(tasks.get(1));
        schedule.addTask(tasks.get(2));

        // when
        schedule.changeTaskDifficulty(NAME3, Difficulty.EASY);
        schedule.changeTaskDifficulty(NAME, Difficulty.NORMAL);

        //then
        assertThat(schedule.getTotalDifficultyScore()).isEqualTo(5);
    }


}

class Schedule {

    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void changeTaskDifficulty(String name, Difficulty difficulty) {
        findTaskBy(name).changeDifficulty(difficulty);
    }

    private Task findTaskBy(String name) {
        return tasks.stream()
                .filter(s -> s.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getTotalDifficultyScore() {
        return tasks.stream()
                .mapToInt(Task::getDifficultyScore)
                .sum();
    }
}
