package hwicode.schedule.timetable.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.timetable.exception.application.SubjectOfSubTaskNotFoundException;
import hwicode.schedule.timetable.exception.application.SubjectOfTaskNotFoundException;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfSubTaskFindRepository;
import hwicode.schedule.timetable.infra.limited_repository.SubjectOfTaskFindRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SubjectFindRepositoryTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    SubjectOfTaskFindRepository subjectOfTaskFindRepository;

    @Autowired
    SubjectOfSubTaskFindRepository subjectOfSubTaskFindRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 존재하지_않는_과제의_주제를_조회하면_에러가_발생한다() {
        //given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> subjectOfTaskFindRepository.findById(noneExistId))
                .isInstanceOf(SubjectOfTaskNotFoundException.class);
    }

    @Test
    void 존재하지_않는_서브_과제의_주제를_조회하면_에러가_발생한다() {
        //given
        Long noneExistId = 1L;

        // when then
        assertThatThrownBy(() -> subjectOfSubTaskFindRepository.findById(noneExistId))
                .isInstanceOf(SubjectOfSubTaskNotFoundException.class);
    }
}
