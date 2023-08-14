package hwicode.schedule.tag.presentation.tag;

import hwicode.schedule.tag.application.TagService;
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
    public TagSaveResponse saveTag(@RequestBody @Valid TagSaveRequest tagSaveRequest) {
        String tagName = tagSaveRequest.getTagName();
        Long tagId = tagService.saveTag(tagName);
        return new TagSaveResponse(tagId, tagName);
    }

    @PatchMapping("/dailyschedule/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public TagNameModifyResponse changeTagName(@PathVariable @Positive Long tagId,
                                               @RequestBody @Valid TagNameModifyRequest tagNameModifyRequest) {
        String newTagName = tagNameModifyRequest.getNewTagName();
        tagService.changeTagName(tagId, newTagName);
        return new TagNameModifyResponse(tagId, newTagName);
    }

}
