package hwicode.schedule.dailyschedule.review.presentation;

import hwicode.schedule.dailyschedule.review.application.query.ReviewCycleQueryService;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.ReviewCycleQueryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewCycleQueryController.class)
class ReviewCycleQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewCycleQueryService reviewCycleQueryService;

    @Test
    void 모든_복습_주기의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(reviewCycleQueryService.getReviewCycleQueryResponses())
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/review-cycles"));

        // then
        perform.andExpect(status().isOk());

        verify(reviewCycleQueryService).getReviewCycleQueryResponses();
    }

}
