package hwicode.schedule.calendar.endtoend;

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

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DailyScheduleEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 날짜로_계획표_조회_요청() {
        //given
        LocalDate date = LocalDate.of(2023, 8, 23);

        RequestSpecification requestSpecification = given().port(port)
                .param("date", String.valueOf(date));

        //when
        Response response = requestSpecification.when()
                .get("/daily-todo-lists");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

}
