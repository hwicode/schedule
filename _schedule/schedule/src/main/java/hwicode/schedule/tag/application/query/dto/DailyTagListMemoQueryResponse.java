package hwicode.schedule.tag.application.query.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DailyTagListMemoQueryResponse {

    private final Long id;
    private final String text;
    private List<MemoTagQueryResponse> memoTagQueryResponses;

    public DailyTagListMemoQueryResponse(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public void setMemoTagQueryResponses(List<MemoTagQueryResponse> memoTagQueryResponses) {
        this.memoTagQueryResponses = memoTagQueryResponses;
    }
}
