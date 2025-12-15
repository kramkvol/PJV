package cz.cvut.fit.sudoku.core;

import lombok.Getter;

@Getter
public final class GameTimer {
    
    private long startTime;
    private boolean running;
    private long accumulatedMillis;
    
    public GameTimer() {
    }
    
    public void start(long alreadyElapsed) {
        accumulatedMillis = alreadyElapsed;
        startTime = System.currentTimeMillis();
        running = true;
    }
    
    public void stop() {
        if (running) {
            accumulatedMillis += System.currentTimeMillis() - startTime;
            running = false;
        }
    }
    
    public long getElapsedMillis() {
        return running
                ? accumulatedMillis + (System.currentTimeMillis() - startTime)
                : accumulatedMillis;
    }
    
    public String getFormattedTime() {
        long totalSec = getElapsedMillis() / 1000;
        long min = totalSec / 60;
        long sec = totalSec % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
