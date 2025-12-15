package cz.cvut.fit.sudoku.mvc.controllers.tasks;

import cz.cvut.fit.sudoku.mvc.LevelFactory;
import cz.cvut.fit.sudoku.mvc.models.Constants;
import cz.cvut.fit.sudoku.mvc.models.MainMenuModel;
import cz.cvut.fit.sudoku.mvc.models.SudokuFieldModel;
import javafx.concurrent.Task;

public class MainMenuLoadingTask extends Task<MainMenuModel> {
    public MainMenuLoadingTask() {}

    @Override
    protected MainMenuModel call() throws Exception {
        updateMessage("Loading Main menu...");
        Thread.sleep(Constants.LOADING_DELAY_1);

        updateMessage("Creating game grid...");
        Thread.sleep(Constants.LOADING_DELAY_2);

        updateMessage("Finalizing...");
        Thread.sleep(Constants.LOADING_DELAY_3);

        return new MainMenuModel();
    }
}
