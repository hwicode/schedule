package hwicode.schedule.dailyschedule.review.presentation;

import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.review.application.query.ReviewCycleQueryService;
import hwicode.schedule.dailyschedule.review.presentation.reviewcycle.ReviewCycleQueryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 모든_복습_주기의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(reviewCycleQueryService.getReviewCycleQueryResponses(any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/review-cycles")
                .header("Authorization", BEARER + "accessToken")
        );

        // then
        perform.andExpect(status().isOk());

        verify(reviewCycleQueryService).getReviewCycleQueryResponses(any());
    }

}
