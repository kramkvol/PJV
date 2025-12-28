# Sudoku - Semester Project

## Description

This project is a **Sudoku game implemented using JavaFX** and developed as a **semester project**.
The main goal of the project is to demonstrate:

- work with **JavaFX UI**
- usage of **Java Collections **
- **multithreading / JavaFX Task / concurrency**
- clean architecture with clear separation of responsibilities

## Application Start

**Entry point:**
- `SudokuApplication.java`

After launching the application, the main menu is displayed, where the user can select a level and start the game.

## Project Structure

```
└─ cz.cvut.fit.sudk
   ├─ mvc
   │  ├─ controllers
   │  │  ├─ AppContext.java
   │  │  ├─ GameLevel.java
   │  │  ├─ LevelFactory.java
   │  │  ├─ MainMenuController.java
   │  │  └─ SudokuFieldController.java
   │  │
   │  ├─ models
   │  │  ├─ Constants.java
   │  │  ├─ LevelModelUtils.java
   │  │  ├─ MainMenuModel.java
   │  │  ├─ PlayerProgress.java
   │  │  └─ SudokuFieldModel.java
   │  │
   │  └─ views
   │     ├─ GameTools.java
   │     ├─ MainMenuView.java
   │     └─ SudokuFieldView.java
   │
   ├─ SudokuApplication.java
   └─ module-info.java

src/main/resources
└─ cz.cvut.fit.sudk
   └─ styles.css
```

## Architecture Overview

The project follows an **MVC-based architecture**:

- **Model** – stores application data and game state
- **View** – JavaFX UI components
- **Controller** – handles user input and coordinates Model–View interaction

Additional utility and factory classes support the core logic.


## Class Responsibilities

### Application Layer

#### `SudokuApplication`
- JavaFX application entry point
- Initializes `Stage`, `Scene`, and root layout
- Creates and mounts the main menu


### Application Context

#### `AppContext`
- Stores shared application state
- Holds references to the primary `Stage` and root `BorderPane`
- Allows controllers to switch screens without tight coupling


### Configuration

#### `Constants`
- Stores global constants used across the application
- Prevents magic numbers and improves maintainability

### Game Logic

#### `GameLevel`
- Represents a single Sudoku level
- Stores original grid, user progress grid, elapsed time, and level metadata
- Provides methods to reset and check level completion

#### `LevelFactory`
- Responsible for creating and caching game levels
- Uses `List<GameLevel>` to store already created levels
- Ensures levels are created only once

#### `LevelModelUtils`
- Utility class with static methods
- Handles grid copying, validation, and Sudoku rules checking
- Used by controllers to validate user input

#### `PlayerProgress`
- Stores player progress data
- Manages unlocked levels
- Determines whether a level is accessible

### Main Menu (MVC)

#### `MainMenuModel`
- Stores menu state (current page, selected level)
- Uses `PlayerProgress` to determine unlocked levels

#### `MainMenuView`
- JavaFX UI for the main menu
- Contains buttons for level selection and navigation
- Updates UI based on model state

#### `MainMenuController`
- Handles menu interactions
- Starts background level loading
- Switches between menu and game screens


### Sudoku Game Screen (MVC)

#### `SudokuFieldModel`
- Stores the currently active `GameLevel`

#### `SudokuFieldView`
- JavaFX UI for the Sudoku board
- Uses a grid of `TextField` elements
- Displays timer and game information

#### `SudokuFieldController`
- Handles user input in the Sudoku grid
- Validates moves using `LevelModelUtils`
- Controls game flow and level completion
- Manages background timer thread

## Semester Requirements Fulfillment

### JavaFX
- UI implemented using JavaFX (`Stage`, `Scene`, layouts, controls)
- Event handling in controllers
- Screen switching via shared application context

### Java Collections
- `List<GameLevel>` and `ArrayList` used in `LevelFactory`
- Collections manage levels and application state

### Multithreading & Concurrency
- Background level loading using `javafx.concurrent.Task`
- Separate timer thread for game time tracking
- UI updates executed safely using `Platform.runLater`
