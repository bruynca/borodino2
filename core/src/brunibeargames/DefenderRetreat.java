package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.Unit;

public class DefenderRetreat implements Observer {
    /**
     * Changes for Borodino
     * This will handle all retreat processing for defenders
     * 1. Determine if retreat is possible
     */
    int losses;
    public boolean canRetreat = false;
    public int cntUnitsCanToRetreat =0;
    public ArrayList<Unit> arrDefenders = new ArrayList<>();
    public ArrayList<Hex> arrHexPossible = new ArrayList();
    Attack attack;
    boolean isAllies;
    HiliteHex hiliteHex;

    DefenderRetreat(Attack attack) {
        Gdx.app.log("DefenderRetreat", "Constructor");
        arrDefenders.addAll(attack.arrDefenders);
        this.attack = attack;
        arrHexPossible.clear();
        isAllies = attack.arrDefenders.get(0).isAllies;
        cntUnitsCanToRetreat = checkRetreat();
        /**
         * get an array of 3 hexes without checkin terrain
         */

    }

    /**
     * check if there is at lease 1 retreat hex available.
     * @return
     */
    public  int  checkRetreat(){
        Gdx.app.log("DefenderRetreat", "checkRetreat");
        canRetreat = true;
        if (arrDefenders.isEmpty()){
            cntUnitsCanToRetreat = 0;
            return cntUnitsCanToRetreat;
        }
        cntUnitsCanToRetreat = arrDefenders.size();
        if (isPlaceToRetreat()){
            setUpRetreat();
            return cntUnitsCanToRetreat;
        }
        /**
         *  no place to retreat
         */
        canRetreat = false;
        return cntUnitsCanToRetreat;
    }

    private void setUpRetreat() {
        Gdx.app.log("DefenderRetreat", "setUpRetreat");
        for (Unit unit : arrDefenders) {
            ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.Retreat, this);
            unit.getMapCounter().getCounterStack().hilite();
        }
    }

    private boolean isPlaceToRetreat() {
        Gdx.app.log("DefenderRetreat", "isPlaceToRetreat");
        arrHexPossible.addAll(attack.hexTarget.arrSurroundHex);
        ArrayList<Hex> arrRemove = new ArrayList<>();
        Unit unitTest= arrDefenders.get(0);
        for (Hex hex : arrHexPossible) {
            if (isAllies) {
                if (hex.getRussianZoc(0)) {
                    arrRemove.add(hex);
                }
                if (hex.isRussianOccupied[0]) {
                    arrRemove.add(hex);
                }
                if (!hex.canOccupy(unitTest)){
                    arrRemove.add(hex);
                }
            } else {
                if (hex.getAlliedZoc(0)) {
                    arrRemove.add(hex);
                }
                if (hex.isAlliedOccupied[0]) {
                    arrRemove.add(hex);
                }
                if (!hex.canOccupy(unitTest)){
                    arrRemove.add(hex);
                }
            }
        }
        arrHexPossible.removeAll(arrRemove);
        if (arrHexPossible.isEmpty()){
            return false;
        }
        return true;
    }

    public int getLosses() {
        return losses;

    }






    @Override
    public void update(Observable observable, Object o) {

    }


    public void doNextRetreat(Unit unit) {
        arrDefenders.remove(unit);
        if (arrDefenders.isEmpty()){
            attack.afterRetreat();
        }
    }
}


