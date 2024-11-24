package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;
import brunibeargames.GamePreferences;
import brunibeargames.GameSetup;
import brunibeargames.Hex;
import brunibeargames.NextPhase;
import brunibeargames.ObserverPackage;
import brunibeargames.SplashScreen;
import brunibeargames.UILoader;
import brunibeargames.Unit.Counter;
import brunibeargames.Unit.Unit;
import brunibeargames.WinModal;

public class WinExitDisplay implements Observer {
    TextureAtlas textureAtlas = SplashScreen.instance.unitsManager.get("units/germancounteratlas.txt");
    TextureRegion close = textureAtlas.findRegion("close");
    Window window;
    Stage stage;
    Label label;
    Table table;
    ArrayList<Counter> arrCounterSave = new ArrayList<>();
    ArrayList<Unit> arrUnits2nth = new ArrayList<>();
    ArrayList<Unit> arrUnitsLehr = new ArrayList<>();

    ArrayList<ArrayList> arrUnitsByTurn = new ArrayList<>();
    ArrayList<Integer> arrTurns = new ArrayList<>();

    ArrayList<Unit> arrUnitsSouthEast = new ArrayList<>();
    ArrayList<Unit> arrUnitsSouth = new ArrayList<>();
    ArrayList<Unit> arrUnitsSouthWest = new ArrayList<>();

    float winWidth = 500; // 900 original
    float winHeight = 200; // 650 original
    final float counterSize = 70f;
    private I18NBundle i18NBundle;
    TextTooltip.TextTooltipStyle tooltipStyle;
    private EventListener hitOK;
    Hex hex;
    int maxRows = 0;
    int maxCounters = 0;
    WinModal winModal;

    public WinExitDisplay() {
        this.hex = hex;
        stage = Borodino.instance.guiStage;
        Borodino.instance.addObserver(this);
        /**
         * tooltip
         */
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
        String title = i18NBundle.format("exitunits");
        window = new Window(title, windowStyle);
        /**
         * close button
         */
        Label lab = window.getTitleLabel();
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
        image.addListener(hitOK);

        window.setModal(false);
        window.setTransform(true);

        int heightWindow = (Counter.sizeOnMap + 100);
        if (winWidth < 120) {
            winWidth = 120;
        }
        window.setSize(winWidth, heightWindow);
        window.setPosition(100, 100);
        WinModal.instance.set();
        createdisplayUnits();
        showWindow();
    }
    private void createdisplayUnits() {

        /**
         *  Calculate min size
         */
        maxRows = 1;
        maxCounters = arrUnits2nth.size();

    }
    private void showWindow(){
        Vector2 v2 = GamePreferences.getWindowLocation("exitdisplay");
        if (v2.x == 0 && v2.y == 0) {
            window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, Gdx.graphics.getHeight() / 2 - window.getHeight() / 2);
            v2.x  = Gdx.graphics.getWidth() - window.getWidth();
            float xMove = Gdx.graphics.getWidth() - window.getWidth();
            window.addAction(Actions.moveTo(xMove, 0, .3F));
        }else{
            window.setPosition(v2.x, v2.y);

        }
        winWidth = 470;
        if (maxCounters > 1) {
            int calWinWidth = maxCounters * (Counter.sizeOnMap + 5) + 100;
            if (calWinWidth >winWidth){
                winWidth = calWinWidth;
            }
        }
        winHeight = (maxRows * (counterSize + 50) + 100);
        winHeight = 200;
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
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

        window.setSize(winWidth,winHeight);
        stage.addActor(window);


    }

    private Table genTable(int[] numOfUnitsToExit, ArrayList<Unit> arrUnitsExited, boolean is2nd) {
        int[] workTable = new int[numOfUnitsToExit.length];
        int exited = arrUnitsExited.size();

        for (int i=0; i<numOfUnitsToExit.length;i++){;
            workTable[i] = numOfUnitsToExit[i] - exited;
            if (workTable[i] < 0){
                workTable[i] = 0;
            }
        }
        Table table = new Table();
        table.debugTable();

        int turn = NextPhase.instance.getTurn();
        turn--; // point to correct table
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        Label label = new Label("dummy",labelStyle);
        for (int i=turn; i < workTable.length;i++) {
            int left= workTable[i] ;
            int cnt = i+1;
            String str = i18NBundle.format("turn") +  cnt+"\n"+"   "+left;
            label = new Label(str, labelStyle);
            table.add(label).width(50).expand();
        }
        return table;


    }

    private Table loadTable(ArrayList<Unit> arrUnits) {
        Table table = new Table();
        final int size =100;
        int i = 0;
        for (final Unit unit : arrUnits) {
            final Counter counter = new Counter(unit, Counter.TypeCounter.GUICounter);
            arrCounterSave.add(counter);
            counter.stack.setTransform(true);
            float ratio =(float) size/Counter.size;
            counter.stack.setScale(ratio);
            counter.getCounterStack().adjustFont(.8f);
            counter.getCounterStack().getStack().setSize(counterSize, counterSize);
            table.add(counter.stack).width(Counter.sizeOnMap).height(Counter.sizeOnMap);
        }
        return table;

    }
    public  void end(){
        Borodino.instance.deleteObserver(this);
        int lastX = (int) window.getX();
        int lastY = (int) window.getY();
        GamePreferences.setWindowLocation("exitdisplay", lastX, lastY);
        window.remove();
        WinModal.instance.release();
    }
    public boolean isVisble() {
        if (window.getParent() != null){
            return true;
        }
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        ObserverPackage op;
        op = (ObserverPackage) arg;
        /**
         *  check if user has clicked outside the window
         *  which is  a cancel
         */
        float  winStartx = window.getX();
        float  winEndx = window.getX()+window.getWidth();
        float  winStarty = window.getY();
        float  winEndy = window.getY()+window.getHeight();
        int reverse = Gdx.graphics.getHeight() - op.y;
        if (op.x < winStartx || op.x > winEndx || reverse < winStarty || reverse > winEndy) {
            end();
        }
    }


}
