package hwicode.schedule.tag.application.find_service;

import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.exception.application.DailyTagListNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyTagListFindService {

    public static DailyTagList findById(DailyTagListRepository dailyTagListRepository, Long dailyTagListId) {
        return dailyTagListRepository.findById(dailyTagListId)
                .orElseThrow(DailyTagListNotFoundException::new);
    }

    public static DailyTagList findDailyTagListWithDailyTags(DailyTagListRepository dailyTagListRepository, Long dailyTagListId) {
        return dailyTagListRepository.findDailyTagListWithDailyTags(dailyTagListId)
                .orElseThrow(DailyTagListNotFoundException::new);
    }
}
