package hwicode.schedule.calendar.infra.other_boundedcontext;

import hwicode.schedule.dailyschedule.review.application.ReviewListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DailySchedulePostSaveService {

    private final ReviewListService reviewListService;

    public void perform(Long useId, Long dailyScheduleId) {
        reviewListService.addReviewTasks(useId, dailyScheduleId);
    }

}
