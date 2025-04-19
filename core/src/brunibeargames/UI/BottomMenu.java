package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Observable;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.ObserverPackage;
import brunibeargames.SplashScreen;
import brunibeargames.UILoader;

public class BottomMenu extends Observable {

    private TextureAtlas.AtlasRegion inquirytexNormal;
    private TextureAtlas.AtlasRegion inquirytextOver;
    private TextureAtlas.AtlasRegion inquirytexDown;
    private Button nextPhase;
    private Button backOut;
    private Button inquiry;
    private final TextTooltip.TextTooltipStyle tooltipStyle;
    private boolean menuVisible = true;
    private EventListener nextPhaseEventListener;
    private EventListener backOutEventListener;
    private EventListener inquiryEventListener;

    private boolean enablePhaseChange = false;
    private boolean warningPhaseChange = false;
    private boolean isWarned=false;

    public static BottomMenu instance;
    private I18NBundle i18NBundle;
    private Group group;
    Stage stage;
    static TextureAtlas textureAtlas;
    private String phaseMessage;
    private String inquiryKey;
    private String backKey;
    private String phaseTitle;
    private String helpMessage;
    private String helpTitle;
    private Object objectGoBack;


    public BottomMenu() {
        stage = Borodino.instance.guiStage;
        instance = this;
        textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");
        i18NBundle = GameMenuLoader.instance.localization;
     //   group = new Group();
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);
        initializeNextPhaseButton();
        initializeGoBackPhaseButton();
        initializeInquiryButton();
        // stage.addActor(group);

    }


    public void showNextPhase() {
        nextPhase.setVisible(true);
        //nextPhase.setPosition(Gdx.graphics.getWidth() - 110, 10);
    }
    public void showInquirNextPhase() {

        inquiry.setVisible(true);
        //nextPhase.setPosition(Gdx.graphics.getWidth() - 110, 10);
    }
    public void showBackOut() {

        backOut.setVisible(true);
        //nextPhase.setPosition(Gdx.graphics.getWidth() - 110, 10);
    }

    private void initializeNextPhaseButton() {

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("nextphase")));
        style.down = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("nextphasepressed")));
        style.checked = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("nextphasepressed")));
        nextPhase = new Button(style);
        nextPhase.setHeight(100 / (1));
        nextPhase.setWidth(100 / (1));
        nextPhase.setVisible(false);

        nextPhase.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!enablePhaseChange) {
                    return;
                }
                if (warningPhaseChange) {
                    if (isWarned) {
                        setChanged();
                        notifyObservers(new ObserverPackage(ObserverPackage.Type.NextPhase,null,0,0));
                        // let update set the next phase
                        //NextPhase.instance.endPhase();
                        isWarned = false;
                        return;
                    }else{
                        isWarned = true;
                        WinText winText = new WinText(phaseTitle,phaseMessage);
                        return;
                    }
                }
            }
        });

        nextPhaseEventListener = new TextTooltip(
                i18NBundle.get("nextphasetooltip"),
                tooltipStyle);

        nextPhase.addListener(nextPhaseEventListener);
        nextPhase.setPosition(Gdx.graphics.getWidth() - 110, 10);

        stage.addActor(nextPhase);
    }
    private void initializeGoBackPhaseButton() {

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("hidemenu")));
        style.down = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("hidemenupressed")));
        style.checked = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("hidemenupressed")));
        backOut = new Button(style);
        backOut.setHeight(100 / (1));
        backOut.setWidth(100 / (1));
        backOut.setVisible(false);
        backOut.setTransform(true);

        backOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setChanged();
                notifyObservers(new ObserverPackage(ObserverPackage.Type.GoBack,null,0,0));
            }
        });

        backOutEventListener = new TextTooltip(
                i18NBundle.get("goback"),
                tooltipStyle);
        backOut.rotateBy(270F);

        backOut.addListener(backOutEventListener);
        float x = nextPhase.getX() - (backOut.getWidth()+ 20);
        backOut.setPosition(x,10+backOut.getHeight());
        stage.addActor(backOut);
    }
    private void initializeInquiryButton() {
        inquirytexNormal = textureAtlas.findRegion("info");
        //inquirytextOver = textureAtlas.findRegion("infoover");
        inquirytexDown = textureAtlas.findRegion("infodown");


        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(inquirytexNormal);
        style.down = new TextureRegionDrawable(inquirytexDown);
        style.checked = new TextureRegionDrawable(inquirytexNormal);
        inquiry = new Button(style);
        inquiry.setHeight(100 / (1));
        inquiry.setWidth(100 / (1));
        inquiry.setVisible(false);

        inquiry.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WinText winText = new WinText(helpTitle,helpMessage);
            }
        });

        inquiryEventListener = new TextTooltip(
                i18NBundle.get("help"),
                tooltipStyle);

        inquiry.addListener(inquiryEventListener);
        float x = backOut.getX() - (inquiry.getWidth()+ 20);
        inquiry.setPosition(x,nextPhase.getY());
        stage.addActor(inquiry);
    }

    public void changeNextPhaseTooltipText(String text) {

        Array<EventListener> listeners = nextPhase.getListeners();
        for (EventListener listener : listeners) {
            if (listener instanceof TextTooltip) {
                ((TextTooltip) listener).getContainer().getActor().setText(text);
                ((TextTooltip) listener).getContainer().getActor().setWrap(false);
                ((TextTooltip) listener).getContainer().getActor().setAlignment(Align.center);
                break;
            }
        }
    }

    public Button getNextPhase() {
        return nextPhase;
    }

    /**
     * used by processing to limit phase change
     * @param enablePhaseChange
     */
    public void setEnablePhaseChange(boolean enablePhaseChange) {
        this.enablePhaseChange = enablePhaseChange;
    }

    public void setInquiryKey(String inquiryKey) {
        this.inquiryKey = inquiryKey;
    }

    public void setBackKey(String backKey) {
        this.backKey = backKey;
    }

    public void setPhaseData(String title, String message) {
        this.phaseMessage = message;
        this.phaseTitle= title;
    }

    public void setWarningPhaseChange(boolean warningPhaseChange) {
        this.warningPhaseChange = warningPhaseChange;
    }

    public void setHelpData(String title, String message) {
        this.helpMessage = message;
        this.helpTitle= title;
    }

}




