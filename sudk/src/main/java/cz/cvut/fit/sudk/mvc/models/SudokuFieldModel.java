package cz.cvut.fit.sudk.mvc.models;

import cz.cvut.fit.sudk.mvc.controllers.GameLevel;
import javafx.util.Pair;

public final class SudokuFieldModel {

    private final GameLevel level;

    public SudokuFieldModel(GameLevel level) { this.level = level; }

    public GameLevel getLevel() { return level; }
}
