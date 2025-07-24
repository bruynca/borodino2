package brunibeargames.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.AIUtil;
import brunibeargames.AdvanceAfterCombat;
import brunibeargames.Combat;
import brunibeargames.DefenderRetreat;
import brunibeargames.Hex;
import brunibeargames.HiliteHex;
import brunibeargames.Move;
import brunibeargames.ObserverPackage;
import brunibeargames.UI.EventConfirm;
import brunibeargames.UI.StopImage;

import static com.badlogic.gdx.Gdx.app;
import static com.badlogic.gdx.Gdx.input;

public class ClickAction implements Observer {
    Unit unit;
    public TypeAction typeAction;
    static ArrayList<ClickAction> arrClickAction = new ArrayList<>();
    static Unit unitInProcess;
    static Hex hexProcess;
    static boolean isLocked = false;
    private HiliteHex hiliteHex;
    private I18NBundle i18NBundle;
    DefenderRetreat defenderRetreat;


    public ClickAction(Unit unit, TypeAction type) {

        this.unit = unit;
        typeAction = type;
        unit.getMapCounter().addClickAction(this);
        arrClickAction.add(this);
        unitInProcess = null; // when new clickactions added
    }
    public ClickAction(Unit unit, TypeAction type, DefenderRetreat defenderRetreat) {
        this.defenderRetreat = defenderRetreat;
        this.unit = unit;
        typeAction = type;
        unit.getMapCounter().addClickAction(this);
        arrClickAction.add(this);
        unitInProcess = null; // when new clickactions added
    }

    /**
     *  USE FO AI
     * @param unit
     * @param type
     * @param b
     */
    public ClickAction(Unit unit, TypeAction type, boolean b) {
        this.unit = unit;
        typeAction = type;
//        unit.getMapCounter().addClickAction(this);
        arrClickAction.add(this);
        unitInProcess = null; // when new clickactions added

    }
    public Unit getUnit(){
        return unit;
    }

    public static void clearClickListeners(){
        arrClickAction.clear();
    }
    public static void clearClickListenersOnUnit(){
        for (ClickAction cl:arrClickAction){
            if (cl.unit.getMapCounter() != null){
                cl.unit.getMapCounter().removeClickAction();
            }
        }
        arrClickAction.clear();
    }


    public static void unLock() {
        isLocked = false;
        unitInProcess = null;
    }


    public void click() {
        app.log("ClickAction","Click unit="+unit+unit.brigade);
        if (isLocked){
            return;
        }
        if (unitInProcess != null){
            if (unitInProcess.getMapCounter() != null){
                if (unitInProcess.getMapCounter().clickAction != null){
                    unitInProcess.getMapCounter().clickAction.cancel();
                    unitInProcess = null;
                        return;
                }else{
                    unitInProcess = null;
                    return; // click action still there
                }
            }else{
                int brk = 0;
            }

        }else{
            int brk =0;
        }
 //       if (hexProcess != null && hexProcess == unit.getHexOccupy()){
//            app.log("ClickAction", "Hex Used");
//            hexProcess = null;
//            return;
//        }
        unitInProcess = unit;

        switch(typeAction){

            case Move:
                Gdx.app.log("ClickAction", "Move on unit" + unit);
                moveSetup(unit);
            case Limber:
            case CombatClick:
                Gdx.app.log("ClickAction", "Combat clicked on unit" + unit);
                unitInProcess = null; //Combat does not extend beyond click
                if (unit.getMapCounter().getCounterStack().isHilited()){
                    unit.getMapCounter().getCounterStack().removeHilite();
                    Combat.instance.removeUnit(unit);
                    return;
                }
                /**
                 *  Check for all units in stack
                 */
                if (input.isKeyPressed(Input.Keys.SHIFT_LEFT) || input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                    for (Unit unitCheck:unit.getHexOccupy().getUnitsInHex()){
                        if (unitCheck.canAttackThisTurn){
                            unitCheck.getMapCounter().getCounterStack().hilite();
                            Combat.instance.addUnit(unitCheck);
                        }
                    }
                    return;
                }else{
                    unit.getMapCounter().getCounterStack().hilite();
                    Combat.instance.addUnit(unit);
                }


              break;
            case Advance:
                app.log("ClickAction", "Advance clicked on unit" + unit);
                /**
                 */
                unit.getMapCounter().counterStack.hilite();
                ArrayList<Hex> arrHexWork = AdvanceAfterCombat.instance.getPossible(unit);
                if (arrHexWork.size()> 0) {
                    hiliteHex = new HiliteHex(AdvanceAfterCombat.instance.getPossible(unit), null,HiliteHex.TypeHilite.Advance, this);
                    AdvanceAfterCombat.instance.addToOK(arrHexWork);
                }else{
                    AdvanceAfterCombat.instance.checkStillPossible();
                }
                break;
            case Supply:
            case Retreat:
                Gdx.app.log("ClickAction", "Retreat on " + unit);
                retreatSetup(unit,defenderRetreat);

                break;

            case SelectDelete:
                Gdx.app.log("ClickAction", "Select clicked on unit" + unit);
//                SecondPanzerLoses.instance.select(unit);
            default:
                break;
        }
    }

    private void retreatSetup(Unit unit, DefenderRetreat defenderRetreat) {
        unit.getMapCounter().counterStack.hilite();
        HiliteHex.TypeHilite type = HiliteHex.TypeHilite.Retreat;
        hiliteHex = new HiliteHex(defenderRetreat.arrHexPossible, null, type, this);
    }

