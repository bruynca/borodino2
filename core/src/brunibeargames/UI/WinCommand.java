package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.GamePreferences;
import brunibeargames.UILoader;
import brunibeargames.Unit.Commander;
import brunibeargames.Unit.Counter;
import brunibeargames.Unit.Officer;
import brunibeargames.WinModal;

/**
 * WinCommand
 *  display a Commnder and allow user to choose corp commander to
 *  activate for this commander
 */

public class WinCommand {

//    TextureAtlas textureAtlas = SplashScreen.instance.unitsManager.get("units/germancounteratlas.txt");
  //  TextureRegion close = textureAtlas.findRegion("close");
    Window window;
    Stage stage;
    Label label;
    Table table;

    float winWidth = 500; // 900 original
    float winHeight = 200; // 650 original
    final float counterSize = 70f;
    private I18NBundle i18NBundle;
    TextTooltip.TextTooltipStyle tooltipStyle;
    private EventListener hitOK;
    WinModal winModal;
    Commander commander;
    ArrayList<Officer> arrOfficerAvailable = new ArrayList<>();
    ArrayList<Officer> arrOfficerSelected = new ArrayList<>();
    ArrayList<Counter> arrCounters = new ArrayList<>();
    public WinCommand(Commander commander){
        this.commander = commander;
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);
        np = new NinePatch(UILoader.instance.unitSelection.asset.get("window"), 10, 10, 33, 6);
        /**
         * window format
         */
        Window.WindowStyle windowStyle = new Window.WindowStyle(Fonts.getFont24(), Color.WHITE, new NinePatchDrawable(np));
        String title = commander.name;
        arrOfficerAvailable.clear();
  //      if (commander.name.contains("urat")){

            arrOfficerAvailable.addAll(commander.getOfficerAvailable());
      //   }

        window = new Window(title, windowStyle);
        /**
         * close button
         */
     /*   Label lab = window.getTitleLabel();
        lab.setAlignment(Align.center);
        Image image = new Image(close);
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                end();
            }
        });
        window.getTitleTable().add(image);
        hitOK = new TextTooltip(
                i18NBundle.format("closereindisplay"),
                tooltipStyle);
        image.addListener(hitOK); */

        window.setModal(false);
        window.setTransform(true);

        int heightWindow = (Counter.sizeOnMap + 100);
        if (winWidth < 120) {
            winWidth = 120;
        }
        window.setSize(winWidth, heightWindow);
        window.setPosition(100, 100);
   //     WinModal.instance.set();
        loadWindow();
        showWindow();


    }

    private void showWindow() {
        Vector2 v2 = GamePreferences.getWindowLocation(commander.name);
        if (v2.x == 0 && v2.y == 0) {
            window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
            v2.x  = Gdx.graphics.getWidth() - window.getWidth();
            float xMove = Gdx.graphics.getWidth() - window.getWidth();
            window.addAction(Actions.moveTo(xMove, 0, .3F));
        }else{
            window.setPosition(v2.x, v2.y);

        }
        winWidth = 400;
        winHeight = 500;
/*        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        String str = i18NBundle.format("secondexit");
        label = new Label(str, labelStyle);
        window.add(label).colspan(maxRows).align(Align.left);
        window.row();
        window.add(table).width(450);
        window.row();
        if (arrUnits2nth.size() > 0) {
            table = loadTable(arrUnits2nth);
            winHeight += (counterSize + 50);
            window.add(table);
            window.row();

        }
*/
        window.setSize(winWidth,winHeight);
        Borodino.instance.guiStage.addActor(window);

    }

    private void loadWindow() {
        window.setDebug(true);
        arrCounters.clear();
     //   window.padTop(0);
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
     //   String str = i18NBundle.format("secondexit");
        String str = "At Borodino, Murat,\n Napoleon's\n flamboyant brother-in-law and\n cavalry commander.\nCan Activate 1 Corp\nor\nCan Acetc";
        label = new Label(str, labelStyle);

        Image image = new Image(commander.getTextureRegion());
        image.setScaling(Scaling.fit);
        window.add(image);
        window.add(label);
        window.row();
        str = "Available";
        label = new Label(str, labelStyle);
        window.add(label).colspan(2);
        window.row();
        for (Officer officer:arrOfficerAvailable){
            Counter counter = new Counter(officer.getUnit(), Counter.TypeCounter.MapCounter);
            Stack stack = counter.getCounterStack().getStack();
            window.add(counter.getCounterStack().getStack());

        }
        window.row();
        str = "Selected";
        label = new Label(str, labelStyle);
        window.add(label).colspan(2);



    }
}
