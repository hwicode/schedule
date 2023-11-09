package hwicode.schedule.calendar.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.infra.jpa_repository.DailyScheduleRepository;
import hwicode.schedule.calendar.presentation.daily_schedule.dto.DailyScheduleSaveRequest;
import io.restassured.http.ContentType;
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
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DailyScheduleEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyScheduleRepository dailyScheduleRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    // 23시 59분 59.99초에 LocalDate.now()를 입력하면 컨트롤러에서 LocalDate.now()를 사용할 때 날짜가 달라질 수 있음
    @Test
    void 계획표_생성_요청() {
        //given
        LocalDate date = LocalDate.now();
        DailyScheduleSaveRequest dailyScheduleSaveRequest = new DailyScheduleSaveRequest(date);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(dailyScheduleSaveRequest);

        //when
        Response response = requestSpecification.when()
                .post("/daily-todo-lists");

        //then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThat(dailyScheduleRepository.findAll()).hasSize(1);
    }

}
