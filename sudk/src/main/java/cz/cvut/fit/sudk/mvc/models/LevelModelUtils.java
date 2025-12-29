package cz.cvut.fit.sudk.mvc.models;

import javafx.util.Pair;

import java.security.SecureRandom;

public final class LevelModelUtils {
    private static final SecureRandom RANDOM = new SecureRandom();

    private LevelModelUtils() {}

    public static int[][] getCopy(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public static Pair<String, Boolean> isValueAvailable(int[][] board, int row, int col, String strValue) {

        int value;
        try {
            value = Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            return new Pair<>("The value " + strValue + " is not a number.", false);
        }

        if (value < Constants.MIN_VALUE || value > Constants.MAX_VALUE) {
            return new Pair<>(
                    "The number " + value + " must be between "
                            + Constants.MIN_VALUE + " and " + Constants.MAX_VALUE + ".",
                    false
            );
        }

        if (!checkRow(board, row, value)) {
            return new Pair<>(value + " is already in the row.", false);
        }

        if (!checkCol(board, col, value)) {
            return new Pair<>(value + " is already in the column.", false);
        }

        if (!checkSquare(board, row, col, value)) {
            return new Pair<>(value + " is already in the 3x3 square.", false);
        }

        return new Pair<>(value + " is OK.", true);
    }

    private static int[][] generateSolvedGrid() {
        int[][] solved = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
        solveSudoku(solved);
        return solved;
    }

    /**
     * Generates a new Sudoku game grid:
     * 1. Generates a fully solved valid Sudoku board.
     * 2. Removes a certain number of cells based on the level difficulty.
     *
     * Difficulty = linear: more level -> more empty cells
     */
    public static int[][] generateGameGrid(int levelNumber) {
        int[][] solved = generateSolvedGrid();
        int numToRemove = calculateNumToRemove(levelNumber);
        return removeCells(solved, numToRemove);
    }

    /**
     * BACKTRACKING SOLVER — core Sudoku solving algorithm.
     *
     * Works by:
     * - Scanning cells row by row
     * - When an empty cell (0) is found, tries numbers 1–9
     * - If placing a number doesn't violate rules:
     *      -> recursively tries to solve the rest of the grid
     * - If recursion fails → backtracks (sets cell back to 0)
     *
     * Returns true once the board is fully solved.
     */
    private static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            for (int col = 0; col < Constants.GRID_SIZE; col++) {
                // Find empty cell
                if (board[row][col] == 0) {
                    // Try possible numbers
                    for (int num = Constants.MIN_VALUE; num <= Constants.MAX_VALUE; num++) {
                        if (isNumberValid(board, row, col, num)) {
                            board[row][col] = num;
                            // Continue solving recursively
                            if (solveSudoku(board)) return true;
                            // Backtracking step
                            board[row][col] = 0;
                        }
                    }
                    // No valid number found -> backtrack
                    return false;
                }
            }
        }
        // No empty cells -> board solved
        return true;
    }

    public static boolean isNumberValid(int[][] board, int row, int col, int value) {
        return checkRow(board, row, value) && checkCol(board, col, value) && checkSquare(board, row, col, value);
    }
    private static boolean checkRow(int[][] board, int row, int value) {
        for (int i = 0; i < Constants.GRID_SIZE; i++) {
            if (board[row][i] == value) return false;
        }
        return true;
    }

    private static boolean checkCol(int[][] board, int col, int value) {
        for (int i = 0; i < Constants.GRID_SIZE; i++) {
            if (board[i][col] == value) return false;
        }
        return true;
    }

    private static boolean checkSquare(int[][] board, int row, int col, int value) {
        int startRow = row - row % Constants.BOX_SIZE;
        int startCol = col - col % Constants.BOX_SIZE;
        for (int i = startRow; i < startRow + Constants.BOX_SIZE; i++) {
            for (int j = startCol; j < startCol + Constants.BOX_SIZE; j++) {
                if (board[i][j] == value) return false;
            }
        }
        return true;
    }

    /**
     * Difficulty scaling:
     * The harder the level, the more cells removed.
     * Formula: MIN_CELLS_TO_REMOVE + (2 * level)
     * capped by MAX_CELLS_TO_REMOVE.
     */
    private static int calculateNumToRemove(int levelNumber) {
        int cellsToRemove = 2 * levelNumber + Constants.MIN_CELLS_TO_REMOVE;
        return Math.min(cellsToRemove, Constants.MAX_CELLS_TO_REMOVE);
    }

    /**
     * Removes a specified number of cells from a solved board.
     */
    private static int[][] removeCells(int[][] board, int numToRemove) {
        int[][] newBoard = getCopy(board);
        // Create a list [0..80] representing each cell
        int[] positions = new int[Constants.TOTAL_CELLS];
        for (int i = 0; i < Constants.TOTAL_CELLS; i++) {
            positions[i] = i;
        }

        /*
         * Shuffle the array:
         * Step 1 — take the element from the end,
         * Step 2 — swap it with any random element before it (including itself),
         * Step 3 — repeat until reaching the beginning of the array.
         *
         * This produces a perfectly uniform random ordering of all positions.
         */
        for (int i = Constants.TOTAL_CELLS - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            int temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }

        // Remove cells in random order
        int removed = 0;
        for (int i = 0; i < Constants.TOTAL_CELLS && removed < numToRemove; i++) {
            int position = positions[i];
            int row = position / Constants.GRID_SIZE;
            int col = position % Constants.GRID_SIZE;
            if (newBoard[row][col] != Constants.EMPTY_CELL) {
                newBoard[row][col] = Constants.EMPTY_CELL;
                removed++;
            }
        }
        return newBoard;
    }
}
