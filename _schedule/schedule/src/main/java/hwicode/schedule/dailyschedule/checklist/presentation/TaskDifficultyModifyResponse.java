package hwicode.schedule.dailyschedule.checklist.presentation;

import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;

public class TaskDifficultyModifyResponse {

    private String taskName;
    private Difficulty modifiedDifficulty;

    public TaskDifficultyModifyResponse() {}

    public TaskDifficultyModifyResponse(String taskName, Difficulty modifiedDifficulty) {
        this.taskName = taskName;
        this.modifiedDifficulty = modifiedDifficulty;
    }

    public String getTaskName() {
        return taskName;
    }

    public Difficulty getModifiedDifficulty() {
        return modifiedDifficulty;
    }
}
