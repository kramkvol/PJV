package cz.cvut.fit.sudk.mvc.views;

import cz.cvut.fit.sudk.mvc.controllers.AppContext;
import cz.cvut.fit.sudk.mvc.models.Constants;
import cz.cvut.fit.sudk.mvc.models.SudokuFieldModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;


public final class SudokuFieldView {
    private final AppContext ctx;
    @Getter private final Button settingsBtn;
    @Getter private final Label infoLabel;
    @Getter private final Label hintLabel;
    private final Label timerLabel;
    @Getter private final TextField[][] cellsGrid;

    public SudokuFieldView(AppContext ctx) {
        this.ctx = ctx;
        settingsBtn = GameTools.createDefaultButton("Settings");
        infoLabel = GameTools.createInfoLabel("Loading...");
        timerLabel = GameTools.createTimerLabel("Time\n");
        hintLabel = GameTools.createHintLabel("Hints will appeared here...");
        cellsGrid = new TextField[Constants.GRID_SIZE][Constants.GRID_SIZE];

        for (int r = 0; r < Constants.GRID_SIZE; r++) {
            for (int c = 0; c < Constants.GRID_SIZE; c++) {
                cellsGrid[r][c] = GameTools.createSudokuCell();
            }
        }
    }

    public void mount() {
        ctx.getStage().setTitle("Sudoku Field");
        ctx.getRoot().setTop(createTop());
        ctx.getRoot().setCenter(createCenter());
        ctx.getRoot().setBottom(createBottom());
    }

    public VBox createTop() {
        HBox h1 = new HBox(settingsBtn);
        h1.setAlignment(Pos.CENTER);
        h1.setSpacing(20);
        HBox h2 = new HBox(timerLabel, infoLabel);
        h2.setAlignment(Pos.CENTER);
        h2.setSpacing(20);
        VBox box = new VBox(h1, h2, hintLabel);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        return box;
    }
    public VBox createCenter() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(Constants.GRID_GAP);
        mainGrid.setVgap(Constants.GRID_GAP);
        mainGrid.setAlignment(Pos.CENTER);
        for (int squareRow = 0; squareRow < Constants.BOX_SIZE; squareRow++) {
            for (int squareCol = 0; squareCol < Constants.BOX_SIZE; squareCol++) {
                GridPane squareGrid = new GridPane();
                squareGrid.setHgap(Constants.CELL_SPACING);
                squareGrid.setVgap(Constants.CELL_SPACING);
                for (int r = 0; r < Constants.BOX_SIZE; r++) {
                    for (int c = 0; c < Constants.BOX_SIZE; c++) {
                        int globalRow = squareRow * Constants.BOX_SIZE + r;
                        int globalCol = squareCol * Constants.BOX_SIZE + c;
                        squareGrid.add(cellsGrid[globalRow][globalCol], c, r);
                    }
                }
                mainGrid.add(squareGrid, squareCol, squareRow);
            }
        }
        HBox wrap = new HBox(mainGrid);
        wrap.setAlignment(Pos.CENTER);
        VBox v = new VBox(wrap);
        v.setAlignment(Pos.CENTER);
        return v;
    }
    public VBox createBottom() {
        return new VBox();
    }
    public void render(SudokuFieldModel model) {
        infoLabel.setText(model.getLevel().getModeName() + "\nLevel: " + model.getLevel().getLevelNumber());
        int[][] gridSave = model.getLevel().getSaveGrid();
        int[][] gridGame = model.getLevel().getGameGrid();
        for (int r = 0; r < Constants.GRID_SIZE; r++) {
            for (int c = 0; c < Constants.GRID_SIZE; c++) {
                if(gridSave[r][c] == Constants.EMPTY_CELL){
                    cellsGrid[r][c].setText("");
                } else {
                    cellsGrid[r][c].setText(String.valueOf(gridSave[r][c]));
                }
                GameTools.setCellLocked(cellsGrid[r][c], gridGame[r][c] != Constants.EMPTY_CELL);
            }
        }
    }
    public void updateTimer(String formatted) {
        timerLabel.setText("Time\n" + formatted);
    }
    public void updateHint(String message) {
        hintLabel.setText(message);
    }
}