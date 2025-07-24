package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import brunibeargames.Unit.Unit;

public class Losses {
    /**
     *  changes to allow for new CRT for Borodino
     *  Losses are all or nothing and we are just going to eliminate
     *  all the units
     *
     *
     */
    boolean areAllEliminated = false;
    public Losses() {
        areAllEliminated = false;
    }
    public Losses(ArrayList<Unit> arrUnits, boolean isPartial) {
        Gdx.app.log("Losses", "Units=" + arrUnits );


        if (arrUnits.size() == 0) {
            areAllEliminated = true;
            return;
        }
        for (Unit unit : arrUnits) {
            unit.eliminate(false);
            CombatResults cr = CombatResults.find(unit);
            cr.setDestroyed(true);
            if (!isPartial) {
                areAllEliminated = true;
            }
        }
    }
    public boolean getAreAlliminated(){
        return areAllEliminated;
    }

    /**
     *  add to losees
     *  first case is units that dont have retreat path
     * @param arrDefenders
     */
    public void addLosses(ArrayList<Unit> arrDefenders) {
        for (Unit unit : arrDefenders)  {
            unit.eliminate(false);
            CombatResults cr = CombatResults.find(unit);
            cr.setDestroyed(true);
                areAllEliminated = true;
        }

    }
}
