package hwicode.schedule.tag.application;

import hwicode.schedule.common.login.validator.PermissionValidator;
import hwicode.schedule.tag.application.dto.memo.*;
import hwicode.schedule.tag.application.find_service.DailyTagListFindService;
import hwicode.schedule.tag.application.find_service.TagFindService;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.MemoTag;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.MemoNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.DailyTagListRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoRepository;
import hwicode.schedule.tag.infra.jpa_repository.MemoTagRepository;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final DailyTagListRepository dailyTagListRepository;
    private final TagRepository tagRepository;
    private final MemoTagRepository memoTagRepository;

    @Transactional
    public Long saveMemo(MemoSaveCommand command) {
        DailyTagList dailyTagList = DailyTagListFindService.findById(dailyTagListRepository, command.getDailyTagListId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyTagList.getUserId());

        Memo memo = dailyTagList.createMemo(command.getText());
        memoRepository.save(memo);
        return memo.getId();
    }

    @Transactional
    public String changeMemoText(MemoModifyTextCommand command) {
        Memo memo = findMemoBy(command.getMemoId());
        PermissionValidator.validateOwnership(command.getUserId(), memo.getUserId());

        memo.changeText(command.getText());
        return command.getText();
    }

    @Transactional
    public Long addTagsToMemo(MemoAddTagsCommand command) {
        Memo memo = findMemoWithMemoTagsBy(command.getMemoId());
        PermissionValidator.validateOwnership(command.getUserId(), memo.getUserId());

        List<Tag> tags = tagRepository.findAllById(command.getTagIds());
        tags.forEach(tag -> PermissionValidator.validateOwnership(
                command.getUserId(), tag.getUserId()
        ));

        List<MemoTag> memoTags = memo.addTags(tags);
        memoTagRepository.saveAll(memoTags);
        return memo.getId();
    }

    @Transactional
    public Long saveMemoWithTags(MemoSaveWithTagsCommand command) {
        DailyTagList dailyTagList = DailyTagListFindService.findById(dailyTagListRepository, command.getDailyTagListId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyTagList.getUserId());

        List<Tag> tags = tagRepository.findAllById(command.getTagIds());
        tags.forEach(tag -> PermissionValidator.validateOwnership(
                command.getUserId(), tag.getUserId()
        ));

        Memo memo = dailyTagList.createMemo(command.getText());
        memo.addTags(tags);
        memoRepository.save(memo);
        return memo.getId();
    }

    @Transactional
    public Long deleteTagToMemo(MemoDeleteTagCommand command) {
        Memo memo = findMemoWithMemoTagsBy(command.getMemoId());
        PermissionValidator.validateOwnership(command.getUserId(), memo.getUserId());

        Tag tag = TagFindService.findById(tagRepository, command.getTagId());
        PermissionValidator.validateOwnership(command.getUserId(), tag.getUserId());

        memo.deleteTag(tag);
        return memo.getId();
    }

    @Transactional
    public Long deleteMemo(MemoDeleteCommand command) {
        Memo memo = findMemoWithMemoTagsBy(command.getMemoId());
        PermissionValidator.validateOwnership(command.getUserId(), memo.getUserId());
        memoRepository.delete(memo);
        return memo.getId();
    }

    private Memo findMemoBy(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(MemoNotFoundException::new);
    }

    private Memo findMemoWithMemoTagsBy(Long memoId) {
        return memoRepository.findMemoWithMemoTags(memoId)
                .orElseThrow(MemoNotFoundException::new);
    }

}
