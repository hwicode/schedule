package hwicode.schedule.calendar.domain;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Goal {

    private String name;
    private GoalStatus goalStatus;
    private final List<CalendarGoal> calendarGoals = new ArrayList<>();
    private final List<SubGoal> subGoals = new ArrayList<>();

    public Goal(String name) {
        this.name = name;
        this.goalStatus = GoalStatus.TODO;
    }

    // 테스트 코드에서만 사용되는 생성자!
    Goal(List<Calendar> calendars) {
        for (Calendar calendar : calendars) {
            calendarGoals.add(new CalendarGoal(calendar, this));
        }
    }

    public String changeSubGoalName(String subGoalName, String newSubGoalName) {
        validateSubGoal(newSubGoalName);
        return findSubGoalBy(subGoalName).changeName(newSubGoalName);
    }

    public SubGoal createSubGoal(String name) {
        validateSubGoal(name);
        SubGoal subGoal = new SubGoal(name);
        subGoals.add(subGoal);

        if (this.goalStatus == GoalStatus.DONE) {
            changeToProgress();
        }
        return subGoal;
    }

    private void validateSubGoal(String name) {
        boolean duplication = subGoals.stream()
                .anyMatch(subGoal -> subGoal.isSame(name));

        if (duplication) {
            throw new IllegalArgumentException();
        }
    }

    public GoalStatus deleteSubGoal(String name) {
        subGoals.remove(findSubGoalBy(name));
        return this.goalStatus;
    }

    public GoalStatus changeToTodo() {
        boolean isAllToDo = isAllSameStatus(SubGoalStatus.TODO);
        if (!isAllToDo) {
            throw new IllegalArgumentException();
        }

        this.goalStatus = GoalStatus.TODO;
        return getGoalStatus();
    }

    public GoalStatus changeToProgress() {
        this.goalStatus = GoalStatus.PROGRESS;
        return getGoalStatus();
    }

    public GoalStatus changeToDone() {
        boolean isAllDone = isAllSameStatus(SubGoalStatus.DONE);
        if (!isAllDone) {
            throw new IllegalArgumentException();
        }

        this.goalStatus = GoalStatus.DONE;
        return getGoalStatus();
    }

    private boolean isAllSameStatus(SubGoalStatus subGoalStatus) {
        int count = (int) subGoals.stream()
                .filter(s -> s.isSameStatus(subGoalStatus))
                .count();

        return count == subGoals.size();
    }

    public GoalStatus changeSubGoalStatus(String subGoalName, SubGoalStatus subGoalStatus) {
        findSubGoalBy(subGoalName).changeStatus(subGoalStatus);
        checkGoalStatusConditions(subGoalStatus);

        return getGoalStatus();
    }

    private SubGoal findSubGoalBy(String name) {
        return subGoals.stream()
                .filter(subGoal -> subGoal.isSame(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void checkGoalStatusConditions(SubGoalStatus subGoalStatus) {
        if (this.goalStatus == GoalStatus.TODO && subGoalStatus != SubGoalStatus.TODO) {
            changeToProgress();
        }

        if (this.goalStatus == GoalStatus.DONE && subGoalStatus != SubGoalStatus.DONE) {
            changeToProgress();
        }
    }

    public void deleteCalendarGoal(YearMonth yearMonth) {
        calendarGoals.remove(findCalendarGoalBy(yearMonth));
    }

    private CalendarGoal findCalendarGoalBy(YearMonth yearMonth) {
        return calendarGoals.stream()
                .filter(calendarGoal -> calendarGoal.isSameCalendar(yearMonth))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isDelete() {
        return calendarGoals.isEmpty();
    }

    String changeName(String name) {
        this.name = name;
        return name;
    }

    boolean isSame(String name) {
        return this.name.equals(name);
    }

    GoalStatus getGoalStatus() {
        return this.goalStatus;
    }

    String getName() {
        return this.name;
    }

}