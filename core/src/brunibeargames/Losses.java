package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

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
    private static I18NBundle i18NBundle;

    boolean areAllEliminated = false;
    boolean isDefender = false;
    public Losses(boolean isDefender) {
        this.isDefender = isDefender;
        areAllEliminated = false;
        i18NBundle = GameMenuLoader.instance.localization;
    }
    public Losses(ArrayList<Unit> arrUnits, boolean isPartial, boolean isDefender) {
        Gdx.app.log("Losses", "Units=" + arrUnits );
        i18NBundle = GameMenuLoader.instance.localization;
        this.isDefender = isDefender;

        if (arrUnits.size() == 0) {
            areAllEliminated = true;
            return;
        }
        for (Unit unit : arrUnits) {
            String str =i18NBundle.format("destroyed", unit.brigade);

            if (isDefender){
                CombatDisplayResults.instance.updateCombatResultsDefender(str);
            }else{
                CombatDisplayResults.instance.updateCombatResultsAttacker(str);
            }
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
     *  Need to change to add for Attacker Retreats
     * @param units
     */
    public void addLosses(ArrayList<Unit> units, TypeLoss typeLoss) {
        for (Unit unit : units)  {
            String str =i18NBundle.format("destroyed", unit.brigade);
            if (typeLoss == TypeLoss.CntRetreat){
                str =i18NBundle.format("cantretreat", unit.brigade);
            }

            if (isDefender){
                CombatDisplayResults.instance.updateCombatResultsDefender(str);
            }else{
                CombatDisplayResults.instance.updateCombatResultsAttacker(str);
            }

            unit.eliminate(false);
            CombatResults cr = CombatResults.find(unit);
            cr.setDestroyed(true);
                areAllEliminated = true;
        }

    }

    public void addLosses(Unit unit, TypeLoss typeLoss) {
        String str =i18NBundle.format("destroyed", unit.brigade);

        if (typeLoss == TypeLoss.CntRetreat){
            str =i18NBundle.format("cantretreat", unit.brigade);
        }

        if (isDefender){
            CombatDisplayResults.instance.updateCombatResultsDefender(str);
        }else{
            CombatDisplayResults.instance.updateCombatResultsAttacker(str);
        }

        unit.eliminate(false);
        CombatResults cr = CombatResults.find(unit);
        cr.setDestroyed(true);
        areAllEliminated = true;

    }
    enum TypeLoss {None, CntRetreat}
}
