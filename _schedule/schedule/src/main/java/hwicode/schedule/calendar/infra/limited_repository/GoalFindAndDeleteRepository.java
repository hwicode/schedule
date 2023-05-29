package hwicode.schedule.calendar.infra.limited_repository;

import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.infra.GoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoalFindAndDeleteRepository {

    private final GoalRepository goalRepository;

    public Goal findGoalWithSubGoals(Long id) {
        return goalRepository.findGoalWithSubGoals(id)
                .orElseThrow(GoalNotFoundException::new);
    }

    public void delete(Goal goal) {
        goalRepository.delete(goal);
    }
}
