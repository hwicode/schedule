package hwicode.schedule.timetable.endtoend;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.TimeTableDataHelper;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import hwicode.schedule.timetable.presentation.learningtime.dto.subject_modify.LearningTimeSubjectModifyRequest;
import hwicode.schedule.timetable.presentation.learningtime.dto.subjectofsubtask_modify.LearningTimeSubjectOfSubTaskModifyRequest;
import hwicode.schedule.timetable.presentation.learningtime.dto.subjectoftask_modify.LearningTimeSubjectOfTaskModifyRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LearningTimeEndToEndTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @Autowired
    SubjectOfTaskRepository subjectOfTaskRepository;

    @Autowired
    SubjectOfSubTaskRepository subjectOfSubTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 학습_주제_삭제_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        learningTime.changeSubject(TimeTableDataHelper.SUBJECT);
        timeTableRepository.save(timeTable);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON);

        // when
        Response response = requestSpecification.when()
                .delete("/dailyschedule/learning-times/{learningTimeId}", learningTime.getId());

        // then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

    @Test
    void 학습_주제_수정_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        LearningTimeSubjectModifyRequest learningTimeSubjectModifyRequest = new LearningTimeSubjectModifyRequest(TimeTableDataHelper.NEW_SUBJECT);

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(learningTimeSubjectModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/learning-times/{learningTimeId}/subject", learningTime.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void Task_학습_주제_수정_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask(TimeTableDataHelper.SUBJECT));

        LearningTimeSubjectOfTaskModifyRequest learningTimeSubjectOfTaskModifyRequest = new LearningTimeSubjectOfTaskModifyRequest(
                subjectOfTask.getId());

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(learningTimeSubjectOfTaskModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/learning-times/{learningTimeId}/subject-of-task", learningTime.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void SubTask_학습_주제_수정_요청() {
        // given
        Long userId = 1L;
        TimeTable timeTable = new TimeTable(TimeTableDataHelper.START_TIME.toLocalDate(), userId);
        LearningTime learningTime = timeTable.createLearningTime(TimeTableDataHelper.START_TIME);
        timeTableRepository.save(timeTable);

        SubjectOfSubTask subjectOfSubTask = subjectOfSubTaskRepository.save(new SubjectOfSubTask(TimeTableDataHelper.SUBJECT));

        LearningTimeSubjectOfSubTaskModifyRequest learningTimeSubjectOfSubTaskModifyRequest = new LearningTimeSubjectOfSubTaskModifyRequest(
                subjectOfSubTask.getId());

        RequestSpecification requestSpecification = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(learningTimeSubjectOfSubTaskModifyRequest);

        // when
        Response response = requestSpecification.when()
                .patch("/dailyschedule/learning-times/{learningTimeId}/subject-of-subtask", learningTime.getId());

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());

        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }
}
