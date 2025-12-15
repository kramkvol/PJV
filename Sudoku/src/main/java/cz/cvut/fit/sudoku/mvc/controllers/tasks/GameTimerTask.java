package cz.cvut.fit.sudoku.mvc.controllers.tasks;

import cz.cvut.fit.sudoku.core.GameTimer;
import cz.cvut.fit.sudoku.mvc.models.Constants;
import javafx.concurrent.Task;

public class GameTimerTask extends Task<String> {
    
    private final GameTimer timer;
    private volatile boolean running = true;
    
    public GameTimerTask(GameTimer timer) {
        this.timer = timer;
    }
    
    @Override
    protected String call() throws Exception {
        while (running && !isCancelled()) {
            updateValue(timer.getFormattedTime());
            Thread.sleep(Constants.TIMER_UPDATE_INTERVAL_MS);
        }
        return timer.getFormattedTime();
    }
    
    public void stop() {
        running = false;
    }
}
