package cz.cvut.fit.sudk.mvc.models;

import javafx.util.Pair;

import java.util.Objects;

import static cz.cvut.fit.sudk.mvc.models.SudokuGenerator.*;

public final class LevelModelUtils {

    private LevelModelUtils() {}

    // ---------------- VALIDATION ----------------
    public static boolean isNumberValid(int[][] board, int row, int col, int value) {

        for (int i = 0; i < Constants.GRID_SIZE; i++) {
            if (board[row][i] == value) return false;
            if (board[i][col] == value) return false;
        }

        int sr = (row / Constants.BOX_SIZE) * Constants.BOX_SIZE;
        int sc = (col / Constants.BOX_SIZE) * Constants.BOX_SIZE;

        for (int i = sr; i < sr + Constants.BOX_SIZE; i++) {
            for (int j = sc; j < sc + Constants.BOX_SIZE; j++) {
                if (board[i][j] == value) return false;
            }
        }

        return true;
    }

    // ----- copy -----

    static int[][] getCopy(int[][] original) {
        Objects.requireNonNull(original, "Board cannot be null");

        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public static boolean containsZero(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Pair<String, Boolean> isValueAvailable( int[][] board, int row, int col, int value) {

        if (!isNumberValid(board, row, col, value)) {
            return new Pair<>("Your number " +  value + " is already \n in the row, column or 3x3 square", false);
        }

        return new Pair<>("Your number " + value + " is OK.", true);
    }
}
