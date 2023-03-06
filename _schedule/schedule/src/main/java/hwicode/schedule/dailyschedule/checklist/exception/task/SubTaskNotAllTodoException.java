package hwicode.schedule.dailyschedule.checklist.exception.task;

public class SubTaskNotAllTodoException extends RuntimeException {

    public SubTaskNotAllTodoException() {
        super("서브 과제가 전부 TODO 상태가 아닙니다.");
    }
}
