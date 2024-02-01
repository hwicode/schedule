package hwicode.schedule.calendar.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.calendar.application.daily_schedule.Time;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.calendar.CalendarDataHelper.createAccessToken;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DailyScheduleEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyScheduleRepository dailyScheduleRepository;

    @Autowired
    TokenProvider tokenProvider;

    @MockBean
    Time time;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 계획표_생성_요청() {
        //given
        Long userId = 1L;
        String accessToken = createAccessToken(tokenProvider, userId);

        LocalDate date = LocalDate.now();
        when(time.now()).thenReturn(date);

        DailyScheduleSaveRequest dailyScheduleSaveRequest = new DailyScheduleSaveRequest(date);

        RequestSpecification requestSpecification = given()
                .port(port)
                .header("Authorization", BEARER + accessToken)
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
