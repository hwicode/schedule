package hwicode.schedule.tag.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.tag.application.DailyTagListService;
import hwicode.schedule.tag.exception.application.DailyTagListNotFoundException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagDuplicateException;
import hwicode.schedule.tag.exception.domain.dailytaglist.DailyTagNotFoundException;
import hwicode.schedule.tag.presentation.dailytaglist.DailyTagListController;
import hwicode.schedule.tag.presentation.dailytaglist.dto.main_tag_modify.DailyTagListMainTagModifyResponse;
import hwicode.schedule.tag.presentation.dailytaglist.dto.tag_add.DailyTagListTagAddRequest;
import hwicode.schedule.tag.presentation.dailytaglist.dto.tag_add.DailyTagListTagAddResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.tag.TagDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyTagListController.class)
class DailyTagListControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyTagListService dailyTagListService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 오늘의_태그_리스트에_태그를_추가를_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        DailyTagListTagAddRequest dailyTagListTagAddRequest = new DailyTagListTagAddRequest(TAG_ID);
        DailyTagListTagAddResponse dailyTagListTagAddResponse = new DailyTagListTagAddResponse(DAILY_TAG_LIST_ID, TAG_ID);

        given(dailyTagListService.addTagToDailyTagList(any(), any()))
                .willReturn(DAILY_TAG_LIST_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags", DAILY_TAG_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dailyTagListTagAddRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(dailyTagListTagAddResponse)
                ));

        verify(dailyTagListService).addTagToDailyTagList(any(), any());
    }

    @Test
    void 오늘의_태그_리스트를_찾을_때_오늘의_태그_리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyTagListNotFoundException dailyTagListNotFoundException = new DailyTagListNotFoundException();
        given(dailyTagListService.addTagToDailyTagList(any(), any()))
                .willThrow(dailyTagListNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags", DAILY_TAG_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new DailyTagListTagAddRequest(TAG_ID)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyTagListNotFoundException.getMessage()));

        verify(dailyTagListService).addTagToDailyTagList(any(), any());
    }

    @Test
    void 오늘의_태그_리스트에_태그를_추가할_때_이미_존재하는_태그라면_에러가_발생한다() throws Exception {
        // given
        DailyTagDuplicateException dailyTagDuplicateException = new DailyTagDuplicateException();
        given(dailyTagListService.addTagToDailyTagList(any(), any()))
                .willThrow(dailyTagDuplicateException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags", DAILY_TAG_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new DailyTagListTagAddRequest(TAG_ID)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(dailyTagDuplicateException.getMessage()));

        verify(dailyTagListService).addTagToDailyTagList(any(), any());
    }

    @Test
    void 오늘의_태그_리스트에_태그의_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        given(dailyTagListService.deleteTagToDailyTagList(any(), any()))
                .willReturn(DAILY_TAG_LIST_ID);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags/{tagId}", DAILY_TAG_LIST_ID, TAG_ID)
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(dailyTagListService).deleteTagToDailyTagList(any(), any());
    }

    @Test
    void 오늘의_태그_리스트에_태그의_삭제할_때_태그가_존재하지_않는다면_에러가_발생한다() throws Exception {
        // given
        DailyTagNotFoundException dailyTagNotFoundException = new DailyTagNotFoundException();
        given(dailyTagListService.deleteTagToDailyTagList(any(), any()))
                .willThrow(dailyTagNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags/{tagId}", DAILY_TAG_LIST_ID, TAG_ID)
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyTagNotFoundException.getMessage()));

        verify(dailyTagListService).deleteTagToDailyTagList(any(), any());
    }

    @Test
    void 오늘의_태그_리스트에_메인_태그의_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        DailyTagListMainTagModifyResponse dailyTagListMainTagModifyResponse = new DailyTagListMainTagModifyResponse(DAILY_TAG_LIST_ID, TAG_ID, TAG_NAME);

        given(dailyTagListService.changeMainTag(any(), any()))
                .willReturn(TAG_NAME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-tag-lists/{dailyTagListId}/tags/{tagId}", DAILY_TAG_LIST_ID, TAG_ID)
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(dailyTagListMainTagModifyResponse)
                ));

        verify(dailyTagListService).changeMainTag(any(), any());
    }

}
