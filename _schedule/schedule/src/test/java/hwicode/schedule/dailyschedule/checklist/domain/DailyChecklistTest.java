package hwicode.schedule.dailyschedule.checklist.domain;

import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.dailyschedule_domain.Difficulty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DailyChecklistTest {

    private DailyChecklist createDailyChecklistWithThreeTaskCheckers(List<TaskChecker> taskCheckers) {
        DailyChecklist dailyChecklist = new DailyChecklist();

        dailyChecklist.addTaskChecker(taskCheckers.get(0));
        dailyChecklist.addTaskChecker(taskCheckers.get(1));
        dailyChecklist.addTaskChecker(taskCheckers.get(2));

        return dailyChecklist;
    }

    private static List<TaskChecker> makeTaskCheckersWithDifficulty(Difficulty difficulty, Difficulty difficulty2, Difficulty difficulty3) {
        return Arrays.asList(
                new TaskChecker(TASK_CHECKER_NAME, TaskStatus.TODO, difficulty),
                new TaskChecker(TASK_CHECKER_NAME2, TaskStatus.TODO, difficulty2),
                new TaskChecker(TASK_CHECKER_NAME3, TaskStatus.TODO, difficulty3)
        );
    }

    @Test
    void 체크리스트에_보통_난이도의_과제체커가_어려움으로_바뀌면_총_점수가_1점_증가한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.NORMAL, Difficulty.NORMAL, Difficulty.NORMAL);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // when
        dailyChecklist.changeDifficulty(TASK_CHECKER_NAME, Difficulty.HARD);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(7);
    }

    @Test
    void 체크리스트에_쉬움_난이도의_과제체커가_어려움으로_바뀌면_총_점수가_2점_증가한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.NORMAL, Difficulty.EASY, Difficulty.NORMAL);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // when
        dailyChecklist.changeDifficulty(TASK_CHECKER_NAME2, Difficulty.HARD);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(7);
    }

    @Test
    void 체크리스트에_어려운_난이도의_과제체커가_쉬움으로_바뀌면_총_점수가_2점_감소한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // when
        dailyChecklist.changeDifficulty(TASK_CHECKER_NAME, Difficulty.EASY);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    void 체크리스트에_보통_난이도의_과제체커가_쉬움으로_바뀌면_총_점수가_1점_감소한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // when
        dailyChecklist.changeDifficulty(TASK_CHECKER_NAME3, Difficulty.EASY);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(6);
    }

    @Test
    void 체크리스트에_보통_난이도의_과제체커를_쉬움으로_바꾸고_어려움_과제체커를_보통으로_바꾸면_총_점수가_3점_감소한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // when
        dailyChecklist.changeDifficulty(TASK_CHECKER_NAME3, Difficulty.EASY);
        dailyChecklist.changeDifficulty(TASK_CHECKER_NAME, Difficulty.NORMAL);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    void 과제체커_3개_중에_NORMAL인_과제체커1개가_삭제되면_총_점수가_2점_감소한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.NORMAL);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // when
        dailyChecklist.deleteTaskChecker(TASK_CHECKER_NAME2);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(5);
    }

    @Test
    void 과제체커_3개_중에_Hard인_과제체커와_EASY인_과제체커가_삭제되면_총_점수가_4점_감소한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // when
        dailyChecklist.deleteTaskChecker(TASK_CHECKER_NAME3);
        dailyChecklist.deleteTaskChecker(TASK_CHECKER_NAME);

        //then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(2);
    }



    @Test
    void 체크리스트에_3개의_과제체커_중_과제체커1개가_삭제됐을_때_성취도를_체크한다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.HARD);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME2, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME3, TaskStatus.PROGRESS);

        //when
        dailyChecklist.deleteTaskChecker(TASK_CHECKER_NAME2);

        // then
        int doneScore = taskCheckers.get(0).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) 6 * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @Test
    void 과제체커의_이름이_중복되면_에러가_발생한다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();
        TaskChecker taskChecker = new TaskChecker(NEW_TASK_CHECKER_NAME, Difficulty.NORMAL);
        dailyChecklist.addTaskChecker(taskChecker);

        TaskChecker duplicatedTaskChecker = new TaskChecker(NEW_TASK_CHECKER_NAME, Difficulty.NORMAL);

        // when then
        assertThatThrownBy(() -> dailyChecklist.addTaskChecker(duplicatedTaskChecker))
                .isInstanceOf(TaskCheckerNameDuplicationException.class);
    }

    @Test
    void 체크리스트에_존재하지_않는_과제체커를_조회하면_에러가_발생한다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();

        // when then
        assertThatThrownBy(() -> dailyChecklist.deleteTaskChecker(NEW_TASK_CHECKER_NAME))
                .isInstanceOf(TaskCheckerNotFoundException.class);
    }

    @Test
    void 체크리스트에_과제체커가_없을_때_성취도를_체크하면_0이_된다() {
        // given
        DailyChecklist dailyChecklist = new DailyChecklist();

        // when then
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(0);
    }

    @Test
    void 체크리스트내에_있는_DONE과제체커에_서브과제체커를_더하면_성취도_계산에서_제외된다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.HARD);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME2, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME3, TaskStatus.DONE);

        //when
        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME2, new SubTaskChecker(NEW_SUB_TASK_CHECKER_NAME));

        // then
        int doneScore = taskCheckers.get(0).getDifficultyScore() + taskCheckers.get(2).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) 8 * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @Test
    void 체크리스트내에_있는_DONE과제체커에_서브과제체커의_상태를_PROGRESS로_바꾸면_성취도_계산에서_제외된다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.HARD);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME2, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME3, TaskStatus.DONE);

        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME2, new SubTaskChecker(NEW_SUB_TASK_CHECKER_NAME));

        //when
        dailyChecklist.changeSubTaskStatus(TASK_CHECKER_NAME2, NEW_SUB_TASK_CHECKER_NAME, SubTaskStatus.PROGRESS);

        // then
        int doneScore = taskCheckers.get(0).getDifficultyScore() + taskCheckers.get(2).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) 8 * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @Test
    void 체크리스트내에_있는_과제체커에_서브과제체커를_삭제하면_서브과제체커가_삭제된다() {
        // given
        List<TaskChecker> taskCheckers = makeTaskCheckersWithDifficulty(Difficulty.HARD, Difficulty.NORMAL, Difficulty.HARD);
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        dailyChecklist.addSubTaskChecker(TASK_CHECKER_NAME, new SubTaskChecker(NEW_SUB_TASK_CHECKER_NAME));

        //when
        dailyChecklist.deleteSubTaskChecker(TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);

        // then
        assertThatThrownBy(() -> dailyChecklist.deleteSubTaskChecker(TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME))
                .isInstanceOf(SubTaskCheckerNotFoundException.class);
    }

    private static Stream<Arguments> provideTaskCheckersAndTotalScore() {
        return Stream.of(
                arguments(makeTaskCheckersWithDifficulty(
                        Difficulty.NORMAL, Difficulty.NORMAL, Difficulty.NORMAL
                ), 6),
                arguments(makeTaskCheckersWithDifficulty(
                        Difficulty.HARD, Difficulty.HARD, Difficulty.HARD
                ), 9),
                arguments(makeTaskCheckersWithDifficulty(
                        Difficulty.NORMAL, Difficulty.HARD, Difficulty.EASY
                ), 6),
                arguments(makeTaskCheckersWithDifficulty(
                        Difficulty.EASY, Difficulty.NORMAL, Difficulty.EASY
                ), 4)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTaskCheckersAndTotalScore")
    void 체크리스트에_과제체커가_생기면_총_점수를_계산할_수_있다(List<TaskChecker> taskCheckers, int totalScore) {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        // then
        assertThat(dailyChecklist.getTotalDifficultyScore()).isEqualTo(totalScore);
    }

    @ParameterizedTest
    @MethodSource("provideTaskCheckersAndTotalScore")
    void 체크리스트에_3개의_과제체커_중_2개를_완료했을_때_성취도를_체크한다(List<TaskChecker> taskCheckers, int totalScore) {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        //when
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME3, TaskStatus.DONE);

        // then
        int doneScore = taskCheckers.get(0).getDifficultyScore() + taskCheckers.get(2).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) totalScore * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @ParameterizedTest
    @MethodSource("provideTaskCheckersAndTotalScore")
    void 체크리스트에_3개의_과제체커_중_1개를_완료했을_때_성취도를_체크한다(List<TaskChecker> taskCheckers, int totalScore) {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        //when
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME, TaskStatus.DONE);
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME3, TaskStatus.PROGRESS);

        // then
        int doneScore = taskCheckers.get(0).getDifficultyScore();
        int donePercent = (int) (doneScore / (double) totalScore * 100);
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

    @ParameterizedTest
    @MethodSource("provideTaskCheckersAndTotalScore")
    void 체크리스트에_3개의_과제체커_중_0개를_완료하면_성취도는_0이_된다(List<TaskChecker> taskCheckers) {
        // given
        DailyChecklist dailyChecklist = createDailyChecklistWithThreeTaskCheckers(taskCheckers);

        //when
        dailyChecklist.changeTaskStatus(TASK_CHECKER_NAME2, TaskStatus.TODO);

        // then
        int donePercent = 0;
        assertThat(dailyChecklist.getTodayDonePercent()).isEqualTo(donePercent);
    }

}
