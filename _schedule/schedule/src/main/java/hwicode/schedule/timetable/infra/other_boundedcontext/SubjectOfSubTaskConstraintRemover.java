package hwicode.schedule.timetable.infra.other_boundedcontext;

import hwicode.schedule.dailyschedule.todolist.infra.service_impl.SubTaskConstraintRemover;
import hwicode.schedule.timetable.infra.jpa_repository.LearningTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class SubjectOfSubTaskConstraintRemover implements SubTaskConstraintRemover {

    private final LearningTimeRepository learningTimeRepository;

    @Override
    @Transactional
    public void delete(Long subTaskId) {
        learningTimeRepository.deleteSubjectOfSubTaskBelongingToLearningTime(subTaskId);
    }
}
