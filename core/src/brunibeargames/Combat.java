package brunibeargames;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.UI.BottomMenu;
import brunibeargames.UI.EventPopUp;
import brunibeargames.UI.WinStackCombat;
import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.Unit;
import brunibeargames.Unit.UnitHexToAttack;

public class Combat implements Observer {

    static public Combat instance;
    boolean isAllies;
    boolean isAI;
    private I18NBundle i18NBundle;

    public ArrayList<Hex> arrHexDefender = new ArrayList<>();
    ArrayList<Stack> arrCombatImageStack = new ArrayList<>();
    ArrayList<ClickListener> arrCombatImageStackListners = new ArrayList<>();

//    WinCombat wincombat;
    Hex hexTarget;
    public ArrayList<Hex> arrHexAttackers = new ArrayList<>();
    ArrayList<Hex> arrHexCheckFirstTime = new ArrayList<>();
    ArrayList<Unit> arrTempAttackers = new ArrayList<>();
    ArrayList<UnitHexToAttack> arrUnitHexAttack = new ArrayList<>();

    Hex hexHilite;
    //	HiliteHex hiliteHex;
    Attack attack;
    boolean isCancelAttack = false;
    boolean isAttackArrows = false;
    TextureRegion combat;

    TextButton attackButton;
    TextButton cancelButton;
    TextureRegion attackButtonTex;
    TextureRegion cancelButtonTex;
    TextureRegion attackButtonOverTex;
    TextureRegion cancelButtonOverTex;
    static Vector2 positionWinCombat = new Vector2(-100, 0);
    static Vector2 positionWinEnemy = new Vector2(-100, 0);
    WinStackCombat winStackCombat;
    TextureAtlas textureAtlas;
    public Combat() {
        instance = this;        i18NBundle= GameMenuLoader.instance.localization;
        TextureAtlas tCombat = SplashScreen.instance.effectsManager.get("effects/combat.txt");
        initializeButtons();
    }
    /**
     * Initialize the Combat Phase
     *
     * @param isAllies
     */
    public void intialize(boolean isAllies, boolean isAI) {
        Gdx.app.log("Combat", "Initialize");
        this.isAllies = isAllies;
        this.isAI = isAI;
        Unit.initUnShade();
        Hex.initCombatFlags();
        doCombatPhase();

    }
    public void doCombatPhase() {

        Gdx.app.log("Combat", "doCombatphase");

        SaveGame.SaveLastPhase(" Last Turn CombatStart", 2);
        AttackArrows.getInstance().removeArrows();
        arrUnitHexAttack.clear();
        arrHexDefender.clear();
        arrHexCheckFirstTime.clear();
        if (isAllies){
            Unit.shadeAllAllies();
        }else{
            Unit.shadeAllRussians();
        }
        //CombatDisplay.instance.end();
        if (isAllies) {
            TurnCounter.instance.updateText(i18NBundle.get("combata"));
        }else{
            TurnCounter.instance.updateText(i18NBundle.get("combatr"));

        }
        ArrayList<Unit> arrUnitWorkFindHexesCanAttack;
    /**
     *  get attacking units
     */
        if (isAllies) {
        arrUnitWorkFindHexesCanAttack = Unit.getOnBoardAllied();
        } else {
        arrUnitWorkFindHexesCanAttack = Unit.getOnBoardRussians();
        }
        UnitHexToAttack.startProcess(); // initialize
    /**
     * get hexes that the units can attack
     */
        for (Unit unit : arrUnitWorkFindHexesCanAttack) {
            Hex hexUnit = unit.getHexOccupy();
            if (unit.canAttackThisTurn && !unit.isArtillery&& !unit.isEliminated() && !unit.isDisorganized()&& unit.getCurrentAttackFactor() > 0) {
                ArrayList<Hex> arrHexWork = hexUnit.getSurround();
                for (Hex hex : arrHexWork) {
                        if (hex.getUnitsInHex().size() > 0) {
                            if (isAllies && hex.checkRussianInHex()|| !isAllies && hex.checkAlliesInHex()) {
                                boolean isFree = true;
                                for (Unit unitCheck : hex.getUnitsInHex()) {
                                    if (unitCheck.hasBeenAttackedThisTurn) {
                                        isFree = false;
                                    }
                                }
                                if (!hex.isHasBeenAttackedThisTurn() && isFree) {
                                    arrHexDefender.add(hex);
                                    UnitHexToAttack unitInt = UnitHexToAttack.addUnitHex(unit,hex);
                                }
                            }
                        }
                }
            }else{
                unit.getMapCounter().getCounterStack().shade();
            }
        }
         HexHelper.removeDupes(arrHexDefender);
        //CombatDisplayResults.instance.end();
        WinCRT.instance.end();
        setUpBottom();
        if (arrHexDefender.size() > 0){
            designateWhoCanBeAttacked(arrHexDefender);
        }else{
            if (isAI){
 //               String str = i18NBundle.get("aicombatdone");
 //               EventOK.instance.addObserver((this));
//                EventOK.instance.show(str);
                return;
            }
            CombatDisplayResults.instance.allowFinish();
            String str = i18NBundle.get("nomorecombat");
            EventPopUp.instance.show(str);
            return;
        }
    }
    private void setUpBottom() {
        String strTit = i18NBundle.get("movementphasehelptitle");
        String strT = i18NBundle.get("movementphasehelp");
        BottomMenu.instance.setHelpData(strTit, strT);
        BottomMenu.instance.showInquirNextPhase();
        BottomMenu.instance.hideBack();
        BottomMenu.instance.setWarningPhaseChange(false);
        BottomMenu.instance.setEnablePhaseChange(false);
        BottomMenu.instance.showNextPhase();
        String message= i18NBundle.get("warnmovephase");
        String title = i18NBundle.get("nextphasebutton");
        BottomMenu.instance.setPhaseData(title, message);
        BottomMenu.instance.addObserver(this);

    }

