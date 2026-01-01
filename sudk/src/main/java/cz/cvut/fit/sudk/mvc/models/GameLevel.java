package cz.cvut.fit.sudk.mvc.models;

import lombok.Getter;
import lombok.Setter;

public class GameLevel {

    @Getter protected final int levelNumber;
    @Getter protected int[][] saveGrid;
    @Getter protected final int[][] gameGrid;
    @Getter @Setter protected long elapsedMillis;
    @Getter protected String modeName;
    
    public GameLevel(int levelNumber) {
        this.levelNumber = levelNumber;
        this.gameGrid = SudokuGenerator.generateGameGrid(levelNumber);
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
}
