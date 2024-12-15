package brunibeargames.UI;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.LoadGamePopUp;

public class MainMenu {

    private Image backGroundImage;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton loadSavedGameButton;
    private TextButton exitButton;
    private TextButton creditsButton;
    private TextButton gameOptionsButton;
    private TextButton optionsButton;

    private CheckBox resetHelp;
    private SelectBox windowsSizeSelectBox;
    private Label windowTextLabel;
    private ButtonGroup buttonGroup;
    private boolean exiting;
    private Stage stage;
    private float screenWidth;
    private float screenHeight;
    private LoadGamePopUp loadGamePopUp;
    private I18NBundle i18NBundle;
    private TextButton.TextButtonStyle textButtonStyle;
    private CheckBox.CheckBoxStyle checkBoxStyle;
    private final int backGroundHeight = 500;
    public static MainMenu instance;
    public MainMenu(Stage stage , Sound sound, TextureAtlas atlas, float screenHeight, float screenWidth){
        this.stage = stage;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        instance = this;

        i18NBundle= GameMenuLoader.instance.localization;
        Fonts.loadFont();
        loadGamePopUp = new LoadGamePopUp(stage);
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            Fonts.setScale(8.0f);
            //         Fonts.setScale(.1f);
        }
        initializeStyles(atlas);
        initializeImageBackGround();
        initializeNewGameButton(screenHeight,  sound );
        initializeLoadSavedGameButton();
        initializeOptionsButton();
        initializeGameOptionsButton();
        initializeExitButton();


    }

    private void initializeImageBackGround() {

        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"),5, 5, 5, 5);
        backGroundImage = new Image();
        backGroundImage.setDrawable(new NinePatchDrawable(np));
//        backGroundImage.setHeight(900 / (1));
//        backGroundImage.setWidth(900 / (1));
        backGroundImage.setHeight(486 / (1));
        backGroundImage.setWidth(474 / (1));
        backGroundImage.setVisible(true);
        backGroundImage.setPosition((screenWidth / 2) - (239 / 1),
                (screenHeight / 2) - (243 / 1));


        stage.addActor(backGroundImage);
    }
    private void initializeStyles(TextureAtlas atlas){


        textButtonStyle = new TextButton.TextButtonStyle(new TextureRegionDrawable(new TextureRegion(atlas.findRegion("unselectedbutton"))),
                new TextureRegionDrawable(new TextureRegion(atlas.findRegion("selectedbutton"))),
                null,
                Fonts.getFont24());
        textButtonStyle.over = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("selectedbutton")));
        if(!Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            textButtonStyle.font.getData().scale(0);
        }


        checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("checkboxselected")));
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(new TextureRegion(GameMenuLoader.instance.gameMenu.asset.get("checkbox")));
        checkBoxStyle.font = Fonts.getFont24();


    }

    private void initializeNewGameButton( float screenHeight,  final Sound sound){

 //       gameSelection = new GameSelection(stage,  sound);
 // new Scenario
        newGameButton = new TextButton(i18NBundle.get("newgame"),textButtonStyle);

        newGameButton.setSize(281/1, 44/1);
        newGameButton.setPosition(0,screenHeight);
        newGameButton.setPosition(backGroundImage.getX() + (((backGroundImage.getWidth()/2) - (newGameButton.getWidth()/2)) ), backGroundImage.getY() + backGroundImage.getHeight() - ((100) / 1));

        newGameButton.setVisible(true);
        newGameButton.setTransform(true);
        //    newGameButton.setScale(.2f);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    backGroundImage.setHeight(backGroundHeight / (1));
                    backGroundImage.setWidth(800 / (1));
                    backGroundImage.setVisible(true);
                    backGroundImage.setPosition((Gdx.graphics.getWidth() / 2) - (400 / 1),
                            (Gdx.graphics.getHeight() / 2) - (250 / 1));
 //                   hide();
 //                   mainMenuButton.setPosition(backGroundImage.getX() + 20, backGroundImage.getY() +  (10 / 1));
 //                   gameSelection.show(true);
                }
            }
        });

        stage.addActor(newGameButton);

    }

    private void initializeLoadSavedGameButton(){

        loadSavedGameButton = new TextButton(i18NBundle.get("loadsavedgame"),textButtonStyle);
        loadSavedGameButton.setSize(281/1, 44/1);
        loadSavedGameButton.setPosition(0,screenHeight);
        loadSavedGameButton.setPosition(backGroundImage.getX() + ((backGroundImage.getWidth()/2) - (loadSavedGameButton.getWidth()/2)), backGroundImage.getY() + backGroundImage.getHeight() - ((160) / 1));

        loadSavedGameButton.setVisible(true);

        loadSavedGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                }
            }
        });

        stage.addActor(loadSavedGameButton);

    }

    private void initializeLoadGameButton( final Sound sound){

        loadGameButton = new TextButton(i18NBundle.get("loadgame"),textButtonStyle);
        loadGameButton.setSize(281/1, 44/1);
        loadGameButton.setPosition(0,screenHeight);
        loadGameButton.setPosition(backGroundImage.getX() + ((backGroundImage.getWidth()/2) - (exitButton.getWidth()/2)), backGroundImage.getY() + backGroundImage.getHeight() - ((460) / 1)); // 360 prev
        loadGameButton.setVisible(false);

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                if (!event.getType().equals("touchUp")) {
//                    if (strGame.length() > 0) {
//                        SplashScreen.instance.end();
//                        EventPopUp.instance.show(i18NBundle.format("loadsavegame"));
//                        Game game = new Game(strGame, false);
//                    }
                }
            }
        );

        stage.addActor(loadGameButton);

    }
    private void initializeCreditsButton(){

        creditsButton = new TextButton(i18NBundle.get("credits"),textButtonStyle);
        creditsButton.setSize(281/1, 44/1);
        creditsButton.setPosition(backGroundImage.getX() + ((backGroundImage.getWidth()/2) - (creditsButton.getWidth()/2)), backGroundImage.getY() + backGroundImage.getHeight() - ((280) / 1));
        creditsButton.setVisible(true);

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
//                    showCredits();
                }
            }
        });

        stage.addActor(creditsButton);
    }

    private void initializeOptionsButton(){

        optionsButton = new TextButton(i18NBundle.get("options"),textButtonStyle);
        optionsButton.setSize(281/1, 44/1);
        optionsButton.setPosition(backGroundImage.getX() + ((backGroundImage.getWidth()/2) - (optionsButton.getWidth()/2)), backGroundImage.getY() + backGroundImage.getHeight() - ((220) / 1));
        optionsButton.setVisible(false);

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    //gameStartChoicesPopUp.show(true);
                }
            }
        });

        stage.addActor(optionsButton);
    }

    private void initializeGameOptionsButton(){

        gameOptionsButton = new TextButton(i18NBundle.get("gameoptions"),textButtonStyle);
        gameOptionsButton.setSize(281/1, 44/1);
        gameOptionsButton.setPosition(backGroundImage.getX() + ((backGroundImage.getWidth()/2) - (gameOptionsButton.getWidth()/2)), backGroundImage.getY() + backGroundImage.getHeight() - ((220) / 1));
        gameOptionsButton.setVisible(true);

        gameOptionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
//                    showOptions();
                }
            }
        });

        stage.addActor(gameOptionsButton);
    }

    private void initializeExitButton(){

        exitButton = new TextButton(i18NBundle.get("exittodesktop"),textButtonStyle);
        exitButton.setSize(281/1, 44/1);
        exitButton.setPosition(backGroundImage.getX() + ((backGroundImage.getWidth()/2) - (exitButton.getWidth()/2)), backGroundImage.getY() + backGroundImage.getHeight() - ((400) / 1));
        exitButton.setVisible(true);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    exiting = true;
                    Gdx.app.exit();
                    //              ardenne.instance.dispose();

                    //                Gdx.app.exit();

                }
            }
        });


        stage.addActor(exitButton);
    }






}
