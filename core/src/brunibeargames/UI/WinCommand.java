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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;

import brunibeargames.Borodino;
import brunibeargames.FontFactory;
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
    float ySelected;
    float yAvailable;

    public WinCommand(Commander commander){
        this.commander = commander;
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(FontFactory.instance.titleFont, Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);

        np = new NinePatch(UILoader.instance.topMenu.asset.get("commandwindow"), 10, 10, 33, 6);
        /**
         * window format
         */
        Window.WindowStyle windowStyle = new Window.WindowStyle(FontFactory.instance.titleFont, Color.WHITE, new NinePatchDrawable(np));
        String title = commander.name;//+" - "+i18NBundle.format("command");
        arrOfficerAvailable.clear();
        arrOfficerAvailable.addAll(commander.getOfficerPossibleAvailable());

        window = new Window(title, windowStyle);

        /**
         * close button
         */
//        Label lab = window.getTitleLabel();
//        lab.setAlignment(Align.center);
/*        Image image = new Image(close);
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
        window.setSize(winWidth,winHeight);
        Borodino.instance.guiStage.addActor(window);

    }

    private void loadWindow() {
       // window.setDebug(true);
        arrCounters.clear();
     //   window.padTop(0);
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        Label.LabelStyle labelStyle2 = new Label.LabelStyle(Fonts.getFont24(), Color.YELLOW);

        Image image = new Image(commander.getTextureRegion());
        image.setScaling(Scaling.fit);
        image.setPosition(3,329);
        /**
         *  image
         */
        window.addActor(image);
        /**
         *  commander narrative
         */
        String key = commander.name+"command";
        String str  = i18NBundle.format(key);

        label = new Label(str, labelStyle);
        label.setPosition(image.getX()+image.getWidth() +3 ,image.getY()+48);
        window.addActor(label);

        str = i18NBundle.format("activate",commander.getCanCommand());
        if (commander.name.contains("urat")){
            str += "\n"+i18NBundle.format("activatehorse",commander.getCanCommand()+1);
        }
        if (commander.name.contains("avout")){
            str += "\n"+i18NBundle.format("activatedavout");
        }
        label = new Label(str, labelStyle2);
        label.setPosition(image.getX()+image.getWidth() +3 ,image.getY());
        window.addActor(label);

        /**
         *  line for available
         */
        Label.LabelStyle labelStyleTitle = new Label.LabelStyle(FontFactory.instance.littleTitleFont, Color.LIGHT_GRAY);
        String str2 = i18NBundle.format("available");
        label = new Label(str2, labelStyleTitle);

        label.setPosition(image.getX()+image.getWidth() +3 ,image.getY()-20);
        yAvailable = image.getY()-20;
        window.addActor(label);

        /**
         *  line for selectede
         */
        str2 = i18NBundle.format("selected");
        label = new Label(str2, labelStyleTitle);
        label.setPosition(image.getX()+image.getWidth() +3 ,image.getY()-109);
        ySelected = image.getY()-109;
        window.addActor(label);

//        Table table = addOfficersAvailableTable();
//        table.setPosition(100,yAvailable-30);
        float x=10;
        //window.addActor(table);
        for (final Officer officer:arrOfficerAvailable) {
            final Counter counter = new Counter(officer.getUnit(), Counter.TypeCounter.GUICounter);
 //           counter.stack.setSize(60,60);
            counter.stack.setScale(.60f);
            counter.stack.setTransform(true);
            counter.stack.setPosition(x,yAvailable-67); //90
            //          counter.getCounterStack().getStack().setSize(counterSize/.8f, counterSize/.8f);
            window.addActor(counter.stack);
            x +=70;
        }





        //       window.row();
/*        window.row();
        str = "Selected";
        window.row();
        float x =3;
        for (Officer officer:arrOfficerAvailable){
            final Counter counter = new Counter(officer.getUnit(), Counter.TypeCounter.GUICounter);
    //        arrCounterSave.add(counter);
            counter.stack.setTransform(true);
            float ratio =(float) 100f/Counter.size;
            counter.stack.setScale(ratio);
            counter.getCounterStack().adjustFont(.8f);
            counter.getCounterStack().getStack().setSize(counterSize, counterSize);
            window.addActor(counter.stack);
            x+=counterSize+3;
            arrCounters.add(counter);

        }*/



    }


    private Table addOfficersAvailableTable() {
        Table table = new Table();
        final int size =100;
        int i = 0;
        for (final Officer officer:arrOfficerAvailable) {
            final Counter counter = new Counter(officer.getUnit(), Counter.TypeCounter.GUICounter);
            counter.stack.setTransform(true);
            float ratio =(float) size/Counter.size;
  //          counter.stack.setScale(ratio);
            counter.getCounterStack().adjustFont(.64f);
  //          counter.getCounterStack().getStack().setSize(counterSize/.8f, counterSize/.8f);
            table.add(counter.stack).width(Counter.sizeOnMap/1.5f).height(Counter.sizeOnMap/1.5f);
        }
        return table;
    }

    public void deleteOfficer(Officer officer) {
    }

    public void addOfficer(Officer officer) {
    }
}
