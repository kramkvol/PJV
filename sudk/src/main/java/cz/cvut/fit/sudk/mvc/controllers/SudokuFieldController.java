package cz.cvut.fit.sudk.mvc.controllers;

import cz.cvut.fit.sudk.mvc.views.GameTools;
import cz.cvut.fit.sudk.mvc.models.LevelModelUtils;
import cz.cvut.fit.sudk.mvc.models.Constants;
import cz.cvut.fit.sudk.mvc.models.SudokuFieldModel;
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

    // MOUNT
    public void mount() {
        view.mount();
        view.render(model);
        attachCells();
        view.getSettingsBtn().setOnAction(e -> pauseGame());
        startTimer();
    }

    // SETTINGS / PAUSE MENU
    private void pauseGame() {
        saveTimerToLevel();
        stopTimer();
        createSettingsDialog();
    }

    private void resumeGame() {
        startTimer();
        refreshUI();
        mount();
        view.updateHint("Game continued.");
    }


    private void restartGame() {
        model.getLevel().restartLevel();
        refreshUI();
        mount();
        view.updateHint("Game restarted.");
    }

    private void onMainMenu() {
        mainMenuController.mount();
    }

    private void createSettingsDialog() {
        VBox settingsBox = new VBox(Constants.VBOX_SPACING);
        settingsBox.setAlignment(Pos.CENTER);

        Button backBtn = GameTools.createDefaultButton("Back to Game");
        Button restartBtn = GameTools.createDefaultButton("Restart Level");
        Button mainMenuBtn = GameTools.createDefaultButton("Main Menu");

        backBtn.setOnAction(e -> resumeGame());
        restartBtn.setOnAction(e -> restartGame());
        mainMenuBtn.setOnAction(e -> onMainMenu());

        settingsBox.getChildren().addAll(backBtn, restartBtn, mainMenuBtn);
        ctx.getRoot().setCenter(settingsBox);
    }

    private void refreshUI() {
        ctx.getRoot().setTop(view.createTop());
        ctx.getRoot().setCenter(view.createCenter());
    }

    // TIMER LOGIC

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
        long total = elapsedBefore + (now - startTimestamp);
        model.getLevel().setElapsedMillis(total);
    }

    private String formatTime(long millis) {
        long sec = millis / 1000;
        long min = sec / 60;
        long rem = sec % 60;
        return String.format("%02d:%02d", min, rem);
    }

    /*
     * Background thread that updates the timer on the screen.
     *
     * How it works:
     * - While `running` is true, the thread repeatedly:
     *      1) Calculates the elapsed time
     *      2) Uses Platform.runLater() to update the UI safely
     *      3) Sleeps for a short interval
     *
     * Why runLater?
     * - JavaFX UI can be updated only from the JavaFX Application Thread.
     *
     * Stopping:
     * - `stopTimer()` sets `running` to false and interrupts the sleep,
     *   so the thread ends quickly.
     *
     * Result:
     * - The timer updates smoothly without freezing the UI.
     */
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
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        public void stopTimer() {
            running = false;
        }
    }

    // GAME LOGIC (CELLS)
    private void attachCells() {
        for (int r = 0; r < Constants.GRID_SIZE; r++) {
            for (int c = 0; c < Constants.GRID_SIZE; c++) {

                TextField cell = view.getCellsGrid()[r][c];
                int row = r, col = c;

                cell.setOnMouseClicked(e -> {
                    if (model.getLevel().getGameGrid()[row][col] == Constants.EMPTY_CELL) {
                        String validNumbers = LevelModelUtils.getValidNumbersForCell(
                                model.getLevel().getSaveGrid(), row, col
                        );
                        view.updateHint("Valid numbers: " + validNumbers);
                    }
                });

                cell.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (!cell.isFocused()) return;

                    int[][] grid = model.getLevel().getSaveGrid();

                    if (newVal.isEmpty()) {
                        grid[row][col] = Constants.EMPTY_CELL;
                        view.updateHint("Cell cleared");
                        view.render(model);
                        return;
                    }

                    Pair<String, Boolean> result =
                            LevelModelUtils.isValueAvailable(grid, row, col, newVal);

                    if (result.getValue()) {
                        try {
                            int inputValue = Integer.parseInt(newVal);
                            if (inputValue >= Constants.MIN_VALUE && inputValue <= Constants.MAX_VALUE) {
                                grid[row][col] = inputValue;
                                view.updateHint("Valid number");

                                if (model.getLevel().isSudokuSolved()) {
                                    view.updateHint("Level completed!");
                                    unlockNextLevel();
                                    openVictoryDialog();
                                }

                                view.render(model);
                            }
                        } catch (NumberFormatException ignore) {}
                    } else {
                        grid[row][col] = Constants.EMPTY_CELL;
                        cell.setText("");
                        view.updateHint("Invalid: " + result.getKey());
                        view.render(model);
                    }
                });
            }
        }
    }

    // GAME END
    private void openVictoryDialog() {
        saveTimerToLevel();
        stopTimer();
        view.updateHint("Level completed!");
    }

    private void unlockNextLevel() {
        if (model != null) {
            int completedLevel = model.getLevel().getLevelNumber();
            mainMenuController.onLevelCompleted(completedLevel);
        }
    }
}
