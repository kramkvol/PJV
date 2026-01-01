package cz.cvut.fit.sudk;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;

public class AppContext {
    @Getter protected final Stage stage;
    @Getter protected final BorderPane root;

    public AppContext(Stage stage, BorderPane root) {
        this.stage = stage;
        this.root = root;
    }
}

