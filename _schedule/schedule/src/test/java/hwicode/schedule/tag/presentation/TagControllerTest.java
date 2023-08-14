package hwicode.schedule.tag.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.tag.application.TagService;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.exception.application.TagNotFoundException;
import hwicode.schedule.tag.presentation.tag.TagController;
import hwicode.schedule.tag.presentation.tag.dto.name_modify.TagNameModifyRequest;
import hwicode.schedule.tag.presentation.tag.dto.name_modify.TagNameModifyResponse;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveRequest;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveResponse;
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

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TagService tagService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 태그_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TagSaveRequest tagSaveRequest = new TagSaveRequest(TAG_NAME);
        TagSaveResponse tagSaveResponse = new TagSaveResponse(TAG_ID, TAG_NAME);

        given(tagService.saveTag(any()))
                .willReturn(TAG_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(tagSaveResponse)
                ));

        verify(tagService).saveTag(any());
    }

    @Test
    void 태그의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TagNameModifyRequest tagNameModifyRequest = new TagNameModifyRequest(NEW_TAG_NAME);
        TagNameModifyResponse tagNameModifyResponse = new TagNameModifyResponse(TAG_ID, NEW_TAG_NAME);

        given(tagService.changeTagName(any(), any()))
                .willReturn(NEW_TAG_NAME);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/tags/{tagId}", TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(tagNameModifyResponse)
                ));

        verify(tagService).changeTagName(any(), any());
    }

    @Test
    void 태그_생성_또는_이름_변경을_요청할_때_태그_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        TagDuplicateException tagDuplicateException = new TagDuplicateException();
        given(tagService.saveTag(any()))
                .willThrow(tagDuplicateException);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TagSaveRequest(TAG_NAME)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(tagDuplicateException.getMessage()));

        verify(tagService).saveTag(any());
    }

    @Test
    void 태그_삭제를_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        given(tagService.deleteTag(any()))
                .willReturn(TAG_ID);

        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/tags/{tagId}", TAG_ID));

        // then
        perform.andExpect(status().isNoContent());

        verify(tagService).deleteTag(any());
    }

    @Test
    void 태그를_찾을_때_태그가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TagNotFoundException tagNotFoundException = new TagNotFoundException();
        given(tagService.deleteTag(any()))
                .willThrow(tagNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/tags/{tagId}", TAG_ID));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(tagNotFoundException.getMessage()));

        verify(tagService).deleteTag(any());
    }

}
