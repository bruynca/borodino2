package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import brunibeargames.AccessInternet;
import brunibeargames.Borodino;
import brunibeargames.Combat;
import brunibeargames.CombatDisplayResults;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.GameSetup;
import brunibeargames.NextPhase;
import brunibeargames.SoundsLoader;
import brunibeargames.SplashScreen;
import brunibeargames.TurnCounter;
import brunibeargames.Unit.ClickAction;

public class VictoryPopup {

    private Image backGroundImage;

    private Label victoryTitle;
    private Label victorText;
    private Image victoryFlag;
    TextureAtlas textureAtlas = SplashScreen.instance.unitsManager.get("units/germancounteratlas.txt");
    TextureRegion flag =  textureAtlas.findRegion("usflag");

    private Image victoryUS = new Image(flag);

    private boolean isDisplayed = false;
    private Stage stage;
    private Group group;
    private I18NBundle i18NBundle;

    static public VictoryPopup instance;


    public VictoryPopup() {

        this.stage = Borodino.instance.guiStage;
        instance = this;
        i18NBundle= GameMenuLoader.instance.localization;

        initialize();
    }

    public void initialize() {
        this.group = new Group();

        initializeImageBackGround();
        initializeLabels();
        addVictoryFlag("german");

        group.setVisible(false);
        stage.addActor(group);
    }

    private void initializeImageBackGround() {
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"),5, 5, 5, 5);
        backGroundImage = new Image();
        backGroundImage.setDrawable(new NinePatchDrawable(np));
        backGroundImage.setVisible(true);
        backGroundImage.setHeight(200);
        backGroundImage.setWidth(600);
        backGroundImage.setPosition((Gdx.graphics.getWidth() / 2) - ((backGroundImage.getWidth() / 2)),
                (Gdx.graphics.getHeight() / 2) - ((backGroundImage.getHeight() / 2)));
        group.addActor(backGroundImage);
    }

    private void initializeLabels()
    {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24();
        victoryTitle = new Label("", style);
        victoryTitle.setHeight(30);
        victoryTitle.setWidth(260);
        victoryTitle.setVisible(true);
        victoryTitle.setPosition(backGroundImage.getX() + 70, backGroundImage.getY() + 50);
        victoryTitle.setAlignment(Align.center);
        group.addActor(victoryTitle);

        victorText = new Label("", style);
        victorText.setHeight(30);
        victorText.setWidth(260);
        victorText.setVisible(true);
        victorText.setPosition(backGroundImage.getX() + 70, backGroundImage.getY() + 50);
        victorText.setAlignment(Align.center);
        group.addActor(victorText);
    }

    private void addVictoryFlag(String country){
        if (country.contains("usa")){
            victoryFlag = victoryUS;
        }else {
            victoryFlag = new Image(GameMenuLoader.instance.victory.asset.get(country));
        }
        victoryFlag.setPosition(backGroundImage.getX() + backGroundImage.getWidth()/2 - victoryFlag.getWidth()/2, backGroundImage.getY() + backGroundImage.getHeight() - 30);
        group.addActor(victoryFlag);
    }





    public void updateText(String text, String country) {
        victoryFlag.remove();
        addVictoryFlag(country);
        victorText.setText(text);
        victorText.pack();
        GlyphLayout layout = victorText.getGlyphLayout();
        float height = layout.height;
        float width = layout.width;
        backGroundImage.setPosition((Gdx.graphics.getWidth() / 2) - ((backGroundImage.getWidth() / 2)),
                (Gdx.graphics.getHeight() / 2) - ((backGroundImage.getHeight() / 2)));
        victorText.setPosition(backGroundImage.getX() + backGroundImage.getWidth()/2 - width/2, backGroundImage.getY() + (backGroundImage.getHeight() / 2) - (height / 2));
        show();
        TurnCounter.instance.updateText(i18NBundle.get("gameend"));
    }

    public void hide() {
        group.setVisible(false);
        isDisplayed = false;
    }

    public void show()
    {
        group.setVisible(true);
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public String announceVictorAtEnd() {
        SoundsLoader.instance.playTada();
            return"";
    }
    public  void announceVictorEliminate(boolean isSecondPanzer){
        SoundsLoader.instance.playTada();
        BottomMenu.instance.setEnablePhaseChange(false);
        saveWinner("allied");
        AccessInternet.updateGame(NextPhase.instance.getTurn(),  "Allies");
        if (isSecondPanzer){
            updateText(i18NBundle.get("2ndlosernoexit"),"usa");
        }else{
            updateText(i18NBundle.get("lehrlosernoexit"),"usa");
        }
        ClickAction.cancelAll();
        Combat.instance.cleanup(true);
        CombatDisplayResults.instance.allowFinish();
        BottomMenu.instance.setEnablePhaseChange(false);
        AccessInternet.updateGame(NextPhase.instance.getTurn(), "Allied");
        return;

    }

    private void checkCounterAttack() {
    }

    private String checkSecondPanzerEnd() {
        /**
         * if end of game check supply
         */
        int turn = NextPhase.instance.getTurn();
        if (turn == GameSetup.instance.getScenario().getLength()){
        }
        saveWinner("allied");
        updateText(i18NBundle.get("2ndlosernoexit"),"usa");
        return "allied";
    }

    private String checkLehr() {
        /**
         * if end of game check supply
         */
        int turn = NextPhase.instance.getTurn();
        if (turn == GameSetup.instance.getScenario().getLength()){

        }
        saveWinner("allied");
        updateText(i18NBundle.get("lehrlosernoexit"),"usa");
        return "allied";

    }

    private String  checkIntro() {
            saveWinner("allied");
            updateText(i18NBundle.get("americanscene1victor"),"usa");
            return "allied";

    }
    private void saveWinner(String strVic){
        AccessInternet.updateGame(GameSetup.instance.getScenario().getLength(), strVic);
        return;

    }
}
