package cz.cvut.fit.sudoku.mvc;

import cz.cvut.fit.sudoku.core.level.GameLevel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LevelFactory {
    private static final Map<Integer, GameLevel> LEVEL_CACHE = new ConcurrentHashMap<>();
    private LevelFactory() {}

    public static GameLevel getOrCreateLevel(int levelNumber) {
        if (levelNumber < 1) throw new IllegalArgumentException("Level number must be >=1");
        return LEVEL_CACHE.computeIfAbsent(levelNumber, GameLevel::new);
    }
}
