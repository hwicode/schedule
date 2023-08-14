package hwicode.schedule.tag.infra.limited_repository;

import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DailyTagConstraintRepository {

    private final DailyTagRepository dailyTagRepository;

    public void deleteTagForeignKeyConstraint(Long tagId) {
        dailyTagRepository.deleteAllDailyTagsBy(tagId);
    }
}
