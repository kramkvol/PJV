package cz.cvut.fit.sudk.mvc.models;

import lombok.Getter;

public final class MainMenuModel {

    private int page = Constants.DEFAULT_PAGE;
    @Getter
    private int selectedIndex = Constants.DEFAULT_SELECTED_INDEX;

    @Getter
    private final PlayerProgress playerProgress;

    public MainMenuModel() {
        this.playerProgress = new PlayerProgress();
    }

    public boolean isLevelUnlocked(int levelNumber) {
        return playerProgress.isLevelUnlocked(levelNumber);
    }

    public boolean canPrev() {
        return page > Constants.DEFAULT_PAGE;
    }
    public boolean canNext() {
        return page + 1 < pagesCount();
    }
    public int firstLevelNumber() {
        return page * Constants.LEVELS_PER_PAGE + 1;
    }
    public int levelsOnPage() {
        return Math.min(Constants.LEVELS_PER_PAGE, Constants.MAX_LEVELS_CLASSIC - page * Constants.LEVELS_PER_PAGE);
    }
    private int pagesCount() {
        int total = Constants.MAX_LEVELS_CLASSIC;
        return (total + Constants.LEVELS_PER_PAGE - 1) / Constants.LEVELS_PER_PAGE;
    }
    public void prevPage() {
        if (canPrev()) {
            page--;
            selectedIndex = Constants.DEFAULT_SELECTED_INDEX;
        }
    }
    public void nextPage() {
        if (canNext()) {
            page++;
            selectedIndex = Constants.DEFAULT_SELECTED_INDEX;
        }
    }
    public void selectLevel(int index) {
        if (index >= 0 && index < levelsOnPage()) {
            selectedIndex = index;
        }
    }
    public int getSelectedLevelNumber() {
        if (selectedIndex < 0) return Constants.DEFAULT_SELECTED_INDEX;
        return firstLevelNumber() + selectedIndex;
    }
}