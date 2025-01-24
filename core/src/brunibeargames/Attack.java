package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import brunibeargames.Unit.Unit;

public class Attack extends Observable implements Observer  {
    ArrayList<Unit> arrAttackers = new ArrayList<>();
    ArrayList<Unit> arrDefenders = new ArrayList<>();
    Hex hexTarget;
    boolean isAllies;
    boolean isMobileAssualt;
    boolean isAI;
    String odds;
    AttackOdds attackOdds;
    float attackStrength;
    float defenseStrength;
    int attackerLoses;
    int attackRetreats;
    int defenderLoses;
    int defenderRetreats;
    Losses defenderLosses;
    Losses attackerLosses;
    boolean isDefendHexVacant = false;
    DefenderRetreat defenderRetreat;
    static public Attack instance;
    boolean isVillage;
    boolean isTown;
    boolean isBridge;
    boolean isTrees;
    boolean isRiver;
    boolean isMechAttack;
    String dieResult;



    public boolean isVillage() {
        return isVillage;
    }

    public boolean isTown() {
        return isTown;
    }

    public boolean isTrees() {
        return isTrees;
    }

    public boolean isRiver() {
        return isRiver;
    }

    public boolean isMechAttack() {
        return isMechAttack;
    }

    public boolean isBridge() {
        return isBridge;
    }

    public String getAttackOdd(){
        return attackOdds.oddactualString;
    }
    public float getActualOdds(){
        return attackOdds.oddsCheck;
    }

    public Attack(Hex hex, boolean isAllies, boolean isMobileAssualt, boolean isAI, Unit unitMobileAssualt) {
        if (!isAI) {
            Gdx.app.log("Attack", "Constructor Hex=" + hex);
        }
        hexTarget = hex;
        instance = this;
        this.isAllies = isAllies;
        this.isAI = isAI;
        this.isMobileAssualt = isMobileAssualt;
        for (Unit unit : hexTarget.getUnitsInHex()) {
            if ((isAllies && !!unit.isAllies) || (!isAllies && unit.isAllies)) {
                arrDefenders.add(unit);
 //                   if (unit.getInSupplyThisTurn()) {
                        defenseStrength += unit.getCurrentAttackFactor();
 //                   } else {
 //                       defenseStrength += unit.getCurrentDefenseFactor() / 2;
 //                   }
                }
            }

        attackOdds = new AttackOdds(this);
        odds = attackOdds.oddactualString;
    }

    public String[] getAttackResults() {
        return AttackOdds.result;
    }

    public int[][] getDice() {
        int[][] tabReturn = new int[AttackOdds.combatResultTableAttacker.length][2];
        for (int i = 0; i < tabReturn.length; i++) {
            tabReturn[i][0] = AttackOdds.combatResultTableAttacker[i][AttackOdds.ixTableView][0];
            tabReturn[i][1] = AttackOdds.combatResultTableAttacker[i][AttackOdds.ixTableView][1];
        }
        return tabReturn;
    }

    public void addAttacker(Unit unit, boolean isAI) {
        if (!isAI) {
            Gdx.app.log("Attack", "add attacker =" + unit);
        }
        if (arrAttackers.contains(unit)) {
            return;
        }
        arrAttackers.add(unit);
        updateDisplayFlags();
        attackStrength = calcAttackStrength();
        attackOdds.update();
        odds = attackOdds.oddactualString;
    }

    private void updateDisplayFlags() {
        isBridge = false;
        isMechAttack = false;
        isRiver = false;
        isTown = false;
        isTown = false;
        isVillage = false;

    }

    private int calcAttackStrength() {
        if (!isAI) {
            Gdx.app.log("Attack", "calacAttackStregtn");
        }

        int retAttack = 0;
        for (Unit unit : arrAttackers) {
            Hex hex;

            hex = unit.getHexOccupy();
            float cntAttackStrength = unit.getCurrentAttackFactor();

  //          if (unit.inSupply()) {
 //           } else {
 //               cntAttackStrength /= 2;
 //           }
            retAttack += cntAttackStrength + .5f;
        }
        return retAttack;
    }

