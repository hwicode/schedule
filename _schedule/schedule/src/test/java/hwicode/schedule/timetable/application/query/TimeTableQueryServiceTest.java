package hwicode.schedule.timetable.application.query;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.application.query.dto.LearningTimeQueryResponse;
import hwicode.schedule.timetable.domain.LearningTime;
import hwicode.schedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.timetable.domain.TimeTable;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfSubTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.SubjectOfTaskRepository;
import hwicode.schedule.timetable.infra.jpa_repository.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static hwicode.schedule.timetable.TimeTableDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TimeTableQueryServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    TimeTableQueryService timeTableQueryService;

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
    void 특정_날짜의_학습_시간들을_조회할_수_있다() {
        // given
        LocalDate date = LocalDate.of(2023, 8, 24);

        TimeTable timeTable = new TimeTable(date);
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = new SubjectOfTask(SUBJECT);
        subjectOfTaskRepository.save(subjectOfTask);

        SubjectOfSubTask subjectOfSubTask = new SubjectOfSubTask(NEW_SUBJECT);
        subjectOfSubTaskRepository.save(subjectOfSubTask);

        LocalDateTime startTime = date.atStartOfDay();
        LearningTimeQueryResponse learningTimeQueryResponse = createLearningTimeWithSubjectOfTask(timeTable, startTime, subjectOfTask);
        LearningTimeQueryResponse learningTimeQueryResponse2 = createLearningTimeWithSubjectOfSubTask(timeTable, startTime.plusHours(1), subjectOfSubTask);
        LearningTimeQueryResponse learningTimeQueryResponse3 = createLearningTimeWithSubject(timeTable, startTime.plusHours(2), SUBJECT);

        // when
        List<LearningTimeQueryResponse> result = timeTableQueryService.getLearningTimeQueryResponses(date);

        // then
        List<LearningTimeQueryResponse> expectedResponses = List.of(learningTimeQueryResponse, learningTimeQueryResponse2, learningTimeQueryResponse3);
        assertThat(result).isEqualTo(expectedResponses);
    }

    private LearningTimeQueryResponse createLearningTimeWithSubjectOfTask(TimeTable timeTable, LocalDateTime startTime, SubjectOfTask subjectOfTask) {
        LearningTime learningTime = new LearningTime(timeTable, startTime);
        learningTime.changeSubjectOfTask(subjectOfTask);

        learningTimeRepository.save(learningTime);

        return LearningTimeQueryResponse.builder()
                .id(learningTime.getId())
                .startTime(startTime)
                .endTime(null)
                .taskId(subjectOfTask.getId())
                .build();
    }

    private LearningTimeQueryResponse createLearningTimeWithSubjectOfSubTask(TimeTable timeTable, LocalDateTime startTime, SubjectOfSubTask subjectOfSubTask) {
        LearningTime learningTime = new LearningTime(timeTable, startTime);
        learningTime.changeSubjectOfSubTask(subjectOfSubTask);

        learningTimeRepository.save(learningTime);

        return  LearningTimeQueryResponse.builder()
                .id(learningTime.getId())
                .startTime(startTime)
                .endTime(null)
                .subTaskId(subjectOfSubTask.getId())
                .build();
    }

    private LearningTimeQueryResponse createLearningTimeWithSubject(TimeTable timeTable, LocalDateTime startTime, String subject) {
        LearningTime learningTime = new LearningTime(timeTable, startTime);
        learningTime.changeSubject(subject);

        learningTimeRepository.save(learningTime);

        return  LearningTimeQueryResponse.builder()
                .id(learningTime.getId())
                .startTime(startTime)
                .endTime(null)
                .subject(SUBJECT)
                .build();
    }

}
