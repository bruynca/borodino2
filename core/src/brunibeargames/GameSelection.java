package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.io.File;

import brunibeargames.UI.EventConfirm;
import brunibeargames.UI.EventOK;
import brunibeargames.UI.EventPopUp;
import brunibeargames.UI.MouseImage;

public class GameSelection {
    static public GameSelection instance;


    private Label game1Label;
    private Label game2Label;
    private Label scenariosLabel;
    private Label scenarioDetailsLabel;
    private CheckBox humanVfrenchAICheckBox;
    private CheckBox humanVRussianAICheckBox;
    private CheckBox emailGermanCheckBox;
    private CheckBox emailAmericanCheckBox;
    private CheckBox hotseatCheckBox;
    private CheckBox easyRussian;
    private CheckBox balanced;

    private CheckBox easyFrench;
    private CheckBox sovietMandatoryAttacks;
    private Image backgroundImage;
    private Group group;
    private I18NBundle i18NBundle;
    private TextButton mainMenuButton;
    private TextButton playButton;
    public TextButton.TextButtonStyle textButtonStyle;
    private CheckBox.CheckBoxStyle checkBoxStyle;
    private ButtonGroup buttonGroupAI;
    private ButtonGroup buttonGroupSetUp;
//    private Setup setup;
    private ScrollPane scenariosScrollPane;
    private final TextTooltip.TextTooltipStyle tooltipStyle;


    public GameSelection(Stage stage, final Sound sound) {
        instance = this;
        EventOK eventOK = new EventOK();
        EventPopUp eventPopUp = new EventPopUp(Borodino.instance.guiStage);
        EventConfirm eventConfirm = new EventConfirm();
        group = new Group();
        group.setVisible(false);
        i18NBundle = GameMenuLoader.instance.localization;

        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);

        MouseImage mouseImage = new MouseImage();
        GameSetup gameSetup = new GameSetup();


        initializeStyles();
        initializeImageBackGround();
        initializeScenariosLabel();
        initializeScenariosScrollPane();
        initializeScenarioDetailsLabel();
        initializegame1Label();
        initializehumanVAmericanAICheckBox();
        initializeMainMenuButton();
        initializePlayButton(sound);
        initializeHotseatCheckBox();
        initializeHumanVGermanAICheckBox();
        initializeEasyRussianCheckBox();
        initializeBalancedCheckBox();
        initializeEasyFrenchCheckBox();
        initializeButtonGroups();

