package hwicode.schedule.timetable.infra.other_boundedcontext;

import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.TaskConstraintRemover;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SubjectOfTaskConstraintRemover implements TaskConstraintRemover {

    private final LearningTimeRepository learningTimeRepository;

    @Override
    @Transactional
    public void delete(Long taskId) {
        learningTimeRepository.deleteSubjectOfTaskBelongingToLearningTime(taskId);
    }
}
