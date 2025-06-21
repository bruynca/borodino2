package brunibeargames.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.Hex;
import brunibeargames.SplashScreen;

public class StopImage {
    private static TextureAtlas textureAtlas;
    private static I18NBundle i18NBundle;
    private static TextTooltip.TextTooltipStyle tooltipStyle;
    public static StopImage instance;
    static ArrayList<StopImage> arrStops = new ArrayList<>();


    private Image image;
    private Hex hex;
    private Type type;
    private EventListener stopEventListener;

    Label label;


    public StopImage() {
        instance = this;
        textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");
        i18NBundle = GameMenuLoader.instance.localization;
        //   group = new Group();
        //NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        //tooltipStyle.background = new NinePatchDrawable(np);
    }
    public StopImage(Hex hex) {
        this.hex = hex;
        Vector2 vec = hex.getCounterPosition();
        String str2 = i18NBundle.format("overstack");

        label = new Label(str2, new Label.LabelStyle(Fonts.getFont24(), Color.WHITE));


        TextureRegion tex = textureAtlas.findRegion("stop");
        image = new Image(tex);
        image.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                 Borodino.instance.mapStage.addActor(label);
                 label.setPosition(vec.x, vec.y+20);
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                label.remove();
            }
        });
        Borodino.instance.mapStage.addActor(image);
        image.setPosition(vec.x, vec.y+20);
        arrStops.add(this);




    }
    public static void remove(){
        for (StopImage stopImage:arrStops){
            stopImage.image.remove();
        }
        arrStops.clear();
    }

    enum Type {OverStack};

}
