package hwicode.schedule.dailyschedule.review.application.dto.review_cycle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReviewCycleSaveCommand {

    private final Long userId;
    private final String name;
    private final List<Integer> cycle;
}
