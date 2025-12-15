package cz.cvut.fit.sudk.mvc.controllers;

import cz.cvut.fit.sudk.mvc.models.LevelModelUtils;
import cz.cvut.fit.sudk.mvc.models.Constants;
import lombok.Getter;
import lombok.Setter;

public class GameLevel {

    @Getter protected final int levelNumber;
    @Getter protected int[][] saveGrid;
    @Getter protected final int[][] gameGrid;
    @Setter
    @Getter protected long elapsedMillis;
    @Getter protected String modeName;
    
    public GameLevel(int levelNumber) {
        this.levelNumber = levelNumber;
        this.gameGrid = LevelModelUtils.generateGameGrid(levelNumber);
        this.saveGrid = LevelModelUtils.getCopy(gameGrid);
        this.elapsedMillis = 0;
        this.modeName = "Sudoku Classic";
    }

    public void saveProgress(int[][] myGrid, long time){
        this.saveGrid = LevelModelUtils.getCopy(myGrid);
        this.elapsedMillis = time;
    }

    public void restartLevel() {
        saveGrid = LevelModelUtils.getCopy(gameGrid);
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
