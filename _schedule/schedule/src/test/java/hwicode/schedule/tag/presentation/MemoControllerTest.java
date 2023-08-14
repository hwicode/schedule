package hwicode.schedule.tag.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.tag.application.MemoService;
import hwicode.schedule.tag.presentation.memo.MemoController;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveRequest;
import hwicode.schedule.tag.presentation.memo.dto.save.MemoSaveResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        MemoSaveRequest memoSaveRequest = new MemoSaveRequest(MEMO_TEXT);
        MemoSaveResponse memoSaveResponse = new MemoSaveResponse(DAILY_TAG_LIST_ID, MEMO_ID, MEMO_TEXT);

        given(memoService.saveMemo(any(), any()))
                .willReturn(MEMO_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-tag-lists/{dailyTagListId}/memos", DAILY_TAG_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memoSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(memoSaveResponse)
                ));

        verify(memoService).saveMemo(any(), any());
    }

}
