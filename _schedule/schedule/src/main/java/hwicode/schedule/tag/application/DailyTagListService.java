package hwicode.schedule.tag.application;

import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListDeleteTagCommand;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListModifyMainTagCommand;
import hwicode.schedule.tag.application.dto.daily_tag_list.DailyTagListSaveTagCommand;
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
    public Long addTagToDailyTagList(DailyTagListSaveTagCommand command) {
        Tag tag = TagFindService.findById(tagRepository, command.getTagId());
        tag.checkOwnership(command.getUserId());

        DailyTagList dailyTagList = DailyTagListFindService.findDailyTagListWithDailyTags(dailyTagListRepository, command.getDailyTagListId());
        dailyTagList.checkOwnership(command.getUserId());

        DailyTag dailyTag = dailyTagList.addTag(tag);
        dailyTagRepository.save(dailyTag);
        return dailyTag.getId();
    }

    @Transactional
    public Long deleteTagToDailyTagList(DailyTagListDeleteTagCommand command) {
        Tag tag = TagFindService.findById(tagRepository, command.getTagId());
        tag.checkOwnership(command.getUserId());

        DailyTagList dailyTagList = DailyTagListFindService.findDailyTagListWithDailyTags(dailyTagListRepository, command.getDailyTagListId());
        dailyTagList.checkOwnership(command.getUserId());

        dailyTagList.deleteTag(tag);
        return dailyTagList.getId();
    }

    @Transactional
    public String changeMainTag(DailyTagListModifyMainTagCommand command) {
        Tag tag = TagFindService.findById(tagRepository, command.getTagId());
        tag.checkOwnership(command.getUserId());

        DailyTagList dailyTagList = DailyTagListFindService.findDailyTagListWithDailyTags(dailyTagListRepository, command.getDailyTagListId());
        dailyTagList.checkOwnership(command.getUserId());

        return dailyTagList.changeMainTag(tag);
    }

}
