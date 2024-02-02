package hwicode.schedule.tag.presentation;

import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.tag.application.query.DailyTagListQueryService;
import hwicode.schedule.tag.presentation.dailytaglist.DailyTagListQueryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.tag.TagDataHelper.DAILY_TAG_LIST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyTagListQueryController.class)
class DailyTagListQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyTagListQueryService dailyTagListQueryService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 날짜로_계획표의_태그들의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        LocalDate date = LocalDate.of(2023, 8, 23);

        given(dailyTagListQueryService.getDailyTagQueryResponses(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/dailyschedule/daily-tag-lists")
                .queryParam("date", String.valueOf(date))
                .header("Authorization", BEARER + "accessToken"));

        // then
        perform.andExpect(status().isOk());

        verify(dailyTagListQueryService).getDailyTagQueryResponses(any(), any());
    }

    @Test
    void 계획표에_존재하는_메모들의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(dailyTagListQueryService.getDailyTagListMemoQueryResponses(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(
                get("/dailyschedule/daily-tag-lists/{dailyTagListId}/memos", DAILY_TAG_LIST_ID)
                        .header("Authorization", BEARER + "accessToken")
        );

        // then
        perform.andExpect(status().isOk());

        verify(dailyTagListQueryService).getDailyTagListMemoQueryResponses(any(), any());
    }

}
