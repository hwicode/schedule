package hwicode.schedule.tag.application;

import hwicode.schedule.tag.application.find_service.TagFindService;
import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import hwicode.schedule.tag.infra.limited_repository.DailyTagConstraintRepository;
import hwicode.schedule.tag.infra.limited_repository.MemoTagConstraintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    private final DailyTagConstraintRepository dailyTagConstraintRepository;
    private final MemoTagConstraintRepository memoTagConstraintRepository;

    @Transactional
    public Long saveTag(String name) {
        validateTagName(name);
        Tag tag = new Tag(name, 1L);
        tagRepository.save(tag);
        return tag.getId();
    }

    @Transactional
    public String changeTagName(Long tagId, String newTagName) {
        validateTagName(newTagName);
        Tag tag = TagFindService.findById(tagRepository, tagId);

        tag.changeName(newTagName);
        return newTagName;
    }

    private void validateTagName(String name) {
        tagRepository.findByName(name)
                .ifPresent(tag -> {
                    throw new TagDuplicateException();
                });
    }

    @Transactional
    public Long deleteTag(Long tagId) {
        Tag tag = TagFindService.findById(tagRepository, tagId);
        deleteForeignKeyConstraint(tagId);
        tagRepository.delete(tag);
        return tagId;
    }

    private void deleteForeignKeyConstraint(Long tagId) {
        dailyTagConstraintRepository.deleteTagForeignKeyConstraint(tagId);
        memoTagConstraintRepository.deleteTagForeignKeyConstraint(tagId);
    }

}