    public void initializeButtons() {
        textureAtlas = SplashScreen.instance.effectsManager.get("effects/combat.txt");
        combat = textureAtlas.findRegion("attack");
        attackButtonTex = textureAtlas.findRegion("attackbutton");
        cancelButtonTex = textureAtlas.findRegion("cancelbutton");
        attackButtonOverTex = textureAtlas.findRegion("attackbuttonover");
        cancelButtonOverTex = textureAtlas.findRegion("cancelbuttonover");

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(new TextureRegionDrawable(attackButtonTex),
                new TextureRegionDrawable(attackButtonOverTex),
                null,
                Fonts.getFont24());
        textButtonStyle.over = new TextureRegionDrawable(attackButtonOverTex);
        if (!Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            textButtonStyle.font.getData().scale(0f);
        }

        attackButton = new TextButton(GameMenuLoader.instance.localization.get("attack"), textButtonStyle);
        attackButton.setSize(200 , 70 );
        attackButton.getLabelCell().padRight(20);
        attackButton.getLabel().setFontScale(2f);

        textButtonStyle = new TextButton.TextButtonStyle(new TextureRegionDrawable(cancelButtonTex),
                new TextureRegionDrawable(cancelButtonOverTex),
                null,
                Fonts.getFont24());
        textButtonStyle.over = new TextureRegionDrawable(cancelButtonOverTex);




     cancelButton = new TextButton(GameMenuLoader.instance.localization.get("cancel"), textButtonStyle);
//     cancelButton.setSize(169, 49);
     cancelButton.setSize(200,70);
     cancelButton.getLabelCell().padLeft(20);
     cancelButton.getLabel().setFontScale(2F);
    }

