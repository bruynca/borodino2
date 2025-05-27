package brunibeargames;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;


public class TurnCounter {
    private Label phaseLabel;
    private Label turnLabel;
    private Label advanceLabel;
    private Group group;
    private I18NBundle i18NBundle;
    private final TextTooltip.TextTooltipStyle tooltipStyle;
    Action blink;
    static public TurnCounter instance;
    Vector2 v2Position;
    public TurnCounter(){
        instance = this;
        group = new Group();
        Borodino.instance.guiStage.addActor(group);
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);

        initialize();
    }

    private void initialize() {

        NinePatch np = new NinePatch(UILoader.instance.topMenu.asset.get("background"), 12, 12, 12, 12);
        Image backgroundImage = new Image();
        backgroundImage.setDrawable(new NinePatchDrawable(np));
        backgroundImage.setSize(450, 80);
        backgroundImage.setPosition(Gdx.graphics.getWidth()/2 - backgroundImage.getWidth() / 2, Gdx.graphics.getHeight() - backgroundImage.getHeight());
        v2Position = new Vector2((Gdx.graphics.getWidth()/2 - backgroundImage.getWidth() / 2) + backgroundImage.getWidth(),Gdx.graphics.getHeight() - backgroundImage.getHeight());
        group.addActor(backgroundImage);

        Label.LabelStyle style = new Label.LabelStyle();
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            style.font = Fonts.getFont24();
        }else{
            style.font = Fonts.getFont24Android();
        }
        phaseLabel = new Label("Game Start", style);
        phaseLabel.setHeight(40);
        phaseLabel.setWidth(260);
        phaseLabel.setVisible(true);
        phaseLabel.setPosition(backgroundImage.getX() + 70, backgroundImage.getY() + 40);
        phaseLabel.setAlignment(Align.center);
        group.addActor(phaseLabel);

 //       String stAd = i18NBundle.get("advance2");
        String stAd = "nex a";
        advanceLabel = new Label(stAd, style);
        advanceLabel.setHeight(40);
        advanceLabel.setWidth(260);
        advanceLabel.setVisible(false);
        advanceLabel.setPosition(backgroundImage.getX() + 70, backgroundImage.getY()+20);
        advanceLabel.setAlignment(Align.center);
        group.addActor(advanceLabel);

        turnLabel = new Label("", style);
        turnLabel.setHeight(40);
        turnLabel.setWidth(260);
        turnLabel.setVisible(true);
        turnLabel.setPosition(backgroundImage.getX() + 70, backgroundImage.getY());
        turnLabel.setAlignment(Align.center);
        group.addActor(turnLabel);

        Image logo1 = new Image();
        logo1.setDrawable(new TextureRegionDrawable(new TextureRegion(UILoader.instance.topMenu.asset.get("frencheagle"))));
        logo1.setPosition(backgroundImage.getX() + 10, backgroundImage.getY() + 10);
        logo1.setSize(60, 60);
        group.addActor(logo1);

        Image logo2 = new Image();
        logo2.setDrawable(new TextureRegionDrawable(new TextureRegion(UILoader.instance.topMenu.asset.get("russianeagle"))));
        logo2.setPosition(backgroundImage.getX() + 450 - 10 - 60 , backgroundImage.getY() + 10);
        logo2.setSize(60, 60);
        group.addActor(logo2);

        group.addListener(new TextTooltip(
                GameMenuLoader.instance.localization.get("manualtooltip"),
                tooltipStyle));



    }
    public Vector2 getEndDisplay(){
        Vector2 v2Return = new Vector2(v2Position.x,v2Position.y);
        return v2Return;
    }

    public void updateText(String text){
        phaseLabel.setText(text);
    }
    public void updateTurn(int turn, Weather.Type wetherType){
        String wether="";
        if (wetherType == Weather.Type.Soup) {
            wether = i18NBundle.get("soup");
        }else if(wetherType== Weather.Type.Clear){
            wether = i18NBundle.get("clear");

        }else if(wetherType== Weather.Type.Overcast){
            wether = i18NBundle.get("overcast");

        }else if(wetherType== Weather.Type.Cloudy){
            wether = i18NBundle.get("cloudy");

        }else if(wetherType== Weather.Type.Mist){
            wether = i18NBundle.get("mist");

        }else if(wetherType== Weather.Type.Variable){
            wether = i18NBundle.get("variable");

        }else if(wetherType== Weather.Type.Bright){
            wether = i18NBundle.get("bright");

        }

        String str = i18NBundle.get("december")+" "+Integer.toString(turn+15)+" "+
//                i18NBundle.get("turn")+" "+turn+" "+ wether;
                "   "+ wether;

        turnLabel.setText(str);
    }

    public void hideTurnDisplay(boolean hide) {
        if (!hide) {
            group.addAction(Actions.fadeOut(0.3f));
        } else {
            group.addAction(Actions.fadeIn(0.3f));
        }
    }
    public void blinkAdvance(){
        advanceLabel.addAction(Actions.forever(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(0.5f),
                Actions.delay(0.5f),
                Actions.fadeOut(0.5f)
        )));
        advanceLabel.setVisible(true);
    }
    public void stopAdvance(){
        advanceLabel.clearActions();
        advanceLabel.setVisible(false);
    }
}


