package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.Unit;

public class AttackerRetreat implements Observer {

        /*
         * Changes for Borodino
         * This will handle all retreat processing for attacker
         * 1. Determine if retreat is possible
         */
        int losses;
        public boolean canRetreat = false;
        public int cntUnitsCanToRetreat =0;
        public ArrayList<Unit> arrAttackers = new ArrayList<>();
        public ArrayList<Hex> arrHexPossible = new ArrayList();
        Attack attack;
        boolean isAllies;
        HiliteHex hiliteHex;
        private static I18NBundle i18NBundle;
        Losses attackerLosses;
        ArrayList<Unit> arrUnitsRetreated = new ArrayList<>();


        AttackerRetreat(Attack attack, Losses attackerLosses) {
            Gdx.app.log("AttackerRetreat", "Constructor");
            i18NBundle = GameMenuLoader.instance.localization;
            this.attack = attack;
            this.attackerLosses = attackerLosses;
            arrAttackers.clear();
            arrAttackers.addAll(attack.arrAttackers);
            arrUnitsRetreated.clear();
            arrUnitsRetreated.addAll(attack.arrAttackers);
            this.attack = attack;
            arrHexPossible.clear();
            isAllies = attack.arrAttackers.get(0).isAllies;
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
            Gdx.app.log("AttackerRetreat", "checkRetreat");
            canRetreat = true;
            if (arrAttackers.isEmpty()){
                cntUnitsCanToRetreat = 0;
                return cntUnitsCanToRetreat;
            }
            cntUnitsCanToRetreat = arrAttackers.size();
            for (Unit unit : arrAttackers) {
                if (isPlaceToRetreat(unit)){
                    setUpRetreat(unit);
                }else{
                    attackerLosses.addLosses(unit, Losses.TypeLoss.CntRetreat);
                    cntUnitsCanToRetreat--;
                }

            }
            /**
             *  no place to retreat
             */
            canRetreat = false;
            return cntUnitsCanToRetreat;
        }

        private void setUpRetreat(Unit unit) {
            Gdx.app.log("AttackerRetreat", "setUpRetreat");
            ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.AttackerRetreat, this);
            unit.getMapCounter().getCounterStack().hilite();
            String str =i18NBundle.format("retreat", unit.brigade);
            CombatDisplayResults.instance.updateCombatResultsAttacker(str);
        }

        private boolean isPlaceToRetreat(Unit unit) {
            Gdx.app.log("AttackerrRetreat", "isPlaceToRetreat");
            arrHexPossible.clear();
            arrHexPossible.addAll(unit.getHexOccupy().arrSurroundHex);
            ArrayList<Hex> arrRemove = new ArrayList<>();
            Unit unitTest= arrAttackers.get(0);
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







        @Override
        public void update(Observable observable, Object o) {

        }


    public ArrayList<Hex> getArrHexPossible(Unit unit) {
            if (isPlaceToRetreat(unit)){
                return arrHexPossible;
            }else{
                ErrorGame errorGame = new ErrorGame("AttackerRetreat Not Possible What changed", "getArrHexPossible");
                return null; // kaboom
            }
    }

    public void doNextRetreat(Unit unit) {
        Gdx.app.log("AttackerrRetreat", "donextRetreat unit="+unit);

        arrAttackers.remove(unit);
            if (arrAttackers.isEmpty()){
                attack.afterRetreat();
            }else{
                unit  = arrAttackers.get(0);
                if (isPlaceToRetreat(unit)){
                    // do nothing  will depend on click
                }else{
                    attackerLosses.addLosses(unit, Losses.TypeLoss.CntRetreat);
                    cntUnitsCanToRetreat--;
                }
            }
    }

    public ArrayList<Unit> getRetreatedUnits(Attack attack) {
            return arrUnitsRetreated;
    }
}
