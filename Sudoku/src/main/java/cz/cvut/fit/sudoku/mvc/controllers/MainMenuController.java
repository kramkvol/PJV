package cz.cvut.fit.sudoku.mvc.controllers;

import cz.cvut.fit.sudoku.mvc.models.MainMenuModel;
import cz.cvut.fit.sudoku.mvc.views.MainMenuView;
import cz.cvut.fit.sudoku.mvc.views.PreloaderView;
import cz.cvut.fit.sudoku.mvc.models.SudokuFieldModel;
import cz.cvut.fit.sudoku.mvc.controllers.tasks.LevelLoadingTask;

public final class MainMenuController {
    private final AppContext ctx;
    private final SudokuFieldController sudokuFieldController;
    private final MainMenuModel model;
    private final MainMenuView view;
    private final PreloaderView preloaderView;

    public MainMenuController(AppContext ctx, SudokuFieldController sudokuFieldController) {
        this.ctx = ctx;
        this.sudokuFieldController = sudokuFieldController;
        this.model = new MainMenuModel();
        this.view = new MainMenuView(ctx);
        this.preloaderView = new PreloaderView(ctx);
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
    
    private void onPlayClicked(int level) {
        LevelLoadingTask loadingTask = new LevelLoadingTask(level);
        
        // Используем вспомогательный класс для интеграции задачи с прелоадером
        PreloaderTaskHelper.executeWithPreloader(
            preloaderView,
            loadingTask,
            // Обработчик успешного завершения
            fieldModel -> {
                sudokuFieldController.setModel(fieldModel);
                sudokuFieldController.mount();
            },
            // Обработчик ошибки
            exception -> {
                preloaderView.updateMessage("Failed to load level. Returning to menu...");
                mount();
            }
        );
    }
}
