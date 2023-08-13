package hwicode.schedule.tag.application;

import hwicode.schedule.tag.domain.DailyTag;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.DailyTagListNotFoundException;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
import hwicode.schedule.tag.infra.DailyTagListRepository;
import hwicode.schedule.tag.infra.DailyTagRepository;
import hwicode.schedule.tag.infra.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DailyTagListAggregateService {

    private final DailyTagListRepository dailyTagListRepository;
    private final TagRepository tagRepository;
    private final DailyTagRepository dailyTagRepository;

    @Transactional
    public Long addTagToDailyTagList(Long dailyTagListId, Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(TagNotFoundException::new);
        DailyTagList dailyTagList = dailyTagListRepository.findDailyTagListWithDailyTags(dailyTagListId)
                .orElseThrow(DailyTagListNotFoundException::new);

        DailyTag dailyTag = dailyTagList.addTag(tag);
        dailyTagRepository.save(dailyTag);
        return dailyTag.getId();
    }

    @Transactional
    public Long deleteTagToDailyTagList(Long dailyTagListId, Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(TagNotFoundException::new);
        DailyTagList dailyTagList = dailyTagListRepository.findDailyTagListWithDailyTags(dailyTagListId)
                .orElseThrow(DailyTagListNotFoundException::new);

        dailyTagList.deleteTag(tag);
        return dailyTagList.getId();
    }

}
