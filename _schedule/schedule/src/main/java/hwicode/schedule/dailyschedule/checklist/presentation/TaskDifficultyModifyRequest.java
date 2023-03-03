package hwicode.schedule.dailyschedule.checklist.presentation;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;

public class TaskDifficultyModifyRequest {

    private Long dailyChecklistId;
    private Difficulty difficulty;

    public TaskDifficultyModifyRequest() {
    }

    public TaskDifficultyModifyRequest(Long dailyChecklistId, Difficulty difficulty) {
        this.dailyChecklistId = dailyChecklistId;
        this.difficulty = difficulty;
    }

    public Long getDailyChecklistId() {
        return dailyChecklistId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
