package cz.cvut.fit.sudoku;

import cz.cvut.fit.sudoku.core.GameTimer;
import cz.cvut.fit.sudoku.mvc.controllers.AppContext;
import cz.cvut.fit.sudoku.mvc.controllers.MainMenuController;
import cz.cvut.fit.sudoku.mvc.controllers.SudokuFieldController;
import cz.cvut.fit.sudoku.mvc.models.Constants;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public final class SudokuApplication extends Application {

    private GameTimer gameTimer;
    private MainMenuController mainMenuController;
    private SudokuFieldController sudokuFieldController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(Constants.PADDING));

        Scene scene = new Scene(root, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Sudoku — Main Menu");
        stage.show();


        AppContext ctx = new AppContext(stage, root);
        MainMenuController tempMainMenuController = new MainMenuController(ctx,null);
        sudokuFieldController = new SudokuFieldController(ctx,  new GameTimer(), tempMainMenuController);
        mainMenuController = new MainMenuController(ctx, sudokuFieldController);

        mainMenuController.mount();
    }
}
