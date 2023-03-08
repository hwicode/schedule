package hwicode.schedule.dailyschedule.checklist.exception;

public abstract class ChecklistBusinessException extends RuntimeException {

    protected ChecklistBusinessException(String message) {
        super(message);
    }
}
