package cz.cvut.fit.sudk.mvc.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public final class GameTools {
    private GameTools() {}

    public static void setButtonSelected(Button button, boolean selected) {
        if (selected) {
            if (!button.getStyleClass().contains("selected")) {
                button.getStyleClass().add("selected");
            }
        } else {
            button.getStyleClass().remove("selected");
        }
    }

    public static void setButtonEnabled(Button button, boolean enabled) {
        button.setDisable(!enabled);
    }

    public static TextField createSudokuCell() {
        TextField cell = new TextField("");
        return cell;
    }

    public static void setCellLocked(TextField cell, boolean locked) {
        cell.setDisable(locked);
        if (locked) {
            if (!cell.getStyleClass().contains("locked")) {
                cell.getStyleClass().add("locked");
            }
        } else {
            cell.getStyleClass().remove("locked");
        }
    }

    public static Label createInfoLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label-info");
        return label;
    }

    public static Label createTimerLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label-timer");
        return label;
    }

    public static Label createHintLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label-hint");
        return label;
    }

    public static Label createLoadingLabel() {
        Label label = new Label("Loading...");
        label.getStyleClass().add("label-loading");
        return label;
    }

    public static Button createDefaultButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button-default");
        return button;
    }

    public static Button createLevelSelectButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("button-level");
        return button;
    }
}
