package brunibeargames.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.Hex;
import brunibeargames.HiliteHex;
import brunibeargames.ObserverPackage;
import brunibeargames.UI.EventConfirm;

import static com.badlogic.gdx.Gdx.app;

public class ClickAction implements Observer {
    Unit unit;
    public TypeAction typeAction;
    static ArrayList<ClickAction> arrClickAction = new ArrayList<>();
    static Unit unitInProcess;
    static Hex hexProcess;
    static boolean isLocked = false;
    private HiliteHex hiliteHex;
    private I18NBundle i18NBundle;


    public ClickAction(Unit unit, TypeAction type) {

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
        app.log("ClickAction","Click unit="+unit+unit.ID);
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
            case Limber:
            case CombatClick:
              break;
            case Advance:
                break;
            case Supply:
            case SelectDelete:
                Gdx.app.log("ClickAction", "Select clicked on unit" + unit);
//                SecondPanzerLoses.instance.select(unit);
            default:
                break;
        }
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
        switch(typeAction){
            case Move:
           case Advance:
            default:
                break;
        }

    }
    private void moveUnit(Unit unit, Hex hex, boolean isAI){

    }
    public void moveSetup(Unit unit){
        unit.getMapCounter().counterStack.hilite();
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

    public enum TypeAction {Move, Limber, CombatClick,
        Command, Supply, Advance,SelectDelete};

}