    public void removeAttacker(Unit unit) {
        Gdx.app.log("Attack", "remove attacker =" + unit);
        if (arrAttackers.contains(unit)) {
            arrAttackers.remove(unit);
        }
        updateDisplayFlags();
        attackStrength = calcAttackStrength();
        attackOdds.update();
        odds = attackOdds.oddactualString;
    }

    public Hex getHexTarget() {
        return hexTarget;
    }

    public boolean isAllies() {
        return isAllies;
    }


    ArrayList<Unit> arrUnitsToRetreat = new ArrayList<>();

    public void dieRoll() {
        Gdx.app.log("Attack", "dieRoll");
        for (Unit unit : arrAttackers) {
            if (!isMobileAssualt) {
                unit.setCanAttackThisTurnOff();
            }
            unit.setHasAttackedThisTurn();
        }
        for (Unit unit : arrDefenders) {
            unit.setHasbeenAttackedThisTurn();
        }

        CombatResults.init();
        attackOdds.update();
  //      CombatDisplay.instance.end();
 //       int calc = 0;
 //       for (int i=0; i< 10000; i++){
 //           calc += getDieRoll();
 //       }
        int die1 = getDieRoll();
        int die2 = getDieRoll();

 //       die1 =6;
 //       die2 = 6;
        Gdx.app.log("Attack", "die=" + die1 + " " + die2);

        dieResult = attackOdds.getResult(die1, die2);
        DiceEffect.instance.addObserver(this);
        DiceEffect.instance.rollBlueDice(die2);
        DiceEffect.instance.rollRedDice(die1);

    }
    public void afterDieRoll(){
        Gdx.app.log("Attack", "dieResult=" + dieResult);
         //     dieResult ="D2r2";
        WinCRT.instance.show(this, dieResult);

        for (int i = 0; i < dieResult.length(); i++) {
            switch (dieResult.charAt(i)) {
                case 'A':
                    attackerLoses = Character.getNumericValue(dieResult.charAt(i + 1));
                    break;
                case 'D':
                    defenderLoses = Character.getNumericValue(dieResult.charAt(i + 1));
                    break;
                case 'r':
                    defenderRetreats = Character.getNumericValue(dieResult.charAt(i + 1));
                    break;
            }
        }
        Gdx.app.log("Attack", "attacker Loses    =" + attackerLoses);
        Gdx.app.log("Attack", "defender Loses    =" + defenderLoses);
        Gdx.app.log("Attack", "defender retreats =" + defenderRetreats);

        defenderLosses = new Losses(arrDefenders, defenderLoses);
        attackerLosses = new Losses(arrAttackers, attackerLoses);
        /**
         *  defender retreats
         */
        if (defenderLosses.areAllEliminated) {
            isDefendHexVacant = true;
        }
        arrUnitsToRetreat.clear();
        if (!defenderLosses.areAllEliminated && defenderRetreats > 0) {
            defenderRetreat = new DefenderRetreat(this);
            if (defenderRetreat.losses > 0) {
                defenderLosses = new Losses(arrDefenders, defenderRetreat.losses);
            }
            /**
             * save all units to retreat and fire the first on not eliminated
             */
            arrUnitsToRetreat.addAll(arrDefenders);

            for (Unit unit : arrDefenders) {
                if (!unit.isEliminated()) {
                    CombatResults cr = CombatResults.find(unit);
                    cr.setHexesRetreated(defenderRetreat.arrRetreatPath.size());
                    instance = this;
                    Move.instance.actualMove(unit, defenderRetreat.arrRetreatPath, Move.AfterMove.Retreats, isAI);
                    return; //
                }
            }
            isDefendHexVacant = true;
        }
        afterRetreat();

    }

