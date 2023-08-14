package hwicode.schedule.tag.application.find_service;

import hwicode.schedule.tag.domain.Tag;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
import hwicode.schedule.tag.infra.jpa_repository.TagRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagFindService {

    public static Tag findById(TagRepository tagRepository, Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(TagNotFoundException::new);
    }
}
