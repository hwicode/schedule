package hwicode.schedule.calendar.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
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
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CalendarQueryEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    CalendarRepository calendarRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 캘린더_조회_요청() {
        //given
        YearMonth noneExistYearMonth = YearMonth.of(2023, 8);

        RequestSpecification requestSpecification = given().port(port)
                .param("yearMonth", String.valueOf(noneExistYearMonth));

        //when
        Response response = requestSpecification.when()
                .get("/calendars");

        //then
        response.then()
                .statusCode(HttpStatus.OK.value());

        assertThat(calendarRepository.findAll()).hasSize(1);
    }

}
