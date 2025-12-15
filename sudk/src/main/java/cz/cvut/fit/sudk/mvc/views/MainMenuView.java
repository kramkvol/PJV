package cz.cvut.fit.sudk.mvc.views;

import cz.cvut.fit.sudk.mvc.controllers.AppContext;
import cz.cvut.fit.sudk.mvc.models.Constants;
import cz.cvut.fit.sudk.mvc.models.MainMenuModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.stream.IntStream;


public final class MainMenuView {

    private final AppContext ctx;

    @Getter
    private final Button playBtn;
    @Getter
    private final Button backBtn;
    @Getter
    private final Button nextBtn;
    @Getter
    private final Button[] levelButtons = new Button[Constants.LEVELS_PER_PAGE];

    public MainMenuView(AppContext ctx) {
        this.ctx = ctx;
        playBtn = GameTools.createDefaultButton("Play");
        backBtn = GameTools.createDefaultButton("Back");
        nextBtn = GameTools.createDefaultButton("Next");

        IntStream.range(0, levelButtons.length).forEach(i -> {
            levelButtons[i] = GameTools.createLevelSelectButton(Integer.toString(i));
        });
    }

    public void mount() {
        ctx.getStage().setTitle("Main Menu");
        
        VBox levelsBox = makeLevelsGrid(5);
        levelsBox.setAlignment(Pos.CENTER);
        
        VBox centerBox = new VBox(20, levelsBox);
        centerBox.setAlignment(Pos.CENTER);
        
        VBox bottomBox = new VBox(20, row(20, backBtn, nextBtn), playBtn);
        bottomBox.setAlignment(Pos.CENTER);

        ctx.getRoot().setTop(null);
        ctx.getRoot().setCenter(centerBox);
        ctx.getRoot().setBottom(bottomBox);
    }

    private HBox row(double spacing, Button... buttons) {
        HBox box = new HBox(spacing, buttons);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox makeLevelsGrid(int cols) {
        VBox levelsBox = new VBox(10);
        levelsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < levelButtons.length; i += cols) {
            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER);
            for (int j = i; j < i + cols && j < levelButtons.length; j++) {
                row.getChildren().add(levelButtons[j]);
            }
            levelsBox.getChildren().add(row);
        }
        return levelsBox;
    }

    public void render(MainMenuModel model) {
        GameTools.setButtonEnabled(backBtn, model.canPrev());
        GameTools.setButtonEnabled(nextBtn, model.canNext());

        // Reset all level buttons
        for (Button b : levelButtons) {
            b.setText("?");
            GameTools.setButtonEnabled(b, false);
            GameTools.setButtonSelected(b, false);
        }

        // Update visible level buttons
        for (int i = 0; i < model.levelsOnPage() && i < levelButtons.length; i++) {
            Button b = levelButtons[i];
            int levelNumber = model.firstLevelNumber() + i;
            b.setText(String.valueOf(levelNumber));
            GameTools.setButtonEnabled(b, model.isLevelUnlocked(levelNumber));
            GameTools.setButtonSelected(b, false);
        }

        // Mark selected button
        int selectedIndex = model.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < model.levelsOnPage()) {
            Button selectedButton = levelButtons[selectedIndex];
            GameTools.setButtonEnabled(selectedButton, true);
            GameTools.setButtonSelected(selectedButton, true);
        }
    }
}