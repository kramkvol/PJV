# Интеграция паттернов проектирования в Sudoku

Этот проект демонстрирует интеграцию четырех основных паттернов проектирования в игре Sudoku:

## 🏗️ Интегрированные паттерны

### 1. **Builder Pattern** - Создание конфигурации
- **Файлы**: `MainMenuConfiguration.java`, `GameFieldConfiguration.java`, `LevelBuilder.java`
- **Использование**: Создание отдельных конфигураций для главного меню и игрового поля
- **Интеграция**: В `SudokuApplication.start()` и `SudokuFieldController` для настройки компонентов

```java
// Конфигурация главного меню
MainMenuConfiguration menuConfig = new MainMenuConfiguration.Builder()
    .setTitle("Sudoku — Main Menu")
    .setWindowWidth(500)
    .setWindowHeight(700)
    .setBackgroundColor("#FFB366")
    .setLevelsPerPage(9)
    .setEnableModeSwitching(true)
    .build();

// Конфигурация игрового поля
GameFieldConfiguration fieldConfig = new GameFieldConfiguration.Builder()
    .setTitle("Sudoku Game")
    .setShowTimer(true)
    .setShowHints(true)
    .setMaxUndoHistory(100)
    .setMaxRedoHistory(50)
    .build();
```

### 2. **Factory Pattern** - Создание уровней
- **Файл**: `LevelFactory.java`
- **Использование**: Создание и кэширование уровней игры
- **Интеграция**: В `MainMenuModel.onPlayClicked()` для загрузки уровней

```java
// Создание уровня через фабрику
BaseLevel level = LevelFactory.getOrCreateLevel("classic", levelNumber);

// Проверка кэша
boolean isCached = LevelFactory.isLevelCached("classic", levelNumber);
```

### 3. **Strategy Pattern** - Выбор игрового режима
- **Файл**: `GameModeStrategy.java`, `ClassicModeStrategy.java`, `KillerModeStrategy.java`
- **Использование**: Определение поведения для разных режимов игры
- **Интеграция**: В `MainMenuController` для переключения режимов

```java
// Переключение стратегии
GameModeStrategy strategy = new ClassicModeStrategy();
String difficulty = strategy.getLevelDifficulty(levelNumber);
int recommendedTime = strategy.getRecommendedTime(levelNumber);
boolean supportsHints = strategy.supportsFeature("hints");
```

### 4. **Command Pattern** - Выполнение команд
- **Файл**: `GameCommand.java`, `MoveCommand.java`, `CommandInvoker.java`
- **Использование**: Выполнение, отмена и повтор команд
- **Интеграция**: В `SudokuFieldController` для управления ходами

```java
// Выполнение команды
MoveCommand moveCommand = new MoveCommand(level, row, col, newValue, oldValue);
boolean success = commandInvoker.executeCommand(moveCommand);

// Отмена команды
boolean undoSuccess = commandInvoker.undoLastCommand();

// Повтор команды
boolean redoSuccess = commandInvoker.redoLastCommand();
```

## 🔄 Взаимодействие паттернов

### Сценарий игры:
1. **Builder** создает конфигурацию приложения
2. **Strategy** определяет режим игры (Classic/Killer)
3. **Factory** создает уровень для выбранного режима
4. **Command** выполняет ходы игрока

### Пример интеграции:
```java
// 1. Builder - конфигурации
MainMenuConfiguration menuConfig = new MainMenuConfiguration.Builder()
    .forClassicMode()
    .setLevelsPerPage(9)
    .build();

GameFieldConfiguration fieldConfig = new GameFieldConfiguration.Builder()
    .forClassicMode()
    .forBeginners()
    .setMaxUndoHistory(100)
    .build();

// 2. Strategy - режим
GameModeStrategy strategy = new ClassicModeStrategy();

// 3. Factory - уровень
BaseLevel level = LevelFactory.getOrCreateLevel("classic", 1);

// 4. Command - ход
MoveCommand move = new MoveCommand(level, 0, 0, 5, 0);
commandInvoker.executeCommand(move);
```

## 📁 Структура файлов

```
src/main/java/cz/cvut/fit/sudoku/patterns/
├── builder/
│   ├── MainMenuConfiguration.java
│   ├── GameFieldConfiguration.java
│   └── LevelBuilder.java
├── command/
│   ├── GameCommand.java
│   ├── MoveCommand.java
│   ├── CommandInvoker.java
│   ├── RestartLevelCommand.java
│   └── PauseResumeCommand.java
├── factory/
│   └── LevelFactory.java
└── strategy/
    ├── GameModeStrategy.java
    ├── ClassicModeStrategy.java
    └── KillerModeStrategy.java
```

## 🚀 Запуск примера

```bash
# Компиляция
javac -cp "lib/*" src/main/java/cz/cvut/fit/sudoku/examples/IntegratedPatternsExample.java

# Запуск
java -cp "src/main/java:lib/*" cz.cvut.fit.sudoku.examples.IntegratedPatternsExample
```

## ✨ Преимущества интеграции

1. **Модульность**: Каждый паттерн решает свою задачу
2. **Расширяемость**: Легко добавлять новые режимы, команды, события
3. **Тестируемость**: Каждый компонент можно тестировать отдельно
4. **Читаемость**: Код структурирован и понятен
5. **Производительность**: Кэширование уровней, оптимизированные события

## 🔧 Настройка

Все паттерны настраиваются через:
- **Builder**: Параметры конфигурации
- **Factory**: Размер кэша, типы уровней
- **Strategy**: Поведение режимов
- **Command**: Размер истории команд

Все паттерны интегрированы в основную логику приложения и работают совместно для обеспечения гибкой и расширяемой архитектуры игры Sudoku.
