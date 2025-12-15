package cz.cvut.fit.sudoku.mvc.controllers;

import cz.cvut.fit.sudoku.mvc.views.PreloaderView;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Вспомогательный класс для интеграции задач с прелоадерами.
 * Упрощает процесс показа прелоадера во время выполнения задач.
 */
public final class PreloaderTaskHelper {
    private PreloaderTaskHelper() {}

    /**
     * Запускает задачу с прелоадером.
     * Автоматически показывает прелоадер, обновляет сообщения и прогресс,
     * и скрывает прелоадер по завершении.
     *
     * @param preloaderView прелоадер для отображения
     * @param task задача для выполнения
     * @param onSuccess обработчик успешного завершения
     * @param onFailure обработчик ошибки
     * @param <T> тип результата задачи
     */
    public static <T> void executeWithPreloader(
            PreloaderView preloaderView,
            Task<T> task,
            java.util.function.Consumer<T> onSuccess,
            java.util.function.Consumer<Throwable> onFailure) {
        
        // Показываем прелоадер
        Platform.runLater(() -> {
            preloaderView.show();
            if (task.getMessage() != null) {
                preloaderView.updateMessage(task.getMessage());
            }
        });

        // Обновляем сообщение прелоадера по мере выполнения задачи
        task.messageProperty().addListener((obs, oldMsg, newMsg) -> {
            Platform.runLater(() -> {
                if (newMsg != null) {
                    preloaderView.updateMessage(newMsg);
                }
            });
        });

        // Обновляем прогресс прелоадера
        task.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            Platform.runLater(() -> {
                if (newProgress != null) {
                    preloaderView.updateProgress(newProgress.doubleValue());
                }
            });
        });

        // Обработка успешного завершения
        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                try {
                    T result = task.get();
                    if (onSuccess != null) {
                        onSuccess.accept(result);
                    }
                } catch (Exception ex) {
                    if (onFailure != null) {
                        onFailure.accept(ex);
                    }
                }
            });
        });

        // Обработка ошибки
        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                Throwable exception = task.getException();
                if (onFailure != null) {
                    onFailure.accept(exception);
                }
            });
        });

        // Запускаем задачу в отдельном потоке
        Thread taskThread = new Thread(task);
        taskThread.setDaemon(true);
        taskThread.start();
    }
}