    public void cancel(){
        unit.getMapCounter().counterStack.removeHilite();
        unitInProcess = null;
        switch(typeAction){
            case Move:
                if (hiliteHex != null) {
                    hiliteHex.remove();
                }
                break;
            case Advance:
                if (hiliteHex != null) {
                    hiliteHex.remove();
                }
                break;

            default:
                break;

        }
    }
    static public void cancelAll(){
        Gdx.app.log("ClickAction", "cancelAll");

        for (ClickAction clickAction:arrClickAction){
            if (!clickAction.unit.isEliminated()) {
                if (clickAction.hiliteHex != null) {
                    clickAction.hiliteHex.remove();
                }
                if (clickAction.unit.getMapCounter() != null) {
                    clickAction.unit.getMapCounter().removeClickAction();
                }

            }
       }

        unitInProcess = null;

    }
    private Unit unitConfirm;
    private Hex  hexConfirm;
    public void process(Hex hex, boolean isAI, HiliteHex.TypeHilite type) {
        app.log("ClickAction", "process " );
        switch(typeAction){
            case Move:
                app.log("ClickAction", "process Move " + unit+" toHex="+hex);
                moveUnit(unit, hex, isAI);
           case Advance:
               app.log("ClickAction", "process Advance=" + unit+" toHex="+hex);
               unit.getMapCounter().getCounterStack().removeHilite();
               unit.getMapCounter().removeClickAction();
               hiliteHex.remove();
               Move.instance.moveUnitAfterAdvance(unit, hex);
               hexProcess = hex;
               AdvanceAfterCombat.instance.checkEnd(unit);
               break;
            case Retreat:
                app.log("ClickAction", "process Retreat=" + unit+" toHex="+hex);
                unit.getMapCounter().getCounterStack().removeHilite();
                unit.getMapCounter().removeClickAction();
                hiliteHex.remove();
                Move.instance.moveUnitAfterAdvance(unit, hex);
                hexProcess = hex;
                defenderRetreat.doNextRetreat(unit);
                break;
            default:
                break;
        }

    }
    private void moveUnit(Unit unit, Hex hex, boolean isAI){
        isLocked = true;
        unit.getMapCounter().getCounterStack().removeHilite();
        unit.getMapCounter().removeClickAction();
        hiliteHex.remove();
        Move.instance.moveUnitFromClick(unit, hex, isAI);
        hexProcess = hex;


    }
    public static int getClickActionsLeft(){
        return arrClickAction.size();
    }
    public static ArrayList<ClickAction> getClickActions(){
        return arrClickAction;
    }

    @Override
    public void update(Observable observable, Object o) {
        ObserverPackage oB = (ObserverPackage) o;
        /**
         *  if yes kick off processing for that type
         */
        if (((ObserverPackage) o).type == ObserverPackage.Type.ConfirmYes){
            EventConfirm.instance.deleteObserver(this);
            moveUnit(unitConfirm,hexConfirm, false);
            return;

        }else{
            EventConfirm.instance.deleteObserver(this);
            return;
        }
    }
    public void moveSetup(Unit unit){
        unit.getMapCounter().counterStack.hilite();

        UnitMove unitMove;
        unitMove = new UnitMove(unit,unit.getCurrentMovement(),false,true,0);

        ArrayList<Hex> arrHexMove = new ArrayList<>();
        arrHexMove.addAll(unitMove.getMovePossible());
        ArrayList<Hex> arrRemove =  getOverStacked(unit, arrHexMove);
        arrHexMove.removeAll(arrRemove);
        ArrayList<Hex> arrRoadMarch = new ArrayList<>();
        AIUtil.RemoveDuplicateHex(arrHexMove);
        arrRoadMarch.addAll(RoadMarch.getMovePossible(unit));
        arrRoadMarch.removeAll(arrHexMove);
        HiliteHex.TypeHilite type = HiliteHex.TypeHilite.Move;
        hiliteHex = new HiliteHex(arrHexMove,arrRoadMarch, type, this);

    }

    /**
     * Rules is ony 2 units per hex if sam division then 3 units per hex
     *
     * @param unit       that is moving
     * @param arrHexMove the move arre=ay passed by UnitMove
     * @return all overstacked hexs
     * assume that passed arrhexcMoves have same type ie allies or russian
     */
    private ArrayList<Hex> getOverStacked(Unit unit, ArrayList<Hex> arrHexMove) {
        ArrayList<Hex> arrRemove = new ArrayList<>();
        ArrayList<Hex> arrWork = new ArrayList<>();
        arrWork.addAll(arrHexMove);
        for (Hex hex : arrWork) {
            if (!hex.canOccupy(unit)) {
               arrRemove.add(hex);
            }
        }
        createStopImages(arrHexMove, arrRemove);
        return arrRemove;
    }

    /**
     * Create Stop images - only do for hexes that are completely surrounded
     * @param arrHexMove
     * @param arrRemove
     */
    private void createStopImages(ArrayList<Hex> arrHexMove, ArrayList<Hex> arrRemove) {
        ArrayList<Hex> arrWork = new ArrayList<>();
        for (Hex hex : arrRemove) {
            boolean isSurround = true;
            for (Hex hex1 : hex.getSurround()) {
                if (!arrHexMove.contains(hex1)) {
                    isSurround = false;
                    break;
                }
            }
            if (isSurround) {
                arrWork.add(hex);
            }
        }
        for (Hex hex:arrWork){
            StopImage stopImage = new StopImage(hex);
        }



    }

    public enum TypeAction {Move, Limber, CombatClick, Retreat,
        Command, Supply, Advance,SelectDelete};

}
