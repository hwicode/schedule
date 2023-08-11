package hwicode.schedule.dailyschedule.review.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.review.application.ReviewCycleAggregateService;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.ReviewCycleController;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify.ReviewCycleCycleModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify.ReviewCycleCycleModifyResponse;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify.ReviewCycleNameModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify.ReviewCycleNameModifyResponse;
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

import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    void 복습_주기의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception{
        // given
        ReviewCycleNameModifyRequest reviewCycleNameModifyRequest = new ReviewCycleNameModifyRequest(NEW_REVIEW_CYCLE_NAME);
        ReviewCycleNameModifyResponse reviewCycleNameModifyResponse = new ReviewCycleNameModifyResponse(REVIEW_CYCLE_ID, NEW_REVIEW_CYCLE_NAME);

        given(reviewCycleAggregateService.changeReviewCycleName(any(), any()))
                .willReturn(NEW_REVIEW_CYCLE_NAME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/review-cycles/{reviewCycleId}/name",
                        REVIEW_CYCLE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCycleNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(reviewCycleNameModifyResponse)
                ));

        verify(reviewCycleAggregateService).changeReviewCycleName(any(), any());
    }

    @Test
    void 복습_주기의_주기_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        List<Integer> cycle = List.of(1);
        ReviewCycleCycleModifyRequest reviewCycleCycleModifyRequest = new ReviewCycleCycleModifyRequest(cycle);
        ReviewCycleCycleModifyResponse reviewCycleCycleModifyResponse = new ReviewCycleCycleModifyResponse(REVIEW_CYCLE_ID, cycle);

        given(reviewCycleAggregateService.changeCycle(any(), any()))
                .willReturn(cycle);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/review-cycles/{reviewCycleId}/cycle", REVIEW_CYCLE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCycleCycleModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(reviewCycleCycleModifyResponse)
                ));

        verify(reviewCycleAggregateService).changeCycle(any(), any());
    }

}
