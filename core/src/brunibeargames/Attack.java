package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
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
    public ArrayList<Unit> arrLossesExAttacker;


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
        arrLossesExAttacker = new ArrayList<>();
        hexTarget = hex;
        instance = this;
        this.isAllies = isAllies;
        this.isAI = isAI;
        this.isMobileAssualt = isMobileAssualt;
        for (Unit unit : hexTarget.getUnitsInHex()) {
            if ((isAllies && !unit.isAllies) || (!isAllies && unit.isAllies)) {
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
        if (!isAI) {
            CombatDisplay.instance.updateCombat(this, odds);
        }

    }

    public String[] getAttackResults() {
        return AttackOdds.result;
    }

    public int[][] getDice() {
        int[][] tabReturn = new int[2][2];
        /*for (int i = 0; i < tabReturn.length; i++) {
            tabReturn[i][0] = AttackOdds.combatResultTableAttacker[i][AttackOdds.ixTableView][0];
            tabReturn[i][1] = AttackOdds.combatResultTableAttacker[i][AttackOdds.ixTableView][1];
        } */
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
        if (!isAI) {
            CombatDisplay.instance.updateCombat(this, odds);
        }
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
        CombatDisplay.instance.updateCombat(this, odds);
    }

    public Hex getHexTarget() {
        return hexTarget;
    }

    public boolean isAllies() {
        return isAllies;
    }



    public void dieRoll() {
        Gdx.app.log("Attack", "dieRoll");
        for (Unit unit : arrAttackers) {
            if (!isMobileAssualt) {
                unit.setCanAttackThisTurnOff();
            }
            unit.setHasAttackedThisTurn();
            unit.getMapCounter().getCounterStack().removeHilite();
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

        dieResult = attackOdds.getResult(die1);
        DiceEffect.instance.addObserver(this);
        DiceEffect.instance.rollBlueDice(die2);
        //DiceEffect.instance.rollRedDice(die1);

    }
    public void afterDieRoll(){
        Gdx.app.log("Attack", "dieResult=" + dieResult);
         //     dieResult ="D2r2";
        WinCRT.instance.show(this, dieResult);
        String strResult = WinCRT.instance.strResult;
        dieResult = "Dr";
        attackerLoses = 0;
        attackRetreats = 0;
        defenderLoses = 0;
        defenderRetreats = 0;
        /*
            for exchange
         */

        CombatDisplayResults.instance.updateResults(dieResult, this);
        boolean isAttackerAllies = false;
        boolean isDefenseAllies = false;
        if (isAllies) {
            isAttackerAllies = true;
            isDefenseAllies = false;
        }else{
            isAttackerAllies = false;
            isDefenseAllies = true;
        }
        defenderLosses = new Losses(isDefenseAllies);
        attackerLosses = new Losses(isAttackerAllies);


        //      for (int i = 0; i < dieResult.length(); i++) {
            switch (dieResult) {
                case "Ar":
                    attackerLoses = 0;
                    attackRetreats = 1;
                    break;
                case "Dr":
                    defenderLoses = 0;
                    defenderRetreats = 1;
                    break;
                case "De":
                    int cntLose= 0;
                    for (Unit unit:hexTarget.getUnitsInHex()) {
                        cntLose += unit.getCurrentAttackFactor();
                    }
                    defenderLosses.addLosses(arrDefenders);
                    defenderLoses = cntLose;
                    break;
                case "Ae":
                    cntLose= 0;
                    for (Unit unit:arrAttackers){
                        cntLose += unit.getCurrentAttackFactor();
                    }
                    attackerLoses = cntLose;
                    defenderLosses = new Losses(arrDefenders, false, true);
                    attackerLosses = new Losses(arrLossesExAttacker, true, false);

                case "Ex":
                    cntLose= 0;
                    for (Unit unit:hexTarget.getUnitsInHex()) {
                        cntLose += unit.getCurrentAttackFactor();
                    }
                    defenderLoses = cntLose;
                    ArrayList<Unit> arrLose =  findUnitsToEliminate(arrAttackers, arrDefenders);
                    // switch
                    arrLossesExAttacker.clear();
                    arrLossesExAttacker.addAll(arrLose);
                    for (Unit unit:arrLossesExAttacker) {
                        cntLose += unit.getCurrentAttackFactor();
                    }
                    attackerLoses = cntLose;
                    defenderLosses = new Losses(arrDefenders, false,true);
                    attackerLosses = new Losses(arrLossesExAttacker, true, false);

                    defenderRetreats = 1;

                    break;
            }
 //       }
        Gdx.app.log("Attack", "attacker Loses    =" + attackerLoses);
        Gdx.app.log("Attack", "defender Loses    =" + defenderLoses);
        Gdx.app.log("Attack", "defender retreats =" + defenderRetreats);
        Gdx.app.log("Attack", "attacker retreats =" + attackRetreats);

        ArrayList<Unit> arrUnitsToRetreat = new ArrayList<>();
        arrUnitsToRetreat.addAll(arrDefenders);
        /**
         *  defender retreats
         */
        if (defenderLosses.areAllEliminated) {
            isDefendHexVacant = true;
        }
        if (!defenderLosses.areAllEliminated && defenderRetreats > 0) {
            defenderRetreat = new DefenderRetreat(this);
            /**
             *  check if canRetreat showed a display
             *  if not add to losses
             */
            if (defenderRetreat.cntUnitsCanToRetreat > 0) {
                // Defender retreat will have set up click actions for units to retreat
                // wait for fire back
                // see Move(unit, defenderRetreat.arrHexPossible, Move.AfterMove.Retreats, isAI);
                return;
            }else{
                /**
                 *  add to losses and go to next stage
                 */
                defenderLosses.addLosses(arrDefenders);
                afterRetreat();
            }
            /**
             * save all units to retreat and fire the first on not eliminated
             */
           // arrUnitsToRetreat.addAll(arrDefenders);

        }
        afterRetreat();

    }

    /**
     *  after first retreat this does all others
     * @param unitDone
     */
    public void doNextRetreat(Unit unitDone){
        Gdx.app.log("Attack", "doNextRetreat unitDone="+unitDone);


    }


    /**
     * This method is called after a retreat has occurred during combat.
     * It updates the combat display with the results of the advance or continue movement options,
     * and displays the overall combat results.
     */
    public void afterRetreat(){
        Gdx.app.log("Attack", "AfterRetreat");

        /**
         *  update combat display with advanve or continue results
         */
        isDefendHexVacant = true;

        if (isDefendHexVacant && attackerLosses != null && !attackerLosses.areAllEliminated){
            AdvanceAfterCombat.instance.doAdvance(this);
        }else{
            CombatDisplayResults.instance.hide();
            Combat.instance.cleanup(true);
            Combat.instance.doCombatPhase();
        }

        /**
         * display the result of combat
         * We have turned the display with Americans first so need to change flags
         */
   /*     if (isAllies()) {
            CombatDisplayResults.instance.updateCombatResultsDefender(CombatResults.arrCombatAxis, false, this);
            CombatDisplayResults.instance.updateCombatResultsAttacker(CombatResults.arrCombatAllied, false, this);
        }else{
            CombatDisplayResults.instance.updateCombatResultsDefender(CombatResults.arrCombatAllied, true, this);
            CombatDisplayResults.instance.updateCombatResultsAttacker(CombatResults.arrCombatAxis, true, this);
        }
        CombatDisplayResults.instance.updateResults(dieResult, this); */
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
    public static ArrayList<Unit> findUnitsToEliminate(ArrayList<Unit> attackers, ArrayList<Unit> defenders) {
        // Calculate total combat factor of defenders
        int defenderTotal =0;
        for (Unit defender : defenders) {
            defenderTotal += defender.getCurrentAttackFactor();
        }

        // If no defenders or no combat factor to match, return empty list
        if (defenderTotal == 0) {
            return new ArrayList<>();
        }

        int n = attackers.size();
        // If no attackers, return empty list (or handle as per game rules)
        if (n == 0) {
            return new ArrayList<>();
        }

        // Calculate total combat factor of attackers
        int attackerTotal = 0;
        for (Unit attacker : attackers) {
            attackerTotal += attacker.getCurrentAttackFactor();
        }

        // If attackers' total is less than defenders', return all attackers
        if (attackerTotal < defenderTotal) {
            return new ArrayList<>(attackers);
        }

        // Dynamic programming to find minimum number of units
        // dp[i][sum] represents the minimum number of units needed to achieve 'sum' using first i units
        int maxSum = Math.min(attackerTotal, defenderTotal + 100); // Cap sum to avoid excessive memory
        int[][] dp = new int[n + 1][maxSum + 1];
        List<Integer>[][] selectedUnits = new List[n + 1][maxSum + 1];

        // Initialize dp and selectedUnits arrays
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= maxSum; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                selectedUnits[i][j] = new ArrayList<>();
            }
        }
        dp[0][0] = 0; // Base case: no units, sum 0

        // Fill dp table
        for (int i = 1; i <= n; i++) {
            int cf = attackers.get(i - 1).getCurrentAttackFactor();
            for (int j = 0; j <= maxSum; j++) {
                // Don't include unit i-1
                dp[i][j] = dp[i - 1][j];
                selectedUnits[i][j] = new ArrayList<>(selectedUnits[i - 1][j]);

                // Include unit i-1 if it improves the solution
                if (j >= cf && dp[i - 1][j - cf] != Integer.MAX_VALUE) {
                    int newCount = dp[i - 1][j - cf] + 1;
                    if (newCount < dp[i][j]) {
                        dp[i][j] = newCount;
                        selectedUnits[i][j] = new ArrayList<>(selectedUnits[i - 1][j - cf]);
                        selectedUnits[i][j].add(i - 1);
                    }
                }
            }
        }

        // Find the smallest sum >= defenderTotal with minimum units
        int minUnits = Integer.MAX_VALUE;
        int bestSum = defenderTotal;
        for (int sum = defenderTotal; sum <= maxSum; sum++) {
            if (dp[n][sum] < minUnits) {
                minUnits = dp[n][sum];
                bestSum = sum;
            }
        }

        // Construct result list
        ArrayList<Unit> unitsToEliminate = new ArrayList<>();
        if (minUnits != Integer.MAX_VALUE) {
            for (int index : selectedUnits[n][bestSum]) {
                unitsToEliminate.add(attackers.get(index));
            }
        } else {
            // Fallback: return all attackers if no valid combination (shouldn't happen given attackerTotal check)
            return new ArrayList<>(attackers);
        }

        return unitsToEliminate;
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


