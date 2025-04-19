package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;

import brunibeargames.Borodino;
import brunibeargames.FontFactory;
import brunibeargames.Fonts;
import brunibeargames.GameMenu;
import brunibeargames.GameMenuLoader;
import brunibeargames.GamePreferences;
import brunibeargames.GameSetup;
import brunibeargames.Hex;
import brunibeargames.HiliteHex;
import brunibeargames.SplashScreen;
import brunibeargames.UILoader;
import brunibeargames.Unit.Counter;
import brunibeargames.Unit.Officer;
import brunibeargames.Unit.Unit;
import brunibeargames.WinModal;

public class WinCommandRandom {
    private  TextButton.TextButtonStyle textButtonStyle;
    //    TextureAtlas textureAtlas = SplashScreen.instance.unitsManager.get("units/germancounteratlas.txt");
    //  TextureRegion close = textureAtlas.findRegion("close");
    public Window window;
    Stage stage;
    Label label;
    Table table;
    HiliteHex hiliteHex;
    float winWidth = 400; // 900 original
    float winHeight = 500; // 650 original
    final float counterSize = 70f;
    private I18NBundle i18NBundle;
    TextTooltip.TextTooltipStyle tooltipStyle;
    private EventListener hitOK;
    WinModal winModal;
    float ySelected;
    float yAvailable;
    Vector2 v2Position;
    TextButton textButtonOK;
    private Image imageRandom;
    ArrayList<Counter> arrCounters = new ArrayList<>();

   ArrayList<Officer> arrOfficers = new ArrayList<>();
   ArrayList<Stack> arrSelectedStacks = new ArrayList<Stack>();
   ArrayList<Stack> arrAvailableStacks = new ArrayList<Stack>();
   ArrayList<Officer> arrOfficerSelected = new ArrayList<>();
   ArrayList<Officer> arrOfficerAvailable = new ArrayList<>();
   boolean isAllied = false;
   TextureAtlas textureAtlas;
   int maxOfficers;
   int dieRollAbove;


    public WinCommandRandom(ArrayList<Officer> arrOfficersIn, float width, float height, Vector2 initPos){
        winWidth = width;
        winHeight = height;
        v2Position = initPos;
        arrOfficers.addAll(arrOfficersIn);
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(FontFactory.instance.titleFont, Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);
        textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");

        np = new NinePatch(UILoader.instance.topMenu.asset.get("commandwindow"), 10, 10, 33, 6);

        /**
         * window format
         */
        Window.WindowStyle windowStyle;
        if (arrOfficers.get(0).isAllied) {
            isAllied = true;
            windowStyle = new Window.WindowStyle(FontFactory.instance.titleFont, Color.WHITE, new NinePatchDrawable(np));
            maxOfficers = GameSetup.instance.getRandomCommandersFrench();
            dieRollAbove = GameSetup.instance.getDicerollsAboveFrench();

        }else{
            isAllied = false;
            windowStyle = new Window.WindowStyle(FontFactory.instance.titleFontRussian, Color.WHITE, new NinePatchDrawable(np));
            maxOfficers = GameSetup.instance.getRandomCommandersRussian();
            dieRollAbove = GameSetup.instance.getDicerollsAboveRussian();

        }
        String title = i18NBundle.format("commandrandom");

        window = new Window(title, windowStyle);
        cancelHover();
        window.setTouchable(Touchable.enabled);

        textButtonStyle = GameMenu.instance.textButtonStyle;
        window.setModal(false);
        window.setTransform(true);
        //     WinModal.instance.set();
        loadWindow();
        showWindow();

    }

    private void showWindow() {
        Vector2 v2 = GamePreferences.getWindowLocation("WinRandom"+isAllied);
        if (v2.x == 0 && v2.y == 0) {
            window.setPosition(v2Position.x, v2Position.y);
        }else{
            window.setPosition(v2.x, v2.y);

        }
        window.setSize(winWidth,winHeight);

        Borodino.instance.guiStage.addActor(window);
    }

