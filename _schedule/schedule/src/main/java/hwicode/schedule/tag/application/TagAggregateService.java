package hwicode.schedule.tag.application;

import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
import hwicode.schedule.tag.infra.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TagAggregateService {

    private final TagRepository tagRepository;

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
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(TagNotFoundException::new);

        tag.changeName(newTagName);
        return newTagName;
    }

    private void validateTagName(String name) {
        tagRepository.findByName(name)
                .ifPresent(tag -> {
                    throw new TagDuplicateException();
                });
    }

}
