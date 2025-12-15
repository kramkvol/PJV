package cz.cvut.fit.sudk.mvc.controllers;

import cz.cvut.fit.sudk.mvc.views.GameTools;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;
import java.util.function.Supplier;

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

