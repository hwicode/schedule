package hwicode.schedule.calendar.infra.limited_repository;

import hwicode.schedule.calendar.domain.SubGoal;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubGoalSaveRepository {

    private final SubGoalRepository subGoalRepository;

    public SubGoal save(SubGoal subGoal) {
        return subGoalRepository.save(subGoal);
    }
}
