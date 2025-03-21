package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
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
import brunibeargames.DoCommand;
import brunibeargames.FontFactory;
import brunibeargames.Fonts;
import brunibeargames.GameMenu;
import brunibeargames.GameMenuLoader;
import brunibeargames.GamePreferences;
import brunibeargames.Hex;
import brunibeargames.HiliteHex;
import brunibeargames.UILoader;
import brunibeargames.Unit.Commander;
import brunibeargames.Unit.Counter;
import brunibeargames.Unit.Officer;
import brunibeargames.Unit.Unit;
import brunibeargames.WinModal;

/**
 * WinCommand
 *  display a Commnder and allow user to choose corp commander to
 *  activate for this commander
 */

public class WinCommand {

    private final TextButton.TextButtonStyle textButtonStyle;
    //    TextureAtlas textureAtlas = SplashScreen.instance.unitsManager.get("units/germancounteratlas.txt");
  //  TextureRegion close = textureAtlas.findRegion("close");
    Window window;
    Stage stage;
    Label label;
    Table table;

    float winWidth = 400; // 900 original
    float winHeight = 500; // 650 original
    final float counterSize = 70f;
    private I18NBundle i18NBundle;
    TextTooltip.TextTooltipStyle tooltipStyle;
    private EventListener hitOK;
    WinModal winModal;
    public Commander commander;
    ArrayList<Officer> arrOfficerAvailable = new ArrayList<>();
    ArrayList<Officer> arrOfficerAvailableOriginal = new ArrayList<>();
    ArrayList<Officer> arrOfficerSelected = new ArrayList<>();
    ArrayList<Counter> arrCounters = new ArrayList<>();
    float ySelected;
    float yAvailable;
    Officer officerDavout;
    boolean isMurat = false;
    boolean isDavout = false;
    Vector2 v2Position;
    TextButton textButtonOK;

