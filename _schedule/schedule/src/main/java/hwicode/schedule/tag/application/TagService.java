package hwicode.schedule.tag.application;

import hwicode.schedule.tag.application.dto.tag.TagDeleteCommand;
import hwicode.schedule.tag.application.dto.tag.TagModifyNameCommand;
import hwicode.schedule.tag.application.dto.tag.TagSaveCommand;
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
    public Long saveTag(TagSaveCommand command) {
        validateTagName(command.getName());
        Tag tag = new Tag(command.getName(), command.getUserId());
        tagRepository.save(tag);
        return tag.getId();
    }

    @Transactional
    public String changeTagName(TagModifyNameCommand command) {
        validateTagName(command.getNewName());

        Tag tag = TagFindService.findById(tagRepository, command.getTagId());
        tag.checkOwnership(command.getUserId());

        tag.changeName(command.getNewName());
        return command.getNewName();
    }

    private void validateTagName(String name) {
        tagRepository.findByName(name)
                .ifPresent(tag -> {
                    throw new TagDuplicateException();
                });
    }

    @Transactional
    public Long deleteTag(TagDeleteCommand command) {
        Tag tag = TagFindService.findById(tagRepository, command.getTagId());
        tag.checkOwnership(command.getUserId());

        deleteForeignKeyConstraint(command.getTagId());
        tagRepository.delete(tag);
        return command.getTagId();
    }

    private void deleteForeignKeyConstraint(Long tagId) {
        dailyTagConstraintRepository.deleteTagForeignKeyConstraint(tagId);
        memoTagConstraintRepository.deleteTagForeignKeyConstraint(tagId);
    }

}
