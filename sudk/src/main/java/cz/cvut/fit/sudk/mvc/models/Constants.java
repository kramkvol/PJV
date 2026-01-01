package cz.cvut.fit.sudk.mvc.models;

public final class Constants {

    private Constants() {}

    // Grid dimensions
    public static final int GRID_SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static final int TOTAL_CELLS = GRID_SIZE * GRID_SIZE;

    // Game levels
    public static final int LEVELS_PER_PAGE = 25;
    public static final int MAX_LEVELS_CLASSIC = 66;
    public static final int MIN_CELLS_TO_REMOVE = 15;
    public static final int MAX_CELLS_TO_REMOVE = 85;

    // Game values
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 9;
    public static final int EMPTY_CELL = 0;

    // Default values
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SELECTED_INDEX = -1;

    // UI spacing and gaps
    public static final int CELL_SPACING = 1;
    public static final int GRID_GAP = 8;
}
