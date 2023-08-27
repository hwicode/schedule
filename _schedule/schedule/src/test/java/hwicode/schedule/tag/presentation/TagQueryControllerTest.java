package hwicode.schedule.tag.presentation;

import hwicode.schedule.tag.application.query.TagQueryService;
import hwicode.schedule.tag.presentation.tag.TagQueryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagQueryController.class)
class TagQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TagQueryService tagQueryService;

    @Test
    void lastDailyTagListId_없이_태그를_통해_계획표의_검색을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(tagQueryService.getDailyTagListSearchQueryResponsePage(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/daily-tag-lists")
                .queryParam("tagId", String.valueOf(TAG_ID)));

        // then
        perform.andExpect(status().isOk());

        verify(tagQueryService).getDailyTagListSearchQueryResponsePage(any(), any());
    }

    @Test
    void 태그를_통해_계획표의_검색을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(tagQueryService.getDailyTagListSearchQueryResponsePage(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/daily-tag-lists")
                .queryParam("tagId", String.valueOf(TAG_ID))
                .queryParam("lastDailyTagListId", String.valueOf(DAILY_TAG_LIST_ID)));

        // then
        perform.andExpect(status().isOk());

        verify(tagQueryService).getDailyTagListSearchQueryResponsePage(any(), any());
    }

    @Test
    void lastMemoId_없이_태그를_통해_메모의_검색을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(tagQueryService.getMemoSearchQueryResponsePage(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/memos")
                .queryParam("tagId", String.valueOf(TAG_ID)));

        // then
        perform.andExpect(status().isOk());

        verify(tagQueryService).getMemoSearchQueryResponsePage(any(), any());
    }

    @Test
    void 태그를_통해_메모의_검색을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(tagQueryService.getMemoSearchQueryResponsePage(any(), any()))
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/memos")
                .queryParam("tagId", String.valueOf(TAG_ID))
                .queryParam("lastMemoId", String.valueOf(MEMO_ID)));

        // then
        perform.andExpect(status().isOk());

        verify(tagQueryService).getMemoSearchQueryResponsePage(any(), any());
    }

    @Test
    void 모든_태그의_조회를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        given(tagQueryService.getTagQueryResponses())
                .willReturn(List.of());

        // when
        ResultActions perform = mockMvc.perform(get("/tags"));

        // then
        perform.andExpect(status().isOk());

        verify(tagQueryService).getTagQueryResponses();
    }

}
