package hwicode.schedule.tag.application;

import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
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
        Tag tag = new Tag(name);
        tagRepository.save(tag);
        return tag.getId();
    }

    @Transactional
    public String changeTagName(Long tagId, String newTagName) {
        validateTagName(newTagName);
        Tag tag = findTagBy(tagId);

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
    public void deleteTag(Long tagId) {
        Tag tag = findTagBy(tagId);
        deleteForeignKeyConstraint(tagId);
        tagRepository.delete(tag);
    }

    private Tag findTagBy(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(TagNotFoundException::new);
    }

    private void deleteForeignKeyConstraint(Long tagId) {
        dailyTagConstraintRepository.deleteTagForeignKeyConstraint(tagId);
        memoTagConstraintRepository.deleteTagForeignKeyConstraint(tagId);
    }

}