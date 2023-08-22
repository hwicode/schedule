package hwicode.schedule.dailyschedule.daily_schedule_query.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.YearMonth;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DailyScheduleQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void daily_schedule_요약본_조회_요청() {
        //given
        YearMonth yearMonth = YearMonth.of(2023, 8);

        RequestSpecification requestSpecification = given().port(port)
                .param("yearMonth", String.valueOf(yearMonth));

        //when
        Response response = requestSpecification.when()
                .get("/dailyschedule/daily-todo-lists");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
