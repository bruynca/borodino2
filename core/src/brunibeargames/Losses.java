package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import brunibeargames.Unit.Unit;

public class Losses {
    /**
     *  changes to allow for new CRT for Borodino
     *  Losses are all or nothing and we are just going to eliminate
     *  all the units
     */
    boolean areAllEliminated = false;
    public Losses(ArrayList<Unit> arrUnits, int toLose) {
        Gdx.app.log("Losses", "Units=" + arrUnits + " to lose=" + toLose);

        if (toLose == 0) {
            return;
        }
        if (arrUnits.size() == 0) {
            areAllEliminated = true;
            return;
        }
        for (Unit unit : arrUnits) {
            unit.eliminate(false);
            CombatResults cr = CombatResults.find(unit);
            cr.setDestroyed(true);
            areAllEliminated = true;
        }
      /*  ArrayList<Unit> arrSorted = new ArrayList<>(); // sort highest first
        int i =0;
        int test =0;
        for (Unit unit:arrUnits) {
            for (i = 0; i < arrSorted.size(); i++) {
                if (unit.getCurrentStep() > arrSorted.get(i).getCurrentStep()) {
                    break;
                }
            }
            arrSorted.add(i, unit);
        }
        for (Unit unit:arrSorted) {
            Gdx.app.log("Losses", "sorted unit=" + unit+" steps="+unit.getCurrentStep());
        }

        /**
         *  lose all to lose max is 4
         */
     /*   int ix = 0;
        while (toLose >0) {
            Unit unitSuffer = arrSorted.get(ix);
            if (unitSuffer.canStepLoss()) {
                unitSuffer.reduceStep();
                CombatResults cr = CombatResults.find(unitSuffer);
                cr.setStepLosses(true);
            }else {
                unitSuffer.eliminate(false);
                arrSorted.remove(unitSuffer);
                CombatResults cr = CombatResults.find(unitSuffer);
                cr.setDestroyed(true);
            }
            toLose--;
            ix++;
            if (arrSorted.isEmpty()) {
                areAllEliminated = true;
                toLose=0;
                break;
            }
            if (ix >= arrSorted.size()) {
                ix =0;
            }
        }*/
    }
    public boolean getAreAlliminated(){
        return areAllEliminated;
    }
}
