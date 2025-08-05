package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.UI.EventPopUp;
import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.Unit;

import static com.badlogic.gdx.Gdx.app;

public class DefenderAdvance implements Observer {
    static public DefenderAdvance instance;
    public DefenderAdvance(){
        instance = this;
    }
    Attack attack;
    int numHexAdvance;
    boolean isSeen = false;
    ArrayList<Hex> arrHexOK = new ArrayList<>();
    ArrayList<Hex> arrHexAdvance = new ArrayList<>();
    ArrayList<Unit> arrUnitsToAdvance =  new ArrayList<>();
    ArrayList<Unit> arrSanity = new ArrayList<>();
    private static I18NBundle i18NBundle;


    /**
     * got here because hex was vacated
     *
     * @param attack
     * @param arrAdvanceTo
     */
    public void doAdvance(Attack attack, ArrayList<Hex> arrAdvanceTo) {
        Gdx.app.log("DefenderAdvance", "doAdvance=" + attack.hexTarget);
        if (!isSeen){
            isSeen = true;
            //          HelpPage.instance.showOther("advanceafterhelp");
        }
        i18NBundle = GameMenuLoader.instance.localization;
        arrHexAdvance.clear();
        arrHexAdvance.addAll(arrAdvanceTo);
        arrUnitsToAdvance.clear();
        arrUnitsToAdvance.addAll(attack.arrDefenders);
        TurnCounter.instance.blinkAdvance();
        this.attack = attack;
        arrHexOK.clear();
        arrHexOK.addAll(arrAdvanceTo);
        for (Unit unit:arrUnitsToAdvance){
            if (!unit.isEliminated()) {
                String str =i18NBundle.format("canadvance", unit.brigade);
                unit.getMapCounter().getCounterStack().hilite();
                CombatDisplayResults.instance.updateCombatResultsDefender(str);
                ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.AdvanceDefender);
                arrHexOK.add(unit.getHexOccupy());
                AttackArrows.getInstance().removeArrows();
            }

        }
        /**
         * shade units that did not attack
         */
      /*  for (Hex hex:arrHexCheck){
            for (Unit unit:hex.getUnitsInHex()){
                if (!attack.arrAttackers.contains(unit)){
                    unit.getMapCounter().getCounterStack().shade();
                }
            }
        } */

        Borodino.instance.addObserver(this);
        //      arrUnitsToAdvance.addAll(attack.arrAttackers);
        //      HelpPage.instance.showOther("advance");
        if (arrHexAdvance.isEmpty()){
            end();
        }
    }
    public void end(){
        Gdx.app.log("DefenderAdvance", "end");
        for (Unit unit:arrUnitsToAdvance){
            unit.getMapCounter().getCounterStack().removeHilite();
        }
        TurnCounter.instance.stopAdvance();
        ClickAction.cancelAll();
        CombatDisplayResults.instance.allowFinish();
        Borodino.instance.deleteObserver(this);
        Combat.instance.doCombatPhase();
    }

    /**
     *  is this the end of all possible advances for this combat
     */
    public void checkEnd(Unit unit){
        Gdx.app.log("DefendersAdvance", "checkend unit="+unit);

        arrUnitsToAdvance.remove(unit);
        if (arrUnitsToAdvance.size() == 0){
            end();
        }
    }







    @Override
    public void update(Observable observable, Object o) {
        ObserverPackage oB = (ObserverPackage) o;
        Hex hex = oB.hex;
        /**
         *  Hex touched
         */
        app.log("Defender Advance","observer update hex="+hex);

        if (oB.type != ObserverPackage.Type.TouchUp) {
            return;
        }
        if (EventPopUp.instance.isShowing()){
            return;
        }
      //  app.log("Defender Advance","observer update hex="+hex);

        if (arrHexOK.contains(hex)) {
            return;
        }else{
            end();
        }
    }

    public ArrayList<Hex> getHexes() {
        return arrHexAdvance;
    }
}
