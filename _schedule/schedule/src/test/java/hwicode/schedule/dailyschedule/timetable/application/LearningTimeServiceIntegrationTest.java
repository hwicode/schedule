package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.domain.TimeTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LearningTimeServiceIntegrationTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    LearningTimeService learningTimeService;

    @Autowired
    TimeTableRepository timeTableRepository;

    @Autowired
    LearningTimeRepository learningTimeRepository;

    @Autowired
    SubjectOfTaskRepository subjectOfTaskRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 학습_시간의_학습_주제를_삭제할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(LocalDate.now());
        LearningTime learningTime = timeTable.createLearningTime(LocalDateTime.now());
        learningTime.changeSubject("학습 주제");
        timeTableRepository.save(timeTable);

        // when
        learningTimeService.deleteSubject(learningTime.getId());

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isFalse();
    }

    @Test
    void 학습_시간의_학습_주제를_수정할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(LocalDate.now());
        LearningTime learningTime = timeTable.createLearningTime(LocalDateTime.now());
        timeTableRepository.save(timeTable);

        // when
        learningTimeService.changeSubject(learningTime.getId(), "학습 주제");

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

    @Test
    void 학습_시간의_Task_학습_주제를_수정할_수_있다() {
        // given
        TimeTable timeTable = new TimeTable(LocalDate.now());
        LearningTime learningTime = timeTable.createLearningTime(LocalDateTime.now());
        timeTableRepository.save(timeTable);

        SubjectOfTask subjectOfTask = subjectOfTaskRepository.save(new SubjectOfTask("학습 주제"));

        // when
        learningTimeService.changeSubjectOfTask(learningTime.getId(), subjectOfTask);

        // then
        LearningTime savedLearningTime = learningTimeRepository.findById(learningTime.getId()).orElseThrow();
        boolean isDelete = savedLearningTime.deleteSubject();
        assertThat(isDelete).isTrue();
    }

}

@Service
class LearningTimeService {

    private final LearningTimeRepository learningTimeRepository;

    public LearningTimeService(LearningTimeRepository learningTimeRepository) {
        this.learningTimeRepository = learningTimeRepository;
    }

    @Transactional
    public boolean deleteSubject(Long learningTimeId) {
        LearningTime learningTime = learningTimeRepository.findById(learningTimeId)
                .orElseThrow(IllegalArgumentException::new);

        return learningTime.deleteSubject();
    }

    @Transactional
    public String changeSubject(Long learningTimeId, String subject) {
        LearningTime learningTime = learningTimeRepository.findById(learningTimeId)
                .orElseThrow(IllegalArgumentException::new);

        return learningTime.changeSubject(subject);
    }

    @Transactional
    public String changeSubjectOfTask(Long learningTimeId, SubjectOfTask subjectOfTask) {
        LearningTime learningTime = learningTimeRepository.findById(learningTimeId)
                .orElseThrow(IllegalArgumentException::new);

        return learningTime.changeSubjectOfTask(subjectOfTask);
    }
}

interface LearningTimeRepository extends JpaRepository<LearningTime, Long> {}
interface TimeTableRepository extends JpaRepository<TimeTable, Long> {}
interface SubjectOfTaskRepository extends JpaRepository<SubjectOfTask, Long> {}