    private void loadWindow() {
        // window.setDebug(true);
        arrCounters.clear();
        //   window.padTop(0);
        Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        Label.LabelStyle labelStyle2 = new Label.LabelStyle(Fonts.getFont24(), Color.YELLOW);
        if (isAllied) {
            imageRandom = new Image(textureAtlas.findRegion("alliedrandom"));

        }else{
            imageRandom = new Image(textureAtlas.findRegion("randomrussian"));
        }
        imageRandom.setScaling(Scaling.fit);
        imageRandom.setPosition(3,329);
        /**
         *  imageCommander
         */
        window.addActor(imageRandom);
        /**
         *  commander narrative
         */
        //       String key = commander.name+"command";
        //       String str  = i18NBundle.format(key);

        //    label.setPosition(imageCommander.getX()+imageCommander.getWidth() +3 ,imageCommander.getY()+48);
        //    window.addActor(label);

        String str = i18NBundle.format("activaterandom");
      /*  if (commander.name.contains("urat")){
            str += "\n"+i18NBundle.format("activatehorse",commander.getCanCommand()+1);
        }
        if (commander.name.contains("avout")){
            str += "\n"+i18NBundle.format("activatedavout");
        }*/
        label = new Label(str, labelStyle2);

        float x = imageRandom.getX()+imageRandom.getWidth() +3;
        float y = imageRandom.getY()+120;
        label.setPosition(x+ 30,y);
        window.addActor(label);
        y -=label.getHeight();
        /**
         *  line for available
         */
        /**
         *  line for selectede
         */
        Label.LabelStyle labelStyleTitle = new Label.LabelStyle(FontFactory.instance.littleTitleFont, Color.LIGHT_GRAY);
        String str2 = i18NBundle.format("selected");
        label = new Label(str2, labelStyleTitle);
        label.setPosition(x+54,y);
        ySelected = y + 4;
        window.addActor(label);



        str2 = i18NBundle.format("available");
        label = new Label(str2, labelStyleTitle);

        label.setPosition(imageRandom.getX()+imageRandom.getWidth() -60 ,imageRandom.getY()-20);
        yAvailable = imageRandom.getY()-20;
        window.addActor(label);


        x=10;
        displayInRange();
        displaySelected();

    }

    private void displayInRange() {
        for (Stack stack:arrAvailableStacks){
            stack.remove();
        }
        arrAvailableStacks.clear();
        float x = 10;
        /**
         *  how many officers can we fit
         */

        float availableLength = winWidth;
        float counterLength = 60;
        float offsett = availableLength/arrOfficerAvailable.size();
        if (offsett > 70){
            offsett = 70;
        }else{
            offsett -=5;
        }
        for (final Officer officer:arrOfficerAvailable) {
            final Counter counter = new Counter(officer.getUnit(), Counter.TypeCounter.GUICounter);
            //           counter.stack.setSize(60,60);
            counter.stack.setScale(.60f);
            counter.stack.setTransform(true);
            counter.stack.setPosition(x,yAvailable-67); //90
            //          counter.getCounterStack().getStack().setSize(counterSize/.8f, counterSize/.8f);
            window.addActor(counter.stack);
            arrAvailableStacks.add(counter.stack);
            x +=offsett;
            addListnerForAdd(counter);
        }

    }