    private void designateWhoCanBeAttacked(ArrayList<Hex> arrHexDefender) {
        Gdx.app.log("Combat", "designateWhoCanBeAttacked");

        clearStacks();
        ArrayList<Hex> arrHexSchedule = new ArrayList<>();
        for (Hex hex : arrHexDefender) {
            if (isAI){
                createCombatImage(hex, false);
            }else {
                arrHexSchedule.add(hex);
            }
        }
        if (!arrHexSchedule.isEmpty()){
            WinModal.instance.set();
            scheduleAttackHilite(arrHexSchedule);
        }
        /**
         * problem with below is that we must put in logic
         * to attack from mre hexes.
         * Similar to above where we show defender hexes to click on
         */
/*        for (UnitHexToAttack unitHex : arrUnitHexAttack){
            if (isAI){
                createCombatImage(unitHex.getUnit().getHexOccupy(), false);
            }else {
                arrHexSchedule.add(unitHex.getUnit().getHexOccupy());
            }
        }
        if (!arrHexSchedule.isEmpty()){
            WinModal.instance.set();
            scheduleAttackHilite(arrHexSchedule);
        }*/
    }
    private void scheduleAttackHilite(final ArrayList<Hex> arrHexToHilite) {
        Gdx.app.log("Combat", "scheduleAttacks arrHexToHile="+arrHexToHilite);
         final Hex hex = arrHexToHilite.get(0);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               if (NextPhase.instance.getPhase() == Phase.COMBAT.ordinal()) {
                                  // SoundsLoader.instance.playLimber();
                                   createCombatImage(hex, true);
                               }
                               arrHexToHilite.remove(hex);
                               if (arrHexToHilite.size() == 0) {
                                   WinModal.instance.release();
                                   return;
                               } else {
                                   scheduleAttackHilite(arrHexToHilite);
                               }
                           }
                       }
                , .16F        //    (delay)
        );
    }

    public void clearStacks() {
        Gdx.app.log("Combat", "clearStacks");

        for (Stack stack : arrCombatImageStack) {
            stack.remove();
        }
        arrCombatImageStack.clear();
    }
    public void createCombatImage(final Hex hex, boolean isClick) {
        Gdx.app.log("Combat", "createCombatImage");
        Image image = new Image(combat);
        final Stack stack = new Stack();
        arrCombatImageStack.add(stack);
        stack.add(image);
        stack.setSize(136, 156);
        Vector2 pos = hex.getCounterPosition();
        stack.setPosition(pos.x - 10, pos.y - 7);
//        stack.setPosition(pos.x - 18, pos.y - 20);

        final Hex hexClick = hex;
        if (isClick) {
            ClickListener clickListener = new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    attack = new Attack(hex, isAllies,false,isAI,null);
                    createAttackDisplay(hexClick, false);
                }
            };
            stack.addListener(clickListener);
            arrCombatImageStackListners.add(clickListener);
        }else{
        }
