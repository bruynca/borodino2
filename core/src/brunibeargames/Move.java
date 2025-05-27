package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;


import java.util.ArrayList;
import java.util.Observable;

import brunibeargames.UI.EventPopUp;
import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.Counter;
import brunibeargames.Unit.Unit;
import brunibeargames.Unit.UnitMove;

public class Move extends Observable {
    static public Move instance;
    private I18NBundle i18NBundle;

    boolean isAllies;
    boolean isAI;
    ArrayList<Unit> arrUnitsInMoa = new ArrayList<>();
    public ArrayList<Hex> arrEntryPoints = new ArrayList<>();
    public Move(){
        instance = this;
        i18NBundle= GameMenuLoader.instance.localization;

    }

    /**
     * Start of the move
     * issue any warnings or instructions
     *
     * @param isAllies
     */
    public void intializeMove(boolean isAllies, boolean isAI, boolean isExplotation) {
        this.isAllies = isAllies;
        this.isAI = isAI;

        doMovePhase(isAllies);
    }
    /**
     * Set all on boards unit counters to receive move requests
     *
     * @param isAllies
     * */
    public void doMovePhase(boolean isAllies) {
        Gdx.app.log("Move", "doMovePhase allies=" + isAllies);
        this.isAllies = isAllies;
        ArrayList<Unit> arrUnitToMoveWork;
        if (isAllies) {
            arrUnitToMoveWork = Unit.getOnBoardAllied();
        } else {
            arrUnitToMoveWork = Unit.getOnBoardAxis();
        }
        /**
         * Check Turn last shade
         */
        int turn = NextPhase.instance.getTurn();
        TurnCounter.instance.updateText(i18NBundle.get("move"));

        ArrayList<Unit> arrUnitToDisplay = new ArrayList<>();
        /**
         *  for restarted games logic   not much at present
         */
        for (Unit unit : arrUnitToMoveWork) {
            arrUnitToDisplay.add(unit);

        }
        if (!arrUnitToDisplay.isEmpty()) {
            WinModal.instance.set();
            scheduleMoveHilite(arrUnitToDisplay);
        }else{
            anyMovesLeft(isAI);
        }

    }
    private void scheduleMoveHilite(final ArrayList<Unit> arrUnitToMove) {
        final Unit unit = arrUnitToMove.get(0);
        Timer.schedule(new Timer.Task(){
                           @Override
                           public void run() {

                               SoundsLoader.instance.playLimber();
                               unit.getMapCounter().getCounterStack().removeShade();
                               unit.getHexOccupy().moveUnitToFront(unit);
                               Counter.rePlace(unit.getHexOccupy());
                               ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.Move);
                               arrUnitToMove.remove(unit);
                               if (arrUnitToMove.size() == 0 ){
                                   WinModal.instance.release();
                                   //anyMovesLeft(isAI);
                                   return;
                               }else{
                                   scheduleMoveHilite(arrUnitToMove);
                               }
                           }
                       }
                , .08F        //    (delay)
        );


    }
    public boolean  anyMovesLeft(boolean isAI) {
        if (ClickAction.getClickActionsLeft() > 0) {
            return true;
        } else {
            if (!isAI) {
                String str = i18NBundle.get("nomoremove");
                EventPopUp.instance.show(str);
                return false;
            }
        }
        return false;
    }

    public ArrayList<Unit> getArrUnitsInMoa(){
        ArrayList<Unit> arrUnitsReturn = new ArrayList<>();
        arrUnitsReturn.addAll(arrUnitsInMoa);
        return arrUnitsReturn;
    }

    ArrayList<Hex> arrMove;
    public void moveUnitFromClick(Unit unit, Hex hex, boolean isAI) {
        UnitMove unitMove = new UnitMove(unit, unit.getCurrentMoveFactor(),true,true,0);
        arrMove=  unitMove.getLeastPath(hex, true, null);
 //       SoundsLoader.instance.playTrucksSound();
        actualMove(unit,arrMove, AfterMove.ToClick, isAI);
    }
    public void moveUnitAfterAdvance(Unit unit,Hex hex) {
        UnitMove unitMove = new UnitMove(unit, 10,true,true,0);
        arrMove=  unitMove.getLeastPath(hex, true, null);
        //       SoundsLoader.instance.playTrucksSound();
        actualMove(unit,arrMove, AfterMove.ToAdvance, isAI);
    }
    public void actualMove(Unit unit, ArrayList<Hex> arrMove, AfterMove afterMove, final boolean isAI) {
        /**
         *  calling rtn should have set off anything
         */
        Gdx.app.log("Move", "actualMove Unit="+unit);
        if (unit.isMechanized) {
            SoundsLoader.instance.playMovementSound();
        }else{
            SoundsLoader.instance.playMarch();
        }

        float delay = .42f;
        WinModal.instance.set(); // freeze counters
        if (unit.getMapCounter() != null) {
            unit.getMapCounter().getCounterStack().setPoints();
        }
      //  unit.getMapCounter().getCounterStack().setSupplyGas();
        float steps = delay / arrMove.size();
        float timer = steps;

        int i = 0;
        Hex hexEnd = null;

        for (Hex hex : arrMove) {
            final Hex hexTime = hex;
            final Hex hexPrevious;
            if (i == 0) {
                hexPrevious = unit.getHexOccupy();
                // set first hex
            } else {
                hexPrevious = arrMove.get(i - 1);
            }
 //           Gdx.app.log("Move", "actualMove before run hex="+hexPrevious+" i="+i);

            final Unit unitMove = unit;
           Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
//                   Gdx.app.log("Move", "actualMove leaving hex="+hexPrevious);

                    if (unitMove.getHexOccupy().leaveHex(unitMove)) {
                        Counter.rePlace(hexPrevious);
                        hexTime.enterHex(unitMove);
                        Counter.rePlace(hexTime);
                    }else{
                        ErrorGame errorGame = new ErrorGame("Move Error Leaving Hex", this);
                    }
                }

            }, timer);

            timer += steps+.08f;
            i++;
        }
        timer += .1f;
        final Unit unitDone  = unit;
        final AfterMove after = afterMove;
        final Hex finalHexEnd = hexEnd;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                              afterMoveDisplay(after, unitDone, isAI, finalHexEnd);
                           }


                       }, timer  //delay * 2f
        );
    }
    public void  afterMoveDisplay(AfterMove af, Unit unit, boolean isAI, Hex hexEnd){
        WinModal.instance.release();
        SoundsLoader.instance.stopSounds();
        if (af == AfterMove.ToClick){
            unit.setMovedThisTurn(NextPhase.instance.getTurn());
            unit.getMapCounter().getCounterStack().shade();
            unit.setCurrentMovement((int) (unit.getHexOccupy().getCalcMoveCost(0)));
            unit.getMapCounter().getCounterStack().setPoints();
       //     unit.getMapCounter().getCounterStack().setSupplyGas();
            if (!isAI) {
                moveReturnFromClick(true,hexEnd,unit);
            }else{
                setChanged();
                notifyObservers(new ObserverPackage(ObserverPackage.Type.MoveFinished,null,0,0));
            }
            return;
        }

        if (af == AfterMove.ToAdvance){
            unit.setMovedThisTurn(NextPhase.instance.getTurn());
            if (unit.getMapCounter() != null) {
                unit.getMapCounter().getCounterStack().shade();
            }
            moveReturnFromClick(false, hexEnd, unit);
            return;
        }
        if (af == AfterMove.Retreats){
            Attack.instance.doNextRetreat(unit);
            return;
        }
        if (af == AfterMove.ToReinforcement){
            unit.setMovedThisTurn(NextPhase.instance.getTurn());
            unit.getMapCounter().getCounterStack().shade();
            unit.setCurrentMovement((int) (unit.getHexOccupy().getCalcMoveCost(0)));
            unit.getMapCounter().getCounterStack().setPoints();
 //           Reinforcement.instance.getScreen().afterMove(unit);
            return;
        }


    }

    public void moveReturnFromClick(boolean isSaveMove, Hex hexExitPanzer, Unit unit){
        if (isSaveMove) {
            SaveGame.SaveLastPhase(" Last Turn", 2);
        }

        /**
         *
         * check if anymoves left
         */
        ClickAction.unLock();
        if (Move.instance.anyMovesLeft(isAI))
        {
            return;
        }

    }

    public void endMove(boolean isAllies, boolean isAI) {
        ArrayList<Unit> arrUnitToMoveWork;
        if (isAllies) {
            arrUnitToMoveWork = Unit.getOnBoardAllied();
        } else {
            arrUnitToMoveWork = Unit.getOnBoardAxis();
        }
        for (Unit unit:arrUnitToMoveWork){
            unit.getMapCounter().getCounterStack().removeHilite();
            unit.getMapCounter().getCounterStack().removeShade();
            unit.getMapCounter().removeClickAction();
        }

    }

    public enum AfterMove {
        ToClick, ToAI, ToRetreat, ToAdvance, ToReturn, ToAfterCombat, ToMOA, ToMOAKeepMove, ToAlliedAI, None, Retreats,ToReinforcement;

    }




    static public boolean isRiverCrossed = false;

    public static float cost(Unit unit, Hex startHex, Hex endHex,  boolean checkTerrain,boolean checkCommand, int thread) {
        /**
         *  start with a cost of 1 for clear
         *  anything else we will add to this cost unless hex is prohibited
         *  IT IS ASSUMED THAT Mobile assault has been checked for validity before
         *  invoking this rtn
         */
        if (startHex.xTable ==20 && startHex.yTable == 14 && endHex.xTable == 20 && endHex.yTable == 15){
            int bt=0;
        }
        isRiverCrossed = false;
//        if (endHex.xTable == 16 && endHex.yTable == 10) {
//              int bg = 0;
//         }
        float cost = 0; // assume clear
        if (checkCommand){
            cost = 1;
        }else{
            int bt=0;
        }


//        if (!startHex.getSurround().contains(endHex)) { // should not happen
//            return 999;
//        }

//        if (!HexCanNotCross.checkCanCross(startHex, endHex)) {
//            return 999;
//        }

        /**
         *  check if target hex has a unit
         *  or in case of fakeAI check if hex has been set
         *
         */
        boolean isEnemyInHex = false;
        if ((unit.isAllies && endHex.isRussianOccupied[thread] || (!unit.isAllies && endHex.isAlliedOccupied[thread]))) {
                return 888;
        }
        /**
         *  add case for check command range and ezoc is covered by same side unit
         */
        if ((unit.isAllies && endHex.isRussianZOC[thread]) || !unit.isAllies && endHex.isAlliedZOC[thread] ) {
            if (checkCommand){
              if ((unit.isRussian && endHex.isRussianOccupied[thread]) ||(unit.isAllies && endHex.isAlliedOccupied[thread])){
                  cost=0;
              }
            }else {
                cost = 999;
            }
        }
        boolean isRoad =false;
        boolean isPath = false;
        if (Hex.isRoadConnection(startHex,endHex)){
            isRoad = true;
            cost += .5F;
        }else if (startHex.isPath() && endHex.isPath() && Hex.isPathConnection(startHex,endHex)){

                cost += .5F;
        }

        int steps =endHex.getStacksIn();


  /*      if (startHex.isStreamBank() && endHex.isStreamBank() && checkTerrain) {
            if (Bridge.isBridge(startHex, endHex)){
              // nothing
            }else if (River.instance.isStreamBetween(startHex, endHex)) {
                isRiverCrossed = true;
                if (unit.isMechanized){
                    if (unit.isTransport) {
                        cost += 14;
                    }else {

                        cost += 12;
                    }
                }else{
                    cost +=3;
                }
            }

        }
        if (endHex.isForest() && !(isPath || isRoad) && checkTerrain){
            if (unit.isMechanized){
                if (unit.isTransport){
                    cost += 6;
                }else {
                    cost += 4;
                }
            }else{
                cost +=2;
            }
        }
        if (!endHex.isForest() && endHex.isTown() && checkTerrain && !(isPath || isRoad)){
            if (unit.isMechanized){
                cost +=2;
            }else{
                cost +=1;
            }
        }
        if (endHex.isCity() && checkTerrain && !(isPath || isRoad)){
            if (unit.isMechanized){
                cost +=2;
            }else{
                cost +=1;
            }
        }

        if (isEnemyInHex) {
            if (unit.isTransport){
                cost =999;
            }else {
                cost += 2.5f; //
                isMOAEncountered = true;

            }
        }*/
 //       if (endHex.xTable == 24 && endHex.yTable == 8){
 //           int bg =0;
 //       }
        return cost;

    }

}
