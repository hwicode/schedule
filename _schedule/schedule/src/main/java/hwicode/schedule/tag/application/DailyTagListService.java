package hwicode.schedule.tag.application;

import hwicode.schedule.tag.application.find_service.DailyTagListFindService;
import hwicode.schedule.tag.application.find_service.TagFindService;
import hwicode.schedule.tag.domain.DailyTag;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DailyTagListService {

    private final DailyTagListRepository dailyTagListRepository;
    private final TagRepository tagRepository;
    private final DailyTagRepository dailyTagRepository;

    @Transactional
    public Long addTagToDailyTagList(Long dailyTagListId, Long tagId) {
        Tag tag = TagFindService.findById(tagRepository, tagId);
        DailyTagList dailyTagList = DailyTagListFindService.findDailyTagListWithDailyTags(dailyTagListRepository, dailyTagListId);

        DailyTag dailyTag = dailyTagList.addTag(tag);
        dailyTagRepository.save(dailyTag);
        return dailyTag.getId();
    }

    @Transactional
    public Long deleteTagToDailyTagList(Long dailyTagListId, Long tagId) {
        Tag tag = TagFindService.findById(tagRepository, tagId);
        DailyTagList dailyTagList = DailyTagListFindService.findDailyTagListWithDailyTags(dailyTagListRepository, dailyTagListId);

        dailyTagList.deleteTag(tag);
        return dailyTagList.getId();
    }

}
