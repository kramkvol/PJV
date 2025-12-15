package cz.cvut.fit.sudoku.mvc.models;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Grid dimensions
    public static final int GRID_SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static final int TOTAL_CELLS = GRID_SIZE * GRID_SIZE;

    // UI dimensions
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 700;

    // Button dimensions
    public static final int DEFAULT_BUTTON_WIDTH = 150;
    public static final int DEFAULT_BUTTON_HEIGHT = 40;

    public static final int LEVEL_BUTTON_WIDTH = 45;
    public static final int LEVEL_BUTTON_HEIGHT = 45;

    // Label dimensions
    public static final int LABEL_WIDTH = 150;
    public static final int LABEL_HEIGHT = 40;

    //Cell dimensions
    public static final int CELL_WIDTH = 45;
    public static final int CELL_HEIGHT = 45;

    // Label dimensions
    public static final int HINT_WIDTH = 420;
    public static final int HINT_HEIGHT = 30;

    // Game levels
    public static final int LEVELS_PER_PAGE = 25;
    public static final int MAX_LEVELS_CLASSIC = 66;
    public static final int MIN_CELLS_TO_REMOVE = 15;
    public static final int MAX_CELLS_TO_REMOVE = 85;

    // Game values
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 9;
    public static final int EMPTY_CELL = 0;
    public static final int TIMER_UPDATE_INTERVAL_MS = 1000;

    // Default values
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SELECTED_INDEX = -1;
    public static final int DEFAULT_LEVEL = 1;
    
    // UI spacing and gaps
    public static final int CELL_SPACING = 1;
    public static final int GRID_GAP = 8;
    public static final int VBOX_SPACING = 15;
    public static final int HBOX_SPACING = 20;
    public static final int PADDING = 20;

    // Loading delays
    public static final int LOADING_DELAY_1 = 500;
    public static final int LOADING_DELAY_2 = 300;
    public static final int LOADING_DELAY_3 = 200;
}
