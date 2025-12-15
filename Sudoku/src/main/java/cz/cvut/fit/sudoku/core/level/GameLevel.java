package cz.cvut.fit.sudoku.core.level;

import cz.cvut.fit.sudoku.mvc.models.Constants;
import cz.cvut.fit.sudoku.mvc.SudokuUtils;
import lombok.Getter;
import lombok.Setter;

public class GameLevel implements IGameLevel {

    @Getter protected final int levelNumber;
    @Getter protected int[][] saveGrid;
    @Getter protected final int[][] gameGrid;
    @Setter @Getter protected long elapsedMillis;
    @Getter protected String modeName;
    
    public GameLevel(int levelNumber) {
        this.levelNumber = levelNumber;
        this.gameGrid = SudokuUtils.generateGameGrid(levelNumber);
        this.saveGrid = SudokuUtils.getCopy(gameGrid);
        this.elapsedMillis = 0;
        this.modeName = "Sudoku Classic";
    }

    public void restartLevel() {
        saveGrid = SudokuUtils.getCopy(gameGrid);
        elapsedMillis = 0;
    }
    
    public boolean isSudokuSolved() {
        for (int[] row : saveGrid) {
            for (int cell : row) {
                if (cell == Constants.EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

}
