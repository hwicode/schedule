package hwicode.schedule.dailyschedule.timetable.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.application.TimeTableService;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator.StartTimeDuplicateException;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfSubTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.SubjectOfTaskRepository;
import hwicode.schedule.dailyschedule.timetable.infra.TimeTableRepository;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.delete.LearningTimeDeleteRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.endtime_modify.EndTimeModifyRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.save.LearningTimeSaveRequest;
import hwicode.schedule.dailyschedule.timetable.presentation.timetable.dto.starttime_modify.StartTimeModifyRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static hwicode.schedule.dailyschedule.timetable.TimeTableDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    @Autowired
    SubjectOfTaskRepository subjectOfTaskRepository;

    @Autowired
    SubjectOfSubTaskRepository subjectOfSubTaskRepository;

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
                .post(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}/learning-times", port));

        // then
        response.then()
                .statusCode(HttpStatus.CREATED.value());

        assertThatThrownBy(() -> timeTableService.saveLearningTime(timeTable.getId(), START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간_시작_시간_변경_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        StartTimeModifyRequest startTimeModifyRequest = createStartTimeModifyRequest(START_TIME, NEW_START_TIME);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .pathParam("learningTimeId", learningTime.getId())
                .contentType(ContentType.JSON)
                .body(startTimeModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/start-time", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        assertThatThrownBy(() -> timeTableService.saveLearningTime(timeTable.getId(), NEW_START_TIME))
                .isInstanceOf(StartTimeDuplicateException.class);
    }

    @Test
    void 학습_시간_끝나는_시간_변경_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTableRepository.save(timeTable);

        LocalDateTime endTime = START_TIME.plusMinutes(30);
        EndTimeModifyRequest endTimeModifyRequest = createEndTimeModifyRequest(START_TIME, endTime);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .pathParam("learningTimeId", learningTime.getId())
                .contentType(ContentType.JSON)
                .body(endTimeModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}/end-time", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isEqualTo(30);
    }

    @Test
    void 학습_시간_삭제_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());
        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        timeTableRepository.save(timeTable);

        LearningTimeDeleteRequest learningTimeDeleteRequest = new LearningTimeDeleteRequest(START_TIME);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .pathParam("learningTimeId", learningTime.getId())
                .contentType(ContentType.JSON)
                .body(learningTimeDeleteRequest);

        // when
        Response response = requestSpecification.when()
                .delete(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}/learning-times/{learningTimeId}", port));

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        TimeTable savedTimeTable = timeTableRepository.findTimeTableWithLearningTimes(timeTable.getId()).orElseThrow();
        assertThat(savedTimeTable.getTotalLearningTime()).isZero();
    }

    @Test
    void 특정_학습_주제_총_학습_시간_요청() {
        // given
        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubject(SUBJECT);

        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .queryParam("subject", SUBJECT);

        // when
        Response response = requestSpecification.when()
                .get(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}/subject-total-time", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void Task_학습_주제_총_학습_시간_요청() {
        // given
        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(SUBJECT));

        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubjectOfTask(subjectOfTask);

        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .queryParam("subject_of_task_id", subjectOfTask.getId());

        // when
        Response response = requestSpecification.when()
                .get(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}/task-total-time", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void SubTask_학습_주제_총_학습_시간_요청() {
        // given
        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(SUBJECT));

        TimeTable timeTable = new TimeTable(START_TIME.toLocalDate());

        LearningTime learningTime = timeTable.createLearningTime(START_TIME);
        timeTable.changeLearningTimeEndTime(START_TIME, START_TIME.plusMinutes(30));
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);

        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .pathParam("timeTableId", timeTable.getId())
                .queryParam("subject_of_subtask_id", subjectOfSubTask.getId());

        // when
        Response response = requestSpecification.when()
                .get(String.format("http://localhost:%s/dailyschedule/timetables/{timeTableId}/subtask-total-time", port));

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }
}
