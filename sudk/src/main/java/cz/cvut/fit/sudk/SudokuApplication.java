package cz.cvut.fit.sudk;

import cz.cvut.fit.sudk.mvc.controllers.MainMenuController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class SudokuApplication extends Application {

    private MainMenuController mainMenuController;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 500, 700);
        scene.getStylesheets().add(getClass().getResource("/cz/cvut/fit/sudk/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Sudoku — Main Menu");
        stage.show();


        AppContext ctx = new AppContext(stage, root);
        mainMenuController = new MainMenuController(ctx);
        mainMenuController.mount();
    }
    public static void main(String[] args) {
        launch();
    }
}
