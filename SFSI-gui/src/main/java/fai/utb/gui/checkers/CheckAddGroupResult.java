package fai.utb.gui.checkers;

/**
 * @author Šimon Zouvala
 */
public enum CheckAddGroupResult {
    QUANTITY_EMPTY(String.class),
    FIELD_OF_STUDY_EMPTY(String.class),
    SEMESTER_NOT_SELECT(String.class),
    DEGREE_NOT_SELECT(String.class),
    LANGUAGE_NOT_SELECT(String.class),
    FORM_OF_STUDY_NOT_SELECT(String.class),
    GRADE_NOT_SELECT(String.class),
    QUANTITY_NEGATIVE(String.class),
    QUANTITY_INVALID(String.class),
    GROUP_ADD(String.class),
    GROUP_ALREADY_EXIST(String.class);

    private final Class<?> type;

    private CheckAddGroupResult(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
