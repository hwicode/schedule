package hwicode.schedule.dailyschedule.review.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.review.application.ReviewCycleAggregateService;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.ReviewCycleController;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_ID;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.REVIEW_CYCLE_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewCycleController.class)
class ReviewCycleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewCycleAggregateService reviewCycleAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 복습_주기를_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        List<Integer> cycle = List.of(1);
        ReviewCycleSaveRequest reviewCycleSaveRequest = new ReviewCycleSaveRequest(REVIEW_CYCLE_NAME, cycle);
        ReviewCycleSaveResponse reviewCycleSaveResponse = new ReviewCycleSaveResponse(REVIEW_CYCLE_ID, REVIEW_CYCLE_NAME, cycle);

        given(reviewCycleAggregateService.saveReviewCycle(any(), any()))
                .willReturn(REVIEW_CYCLE_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/review-cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCycleSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(reviewCycleSaveResponse)
                ));

        verify(reviewCycleAggregateService).saveReviewCycle(any(), any());
    }

}
