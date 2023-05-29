package hwicode.schedule.calendar.infra.limited_repository;

import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.exception.infra.GoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoalFindAndSaveRepository {

    private final GoalRepository goalRepository;

    public Goal save(Goal goal) {
        return goalRepository.save(goal);
    }

    public Goal findById(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(GoalNotFoundException::new);
    }

}
