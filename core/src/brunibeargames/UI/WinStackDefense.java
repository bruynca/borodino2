package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

import brunibeargames.Attack;
import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.GamePreferences;
import brunibeargames.GameSelection;
import brunibeargames.Hex;
import brunibeargames.SplashScreen;
import brunibeargames.UILoader;
import brunibeargames.Unit.Counter;
import brunibeargames.Unit.Unit;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class WinStackDefense {
    TextureAtlas textureAtlas = SplashScreen.instance.UIManager.get("menus/unitselection.txt");
    Texture ok =  null;


    TextTooltip.TextTooltipStyle tooltipStyle;
    Window window;
    Stage stage;
    int cntCountersToProcess =0;
    I18NBundle i18NBundle;
    ArrayList<Counter> counterArrayList = new ArrayList<>();
    ArrayList<Unit> arrUnits = new ArrayList<>();
    Attack attack;
    Hex hex;
    private boolean isActive = true;
    private EventListener hitOK;
    TextButton.TextButtonStyle tx = GameSelection.instance.textButtonStyle;
    TextButton attackButton;
    TextButton dummyButton;
    boolean isRollDie = false;
    int cntHilited = 0;


    /**
     *  display units being attacked
      *
     * */
    public WinStackDefense(Hex hexTarget) {
        Gdx.app.log("WinStackDefense", "Observer");

        stage= Borodino.instance.guiStage;

        ok = UILoader.instance.unitSelection.asset.get("ok").getTexture();
        arrUnits.addAll(hexTarget.getUnitsInHex());
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);
        np = new NinePatch(UILoader.instance.unitSelection.asset.get("window"), 10, 10, 33, 6);
        Window.WindowStyle windowStyle = new Window.WindowStyle(Fonts.getFont24(), WHITE, new NinePatchDrawable(np));


        String title = i18NBundle.format("defendstack");
        window = new Window(title, windowStyle);
        Label lab = window.getTitleLabel();
        lab.setScale(1.5f);
        lab.setAlignment(Align.center);

        window.setModal(false);
        window.setTransform(true);

        cntCountersToProcess = arrUnits.size();
        if (cntCountersToProcess > 12){
            cntCountersToProcess =12;
        }
        int widthWindow;
        if (cntCountersToProcess > 1) {
            widthWindow = cntCountersToProcess * (Counter.sizeOnMap + 5) + 100;
        }else {
            widthWindow =  (int) (2.4 *(Counter.sizeOnMap + 1))  + 100;
        }
        int rows = 1;
        if (arrUnits.size() > 12 ){
            rows =2;
        }
        int heightWindow = (Counter.sizeOnMap * rows + 100);
        window.setSize(widthWindow,heightWindow);
        window.setPosition(100,100);
        showWindow();

    }
    private void showWindow() {
        Vector2 v2 = GamePreferences.getWindowLocation("DefenseOfUnits");
        if (v2.x == 0 && v2.y == 0) {
            window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, Gdx.graphics.getHeight() / 3 - window.getHeight() / 2);
            v2.x = Gdx.graphics.getWidth() - window.getWidth();
            float xMove = Gdx.graphics.getWidth() - window.getWidth();
            window.addAction(Actions.moveTo(xMove, 0, .3F));
        } else {
            window.setPosition(v2.x, v2.y);

        }
        window.remove();

        setCounters();
        window.row();
        //  Label = getActivatedLabel();
        //  window.row();
        /**
         *  dummy used for positioning
         */
        //window.add(dummyButton).width(150).height(40).bottom().padBottom(15).row();
        // window.add(textButton).width(textButton.getWidth()).height(textButton.getHeight());
        //window.row();
        //    window.pack();
        //window.addActor(attackButton);
        float widthWindow = window.getWidth();
        float heightWindow = window.getHeight();
        //attackButton.setX(widthWindow/2 - attackButton.getWidth()/2);
        //attackButton.setY(dummyButton.getY());
        stage.addActor(window);
        window.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                end();
            }
        });
    }

    /**
     * create new UI counters with listners for our window
     * These counters are differant than the map counters and are only used for this UI screen
     */
    private void  setCounters(){
        final int size =100;
        boolean isFirst = true;
        int cnt = 0;
        for (Unit unit: arrUnits)
        {
            final Counter counter = new Counter(unit, Counter.TypeCounter.GUICounter);
            counter.stack.setTransform(true);
            float ratio =(float) size/Counter.size;
            counter.stack.setScale(ratio);
            counter.getCounterStack().adjustFont(.8f);
            //              counter.stack.setSize( Counter.sizeOnMap,Counter.sizeOnMap);
            window.add(counter.stack).width(Counter.sizeOnMap).height(Counter.sizeOnMap).padBottom(8);
            counterArrayList.add(counter);
            cnt ++;
            if (cnt == 12){
                window.row();
            }

        }
        //        reCalculate();
    }


    public void end(){
        Gdx.app.log("WinStackDefense", "end()");
        isActive =false;
        int lastX = (int) window.getX();
        int lastY = (int) window.getY();
        GamePreferences.setWindowLocation("DefenseOfUnits", lastX, lastY);
        window.remove();

    }


    /**
     *  not used see winsStackCombat
     */
    public float[] getPoly() {
        Polygon polygon = new Polygon();
        Gdx.app.log("WinStackDefense ", "get Ploygon");
        float[] vertices = new float[8];
        polygon.setVertices(vertices);
        vertices[0]  = window.getX();
        vertices[1] = window.getX()+window.getWidth();
        vertices[2] = window.getY();
        vertices[3] = window.getY()-window.getHeight();
        return vertices;
    }

}
