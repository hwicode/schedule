package hwicode.schedule.tag.presentation.tag;

import hwicode.schedule.common.config.auth.LoginInfo;
import hwicode.schedule.common.config.auth.LoginUser;
import hwicode.schedule.tag.application.TagService;
import hwicode.schedule.tag.application.dto.tag.TagDeleteCommand;
import hwicode.schedule.tag.application.dto.tag.TagModifyNameCommand;
import hwicode.schedule.tag.application.dto.tag.TagSaveCommand;
import hwicode.schedule.tag.presentation.tag.dto.name_modify.TagNameModifyRequest;
import hwicode.schedule.tag.presentation.tag.dto.name_modify.TagNameModifyResponse;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveRequest;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class TagController {

    private final TagService tagService;

    @PostMapping("/dailyschedule/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagSaveResponse saveTag(@LoginUser LoginInfo loginInfo,
                                   @RequestBody @Valid TagSaveRequest request) {
        TagSaveCommand command = new TagSaveCommand(loginInfo.getUserId(), request.getTagName());
        Long tagId = tagService.saveTag(command);
        return new TagSaveResponse(tagId, command.getName());
    }

    @PatchMapping("/dailyschedule/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public TagNameModifyResponse changeTagName(@LoginUser LoginInfo loginInfo,
                                               @PathVariable @Positive Long tagId,
                                               @RequestBody @Valid TagNameModifyRequest request) {
        TagModifyNameCommand command = new TagModifyNameCommand(
                loginInfo.getUserId(), tagId, request.getNewTagName()
        );
        tagService.changeTagName(command);
        return new TagNameModifyResponse(tagId, command.getNewName());
    }

    @DeleteMapping("/dailyschedule/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@LoginUser LoginInfo loginInfo,
                          @PathVariable @Positive Long tagId) {
        TagDeleteCommand command = new TagDeleteCommand(loginInfo.getUserId(), tagId);
        tagService.deleteTag(command);
    }

}
