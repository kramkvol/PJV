package cz.cvut.fit.sudoku.mvc.models;

public final class PlayerProgress {
    
    private int maxUnlockedLevel;
    
    public PlayerProgress() {
        this.maxUnlockedLevel = 1;
    }
    
    public void unlockNextLevel(int completedLevel) {
        if (completedLevel >= maxUnlockedLevel) {
            int nextLevel = completedLevel + 1;
            if (nextLevel <= Constants.MAX_LEVELS_CLASSIC) {
                maxUnlockedLevel = nextLevel;
            }
        }
    }
    
    public boolean isLevelUnlocked(int levelNumber) {
        return levelNumber <= maxUnlockedLevel;
    }
}
