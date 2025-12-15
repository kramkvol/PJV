package cz.cvut.fit.sudoku.mvc.controllers;

import cz.cvut.fit.sudoku.core.GameTimer;
import cz.cvut.fit.sudoku.mvc.GameTools;
import cz.cvut.fit.sudoku.mvc.models.SudokuFieldModel;
import cz.cvut.fit.sudoku.mvc.views.SudokuFieldView;
import javafx.application.Platform;
import cz.cvut.fit.sudoku.mvc.controllers.tasks.GameTimerTask;
import cz.cvut.fit.sudoku.mvc.SudokuUtils;
import cz.cvut.fit.sudoku.mvc.models.Constants;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.util.Pair;

public final class SudokuFieldController {
    private final AppContext ctx;
    private final GameTimer timer;
    private final MainMenuController mainMenuController;
    private SudokuFieldModel model;
    private SudokuFieldView view;
    private GameTimerTask timerTask;

    public SudokuFieldController(AppContext ctx, GameTimer timer, MainMenuController mainMenuController) {
        this.ctx = ctx;
        this.timer = timer;
        this.mainMenuController = mainMenuController;
    }

    public void setModel(SudokuFieldModel model) {
        this.model = model;
        this.view = new SudokuFieldView(ctx);
    }

    public void mount() {
        view.mount();
        view.render(model);
        attachCells();
        view.getSettingsBtn().setOnAction(e -> openSettings());
        long savedElapsed = model.getLevel().getElapsedMillis();
        timer.start(savedElapsed);
        startTimerTask();
    }

    private void openSettings() {
        saveTimerState();
        timer.stop();
        stopTimerTask();
        createSettingsDialog();
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
    private void resumeGame() {
        saveTimerState();
        refreshUI();
        mount();
        view.updateHint("Game continued.");
    }
    
    private void restartGame() {
        model.getLevel().setElapsedMillis(0);
        model.getLevel().restartLevel();
        refreshUI();
        mount();
        view.updateHint("Game restarted.");
    }
    
    private void refreshUI() {
        ctx.getRoot().setTop(view.createTop());
        ctx.getRoot().setCenter(view.createCenter());
    }
    private void onMainMenu() {
        saveTimerState();
        Platform.runLater(() -> {
            view.updateHint("Returning to main menu...");
            mainMenuController.mount();
        });
    }
    private void saveTimerState() {
        if (model != null) {
            long currentElapsed = timer.getElapsedMillis();
            model.getLevel().setElapsedMillis(currentElapsed);
        }
    }
    
    private void startTimerTask() {
        stopTimerTask();
        timerTask = new GameTimerTask(timer);
        timerTask.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                view.updateTimer(newVal);
            }
        });
        Thread timerThread = new Thread(timerTask);
        timerThread.setDaemon(true);
        timerThread.start();
    }
    
    private void stopTimerTask() {
        if (timerTask != null) {
            timerTask.stop();
            timerTask.cancel();
            timerTask = null;
        }
    }
    
    private void openVictoryDialog() {
        saveTimerState();
        timer.stop();
        stopTimerTask();
        view.updateHint("Победа!");
    }
    private void unlockNextLevel() {
        if (model != null) {
            int completedLevel = model.getLevel().getLevelNumber();
            mainMenuController.onLevelCompleted(completedLevel);
        }
    }
    
    private void attachCells() {
        for (int r = 0; r < Constants.GRID_SIZE; r++) {
            for (int c = 0; c < Constants.GRID_SIZE; c++) {
                TextField cell = view.getCellsGrid()[r][c];
                final int row = r, col = c;
                cell.setOnMouseClicked(e -> {
                    if (model.getLevel().getGameGrid()[row][col] == Constants.EMPTY_CELL) {
                        String validNumbers = SudokuUtils.getValidNumbersForCell(model.getLevel().getSaveGrid(), row, col);
                        view.updateHint("Valid numbers for this cell: " + validNumbers);
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
                    Pair<String, Boolean> result = SudokuUtils.isValueAvailable(grid, row, col, newVal);
                    if (result.getValue()) {
                        try {
                            int inputValue = Integer.parseInt(newVal);
                            if (inputValue >= Constants.MIN_VALUE && inputValue <= Constants.MAX_VALUE) {
                                grid[row][col] = inputValue;
                                view.updateHint("Valid number");
                                if (model.getLevel().isSudokuSolved()) {
                                    view.updateHint("✅ Congratulations! Level completed!");
                                    unlockNextLevel();
                                    openVictoryDialog();
                                }
                                view.render(model);
                            }
                        } catch (NumberFormatException ignore) {}
                    } else {
                        grid[row][col] = Constants.EMPTY_CELL;
                        cell.setText("");
                        view.updateHint("Invalid number: " + result.getKey());
                        view.render(model);
                    }
                });
            }
        }
    }
}
