package hwicode.schedule.tag.presentation.dailytaglist;

import hwicode.schedule.tag.application.query.DailyTagListQueryService;
import hwicode.schedule.tag.application.query.dto.DailyTagQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DailyTagListQueryController {

    private final DailyTagListQueryService dailyTagListQueryService;

    @GetMapping("/dailyschedule/daily-tag-lists")
    public List<DailyTagQueryResponse> getDailyTagQueryResponses(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return dailyTagListQueryService.getDailyTagQueryResponses(date);
    }

}
