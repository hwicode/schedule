package hwicode.schedule.dailyschedule.timetable.application;

import hwicode.schedule.dailyschedule.timetable.domain.LearningTime;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfSubTask;
import hwicode.schedule.dailyschedule.timetable.domain.SubjectOfTask;
import hwicode.schedule.dailyschedule.timetable.infra.LearningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class LearningTimeService {

    private final LearningTimeRepository learningTimeRepository;

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

    @Transactional
    public String changeSubjectOfSubTask(Long learningTimeId, SubjectOfSubTask subjectOfSubTask) {
        LearningTime learningTime = learningTimeRepository.findById(learningTimeId)
                .orElseThrow(IllegalArgumentException::new);

        return learningTime.changeSubjectOfSubTask(subjectOfSubTask);
    }
}
