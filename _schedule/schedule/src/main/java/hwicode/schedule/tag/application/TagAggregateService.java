package hwicode.schedule.tag.application;

import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
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

    private void validateTagName(String name) {
        tagRepository.findByName(name)
                .ifPresent(tag -> {
                    throw new TagDuplicateException();
                });
    }

}
