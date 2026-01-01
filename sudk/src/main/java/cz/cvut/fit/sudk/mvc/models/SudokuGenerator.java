package cz.cvut.fit.sudk.mvc.models;

import java.security.SecureRandom;

public final class SudokuGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private SudokuGenerator(){}

    static int[][] generateGameGrid(int levelNumber) {
        int[][] solved = generateSolvedGrid();
        int cellsToRemove = calculateNumToRemove(levelNumber);
        return removeCells(solved, cellsToRemove);
    }

    // ----- solver -----

    private static boolean isSudokuSolved(int[][] board) {
        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            for (int col = 0; col < Constants.GRID_SIZE; col++) {
                if (board[row][col] == Constants.EMPTY_CELL) {

                    int[] numbers = shuffledNumbers();
                    for (int num : numbers) {
                        if (LevelModelUtils.isNumberValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (isSudokuSolved(board)) return true;
                            board[row][col] = Constants.EMPTY_CELL;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] shuffledNumbers() {
        int[] nums = new int[Constants.MAX_VALUE];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i + 1;
        }
        for (int i = nums.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }
        return nums;
    }


    private static int[][] generateSolvedGrid() {
        int[][] board = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
        isSudokuSolved(board);
        return board;
    }

    // ----- difficulty -----

    private static int calculateNumToRemove(int levelNumber) {
        int cells = Constants.MIN_CELLS_TO_REMOVE + 2 * levelNumber;
        return Math.min(cells, Constants.MAX_CELLS_TO_REMOVE);
    }

    private static int[][] removeCells(int[][] board, int numToRemove) {
        int[][] copy = LevelModelUtils.getCopy(board);

        int[] positions = new int[Constants.TOTAL_CELLS];
        for (int i = 0; i < positions.length; i++) positions[i] = i;

        for (int i = positions.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            int tmp = positions[i];
            positions[i] = positions[j];
            positions[j] = tmp;
        }

        int removed = 0;
        for (int pos : positions) {
            if (removed >= numToRemove) break;
            int r = pos / Constants.GRID_SIZE;
            int c = pos % Constants.GRID_SIZE;
            if (copy[r][c] != Constants.EMPTY_CELL) {
                copy[r][c] = Constants.EMPTY_CELL;
                removed++;
            }
        }
        return copy;
    }
}
