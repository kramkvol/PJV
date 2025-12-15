package cz.cvut.fit.sudoku.mvc.controllers.tasks;

import cz.cvut.fit.sudoku.mvc.LevelFactory;
import cz.cvut.fit.sudoku.mvc.models.SudokuFieldModel;
import cz.cvut.fit.sudoku.mvc.models.Constants;
import javafx.concurrent.Task;

public class LevelLoadingTask extends Task<SudokuFieldModel> {
    
    private final int levelNumber;
    
    public LevelLoadingTask(int levelNumber) {
        this.levelNumber = levelNumber;
    }
    
    @Override
    protected SudokuFieldModel call() throws Exception {
        updateMessage("Loading level " + levelNumber + "...");
        updateProgress(0.0, 1.0);
        Thread.sleep(Constants.LOADING_DELAY_1);
        
        updateMessage("Creating game grid...");
        updateProgress(0.33, 1.0);
        Thread.sleep(Constants.LOADING_DELAY_2);
        
        updateMessage("Finalizing...");
        updateProgress(0.66, 1.0);
        Thread.sleep(Constants.LOADING_DELAY_3);
        
        updateProgress(1.0, 1.0);
        return new SudokuFieldModel(LevelFactory.getOrCreateLevel(levelNumber));
    }
}
