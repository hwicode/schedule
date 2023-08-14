package hwicode.schedule.tag.application;

import hwicode.schedule.tag.application.find_service.TagFindService;
import hwicode.schedule.tag.domain.DailyTagList;
import hwicode.schedule.tag.domain.Memo;
import hwicode.schedule.tag.domain.MemoTag;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.DailyTagListNotFoundException;
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
    public Long saveMemo(Long dailyTagListId, String text) {
        DailyTagList dailyTagList = dailyTagListRepository.findById(dailyTagListId)
                .orElseThrow(DailyTagListNotFoundException::new);
        Memo memo = new Memo(text, dailyTagList);
        memoRepository.save(memo);
        return memo.getId();
    }

    @Transactional
    public String changeMemoText(Long memoId, String text) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(MemoNotFoundException::new);
        memo.changeText(text);
        return text;
    }

    @Transactional
    public Long addTagsToMemo(Long memoId, List<Long> tagIds) {
        Memo memo = memoRepository.findMemoWithMemoTags(memoId)
                .orElseThrow(MemoNotFoundException::new);
        List<Tag> tags = tagRepository.findAllById(tagIds);

        List<MemoTag> memoTags = memo.addTags(tags);
        memoTagRepository.saveAll(memoTags);
        return memo.getId();
    }

    @Transactional
    public Long saveMemoWithTags(Long dailyTagListId, String text, List<Long> tagIds) {
        DailyTagList dailyTagList = dailyTagListRepository.findById(dailyTagListId)
                .orElseThrow(DailyTagListNotFoundException::new);
        List<Tag> tags = tagRepository.findAllById(tagIds);

        Memo memo = new Memo(text, dailyTagList);
        memo.addTags(tags);
        memoRepository.save(memo);
        return memo.getId();
    }

    @Transactional
    public Long deleteTagToMemo(Long memoId, Long tagId) {
        Memo memo = memoRepository.findMemoWithMemoTags(memoId)
                .orElseThrow(MemoNotFoundException::new);
        Tag tag = TagFindService.findById(tagRepository, tagId);

        memo.deleteTag(tag);
        return memoId;
    }

    @Transactional
    public Long deleteMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(MemoNotFoundException::new);
        memoRepository.delete(memo);
        return memo.getId();
    }

}
