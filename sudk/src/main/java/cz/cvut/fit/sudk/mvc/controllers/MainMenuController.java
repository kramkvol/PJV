package cz.cvut.fit.sudk.mvc.controllers;


import cz.cvut.fit.sudk.mvc.models.MainMenuModel;
import cz.cvut.fit.sudk.mvc.models.SudokuFieldModel;
import cz.cvut.fit.sudk.mvc.views.GameTools;
import cz.cvut.fit.sudk.mvc.views.MainMenuView;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public final class MainMenuController {
    private final AppContext ctx;
    private final MainMenuModel model;
    private final MainMenuView view;

    public MainMenuController(AppContext ctx) {
        this.ctx = ctx;
        this.model = new MainMenuModel();
        this.view = new MainMenuView(ctx);
    }

    public void mount() {
        view.mount();
        wire();
        view.render(model);
    }

    public void onLevelCompleted(int completedLevel) {
        model.getPlayerProgress().unlockNextLevel(completedLevel);
        view.render(model);
    }
    private void wire() {
        view.getBackBtn().setOnAction(e -> {
            model.prevPage();
            view.render(model);
        });
        view.getNextBtn().setOnAction(e -> {
            model.nextPage();
            view.render(model);
        });
        for (int i = 0; i < view.getLevelButtons().length; i++) {
            final int index = i;
            view.getLevelButtons()[i].setOnAction(e -> {
                if (index < model.levelsOnPage() && model.isLevelUnlocked(model.firstLevelNumber() + index)) {
                    model.selectLevel(index);
                    view.render(model);
                }
            });
        }
        view.getPlayBtn().setOnAction(e -> {
            int selectedLevel = model.getSelectedLevelNumber();
            if (selectedLevel > 0) {
                onPlayClicked(selectedLevel);
            }
            view.render(model);
        });
    }

    /*
     * Handles the "Play" button click.
     *
     * Flow:
     * 1. Immediately replace the current UI with a simple loading screen.
     *    This keeps the JavaFX Application Thread free and responsive
     *    while the level is being prepared.
     *
     * 2. Start a background Task<GameLevel> in a separate thread.
     *    - The heavy operation (LevelFactory.getOrCreateLevel) must NOT run
     *      on the JavaFX Application Thread, otherwise the UI would freeze.
     */
    private void onPlayClicked(int level) {

        final class LoadingView {
            private final AppContext ctx;

            public LoadingView(AppContext ctx) {
                this.ctx = ctx;
            }

            public void mount() {
                VBox centerBox = new VBox();
                centerBox.setAlignment(Pos.CENTER);
                centerBox.getChildren().add(GameTools.createLoadingLabel());

                ctx.getRoot().setTop(null);
                ctx.getRoot().setCenter(centerBox);
                ctx.getRoot().setBottom(null);
            }
        }

        LoadingView view = new LoadingView(ctx);
        view.mount();

        Task<GameLevel> loadLevelTask = getLoadLevelTask(level);
        new Thread(loadLevelTask).start();
    }

    private Task<GameLevel> getLoadLevelTask(int level) {
        Task<GameLevel> loadLevelTask = new Task<>() {
            @Override
            protected GameLevel call() throws Exception {
                System.out.println("This code is running in a new thread");
                return LevelFactory.getOrCreateLevel(level);
            }
        };

        loadLevelTask.setOnSucceeded(event -> {
            GameLevel loadedLevel = loadLevelTask.getValue();
            SudokuFieldModel model = new SudokuFieldModel(loadedLevel);
            SudokuFieldController sudokuFieldController = new SudokuFieldController(ctx, this);
            sudokuFieldController.setModel(model);
            sudokuFieldController.mount();
        });
        return loadLevelTask;
    }
}
