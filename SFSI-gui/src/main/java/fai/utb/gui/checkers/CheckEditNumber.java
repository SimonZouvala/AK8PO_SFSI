package fai.utb.gui.checkers;

/**
 * Handles warning messages when set number students or capacity (in group or subject)for language mutations
 * @author Šimon Zouvala
 */
public enum CheckEditNumber {
    QUANTITY_UNIVERSE_EMPTY(String.class),
    QUANTITY_UNIVERSE_INVALID(String.class),
    QUANTITY_UNIVERSE_NEGATIVE(String.class),
    ERROR_WITH_NUMBER(String.class),
    OK(String.class);

    private final Class<?> type;

    private CheckEditNumber(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
