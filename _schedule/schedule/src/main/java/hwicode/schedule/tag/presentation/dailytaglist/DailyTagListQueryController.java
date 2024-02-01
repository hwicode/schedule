package hwicode.schedule.tag.presentation.dailytaglist;

import hwicode.schedule.tag.application.query.DailyTagListQueryService;
import hwicode.schedule.tag.application.query.dto.DailyTagListMemoQueryResponse;
import hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class DailyTagListQueryController {

    private final DailyTagListQueryService dailyTagListQueryService;

    @GetMapping("/dailyschedule/daily-tag-lists")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTagQueryResponse> getDailyTagQueryResponses(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return dailyTagListQueryService.getDailyTagQueryResponses(1L, date);
    }

    @GetMapping("/dailyschedule/daily-tag-lists/{dailyTagListId}/memos")
    @ResponseStatus(HttpStatus.OK)
    public List<DailyTagListMemoQueryResponse> getDailyTagListMemoQueryResponses(@PathVariable @Positive Long dailyTagListId) {
        return dailyTagListQueryService.getDailyTagListMemoQueryResponses(1L, dailyTagListId);
    }

}
