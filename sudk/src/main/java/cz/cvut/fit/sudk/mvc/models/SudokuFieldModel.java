package cz.cvut.fit.sudk.mvc.models;

import lombok.Getter;

public final class SudokuFieldModel {

    @Getter private final GameLevel level;

    public SudokuFieldModel(GameLevel level) { this.level = level; }

}
