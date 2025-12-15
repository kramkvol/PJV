package cz.cvut.fit.sudk.mvc.models;

import javafx.util.Pair;

import java.security.SecureRandom;
import java.util.Arrays;

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

    /**
     * Checks whether a string value can be placed in a given Sudoku cell.
     *
     * Validates:
     * - the string is a number
     * - the number is in allowed bounds
     * - it does not violate Sudoku rules (row/column/3×3 square)
     *
     * Returns Pair(message, isValid)
     */
    public static Pair<String, Boolean> isValueAvailable(int[][] board, int row, int col, String strValue) {
        try {
            int intValue = Integer.parseInt(strValue);
            if (intValue < Constants.MIN_VALUE || intValue > Constants.MAX_VALUE) {
                return new Pair<>("The number (" + intValue + ") must be between " + Constants.MIN_VALUE + " and " + Constants.MAX_VALUE + ".", false);
            }
            if (!checkRow(board, row, intValue)) return new Pair<>("Already in row", false);
            if (!checkCol(board, col, intValue)) return new Pair<>("Already in column", false);
            if (!checkSquare(board, row, col, intValue)) return new Pair<>("Already in 3x3", false);
            return new Pair<>("Ok", true);
        } catch (NumberFormatException e) {
            return new Pair<>("The value (" + strValue + ") is not a number.", false);
        }
    }

    /**
     * Returns a space-separated list of all valid numbers
     * that can be placed into the specified cell.
     * Method is used for hints in GUI.
     */
    public static String getValidNumbersForCell(int[][] board, int row, int col) {
        StringBuilder result = new StringBuilder();
        for (int num = Constants.MIN_VALUE; num <= Constants.MAX_VALUE; num++) {
            // Only append numbers that don't break Sudoku rules.
            if (isNumberValid(board, row, col, num)) {
                if (!result.isEmpty()) {
                    result.append(" ");
                }
                result.append(num);
            }
        }
        return result.toString();
    }

    /**
     * Creates an empty 9×9 board and fills it using backtracking.
     */
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

    private static boolean isNumberValid(int[][] board, int row, int col, int value) {
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
        System.out.println(Arrays.deepToString(board));
        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.deepToString(newBoard));
        return newBoard;
    }
}