//		stack.addAction(Actions.sequence(Actions.fadeOut(0.05f), Actions.fadeIn(0.25f)));
        Borodino.instance.mapStage.addActor(stack);
    }
    public void setAttackForAI(Attack attack){
        this.attack = attack;
    }

    /**
     *
     * @param hex
     */
    public void createAttackDisplay(Hex hex, boolean isAI) {
        /**
         *  retained for AI  replaced by WinStack Combat
         */
        Gdx.app.log("Combat", "createAttackDispay hex=" + hex);
        clearStacks();
        /**
         *  only show Combat attack display for ai legacy
          */
        if (isAI) {
            createCombatImage(hex, false);
        }
        hexTarget = hex;
        /**
         * get all attackers
         */
        ArrayList<Hex> arrHexSurround = hex.getSurround();
        arrTempAttackers = new ArrayList<>();
        arrHexAttackers = new ArrayList<>();
        for (Hex hexCheck : arrHexSurround) {
            if (isAllies && hexCheck.checkAlliesInHex() || !isAllies && hexCheck.checkRussianInHex()) {
                for (Unit unit : hexCheck.getUnitsInHex()) {
                    if (unit.canAttackThisTurn && !unit.isArtillery && !unit.isEliminated()&& unit.getCurrentAttackFactor() > 0) {
                        arrTempAttackers.add(unit);
                        if (!arrHexAttackers.contains(unit.getHexOccupy())) {
                            arrHexAttackers.add(unit.getHexOccupy());
                        }
                        ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.CombatClick);
                        unit.getMapCounter().getCounterStack().removeShade();
                    } else {
                        /**
                         *  so they dont get click
                         */
                        if (unit.isArtillery) {
                            unit.getHexOccupy().moveUnitToBack(unit);
                            unit.getMapCounter().rePlace(unit.getHexOccupy());
  //                          reDisplay();
                        }
                    }
                }
            }
        }

        hexTarget = hex;
        AttackArrows.getInstance().showArrows(arrHexAttackers, hexTarget);
        if (!isAI) {

            winStackCombat = new WinStackCombat(arrTempAttackers, hexTarget);
        }
        //ardenne.instance.addObserver(this);

    }

    /**
     *   For AI
     * @return hexes that can be attacked
     */
    public


    ArrayList<Hex> getAttackedHexesForAI(boolean isAllies){
        ArrayList<Unit> arrUnitWorkFindHexesCanAttack;
        ArrayList<Hex> arrHexReturn = new ArrayList<>();
        if (isAllies) {
            arrUnitWorkFindHexesCanAttack = Unit.getOnBoardAllied();
        } else {
            arrUnitWorkFindHexesCanAttack = Unit.getOnBoardRussians();
        }
        /**
         * get hexes that the units can attack
         */
        for (Unit unit : arrUnitWorkFindHexesCanAttack) {
            Hex hexUnit = unit.getHexOccupy();
            if (unit.canAttackThisTurn && !unit.isArtillery && !unit.isEliminated() && !unit.isDisorganized() && unit.getCurrentAttackFactor() > 0) {
                ArrayList<Hex> arrHexWork = hexUnit.getSurround();
                for (Hex hex : arrHexWork) {
                    if (hex.getUnitsInHex().size() > 0) {
                        if (isAllies && hex.checkRussianInHex() || !isAllies && hex.checkAlliesInHex()) {
                            if (!hex.isHasBeenAttackedThisTurn()) {
                                arrHexReturn.add(hex);
                            }
                        }
                    }
                }
            }
        }
        return arrHexReturn;
    }
    public  ArrayList<Unit> getUnitsCanAttackForAI(Hex hex, boolean isAllies){
        ArrayList<Hex> arrHexSurround = hex.getSurround();
        ArrayList<Unit> arrReturn = new ArrayList<>();
        for (Hex hexCheck : arrHexSurround) {
            if (isAllies && hexCheck.checkAlliesInHex() || !isAllies && hexCheck.checkRussianInHex()) {
                for (Unit unit : hexCheck.getUnitsInHex()) {
                    if (unit.canAttackThisTurn && !unit.isArtillery && !unit.isEliminated()&& unit.getCurrentAttackFactor() > 0) {
                        arrReturn.add(unit);
                    }
                }
            }
        }
        return arrReturn;
    }

    public void refreahAttackCancel(){
        addAttackCancel(hexTarget);
    }
    private void addAttackCancel(Hex hex) {
        Gdx.app.log("Combat", "addAttackCancel");
        Borodino.instance.deleteObserver(this);
        isCancelAttack = true;
        isAttackArrows = true;
        Vector2 pos = hex.getCounterPosition();
        attackButton.setPosition(pos.x + 100, pos.y - 10);
        attackButton.clearListeners();
        attackButton.remove();
        if (!isAI) {
            attackButton.addListener(new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Combat", "addAttackCancel pressed");

                    if (attack.arrAttackers.size() == 0) {
                        //                   EventManager.instance.errorMessage(GameMenuLoader.instance.localization.get("error7"));
                        return;
                    } else {
                        cleanup(true);
                        doDieRoll();
                    }
                    return;
                }
            });
        }
        Borodino.instance.mapStage.addActor(attackButton);
        attackButton.toFront();
