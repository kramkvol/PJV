package cz.cvut.fit.sudk.mvc.controllers;

import cz.cvut.fit.sudk.mvc.models.Constants;
import cz.cvut.fit.sudk.mvc.models.LevelModelUtils;
import cz.cvut.fit.sudk.mvc.models.SudokuFieldModel;
import cz.cvut.fit.sudk.mvc.views.GameTools;
import cz.cvut.fit.sudk.mvc.views.SudokuFieldView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

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
        view.mount();
        view.render(model);
        attachCells();
        view.getSettingsBtn().setOnAction(e -> clickPause());
        startTimer();
    }

    // ---------------- GAME FLOW ----------------

    private void clickPause() {
        saveTimerToLevel();
        stopTimer();
        createSettingsDialog();
    }

    private void clickResume() {
        startTimer();
        refreshUI();
        mount();
        view.updateHint("Game continued.");
    }

    private void clickRestart() {
        model.getLevel().restartLevel();
        refreshUI();
        mount();
        view.updateHint("Game restarted.");
    }

    private void onMainMenu() {
        mainMenuController.mount();
    }

    // ---------------- UI DIALOGS ----------------

    private void createSettingsDialog() {
        VBox box = new VBox(Constants.VBOX_SPACING);
        box.setAlignment(Pos.CENTER);

        Button backBtn = GameTools.createDefaultButton("Back to Game");
        Button restartBtn = GameTools.createDefaultButton("Restart Level");
        Button mainMenuBtn = GameTools.createDefaultButton("Main Menu");

        backBtn.setOnAction(e -> clickResume());
        restartBtn.setOnAction(e -> clickRestart());
        mainMenuBtn.setOnAction(e -> onMainMenu());

        box.getChildren().addAll(backBtn, restartBtn, mainMenuBtn);
        ctx.getRoot().setCenter(box);
    }

    private void refreshUI() {
        ctx.getRoot().setTop(view.createTop());
        ctx.getRoot().setCenter(view.createCenter());
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

    private String formatTime(long millis) {
        long sec = millis / 1000;
        return String.format("%02d:%02d", sec / 60, sec % 60);
    }

    private class TimerThread extends Thread {

        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                Platform.runLater(() -> {
                    long now = System.currentTimeMillis();
                    long total = elapsedBefore + (now - startTimestamp);
                    view.updateTimer(formatTime(total));
                });

                try {
                    Thread.sleep(Constants.TIMER_UPDATE_INTERVAL_MS);
                } catch (InterruptedException ignored) {
                    break;
                }
            }
        }

        public void stopTimer() {
            running = false;
        }
    }

    // ---------------- CELLS ----------------

    private void attachCells() {
        for (int row = 0; row < Constants.GRID_SIZE; row++) {
            for (int col = 0; col < Constants.GRID_SIZE; col++) {
                attachMouseHandler(row, col);
                attachTextHandler(row, col);
            }
        }
    }

    private void attachTextHandler(int row, int col) {
        TextField[][] cellsGrid = view.getCellsGrid();
        int[][] saveLevelGrid = model.getLevel().getSaveGrid();
        cellsGrid[row][col].textProperty().addListener((obs, oldVal, newVal) -> {
            Pair<String, Boolean> result = LevelModelUtils.isValueAvailable(saveLevelGrid, row, col, newVal);
            if (result.getValue()) {
                saveLevelGrid[row][col] = Integer.parseInt(newVal);
                if (model.getLevel().isSudokuSolved()) {
                    startVictoryDialog();
                }
            }
            if (!result.getValue()) {
                cellsGrid[row][col].setText("");
                saveLevelGrid[row][col] = 0;
            }

            view.updateHint(result.getKey());

        });
    }

    private void attachMouseHandler(int row, int col) {
        TextField[][] cellsGrid = view.getCellsGrid();
        cellsGrid[row][col].setOnMouseClicked(e -> {
            view.updateHint("Valid numbers: " + getValidNumbersForCell(row, col));
        });
    }

    // ---------------- CELL LOGIC ----------------

    public String getValidNumbersForCell(int row, int col) {
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

    public void startVictoryDialog() {
        saveTimerToLevel();
        stopTimer();
        if (model != null) {
            int completedLevel = model.getLevel().getLevelNumber();
            mainMenuController.getModel().getPlayerProgress().unlockNextLevel(completedLevel);
            view.render(model);
        }
        view.createVictoryDialog();
        view.getRestartBtn().setOnAction(e -> clickRestart());
        view.getMainMenuBtn().setOnAction(e -> onMainMenu());
    }

}