    private void addListnerForAdd(Counter counter) {
        counter.stack.addListener(new ClickListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Counter","TouchDown unit="+counter.getUnit());
                if (event.getButton( ) == Input.Buttons.RIGHT)
                {
                    Gdx.app.log("Counter","Right");
                    ArrayList<Unit> arrUnits = new ArrayList<Unit>();
                    arrUnits.addAll(counter.getUnit().getOfficer().getUnitsAvailable());
                    ArrayList<Hex> arrHex = new ArrayList<Hex>();
                    for (Unit unit:arrUnits) {
                        arrHex.add(unit.getHexOccupy());
                    }
                    hiliteHex = new HiliteHex(arrHex, HiliteHex.TypeHilite.AI, null);;
                }
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //             Gdx.app.log("Counter","TouchUp");
                /**
                 *  must have room for commands
                 *  in caseof marat allowed 2 Calvary
                 *
                 */
                if (hiliteHex != null){
                    hiliteHex.remove();
                }

                boolean canCommand = false;
                if (arrOfficerSelected.size() == maxOfficers){
                    canCommand = true;
                }

                if (event.getButton( ) == Input.Buttons.LEFT)
                {
                    if (canCommand){
                        Gdx.app.log("Counter", "Left TouchUp unit=" + counter.getUnit().brigade);
                        arrOfficerAvailable.remove(counter.getUnit().getOfficer());
                        arrOfficerSelected.add(counter.getUnit().getOfficer());
                        displaySelected();
                        displayInRange();
                        //DoCommand.instance.activateOfficer(counter.getUnit().getOfficer());
                        addButtonOK();
                    }
                }
            }
        });


    }

    private void displaySelected() {
        float x = imageRandom.getX()+imageRandom.getWidth() +15;
        /**
         *  how many officers can we fit
         */
        float offsett = 70;
        for (Stack stack:arrSelectedStacks){
            stack.remove();
        }
        arrSelectedStacks.clear();
        for (final Officer officer:arrOfficerSelected) {
            final Counter counter = new Counter(officer.getUnit(), Counter.TypeCounter.GUICounter);
            //           counter.stack.setSize(60,60);
            counter.stack.setScale(.60f);
            counter.stack.setTransform(true);
            counter.stack.setPosition(x,ySelected-75); //90
            //          counter.getCounterStack().getStack().setSize(counterSize/.8f, counterSize/.8f);
            arrSelectedStacks.add(counter.stack);
            window.addActor(counter.stack);
            x +=offsett;
            addListnerForRemove(counter);
        }

    }

    private void addListnerForRemove(Counter counter) {
        counter.stack.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Counter", "TouchDown unit=" + counter.getUnit());
                if (event.getButton() == Input.Buttons.RIGHT) {
                    Gdx.app.log("Counter", "Right");
                    ArrayList<Unit> arrUnits = new ArrayList<Unit>();
                    arrUnits.addAll(counter.getUnit().getOfficer().getUnitsAvailable());
                    /**
                     *  drop all that have been activated
                     *
                     */
                    ArrayList<Unit> arrRemove = new ArrayList<Unit>();
                    for (Unit unit : arrUnits) {
                        if (unit.isHasBeensActivatedThisTurn()) {
                            arrRemove.add(unit);
                        }
                    }
                    arrUnits.removeAll(arrRemove);
                    ArrayList<Hex> arrHex = new ArrayList<Hex>();
                    for (Unit unit : arrUnits) {
                        arrHex.add(unit.getHexOccupy());
                    }
                    //hiliteHex = new HiliteHex(arrHex, HiliteHex.TypeHilite.AI, null);

                }
                return true;

            }

            private void displayInRange() {
            }

            void cancelHover() {
                window.addListener(new ClickListener() {

                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        // Gdx.app.log("Counter ", "enter unit="+unit);
                        Borodino.instance.setInHover(true);
                    }

                    public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        //  Gdx.app.log("Counter", "exit unit="+unit);
                        Borodino.instance.setInHover(false);

                    }

                });
            }
        });
    }
    void cancelHover(){
        window.addListener(new ClickListener() {

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Gdx.app.log("Counter ", "enter unit="+unit);
                Borodino.instance.setInHover(true);
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                //  Gdx.app.log("Counter", "exit unit="+unit);
                Borodino.instance.setInHover(false);

            }

        });
    }
    public void addButtonOK() {
        textButtonOK = new TextButton(i18NBundle.format("ok"), textButtonStyle);
        textButtonOK.setSize(100, 60);
        textButtonOK.setPosition((window.getWidth() - textButtonOK.getWidth()) / 2, 30);
        textButtonOK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    //DoCommand.instance.allocate(commander,arrOfficerSelected);
                    GamePreferences.setWindowLocation("WinRandom"+isAllied, (int) window.getX(), (int) window.getY());
                    end();

                }

            }
        });
        window.addActor(textButtonOK);
    }
    void end(){
        if (textButtonOK != null){
            textButtonOK.remove();
        }
        if (hiliteHex != null) {
            hiliteHex.remove();
        }
        window.remove();

    }


}



