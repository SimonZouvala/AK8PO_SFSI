package fai.utb.gui.checkers;

/**
 * @author Šimon Zouvala
 */
public enum CheckAddWorkLabelResult {
    NAME_EMPTY(String.class),
    LANGUAGE_NOT_SELECT(String.class),
    COMPLETION_NOT_SELECT(String.class),
    NUMBER_OF_WEEKS_CAPACITY_NOT_SELECT(String.class),
    POINTS_EMPTY(String.class),
    NUMBER_OF_STUDENTS_EMPTY(String.class),
    LESSON_TYPE_NOT_SELECT(String.class),
    NUMBER_OF_HOURS_EMPTY(String.class),
    POINTS_INVALID(String.class),
    POINTS_NEGATIVE(String.class),
    NUMBER_OF_STUDENTS_INVALID(String.class),
    NUMBER_OF_STUDENTS_NEGATIVE(String.class),
    NUMBER_OF_HOURS_INVALID(String.class),
    NUMBER_OF_HOURS_NEGATIVE(String.class),
    LESSON_TYPE_AND_COMPLETION_NOT_SELECT(String.class),
    WORKLABEL_ALREADY_EXIST(String.class),
    WORKLABEL_ADD(String.class);





    private final Class<?> type;

    private CheckAddWorkLabelResult(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

}
