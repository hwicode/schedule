package hwicode.schedule.dailyschedule.timetable.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.START_TIME;
import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.createLearningTimeSaveRequest;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeTableEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableService timeTableService;

    @Autowired
    TimeTableRepository timeTableRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 학습_시간_생성_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        timeTableRepository.save(timeTable);

        LearningTimeSaveRequest learningTimeSaveRequest = createLearningTimeSaveRequest(START_TIME);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .contentType(ContentType.JSON)
                .body(learningTimeSaveRequest);

        // when
        Response response = requestSpecification.when()
                .post(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}", port));

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThatThrownBy(() -> timeTableService.saveLearningTime(timeTable.getId(), START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

}
