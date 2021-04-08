package fai.utb.gui.checkers;

/**
 * @author Šimon Zouvala
 */
public enum CheckAddSubjectResult {
    NAME_EMPTY(String.class),
    ACRONYM_EMPTY(String.class),
    LANGUAGE_NOT_SELECT(String.class),
    COMPLETION_NOT_SELECT(String.class),
    LECTURE_CAPACITY_INVALID(String.class),
    SEMINAR_CAPACITY_INVALID(String.class),
    EXERCISE_CAPACITY_INVALID(String.class),
    CLASSROOM_CAPACITY_INVALID(String.class),
    NUMBER_OF_WEEKS_CAPACITY_INVALID(String.class),
    LECTURE_CAPACITY_EMPTY(String.class),
    SEMINAR_CAPACITY_EMPTY(String.class),
    EXERCISE_CAPACITY_EMPTY(String.class),
    CLASSROOM_CAPACITY_EMPTY(String.class),
    NUMBER_OF_WEEKS_CAPACITY_EMPTY(String.class),
    TEACHER_EMPTY(String.class),
    SUBJECT_ADD(String.class),
    SUBJECT_ALREADY_EXIST(String.class),
    LECTURE_CAPACITY_NEGATIVE(String.class),
    SEMINAR_CAPACITY_NEGATIVE(String.class),
    EXERCISE_CAPACITY_NEGATIVE(String.class),
    CLASSROOM_CAPACITY_NEGATIVE(String.class),
    NUMBER_OF_WEEKS_CAPACITY_NEGATIVE(String.class);


    private final Class<?> type;

    private CheckAddSubjectResult(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

}
