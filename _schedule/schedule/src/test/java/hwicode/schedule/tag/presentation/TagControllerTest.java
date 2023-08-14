package hwicode.schedule.tag.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.tag.application.TagService;
import hwicode.schedule.tag.exception.application.TagDuplicateException;
import hwicode.schedule.tag.presentation.tag.TagController;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveRequest;
import hwicode.schedule.tag.presentation.tag.dto.save.TagSaveResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.tag.TagDataHelper.TAG_ID;
import static hwicode.schedule.tag.TagDataHelper.TAG_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        ResultActions resultActions = mockMvc.perform(post("/dailyschedule/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagSaveRequest)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(tagSaveResponse)
                ));

        verify(tagService).saveTag(any());
    }

    @Test
    void 태그_생성을_요청할_때_태그_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        TagDuplicateException tagDuplicateException = new TagDuplicateException();
        given(tagService.saveTag(any()))
                .willThrow(tagDuplicateException);

        // when
        ResultActions resultActions = mockMvc.perform(post("/dailyschedule/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TagSaveRequest(TAG_NAME)
                )));

        // then
        resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(tagDuplicateException.getMessage()));

        verify(tagService).saveTag(any());
    }

}
