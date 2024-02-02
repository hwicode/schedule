package hwicode.schedule.common.login.validator;

public class PermissionValidator {

    private PermissionValidator(){}

    public static void validateOwnership(Long requestUserId, Long userId) {
        if (!userId.equals(requestUserId)) {
            throw new OwnerForbiddenException();
        }
    }
}