        stage.addActor(group);
    }

    public void show(boolean display, TextButton mainMenuButton) {
        this.mainMenuButton = mainMenuButton;
        mainMenuButton.setPosition(backgroundImage.getX() + 20, backgroundImage.getY() +  (10 / 1));

        group.setVisible(display);
    }

    public boolean isVisible() {
        return group.isVisible();
    }

    private void initializeStyles() {

        textButtonStyle = new TextButton.TextButtonStyle(new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("unselectedbutton"))),
                new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("selectedbutton"))),
                null,
                Fonts.getFont24());
        textButtonStyle.over = new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("selectedbutton")));

        checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("checkboxselected")));
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("checkbox")));
        checkBoxStyle.font = Fonts.getFont24();
    }

    private void initializeImageBackGround() {

 //       NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 5, 5, 5, 5);
        TextureRegion texp = GameMenuLoader.instance.gameMenu.asset.get("backimage");
        backgroundImage = new Image(texp);
 //       backgroundImage.setDrawable(new NinePatchDrawable(np));
        backgroundImage.setHeight(GameMenu.instance.getBackGroundHeight() / (1));
        backgroundImage.setWidth(1000 / (1));
        backgroundImage.setVisible(true);
        backgroundImage.setPosition((Gdx.graphics.getWidth() / 2) - (400 / 1),
                (Gdx.graphics.getHeight() / 2) - (250 / 1));


        group.addActor(backgroundImage);
    }

    private void initializeScenariosLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24();
        scenariosLabel = new Label(i18NBundle.get("scenarios"), style);
        scenariosLabel.setSize(281 / 1, 44 / 1);
        scenariosLabel.setPosition(backgroundImage.getX()  + 20, backgroundImage.getY() + backgroundImage.getHeight() - (50 / 1));
        scenariosLabel.setVisible(true);

        group.addActor(scenariosLabel);
    }

    private void initializeScenarioDetailsLabel() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24();
        scenarioDetailsLabel = new Label(i18NBundle.get("scenariodetail"), style);
        scenarioDetailsLabel.setSize(281 / 1, 44 / 1);
        scenarioDetailsLabel.setPosition(scenariosScrollPane.getX() + scenariosScrollPane.getWidth() +10, backgroundImage.getY() + backgroundImage.getHeight() - (40 / 1));
        scenarioDetailsLabel.setVisible(true);

        //group.addActor(scenarioDetailsLabel);
    }

    private void initializegame1Label() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24();
        game1Label = new Label(i18NBundle.get("scenario1"), style);
        game1Label.setSize(281 / 1, 44 / 1);
        game1Label.setPosition(scenariosScrollPane.getX() + scenariosScrollPane.getWidth() +10, backgroundImage.getY() + backgroundImage.getHeight() - (150 / 1));
        game1Label.setVisible(true);

        group.addActor(game1Label);
    }

    private void initializeMainMenuButton() {

        mainMenuButton = new TextButton(i18NBundle.get("mainmenu"), textButtonStyle);
        mainMenuButton.setSize(281 / 1, 44 / 1);
        mainMenuButton.setPosition(backgroundImage.getX() + ((backgroundImage.getWidth() / 2)), backgroundImage.getY() + (10 / 1));
        mainMenuButton.setVisible(true);

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    backgroundImage.setHeight(426 / (1));
                    backgroundImage.setWidth(474 / (1));
                    backgroundImage.setVisible(true);
                    backgroundImage.setPosition((Gdx.graphics.getWidth() / 2) - (239 / 1),
                            (Gdx.graphics.getHeight() / 2) - (208 / 1));
                    mainMenuButton.setPosition(backgroundImage.getX() + ((backgroundImage.getWidth() / 2)), backgroundImage.getY() + backgroundImage.getHeight() - ((410) / 1));

                }
            }
        });

        //group.addActor(mainMenuButton);
    }

    private void initializePlayButton(final Sound sound) {

        playButton = new TextButton(i18NBundle.get("play"), textButtonStyle);
        playButton.setSize(281 / 1, 44 / 1);
        playButton.setVisible(true);
        playButton.setPosition(backgroundImage.getX() + backgroundImage.getWidth() - (playButton.getWidth() + 20), backgroundImage.getY() + (10 / 1));
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                    SplashScreen.instance.end();
                    Game game = new Game("", false);
//                    sound.stop();
//                    game.showGameScreen(false);
                }
            }
        });


        group.addActor(playButton);
    }

    private void initializeEasyRussianCheckBox() {

        easyRussian = new CheckBox("  " + i18NBundle.get("easyrussian"), checkBoxStyle);
        easyRussian.setPosition(backgroundImage.getX() + backgroundImage.getWidth()/2 + (20 / 1), scenariosScrollPane.getY() - 80);
        easyRussian.setVisible(true);
        easyRussian.setChecked(true);

        easyRussian.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (easyRussian.isChecked()) {
                        GameSetup.instance.setEasyRussian(true);
                    } else {
                        GameSetup.instance.setEasyRussian(false);
                    }
                }
            }
        });

        group.addActor(easyRussian);

    }


    private void initializeBalancedCheckBox() {

        balanced = new CheckBox("  " + i18NBundle.get("balanced"), checkBoxStyle);
        balanced.setPosition(backgroundImage.getX() + backgroundImage.getWidth()/2 + (20 / 1), scenariosScrollPane.getY() - 130);
        balanced.setVisible(true);
        balanced.setChecked(false);
        GameSetup.instance.setEasyRussian(false);

        balanced.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (balanced.isChecked()) {
                        GameSetup.instance.setBalanced(true);
                    } else {
                        GameSetup.instance.setBalanced(false);
                    }
                }
            }
        });

        group.addActor(balanced);

    }

    private void initializeHotseatCheckBox() {

        hotseatCheckBox = new CheckBox("  " + i18NBundle.get("twoplayer"), checkBoxStyle);
        hotseatCheckBox.setPosition(backgroundImage.getX() + (20 / 1), scenariosScrollPane.getY() - 130);

        hotseatCheckBox.setVisible(true);
        hotseatCheckBox.setChecked(false);

        hotseatCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (hotseatCheckBox.isChecked()) {
                        GameSetup.instance.setHotSeatGame(true);
                        GameSetup.instance.setRussianVersusAI(false);
                        GameSetup.instance.setFrenchVersusAI(false);
                     //   MouseImage.instance.setMouseHotseat();

                    } else {
                        MouseImage.instance.mouseImageReset();
                        GameSetup.instance.setHotSeatGame(false);
                    }
                }
            }
        });


        group.addActor(hotseatCheckBox);

    }

    private void initializeHumanVGermanAICheckBox() {

 
        humanVRussianAICheckBox = new CheckBox("  " + i18NBundle.format("playas", i18NBundle.get("russian")), checkBoxStyle);
        humanVRussianAICheckBox.setPosition(backgroundImage.getX() + (20 / 1), scenariosScrollPane.getY() - 80);

        humanVRussianAICheckBox.setVisible(true);
        humanVRussianAICheckBox.setChecked(false);

        humanVRussianAICheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (humanVRussianAICheckBox.isChecked()) {
                        GameSetup.instance.setRussianVersusAI(true);
                        GameSetup.instance.setHotSeatGame(false);
                        GameSetup.instance.setFrenchVersusAI(false);
                        //MouseImage.instance.setMouseGerman();

                    } else {
                        MouseImage.instance.mouseImageReset();
                        GameSetup.instance.setRussianVersusAI(false);

                    }
                }
            }
        });


        group.addActor(humanVRussianAICheckBox);

    }

     private void initializehumanVAmericanAICheckBox() {

        humanVfrenchAICheckBox = new CheckBox("  " + i18NBundle.format("playas", i18NBundle.get("french")), checkBoxStyle);
        humanVfrenchAICheckBox.setPosition(backgroundImage.getX() + (20 / 1), scenariosScrollPane.getY() - 180);

        humanVfrenchAICheckBox.setVisible(true);
        humanVfrenchAICheckBox.setChecked(false);

        humanVfrenchAICheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (humanVfrenchAICheckBox.isChecked()) {
                        GameSetup.instance.setFrenchVersusAI(true);
                        GameSetup.instance.setRussianVersusAI(false);
                        GameSetup.instance.setHotSeatGame(false);
                        //MouseImage.instance.setMouseAmerican();


                    } else {
                        MouseImage.instance.mouseImageReset();
                        GameSetup.instance.setFrenchVersusAI(false);
                    }
                }
            }
        });


        group.addActor(humanVfrenchAICheckBox);

    }

    private void initializeEmailGermanCheckBox() {

        humanVfrenchAICheckBox = new CheckBox("  " + i18NBundle.format("playas", i18NBundle.get("american")), checkBoxStyle);
        humanVfrenchAICheckBox.setPosition(backgroundImage.getX() + (20 / 1), scenariosScrollPane.getY() - 180);

        humanVfrenchAICheckBox.setVisible(true);
        humanVfrenchAICheckBox.setChecked(false);

        humanVfrenchAICheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (humanVfrenchAICheckBox.isChecked()) {
                        GameSetup.instance.setFrenchVersusAI(true);
                        GameSetup.instance.setRussianVersusAI(false);
                        GameSetup.instance.setHotSeatGame(false);
                        MouseImage.instance.setMouseAmerican();


                    } else {
                        MouseImage.instance.mouseImageReset();
                        GameSetup.instance.setFrenchVersusAI(false);
                    }
                }
            }
        });


        group.addActor(humanVfrenchAICheckBox);

    }
    private void initializeEasyFrenchCheckBox() {


        easyFrench = new CheckBox("  " + i18NBundle.get("easyfrench"), checkBoxStyle);
        easyFrench.setPosition(backgroundImage.getX() + backgroundImage.getWidth()/2 + (20 / 1), scenariosScrollPane.getY() - 180);

        easyFrench.setVisible(true);
        easyFrench.setChecked(false);


        easyFrench.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (easyFrench.isChecked()) {
                        GameSetup.instance.setEasyFrench(true);
                    } else {
                        GameSetup.instance.setEasyFrench(false);
                    }
                }
            }
        });

  //      easyAmerican.addListener(new TextTooltip(
  //              i18NBundle.get("easyamericantooltip"),
  ///              tooltipStyle));

       group.addActor(easyFrench);

    }



    private void initializeScenariosScrollPane() {

//        stateEngine.setScenarioName("Full Campaign ");

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = Fonts.getFont24();
        listStyle.fontColorSelected.set(Color.WHITE);
        listStyle.fontColorUnselected.set(Color.GRAY);
        listStyle.selection = new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("unselectedbutton")));
        listStyle.down = new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("selectedbutton")));
        listStyle.selection.setTopHeight(5);
        listStyle.selection.setBottomHeight(5);
        listStyle.selection.setLeftWidth(5);
        listStyle.selection.setRightWidth(5);
        listStyle.selection.setMinWidth(400);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 5, 5, 5, 5);
        listStyle.background = new NinePatchDrawable(np);
        final List<String> list = new List<>(listStyle);
        File folder = GamePreferences.getSaveGamesLocation().file();
        int filecount = 0;
        String[] strings = new String[4];
        strings[0] = "September 5th";
        strings[1] = "September 6th";
        strings[2] = "September 7th";
        strings[3] = "Free Setup";
 //       strings[3] = "CounterAttack";

        list.setItems(strings);

        scenariosScrollPane = new ScrollPane(list);
        scenariosScrollPane.setForceScroll(true, false);
        scenariosScrollPane.setWidth(300);
        scenariosScrollPane.setHeight(150);
        //scrollPane.setBounds(0, 0, 281/1, backGroundImage.getHeight() - 20);
        scenariosScrollPane.setSmoothScrolling(false);
        scenariosScrollPane.setPosition(backgroundImage.getX() + 20 , backgroundImage.getY() + backgroundImage.getHeight() - ((200) / 1));
        scenariosScrollPane.setVisible(true);

        scenariosScrollPane.getActor().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    List<String> list = (List<String>) scenariosScrollPane.getActor();
                    String choice = list.getSelected();
                    if (choice.equals("September 5th")){
                        game1Label.setText(i18NBundle.get("scenario1"));
                        GameSetup.instance.setScenario(GameSetup.Scenario.Sept5);
                    }else if (choice.equals("September 6th")){
                        list.setSelected("September 6th");
                        game1Label.setText(i18NBundle.get("scenario2"));
                        GameSetup.instance.setScenario(GameSetup.Scenario.Sept6);
                    }else if (choice.equals("September 7th")){
                        list.setSelected("September 7th");
                        game1Label.setText(i18NBundle.get("scenario3"));
                        GameSetup.instance.setScenario(GameSetup.Scenario.Sept5);
                    }else if (choice.equals("Free Setup")){
                        list.setSelected("Free Setup");
                        game1Label.setText(i18NBundle.get("scenario4"));
                        GameSetup.instance.setScenario(GameSetup.Scenario.Free);
                    }else{
                        EventOK.instance.show(i18NBundle.get("notavailable"));
                        list.setSelected("Introduction");
                        return;

//                        game1Label.setText(i18NBundle.get("scenario4"));
//                        GameSetup.instance.setScenario(GameSetup.Scenario.CounterAttack);
                    }
                }
            }
        });

        group.addActor(scenariosScrollPane);
    }

    private void initializeButtonGroups() {
        buttonGroupAI = new ButtonGroup();
        buttonGroupAI.add(hotseatCheckBox);
        buttonGroupAI.add(humanVRussianAICheckBox);
        buttonGroupAI.add(humanVfrenchAICheckBox);
        buttonGroupAI.setMaxCheckCount(1);
        humanVRussianAICheckBox.setChecked(true);
        GameSetup.instance.setRussianVersusAI(true);

        buttonGroupSetUp = new ButtonGroup();
        buttonGroupSetUp.add(easyRussian);
        buttonGroupSetUp.add(balanced);
        buttonGroupSetUp.add(easyFrench);
        buttonGroupSetUp.setMaxCheckCount(1);
        easyRussian.setChecked(true);
        GameSetup.instance.setEasyRussian(true);

    }
}
