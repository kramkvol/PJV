package cz.cvut.fit.sudk.mvc.controllers;

import cz.cvut.fit.sudk.mvc.models.GameLevel;

import java.util.ArrayList;
import java.util.List;

public final class LevelFactory {

    private static final List<GameLevel> LEVELS = new ArrayList<>();

    private LevelFactory() {}

    static GameLevel getOrCreateLevel(int levelNumber) {
        if (levelNumber < 1)
            throw new IllegalArgumentException("Level number must be >= 1");

        int index = levelNumber - 1;

        if (index < LEVELS.size()) {
            return LEVELS.get(index);
        }

        while (LEVELS.size() <= index) {
            int nextLevelNumber = LEVELS.size() + 1;
            LEVELS.add(new GameLevel(nextLevelNumber));
        }

        return LEVELS.get(index);
    }
}
