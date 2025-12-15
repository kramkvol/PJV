package cz.cvut.fit.sudoku.mvc.views;

import cz.cvut.fit.sudoku.mvc.controllers.AppContext;
import cz.cvut.fit.sudoku.mvc.GameTools;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import lombok.Getter;

public final class PreloaderView {
    private final AppContext ctx;
    @Getter
    private final ProgressIndicator progressIndicator;
    @Getter
    private final Label messageLabel;
    private final VBox preloaderBox;

    public PreloaderView(AppContext ctx) {
        this.ctx = ctx;
        progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressIndicator.setStyle("-fx-progress-color: #4a90e2;");
        
        messageLabel = GameTools.createInfoLabel("Loading...");
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        
        preloaderBox = new VBox(20);
        preloaderBox.setAlignment(Pos.CENTER);
        preloaderBox.getChildren().addAll(progressIndicator, messageLabel);
        preloaderBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);");
    }

    public void show() {
        ctx.getRoot().setCenter(preloaderBox);
    }

    public void hide() {
        // Preloader will be replaced by the actual content
    }

    public void updateMessage(String message) {
        messageLabel.setText(message);
    }

    public void updateProgress(double progress) {
        if (progress >= 0 && progress <= 1) {
            progressIndicator.setProgress(progress);
        }
    }
}

