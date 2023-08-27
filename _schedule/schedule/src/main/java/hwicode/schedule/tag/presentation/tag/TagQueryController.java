package hwicode.schedule.tag.presentation.tag;

import hwicode.schedule.tag.application.query.TagQueryService;
import hwicode.schedule.tag.application.query.dto.DailyTagListSearchQueryResponse;
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

    @GetMapping("/daily-tag-lists")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTagListSearchQueryResponse> getDailyTagListSearchQueryResponsePage(@RequestParam @Positive Long tagId,
                                                                                        @RequestParam(required = false) Long lastDailyTagListId) {
        return tagQueryService.getDailyTagListSearchQueryResponsePage(tagId, lastDailyTagListId);
    }

}
