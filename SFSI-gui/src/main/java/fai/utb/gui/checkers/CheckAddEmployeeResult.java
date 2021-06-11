package fai.utb.gui.checkers;

/**
 * Handles warning messages when adding employee for language mutations
 * @author Šimon Zouvala
 */
public enum CheckAddEmployeeResult {
    NAME_EMPTY(String.class),
    SURNAME_EMPTY(String.class),
    JOB_TIME_NOT_SELECT(String.class),
    EMAIL_INVALID(String.class),
    EMAIL_EMPTY(String.class),
    PHONE_EMPTY(String.class),
    PHONE_INVALID(String.class),
    EMPLOYEE_ADD(String.class),
    EMPLOYEE_ALREADY_EXIST(String.class);

    private final Class<?> type;

    private CheckAddEmployeeResult(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

}
