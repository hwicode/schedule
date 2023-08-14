package hwicode.schedule.tag.presentation.tag;

import hwicode.schedule.tag.application.TagService;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveRequest;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}
