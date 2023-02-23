package hwicode.schedule.dailyschedule.checklist;

import hwicode.schedule.dailyschedule.checklist.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.Difficulty;
import hwicode.schedule.dailyschedule.checklist.Status;
import hwicode.schedule.dailyschedule.checklist.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduleScoreAndPercentTest {

    DailyChecklist dailyChecklist;

    String NAME = "name";
    String NAME2 = "name2";
    String NAME3 = "name3";

    @BeforeEach
    public void beforeEach() {
        dailyChecklist = new DailyChecklist();
    }

    @ParameterizedTest
    @MethodSource("provideTasksAndTotalScore")
    public void 계획표에_과제가_생기면_총_점수를_계산할_수_있다(List<Task> tasks, int totalScore) {
        // given
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(totalScore);
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
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // when
        dailyChecklist.changeTaskDifficulty(NAME, Difficulty.HARD);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(7);
    }

    @Test
    public void 계획표에_쉬움_난이도의_과제가_어려움으로_바뀌면_총_점수가_2점_증가한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.NORMAL, Difficulty.EASY, Difficulty.NORMAL);
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // when
        dailyChecklist.changeTaskDifficulty(NAME2, Difficulty.HARD);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(7);
    }

    @Test
    public void 계획표에_어려운_난이도의_과제가_쉬움으로_바뀌면_총_점수가_2점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // when
        dailyChecklist.changeTaskDifficulty(NAME, Difficulty.EASY);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    public void 계획표에_보통_난이도의_과제가_쉬움으로_바뀌면_총_점수가_1점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // when
        dailyChecklist.changeTaskDifficulty(NAME3, Difficulty.EASY);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(6);
    }

    @Test
    public void 계획표에_보통_난이도의_과제를_쉬움으로_바꾸고_어려움_과제를_보통으로_바꾸면_총_점수가_3점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // when
        dailyChecklist.changeTaskDifficulty(NAME3, Difficulty.EASY);
        dailyChecklist.changeTaskDifficulty(NAME, Difficulty.NORMAL);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    public void 과제_3개_중에_NORMAL인_과제1개가_삭제되면_총_점수가_2점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // when
        dailyChecklist.deleteTask(NAME2);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    public void 과제_3개_중에_Hard인_과제와_EASY인_과제가_삭제되면_총_점수가_4점_감소한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD);
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        // when
        dailyChecklist.deleteTask(NAME3);
        dailyChecklist.deleteTask(NAME);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(2);
    }

    @ParameterizedTest
    @MethodSource("provideTasksAndTotalScore")
    public void 계획표에_3개의_과제_중_2개를_완료했을_때_성취도를_체크한다(List<Task> tasks, int totalScore) {
        // given
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        //when
        dailyChecklist.changeTaskStatus(NAME, Status.DONE);
        dailyChecklist.changeTaskStatus(NAME3, Status.DONE);

        // then
        int doneScore = tasks.get(0).getDifficultyScore() + tasks.get(2).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) totalScore * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @ParameterizedTest
    @MethodSource("provideTasksAndTotalScore")
    public void 계획표에_3개의_과제_중_1개를_완료했을_때_성취도를_체크한다(List<Task> tasks, int totalScore) {
        // given
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        //when
        dailyChecklist.changeTaskStatus(NAME, Status.DONE);
        dailyChecklist.changeTaskStatus(NAME3, Status.PROGRESS);

        // then
        int doneScore = tasks.get(0).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) totalScore * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @ParameterizedTest
    @MethodSource("provideTasksAndTotalScore")
    public void 계획표에_3개의_과제_중_0개를_완료하면_성취도는_0이_된다(List<Task> tasks, int totalScore) {
        // given
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        //when
        dailyChecklist.changeTaskStatus(NAME2, Status.TODO);

        // then
        int donePercent = 0;
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @Test
    public void 계획표에_3개의_과제_중_과제1개가_삭제됐을_때_성취도를_체크한다() {
        // given
        List<Task> tasks = makeTasksWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.HARD);
        dailyChecklist.addTask(tasks.get(0));
        dailyChecklist.addTask(tasks.get(1));
        dailyChecklist.addTask(tasks.get(2));

        dailyChecklist.changeTaskStatus(NAME, Status.DONE);
        dailyChecklist.changeTaskStatus(NAME2, Status.DONE);
        dailyChecklist.changeTaskStatus(NAME3, Status.PROGRESS);

        //when
        dailyChecklist.deleteTask(NAME2);

        // then
        int doneScore = tasks.get(0).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) 6 * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

}
