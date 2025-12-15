package cz.cvut.fit.sudoku.mvc.models;

import cz.cvut.fit.sudoku.core.level.GameLevel;
import lombok.Getter;

public final class SudokuFieldModel {
    @Getter
    private final GameLevel level;

    public SudokuFieldModel(GameLevel level) {
        this.level = level;
    }
}