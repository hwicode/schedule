package hwicode.schedule.tag.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.tag.application.MemoService;
import hwicode.schedule.tag.exception.application.MemoNotFoundException;
import hwicode.schedule.tag.presentation.memo.MemoController;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveRequest;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveResponse;
import hwicode.schedule.tag.presentation.memo.dto.save_with_tags.MemoSaveWithTagsRequest;
import hwicode.schedule.tag.presentation.memo.dto.save_with_tags.MemoSaveWithTagsResponse;
import hwicode.schedule.tag.presentation.memo.dto.tags_add.MemoTagsAddRequest;
import hwicode.schedule.tag.presentation.memo.dto.tags_add.MemoTagsAddResponse;
import hwicode.schedule.tag.presentation.memo.dto.text_modify.MemoTextModifyRequest;
import hwicode.schedule.tag.presentation.memo.dto.text_modify.MemoTextModifyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemoController.class)
class MemoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemoService memoService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 메모의_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        MemoSaveRequest memoSaveRequest = new MemoSaveRequest(DAILY_TAG_LIST_ID, MEMO_TEXT);
        MemoSaveResponse memoSaveResponse = new MemoSaveResponse(DAILY_TAG_LIST_ID, MEMO_ID, MEMO_TEXT);

        given(memoService.saveMemo(any(), any()))
                .willReturn(MEMO_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/memos", DAILY_TAG_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memoSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(memoSaveResponse)
                ));

        verify(memoService).saveMemo(any(), any());
    }

    @Test
    void 메모의_내용_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        MemoTextModifyRequest memoTextModifyRequest = new MemoTextModifyRequest(NEW_MEMO_TEXT);
        MemoTextModifyResponse memoTextModifyResponse = new MemoTextModifyResponse(MEMO_ID, NEW_MEMO_TEXT);

        given(memoService.changeMemoText(any(), any()))
                .willReturn(NEW_MEMO_TEXT);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/memos/{memoId}", MEMO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memoTextModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(memoTextModifyResponse)
                ));

        verify(memoService).changeMemoText(any(), any());
    }

    @Test
    void 메모를_찾을_때_메모가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        MemoNotFoundException memoNotFoundException = new MemoNotFoundException();
        given(memoService.changeMemoText(any(), any()))
                .willThrow(memoNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/memos/{memoId}", MEMO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new MemoTextModifyRequest(NEW_MEMO_TEXT)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(memoNotFoundException.getMessage()));

        verify(memoService).changeMemoText(any(), any());
    }

    @Test
    void 메모에_태그_여러_개_추가를_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        MemoTagsAddRequest memoTagsAddRequest = new MemoTagsAddRequest(Set.of(TAG_ID));
        MemoTagsAddResponse memoTagsAddResponse = new MemoTagsAddResponse(MEMO_ID, List.of(TAG_ID));

        given(memoService.addTagsToMemo(any(), any()))
                .willReturn(MEMO_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/memos/{memoId}/tags", MEMO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memoTagsAddRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(memoTagsAddResponse)
                ));

        verify(memoService).addTagsToMemo(any(), any());
    }

    @Test
    void 메모에_여러_개의_태그를_추가하여_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        MemoSaveWithTagsRequest memoSaveWithTagsRequest = new MemoSaveWithTagsRequest(DAILY_TAG_LIST_ID, MEMO_TEXT, Set.of(TAG_ID));
        MemoSaveWithTagsResponse memoSaveWithTagsResponse = new MemoSaveWithTagsResponse(DAILY_TAG_LIST_ID, MEMO_ID, MEMO_TEXT, List.of(TAG_ID));

        given(memoService.saveMemoWithTags(any(), any(), any()))
                .willReturn(MEMO_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/memos/tags", DAILY_TAG_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memoSaveWithTagsRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(memoSaveWithTagsResponse)
                ));

        verify(memoService).saveMemoWithTags(any(), any(), any());
    }

    @Test
    void 메모에_태그의_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        given(memoService.deleteTagToMemo(any(), any()))
                .willReturn(MEMO_ID);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/memos/{memoId}/tags/{tagId}", MEMO_ID, TAG_ID)
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(memoService).deleteTagToMemo(any(), any());
    }

    @Test
    void 메모의_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        given(memoService.deleteMemo(any()))
                .willReturn(MEMO_ID);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/memos/{memoId}", MEMO_ID)
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(memoService).deleteMemo(any());
    }

}
