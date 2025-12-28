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


## Class Responsibilities (by Package)

### `cz.cvut.fit.sudk`

#### `SudokuApplication`
- JavaFX application entry point (`extends Application`)
- Initializes `Stage`, `Scene`, and root layout
- Creates `AppContext` and mounts the main menu view

---

### `cz.cvut.fit.sudk.mvc.controllers`

#### `AppContext`
- Stores shared application state
- Holds references to the primary `Stage` and root `BorderPane`
- Enables screen switching between menu and game views

#### `GameLevel`
- Represents a single Sudoku level
- Stores:
  - original grid
  - user-editable grid
  - elapsed game time
  - level metadata (level number, difficulty)
- Provides methods for resetting the level and checking completion

#### `LevelFactory`
- Responsible for creating and caching `GameLevel` instances
- Internally uses `List<GameLevel>` as a cache
- Ensures each level is created only once and reused

#### `MainMenuController`
- Handles user interactions in the main menu
- Coordinates communication between `MainMenuView` and `MainMenuModel`
- Starts background loading of game levels using `Task`
- Switches the active screen to the Sudoku game

#### `SudokuFieldController`
- Controls the main game logic
- Processes user input from the Sudoku grid
- Validates moves using `LevelModelUtils`
- Manages the game timer thread
- Detects level completion and unlocks the next level

### `cz.cvut.fit.sudk.mvc.models`

#### `Constants`
- Stores global configuration constants
- Eliminates magic numbers across the project

#### `LevelModelUtils`
- Utility class with static helper methods
- Handles:
  - deep copying of Sudoku grids
  - validation of user input
  - checking Sudoku rules (rows, columns, sub-grids)

#### `MainMenuModel`
- Stores the state of the main menu
- Tracks current page, selected level, and available levels
- Uses `PlayerProgress` to determine which levels are unlocked

#### `PlayerProgress`
- Stores player progression data
- Tracks the highest unlocked level
- Provides logic for unlocking new levels

#### `SudokuFieldModel`
- Holds the currently active `GameLevel`
- Acts as the model for the Sudoku game screen

### `cz.cvut.fit.sudk.mvc.views`

#### `GameTools`
- UI-related helper methods
- Shared functionality used by multiple views

#### `MainMenuView`
- JavaFX UI for the main menu screen
- Displays level selection buttons and navigation controls
- Renders UI state based on `MainMenuModel`

#### `SudokuFieldView`
- JavaFX UI for the Sudoku game screen
- Displays the Sudoku grid, timer, and game status
- Updates UI elements based on game state

## Resources

### `styles.css`
- JavaFX CSS stylesheet
- Defines the visual appearance of UI components

## Semester Requirements Fulfillment

### JavaFX
- UI implemented using JavaFX (`Stage`, `Scene`, layouts, controls)
- Event handling via controllers
- Screen switching using shared `AppContext`

### Java Collections
- `List<GameLevel>` and `ArrayList` used in `LevelFactory`
- Collections manage level instances and game state

### Multithreading & Concurrency

#### Background level loading
- Implemented using `javafx.concurrent.Task`
- Levels are loaded in a separate thread to avoid UI blocking

#### Game timer
- Implemented using a dedicated background thread
- UI updates executed safely using `Platform.runLater`

## Application Screens Overview

This section provides an overview of the main application screens and explains what is displayed on each of them.
All screenshots are stored in the `pictures/` directory and referenced directly from this README file.

### Main Menu Screen

![Main Menu Screen](pictures/main-menu.png)

The **Main Menu screen** is the first screen displayed after launching the application.

**Displayed elements:**
- list of available Sudoku levels
- navigation buttons for switching between pages of levels
- visual indication of locked and unlocked levels
- *Play* button to start the selected level

**Purpose:**
- allows the user to select a Sudoku level
- reflects the current player progress
- serves as the entry point to the game

### Sudoku Game Screen

![Sudoku Game Screen](pictures/sudoku-game.png)

The **Sudoku Game screen** represents the main gameplay interface.

**Displayed elements:**
- Sudoku grid implemented using editable text fields
- timer displaying elapsed game time
- game information and hints
- control buttons for navigation and settings

**Purpose:**
- allows the user to play the Sudoku game
- validates user input according to Sudoku rules
- updates the game state and detects level completion

### Level Loading Screen

![Loading Screen](pictures/loading.png)

The **Loading screen** is displayed while a Sudoku level is being loaded in the background.

**Purpose:**
- prevents UI freezing during level initialization
- demonstrates the use of `javafx.concurrent.Task`
- improves user experience by providing visual feedback