//        Unit.stateEngine.getMapStage().addActor(attackButton);

        cancelButton.setPosition(pos.x - 190, pos.y - 10);
        cancelButton.clearListeners();
        cancelButton.remove();
        if (!isAI) {
            cancelButton.addListener(new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    boolean isCancel = true;
                    cleanup(isCancel);
                    doCombatPhase();
                    return;
                }
            });
        }
      Borodino.instance.mapStage.addActor(cancelButton);
       cancelButton.toFront();

    }
    public void doDieRoll(){
        if (attack != null){
            attack.dieRoll();
        }
    }

    public void cleanup(boolean isCancel) {
        Gdx.app.log("Combat", "cleanupDisplay");
        if (isCancel){
                for (Unit unit: arrTempAttackers){
                    if (!unit.isEliminated()) {
                        unit.getMapCounter().getCounterStack().removeHilite();
                    }
                }
                ClickAction.cancelAll();
        }
        clearStacks();
        AttackArrows.getInstance().removeArrows();
        isAttackArrows = false;

        attackButton.clearListeners();
        attackButton.remove();
        cancelButton.clearListeners();
        cancelButton.remove();
  //      CombatDisplayResults.instance.hide();
        WinCRT.instance.end();
        CombatDisplay.instance.end();
        CombatDisplayResults.instance.allowFinish();

        Borodino.instance.deleteObserver(this);

    }
    public boolean isAttackArrows(){
        return isAttackArrows;
    }

    public void removeUnit(Unit unit) {
        Gdx.app.log("Combat", "removeUnit");

        attack.removeAttacker(unit);
        if (attack.arrAttackers.size() == 0){
            attackButton.clearListeners();
            attackButton.remove();
            cancelButton.clearListeners();
            cancelButton.remove();
        }
    }
   public void addUnit(Unit unit) {
       Gdx.app.log("Combat", "addUnit");

       attack.addAttacker(unit, false);
       if (attack.arrAttackers.size() == 1){
 //          addAttackCancel(attack.hexTarget);
       }
 //      addAttackCancel(hexTarget);

   }

    /**
     *  Does not require Combat to be invoked
     * @param isAllies
     * @param aio
     * @return
     */
    public static ArrayList<Hex> getAttackedHexesForAI(boolean isAllies, AIOrders aio){
        ArrayList<Hex> arrHexReturn = new ArrayList<>();
        /**
         * get hexes that the units can attack
         */
        int ix=0;
        for (Unit unit : aio.getArrUnit()) {
            Hex hexUnit = aio.getArrHexMoveTo().get(ix);
            if (unit.canAttackThisTurn && !unit.isArtillery && !unit.isEliminated() && !unit.isDisorganized() && unit.getCurrentAttackFactor() > 0) {
                ArrayList<Hex> arrHexWork = hexUnit.getSurround();
                for (Hex hex : arrHexWork) {
                    if (hex.getUnitsInHex().size() > 0) {
                        if (isAllies && hex.checkRussianInHex() || !isAllies && hex.checkAlliesInHex()) {
                            if (!arrHexReturn.contains(hex)) {
                                arrHexReturn.add(hex);
                            }
                        }
                    }
                }
            }
            ix++;
        }
        return arrHexReturn;
    }

    @Override
    public void update(Observable observable, Object o) {
        Gdx.app.log("Combat", "Observer");

        ObserverPackage oB = (ObserverPackage) o;
        Hex hex = oB.hex;
        /**
         *  OK for AI
         */
        if (oB.type != ObserverPackage.Type.OK && isAI) {
            EventOK.instance.deleteObserver(this);
            NextPhase.instance.nextPhase();
            return;
        }
        /**
         * hex touched
         */
        if (oB.type != ObserverPackage.Type.TouchUp) {
            return;
        }
        if (arrHexAttackers.contains(hex) || hex == hexTarget) {
            return;
        }
        /**
         *  implied combatdisplayresult
         */
        if (winStackCombat != null && winStackCombat.isActive()){
            // do nothing
        }else {

            cleanup(true);   // now done in WinStackCombet
            doCombatPhase();
        }
    }




}

