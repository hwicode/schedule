package hwicode.schedule.dailyschedule.review.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.review.application.ReviewCycleAggregateService;
import hwicode.schedule.dailyschedule.review.exception.application.review_task_service.ReviewCycleNotFoundException;
import hwicode.schedule.dailyschedule.review.exception.domain.review_cycle.InvalidReviewCycleDateException;
import hwicode.schedule.dailyschedule.review.exception.domain.review_cycle.ReviewCycleNullException;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.ReviewCycleController;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify.ReviewCycleCycleModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.cycle_modify.ReviewCycleCycleModifyResponse;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify.ReviewCycleNameModifyRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.name_modify.ReviewCycleNameModifyResponse;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveRequest;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.dto.save.ReviewCycleSaveResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.dailyschedule.review.ReviewDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewCycleController.class)
class ReviewCycleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewCycleAggregateService reviewCycleAggregateService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 복습_주기를_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        Set<Integer> cycle = Set.of(1);
        ReviewCycleSaveRequest reviewCycleSaveRequest = new ReviewCycleSaveRequest(REVIEW_CYCLE_NAME, cycle);
        ReviewCycleSaveResponse reviewCycleSaveResponse = new ReviewCycleSaveResponse(REVIEW_CYCLE_ID, REVIEW_CYCLE_NAME, new ArrayList<>(cycle));

        given(reviewCycleAggregateService.saveReviewCycle(any()))
                .willReturn(REVIEW_CYCLE_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/review-cycles")
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewCycleSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(reviewCycleSaveResponse)
                ));

        verify(reviewCycleAggregateService).saveReviewCycle(any());
    }

    @Test
    void 복습_주기를_생성을_요청할_때_복습_주기에_null값이_있으면_에러가_발생한다() throws Exception {
        // given
        ReviewCycleNullException reviewCycleNullException = new ReviewCycleNullException();
        given(reviewCycleAggregateService.saveReviewCycle(any()))
                .willThrow(reviewCycleNullException);

        Set<Integer> set = new HashSet<>();
        set.add(null);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/review-cycles")
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ReviewCycleSaveRequest(REVIEW_CYCLE_NAME, set)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(reviewCycleNullException.getMessage()));

        verify(reviewCycleAggregateService).saveReviewCycle(any());
    }

    @Test
    void 복습_주기를_생성을_요청할_때_복습_주기의_크기가_유효하지_않으면_에러가_발생한다() throws Exception {
        // given
        InvalidReviewCycleDateException invalidReviewCycleDateException = new InvalidReviewCycleDateException();
        given(reviewCycleAggregateService.saveReviewCycle(any()))
                .willThrow(invalidReviewCycleDateException);

        Set<Integer> overSizeReviewCycle = Set.of(1);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/review-cycles")
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ReviewCycleSaveRequest(REVIEW_CYCLE_NAME, overSizeReviewCycle)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(invalidReviewCycleDateException.getMessage()));

        verify(reviewCycleAggregateService).saveReviewCycle(any());
    }

    @Test
    void 복습_주기의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        ReviewCycleNameModifyRequest reviewCycleNameModifyRequest = new ReviewCycleNameModifyRequest(NEW_REVIEW_CYCLE_NAME);
        ReviewCycleNameModifyResponse reviewCycleNameModifyResponse = new ReviewCycleNameModifyResponse(REVIEW_CYCLE_ID, NEW_REVIEW_CYCLE_NAME);

        given(reviewCycleAggregateService.changeReviewCycleName(any()))
                .willReturn(NEW_REVIEW_CYCLE_NAME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/review-cycles/{reviewCycleId}/name",
                        REVIEW_CYCLE_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewCycleNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(reviewCycleNameModifyResponse)
                ));

        verify(reviewCycleAggregateService).changeReviewCycleName(any());
    }

    @Test
    void 복습_주기의_주기_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        Set<Integer> cycle = Set.of(1);
        ReviewCycleCycleModifyRequest reviewCycleCycleModifyRequest = new ReviewCycleCycleModifyRequest(cycle);
        ReviewCycleCycleModifyResponse reviewCycleCycleModifyResponse = new ReviewCycleCycleModifyResponse(REVIEW_CYCLE_ID, new ArrayList<>(cycle));

        given(reviewCycleAggregateService.changeCycle(any()))
                .willReturn(new ArrayList<>(cycle));

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/review-cycles/{reviewCycleId}/cycle", REVIEW_CYCLE_ID)
                .header("Authorization", BEARER + "accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCycleCycleModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(reviewCycleCycleModifyResponse)
                ));

        verify(reviewCycleAggregateService).changeCycle(any());
    }

    @Test
    void 복습_주기의_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        given(reviewCycleAggregateService.deleteReviewCycle(any()))
                .willReturn(REVIEW_CYCLE_ID);

        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/review-cycles/{reviewCycleId}",
                REVIEW_CYCLE_ID)
                .header("Authorization", BEARER + "accessToken"));

        // then
        perform.andExpect(status().isNoContent());

        verify(reviewCycleAggregateService).deleteReviewCycle(any());
    }

    @Test
    void 복습_주기의_삭제을_요청할_때_복습_주기가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        ReviewCycleNotFoundException reviewCycleNotFoundException = new ReviewCycleNotFoundException();

        given(reviewCycleAggregateService.deleteReviewCycle(any()))
                .willThrow(reviewCycleNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/review-cycles/{reviewCycleId}",
                REVIEW_CYCLE_ID)
                .header("Authorization", BEARER + "accessToken"));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(reviewCycleNotFoundException.getMessage()));

        verify(reviewCycleAggregateService).deleteReviewCycle(any());
    }

}
