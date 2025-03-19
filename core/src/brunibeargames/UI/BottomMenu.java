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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.NextPhase;
import brunibeargames.SplashScreen;
import brunibeargames.UILoader;

public class BottomMenu {

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

    private boolean enablePhaseChange = true;
    public static BottomMenu instance;
    private I18NBundle i18NBundle;
    private Group group;
    private String cantChangePhaseMessage = "";
    Stage stage;
    static TextureAtlas textureAtlas;


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

    public void show() {
        if (group.getY() <= 50 && !menuVisible) {
            menuVisible = true;
            group.addAction(Actions.moveBy(0, 150, 0.5f));
        }
    }


    public void hide() {
        if (group.getY() > -1 && menuVisible) {
            menuVisible = false;
            group.addAction(Actions.moveBy(0, -150, 0.5f));
        }
    }

    public void enablePhaseChange() {
    	enablePhaseChange = true;
    }

    public void showBottomMenu() {

        nextPhase.setVisible(true);
        nextPhase.setPosition(Gdx.graphics.getWidth() - 110, 10);
    }

    private void initializeNextPhaseButton() {

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("nextphase")));
        style.down = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("nextphasepressed")));
        style.checked = new TextureRegionDrawable(new TextureRegion(UILoader.instance.bottomMenu.button.get("nextphasepressed")));
        nextPhase = new Button(style);
        nextPhase.setHeight(100 / (1));
        nextPhase.setWidth(100 / (1));
        nextPhase.setVisible(true);

        nextPhase.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enablePhaseChange) {
                    nextPhase.setChecked(false);
                    Gdx.app.log("BottomMenu", "NextPhase Pressed");

                    NextPhase.instance.endPhase();
                }else if (!enablePhaseChange){
                    nextPhase.setChecked(false);
 //                   EventManager.instance.errorMessage(cantChangePhaseMessage);
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
        backOut.setVisible(true);
        backOut.setTransform(true);

        backOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enablePhaseChange) {
                    nextPhase.setChecked(false);
                    Gdx.app.log("BottomMenu", "NextPhase Pressed");

                    NextPhase.instance.endPhase();
                }else if (!enablePhaseChange){
                    nextPhase.setChecked(false);
                    //                   EventManager.instance.errorMessage(cantChangePhaseMessage);
                }
            }
        });

        backOutEventListener = new TextTooltip(
                i18NBundle.get("nextphasetooltip"),
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
        inquiry.setVisible(true);

        inquiry.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enablePhaseChange) {
                    nextPhase.setChecked(false);
                    Gdx.app.log("BottomMenu", "NextPhase Pressed");

                    NextPhase.instance.endPhase();
                }else if (!enablePhaseChange){
                    nextPhase.setChecked(false);
                    //                   EventManager.instance.errorMessage(cantChangePhaseMessage);
                }
            }
        });

        inquiryEventListener = new TextTooltip(
                i18NBundle.get("nextphasetooltip"),
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

    public void setEnablePhaseChange(boolean enablePhaseChange) {
        this.enablePhaseChange = enablePhaseChange;
    }

    public void setCantChangePhaseMessage(String cantChangePhaseMessage) {
        this.cantChangePhaseMessage = cantChangePhaseMessage;
    }
}




