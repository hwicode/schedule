package hwicode.schedule.dailyschedule.daily_schedule_query.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.shared_domain.Emoji;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DailyScheduleQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void daily_schedule_한_달_치_요약본_조회_요청() {
        //given
        YearMonth yearMonth = YearMonth.of(2023, 8);

        RequestSpecification requestSpecification = given().port(port)
                .param("yearMonth", String.valueOf(yearMonth));

        //when
        Response response = requestSpecification.when()
                .get("/dailyschedule/calendar/daily-todo-lists");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void daily_schedule_조회_요청() {
        // given
        Long dailyToDoListId = saveDailySchedule(LocalDate.of(2023, 8, 23));

        RequestSpecification requestSpecification = given().port(port);

        // when
        Response response = requestSpecification.when()
                .get("/dailyschedule/daily-todo-lists/{dailyToDoListId}", dailyToDoListId);

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    private Long saveDailySchedule(LocalDate date) {
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
        return key.longValue();
    }

}