    public WinCommand(Commander commander, float width, float height, Vector2 initPos){
        winWidth = width;
        winHeight = height;
        v2Position = initPos;
        this.commander = commander;
        officerDavout = Officer.getOfficer("Davout");
        if (commander.name.contains("urat")){
            isMurat = true;
        }
        if (commander.name.contains("avout")){
            isDavout = true;
        }
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(FontFactory.instance.titleFont, Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);

        np = new NinePatch(UILoader.instance.topMenu.asset.get("commandwindow"), 10, 10, 33, 6);
        /**
         * window format
         */
        Window.WindowStyle windowStyle;
        if (commander.isAllied) {
            windowStyle = new Window.WindowStyle(FontFactory.instance.titleFont, Color.WHITE, new NinePatchDrawable(np));
        }else{
            windowStyle = new Window.WindowStyle(FontFactory.instance.titleFontRussian, Color.WHITE, new NinePatchDrawable(np));
        }
        String title = commander.name;//+" - "+i18NBundle.format("command");

        arrOfficerAvailable.clear();
        arrOfficerAvailable.addAll(commander.getOfficerPossibleAvailable());
        arrOfficerAvailableOriginal.clear();
        arrOfficerAvailableOriginal.addAll(arrOfficerAvailable);
        arrOfficerSelected.clear();
        if (commander.isAllied){
            arrOfficerAvailable.remove(officerDavout);
            if (commander.name.contains("avout")){
                arrOfficerSelected.add(officerDavout);
            }
        }

        window = new Window(title, windowStyle);
        window.setTouchable(Touchable.enabled);

        textButtonStyle = GameMenu.instance.textButtonStyle;

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

        label.setPosition(image.getX()+image.getWidth() -60 ,image.getY()-20);
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

        float x=10;
        displayInRange();
        displaySelected();


    }
    ArrayList<Stack> arrSelectedStacks = new ArrayList<Stack>();
    private void displaySelected() {
        float x = 10;
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
            counter.stack.setPosition(x,ySelected-65); //90
            //          counter.getCounterStack().getStack().setSize(counterSize/.8f, counterSize/.8f);
            arrSelectedStacks.add(counter.stack);
            window.addActor(counter.stack);
            x +=offsett;
            addListnerForRemove(counter);
        }

    }


    /**
     *  add officers available to this commander
     */
    ArrayList<Stack> arrAvailableStacks = new ArrayList<Stack>();
    public void displayInRange() {
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
    HiliteHex hiliteHex;
    private void addListnerForAdd(Counter counter) {
        counter.stack.addListener(new ClickListener() {
/*
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.app.log("Counter ", "enter unit="+unit);
                checkExplode(counter);
                checkDesc(counter);
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.app.log("Counter", "exit unit="+unit);
                checkImplode();
                checkDescGone();
            }*/
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
                if (commander.getCanCommand() > arrOfficerSelected.size()){
                    canCommand = true;
                }else{
                    if ((isMurat && arrOfficerSelected.get(0).corp.number.contains("C") &&
                       (arrOfficerSelected.size() == 1) &&
                      counter.getUnit().getOfficer().corp.number.contains("C"))){
                        canCommand = true;
                    }else{
                        if (isDavout && arrOfficerSelected.size() == 1){
                            canCommand =true;
                        }
                    }

                }

                if (event.getButton( ) == Input.Buttons.LEFT)
                {
                    if (canCommand){
                        Gdx.app.log("Counter", "Left TouchUp unit=" + counter.getUnit().brigade);
                        arrOfficerAvailable.remove(counter.getUnit().getOfficer());
                        arrOfficerSelected.add(counter.getUnit().getOfficer());
                        displaySelected();
                        displayInRange();
                        DoCommand.instance.takeOfficer(counter.getUnit().getOfficer());
                        addButtonOK();
                    }
                }
            }
       });

    }
    private void addListnerForRemove(Counter counter) {
        counter.stack.addListener(new ClickListener() {
            /*
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            Gdx.app.log("Counter ", "enter unit="+unit);
                            checkExplode(counter);
                            checkDesc(counter);
                        }
                        public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            Gdx.app.log("Counter", "exit unit="+unit);
                            checkImplode();
                            checkDescGone();
                        }
                        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                            Gdx.app.log("Counter","TouchDown unit="+unit);
                            Borodino.instance.unitPlace = unit;
                            if (event.getButton( ) == Input.Buttons.RIGHT)
                            {
                                Gdx.app.log("Counter","Right");
                                cycleUnits();

                            }
                            return true;
                        }*/
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Counter","TouchDown unit="+counter.getUnit());
                if (event.getButton( ) == Input.Buttons.RIGHT)
                {
                    Gdx.app.log("Counter","Right");
                    ArrayList<Unit> arrUnits = new ArrayList<Unit>();
                    arrUnits.addAll(counter.getUnit().getOfficer().getUnitsAvailable());
                    /**
                     *  drop all that have been activated
                     *
                     */
                    ArrayList<Unit> arrRemove = new ArrayList<Unit>();
                    for (Unit unit:arrUnits){
                        if (unit.isHasBeensActivatedThisTurn()){
                            arrRemove.add(unit);
                        }
                    }
                    arrUnits.removeAll(arrRemove);
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
                if (isDavout && counter.getUnit().getOfficer().name.contains("avout")){

                }else{
                    canCommand= true;
                }
                if (event.getButton( ) == Input.Buttons.LEFT)
                {
                    if (canCommand){
                        Gdx.app.log("Counter", "Left TouchUp unit=" + counter.getUnit().brigade);
                        arrOfficerSelected.remove(counter.getUnit().getOfficer());
                        arrOfficerAvailable.add(counter.getUnit().getOfficer());
                        displaySelected();
                        displayInRange();
                        DoCommand.instance.addOfficer(counter.getUnit().getOfficer());
                        if (textButtonOK != null){
                            textButtonOK.remove();
                        }

                    }
                }
            }

        });

    }


    public void deleteOfficer(Officer officer) {
        arrOfficerAvailable.remove(officer);
        displayInRange();
    }

    public void addOfficer(Officer officer) {
        if (!arrOfficerAvailableOriginal.contains(officer)){
            return;
        }
        if (!arrOfficerAvailable.contains(officer)) {
            arrOfficerAvailable.add(officer);
        }

        displayInRange();
    }
    public void addButtonOK() {
        textButtonOK = new TextButton(i18NBundle.format("ok"), textButtonStyle);
        textButtonOK.setSize(100, 60);
        textButtonOK.setPosition((window.getWidth() - textButtonOK.getWidth()) / 2, 30);
        textButtonOK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    DoCommand.instance.allocate(commander,arrOfficerSelected);
                    GamePreferences.setWindowLocation(commander.name, (int) window.getX(), (int) window.getY());
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
