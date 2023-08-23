package hwicode.schedule.dailyschedule.daily_schedule_query.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.SubTaskQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.TaskQueryResponse;
import hwicode.schedule.dailyschedule.shared_domain.*;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class DailyScheduleQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DailyScheduleQueryService dailyScheduleQueryService;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    private static Stream<List<Integer>> providePlusDays() {
            return Stream.of(
                    List.of(1, 2, 4, 7, 14, 60),
                    List.of(4, 5, 6, 10, 20, 50),
                    List.of(2, 5, 7, 8, 9, 12, 30)
            );
    }

    @MethodSource("providePlusDays")
    @ParameterizedTest
    void daily_schedule_테이블에서_daily_schedule의_간략한_정보를_조회할_수_있다(List<Integer> plusDays) {
        // given
        LocalDate date = LocalDate.of(2023, 8, 1);

        List<DailyScheduleSummaryQueryResponse> expectedResponses = new ArrayList<>();
        for (int plusDay : plusDays) {
            DailyScheduleSummaryQueryResponse dailyScheduleSummaryQueryResponse = saveDailyScheduleSummary(date.plusDays(plusDay));

            if (plusDay <= 30) {
                expectedResponses.add(dailyScheduleSummaryQueryResponse);
            }
        }

        // when
        List<DailyScheduleSummaryQueryResponse> result = dailyScheduleQueryService.getDailyToDoListQueryResponse(YearMonth.from(date));

        // then
        assertThat(result).isEqualTo(expectedResponses);
    }

    private DailyScheduleSummaryQueryResponse saveDailyScheduleSummary(LocalDate date) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("daily_schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("today", date);
        parameters.put("total_difficulty_score", 4);
        parameters.put("today_done_percent", 50);
        parameters.put("total_learning_time", 180);
        parameters.put("emoji", Emoji.NOT_BAD.name());
        parameters.put("main_tag_name", "rr");

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new DailyScheduleSummaryQueryResponse(
                key.longValue(), date, 4, 50, Emoji.NOT_BAD, "rr"
        );
    }

    private static Stream<Arguments> provideNumberOfTaskAndSubTask() {
        return Stream.of(
                arguments(1, 5), arguments(1, 2), arguments(2, 3),
                arguments(2, 4), arguments(3, 2), arguments(3, 5)
        );
    }

    @MethodSource("provideNumberOfTaskAndSubTask")
    @ParameterizedTest
    void daily_schedule과_task_그리고_sub_task_테이블에서_직접_조회할_수_있다(int numberOfTask, int numberOfSubTask) {
        // given
        DailyScheduleQueryResponse dailyScheduleQueryResponse = saveDailySchedule(LocalDate.of(2023, 8, 1));

        List<TaskQueryResponse> taskQueryResponses = new ArrayList<>();
        for (int i = 0; i < numberOfTask; i++) {
            TaskQueryResponse taskQueryResponse = saveTask(dailyScheduleQueryResponse.getId());
            addSubTaskQueryResponsesTo(taskQueryResponse, numberOfSubTask);
            taskQueryResponses.add(taskQueryResponse);
        }

        dailyScheduleQueryResponse.setTaskQueryResponses(taskQueryResponses);

        // when
        DailyScheduleQueryResponse result = dailyScheduleQueryService.getDailyScheduleQueryResponse(dailyScheduleQueryResponse.getId());

        // then
        assertThat(dailyScheduleQueryResponse).isEqualTo(result);
    }

    private void addSubTaskQueryResponsesTo(TaskQueryResponse taskQueryResponse, int numberOfSubTask) {
        List<SubTaskQueryResponse> subTaskQueryResponses = new ArrayList<>();
        for (int i = 0; i < numberOfSubTask; i++) {
            subTaskQueryResponses.add(saveSubTask(taskQueryResponse.getId()));
        }
        taskQueryResponse.setSubTaskQueryResponses(subTaskQueryResponses);
    }

    private DailyScheduleQueryResponse saveDailySchedule(LocalDate date) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("daily_schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("today", date);
        parameters.put("total_difficulty_score", 4);
        parameters.put("today_done_percent", 50);
        parameters.put("total_learning_time", 180);
        parameters.put("emoji", Emoji.NOT_BAD.name());
        parameters.put("main_tag_name", "rr");
        parameters.put("review", "review");

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new DailyScheduleQueryResponse(
                key.longValue(), date, 4, 50, 180, Emoji.NOT_BAD, "rr", "review"
        );
    }

    private TaskQueryResponse saveTask(Long dailyScheduleId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("task").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "name");
        parameters.put("priority", Priority.SECOND.name());
        parameters.put("importance", Importance.SECOND.name());
        parameters.put("difficulty", Difficulty.NORMAL.name());
        parameters.put("task_status", TaskStatus.TODO.name());
        parameters.put("daily_schedule_id", dailyScheduleId);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new TaskQueryResponse(
                key.longValue(), "name", Priority.SECOND, Importance.SECOND, Difficulty.NORMAL, TaskStatus.TODO
        );
    }

    private SubTaskQueryResponse saveSubTask(Long taskId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("sub_task").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "name");
        parameters.put("sub_task_status", SubTaskStatus.TODO.name());
        parameters.put("task_id", taskId);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new SubTaskQueryResponse(
                key.longValue(), "name", SubTaskStatus.TODO, taskId
        );
    }

}
