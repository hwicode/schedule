package hwicode.schedule.tag.presentation.tag;

import hwicode.schedule.tag.application.query.TagQueryService;
import hwicode.schedule.tag.application.query.dto.DailyTagListSearchQueryResponse;
import hwicode.schedule.tag.application.query.dto.MemoSearchQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagQueryResponse;
import hwicode.schedule.tag.application.query.dto.TagSearchQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class TagQueryController {

    private final TagQueryService tagQueryService;

    @GetMapping("/search/daily-tag-lists")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTagListSearchQueryResponse> getDailyTagListSearchQueryResponsePage(@RequestParam @Positive Long tagId,
                                                                                        @RequestParam(required = false) Long lastDailyTagListId) {
        return tagQueryService.getDailyTagListSearchQueryResponsePage(tagId, lastDailyTagListId);
    }

    @GetMapping("/search/memos")
    @ResponseStatus(HttpStatus.OK)
    public List<MemoSearchQueryResponse> getMemoSearchQueryResponsePage(@RequestParam @Positive Long tagId,
                                                                        @RequestParam(required = false) Long lastMemoId) {
        return tagQueryService.getMemoSearchQueryResponsePage(tagId, lastMemoId);
    }

    @GetMapping("/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<TagQueryResponse> getTagQueryResponses() {
        return tagQueryService.getTagQueryResponses();
    }

    @GetMapping("/search/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<TagSearchQueryResponse> getTagSearchQueryResponses(@RequestParam String nameKeyword) {
        return tagQueryService.getTagSearchQueryResponses(nameKeyword);
    }

}
