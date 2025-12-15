package cz.cvut.fit.sudoku.mvc.controllers;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppContext {
    protected final Stage stage;
    protected final BorderPane root;

    public AppContext(Stage stage, BorderPane root) {
        this.stage = stage;
        this.root = root;
    }

    public Stage getStage() { return stage; }
    public BorderPane getRoot() { return root; }
}

