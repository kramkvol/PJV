package cz.cvut.fit.sudk.mvc.controllers;

import cz.cvut.fit.sudk.AppContext;
import cz.cvut.fit.sudk.mvc.models.*;
import cz.cvut.fit.sudk.mvc.views.GameTools;
import cz.cvut.fit.sudk.mvc.views.SudokuFieldView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Arrays;

public final class SudokuFieldController {

    private final AppContext ctx;
    private final MainMenuController mainMenuController;

    private SudokuFieldModel model;
    private SudokuFieldView view;

    // ---- TIMER FIELDS ----
    private long startTimestamp;
    private long elapsedBefore;
    private TimerThread timerThread;

    public SudokuFieldController(AppContext ctx, MainMenuController mainMenuController) {
        this.ctx = ctx;
        this.mainMenuController = mainMenuController;
    }

    public void setModel(SudokuFieldModel model) {
        this.model = model;
        this.view = new SudokuFieldView(ctx);
    }

    // ---------------- MOUNT ----------------

    public void mount() {
        if(LevelModelUtils.containsZero(model.getLevel().getSaveGrid())){
            view.mount();
            view.renderLevel(model);
            attachCells();
            view.getSettingsBtn().setOnAction(e -> clickSettings());
            startTimer();
        } else {
            stopTimer();
            view.mount();
            view.renderCompletedLevel(model);
            view.getSettingsBtn().setOnAction(e -> clickSettings());
            view.updateHint("Level completed \n Open the Settings to restart/open main menu");
        }
    }

    // ---------------- GAME FLOW ----------------

    private void clickSettings() {
        saveTimerToLevel();
        stopTimer();

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        Button backBtn = GameTools.createDefaultButton("Back to Game");
        Button restartBtn = GameTools.createDefaultButton("Restart Level");
        Button mainMenuBtn = GameTools.createDefaultButton("Main Menu");

        backBtn.setOnAction(e -> clickResume());
        restartBtn.setOnAction(e -> clickRestart());
        mainMenuBtn.setOnAction(e -> clickMainMenu());

        box.getChildren().addAll(backBtn, restartBtn, mainMenuBtn);
        ctx.getRoot().setCenter(box);
    }

    private void clickResume() {
        mount();
        view.updateHint("Game continued.");
    }

    private void clickRestart() {
        model.getLevel().restartLevel();
        mount();
        view.updateHint("Game restarted.");
    }

    private void clickMainMenu() {
        mainMenuController.mount();
    }

    // ---------------- TIMER ----------------

    private void startTimer() {
        stopTimer();
        elapsedBefore = model.getLevel().getElapsedMillis();
        startTimestamp = System.currentTimeMillis();

        timerThread = new TimerThread();
        timerThread.start();
    }

    private void stopTimer() {
        if (timerThread != null) {
            timerThread.stopTimer();
            timerThread = null;
        }
    }

    private void saveTimerToLevel() {
        long now = System.currentTimeMillis();
        model.getLevel().setElapsedMillis(elapsedBefore + (now - startTimestamp));
    }

    private class TimerThread extends Thread {

        private volatile boolean running = true;

        @Override
        public void run() {
            System.out.println("[TimerThread] run() entered (thread started)");
            while (running) {
                Platform.runLater(() -> {
                    long now = System.currentTimeMillis();
                    long total = elapsedBefore + (now - startTimestamp);
                    view.updateTimer(total);
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    System.out.println("[TimerThread] interrupted during sleep");
                    break;
                }
            }
            System.out.println("[TimerThread] interrupted during sleep");
        }

        public void stopTimer() {
            System.out.println("[TimerThread] interrupted during sleep");
            running = false;
        }
    }

    // ---------------- CELLS ----------------

    private boolean cellsAttached = false;

    private void attachCells() {
        if (cellsAttached) return;

        int[][] gameGrid = model.getLevel().getGameGrid();

        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            for (int col = 0; col < Constants.GRID_SIZE; col++) {
                if (gameGrid[row][col] == 0) {
                    attachEditableCell(row, col);
                }
            }
        }
        cellsAttached = true;
    }

    private void attachEditableCell(int row, int col) {
        TextField cell = view.getCellsGrid()[row][col];
        int[][] saveGrid = model.getLevel().getSaveGrid();
        int[][] gameGrid = model.getLevel().getGameGrid();

        // ---- Mouse ----
        cell.setOnMouseClicked(e -> view.updateHint("Valid numbers: " + getValidNumbersForCell(row, col)));


        // ---- INPUT FILTER (1–9 only) ----
        cell.setTextFormatter(new TextFormatter<String>(change -> {String newText = change.getControlNewText();
            if (newText.isEmpty()) {  return change; }
            if (newText.matches("[1-9]")) { return change; }

            view.updateHint("You can try to set 1-9 only");
            return null;
        }));

        // ---- Text ----
        cell.textProperty().addListener((obs, oldVal, newVal) -> {


            if (newVal.isEmpty()) {
                saveGrid[row][col] = 0;
            } else {

                Pair<String, Boolean> result = LevelModelUtils.isValueAvailable(saveGrid, row, col, Integer.parseInt(newVal));

                if (!result.getValue()) {
                    cell.clear();
                    saveGrid[row][col] = 0;
                    view.updateHint(result.getKey());
                } else {
                    int value = Integer.parseInt(newVal);
                    saveGrid[row][col] = value;
                    view.updateHint(result.getKey());

                    if (!LevelModelUtils.containsZero(saveGrid)) {
                        saveTimerToLevel();
                        stopTimer();
                        view.renderCompletedLevel(model);
                        view.updateHint("Level completed \n Open the Settings to restart/open main menu");
                        mainMenuController.getModel().getPlayerProgress().unlockNextLevel(model.getLevel().getLevelNumber());
                    }
                }
            }
            System.out.println("GameGrid: " + Arrays.deepToString(gameGrid));
            System.out.println("SaveGrid: " + Arrays.deepToString(saveGrid) + "\n");
        });
    }


    // ---------------- CELL LOGIC ----------------

    private String getValidNumbersForCell(int row, int col) {
        int[][] board = model.getLevel().getGameGrid();
        StringBuilder result = new StringBuilder();
        for (int num = Constants.MIN_VALUE; num <= Constants.MAX_VALUE; num++) {
            if (LevelModelUtils.isNumberValid(board, row, col, num)) {
                if (!result.isEmpty()) {
                    result.append(" ");
                }
                result.append(num);
            }
        }
        return result.toString();
    }

}