    /**
     *  after first retreat this does all others
     * @param unitDone
     */
    public void doNextRetreat(Unit unitDone){
        Gdx.app.log("Attack", "doNextRetreat unitDone="+unitDone);

        arrUnitsToRetreat.remove(unitDone);
        ArrayList<Unit> arrRemove = new ArrayList<>();

        for (Unit unit : arrUnitsToRetreat) {
            if (!unit.isEliminated()) {
  //              arrUnitsToRetreat.add(unit); // why ??
                Gdx.app.log("Attack", "doNextRetreat UnitNot Elim");
                CombatResults cr = CombatResults.find(unit);
                cr.setHexesRetreated(defenderRetreat.arrRetreatPath.size());
                Move.instance.actualMove(unit, defenderRetreat.arrRetreatPath, Move.AfterMove.Retreats, isAI);
                return; //
            }else {
                arrRemove.add(unit);
            }
        }
        arrUnitsToRetreat.removeAll(arrRemove);
        if (arrUnitsToRetreat.size() == 0) {
            isDefendHexVacant = true;
            afterRetreat();
        }
    }


    public void afterRetreat(){
        Gdx.app.log("Attack", "AfterRetreat");

        /**
         *  update combat display with advanve or continue results
         */

        if (isDefendHexVacant && attackerLosses != null && !attackerLosses.areAllEliminated){
            if (isMobileAssualt) {
                for (Unit unit : arrAttackers) {
                    if (!unit.isEliminated() && unit.getCurrentMoveFactor() > 0){
                        CombatResults cb = new CombatResults(unit);
                        cb.setCanContinueMovement(true);
                    }
                }
            }else{
                for (Unit unit : arrAttackers) {
                    if (!unit.isEliminated()){
                        CombatResults cb = new CombatResults(unit);
                        cb.setCanAdvance(true);
                    }
                }
            }
        }

        /**
         * display the result of combat
         * We have turned the display with Americans first so need to change flags
         */
        if (isAllies()) {
            CombatDisplayResults.instance.updateCombatResultsDefender(CombatResults.arrCombatAxis, false, this);
            CombatDisplayResults.instance.updateCombatResultsAttacker(CombatResults.arrCombatAllied, false, this);
        }else{
            CombatDisplayResults.instance.updateCombatResultsDefender(CombatResults.arrCombatAllied, true, this);
            CombatDisplayResults.instance.updateCombatResultsAttacker(CombatResults.arrCombatAxis, true, this);
        }
        CombatDisplayResults.instance.updateResults(dieResult);
        return;

    }

    /**
     *
     * @param object
     * @return true if advance after
     */
    public void afterDisplay(Object object){
        Gdx.app.log("Attack", "AfterDisplay invoked by:"+object.toString());

        /**
         *  defender doesnt retreat and mobile assault stop
         */
        if (!isDefendHexVacant && isMobileAssualt) {
            for (Unit unit : arrAttackers) {
                if (!unit.isEliminated()) {
                    unit.getMapCounter().removeClickAction();
                    unit.getMapCounter().getCounterStack().shade();
                }
            }
            return;
        }
        if (attackerLosses.getAreAlliminated()){
            Combat.instance.cleanup(true);
            Combat.instance.doCombatPhase();
        }
        if (isDefendHexVacant){
{
                if (isAI) {
                    Gdx.app.log("Attack", "Check AIAdvance");

                    setChanged();
                    notifyObservers(new ObserverPackage(ObserverPackage.Type.Advance, null, 0, 0));
                    return;
                }
                AdvanceAfterCombat.instance.doAdvance(this);
                return;
            }
        }
        if (!isAI) {
            Combat.instance.cleanup(true);
            Combat.instance.doCombatPhase();
        }else{
            setChanged();
            notifyObservers(new ObserverPackage(ObserverPackage.Type.AfterAttackDisplay,null,0,0));
        }
        return;

    }
    public int getDieRoll()
    {
        Random diceRoller = new Random();
        int diceResult = diceRoller.nextInt(6) + 1;
//        int die = diceRoller.nextInt(6) + 1;
        int die = (int)(Math.random()*6) + 1;
        return die;

    }
    public float getAttackStrength() {
        return attackStrength;
    }
    public ArrayList<Unit> getAttackers(){
        return arrAttackers;
    }
    public float getDefenseStrength() {
        return defenseStrength;
    }


    @Override
    public void update(Observable observable, Object o) {
        ObserverPackage oB = (ObserverPackage) o;
        /**
         *  Hex touched
         */
        if (oB.type == ObserverPackage.Type.DiceRollFinished) {
            afterDieRoll();
            DiceEffect.instance.deleteObserver(this);
            return;
        }

    }
 }


