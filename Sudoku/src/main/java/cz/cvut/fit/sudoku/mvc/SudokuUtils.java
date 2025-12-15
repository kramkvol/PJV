package cz.cvut.fit.sudoku.mvc;

import cz.cvut.fit.sudoku.mvc.models.Constants;
import javafx.util.Pair;
import java.security.SecureRandom;

public final class SudokuUtils {
    private static final SecureRandom RANDOM = new SecureRandom();

    private SudokuUtils() {}

    public static int[][] getCopy(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

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

    public static String getValidNumbersForCell(int[][] board, int row, int col) {
        StringBuilder result = new StringBuilder();
        for (int num = Constants.MIN_VALUE; num <= Constants.MAX_VALUE; num++) {
            if (isNumberValid(board, row, col, num)) {
                if (!result.isEmpty()) {
                    result.append(" ");
                }
                result.append(num);
            }
        }
        return result.toString();
    }

    public static int[][] generateGameGrid(int levelNumber) {
        int[][] solved = generateSolvedGrid();
        int numToRemove = calculateNumToRemove(levelNumber);
        return removeCells(solved, numToRemove);
    }

    private static int[][] generateSolvedGrid() {
        int[][] solved = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
        solveSudoku(solved);
        return solved;
    }
    
    private static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            for (int col = 0; col < Constants.GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = Constants.MIN_VALUE; num <= Constants.MAX_VALUE; num++) {
                        if (isNumberValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
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
    private static int calculateNumToRemove(int levelNumber) {
        int cellsToRemove = 2 * levelNumber + Constants.MIN_CELLS_TO_REMOVE;
        return Math.min(cellsToRemove, Constants.MAX_CELLS_TO_REMOVE);
    }
    private static int[][] removeCells(int[][] board, int numToRemove) {
        int[][] newBoard = getCopy(board);
        int[] positions = new int[Constants.TOTAL_CELLS];
        for (int i = 0; i < Constants.TOTAL_CELLS; i++) {
            positions[i] = i;
        }
        
        // Fisher-Yates shuffle для случайного порядка
        for (int i = Constants.TOTAL_CELLS - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            int temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }
        
        // Удаляем клетки в случайном порядке
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
