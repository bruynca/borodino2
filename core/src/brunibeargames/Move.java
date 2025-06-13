package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;


import java.util.ArrayList;
import java.util.Observable;

import brunibeargames.UI.BottomMenu;
import brunibeargames.UI.EventPopUp;
import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.Counter;
import brunibeargames.Unit.RoadMarch;
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
        String strTit = i18NBundle.get("movementphasehelptitle");
        String strT = i18NBundle.get("movementphasehelp");

        BottomMenu.instance.setHelpData(strTit, strT);
        BottomMenu.instance.showInquirNextPhase();
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
            if (unit.getTurnMoved() >= turn) {
                unit.getMapCounter().getCounterStack().shade();
            } else {
                unit.getMapCounter().getCounterStack().removeShade();
                unit.getHexOccupy().moveUnitToFront(unit);
                Counter.rePlace(unit.getHexOccupy());
                ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.Move);
            }

        }
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
        /**
         *  Road march
         */
        if (!arrMove.contains(hex)){
            RoadMarch.addRoadMarchHexes(arrMove,unit,hex);
        }
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
          //  unit.getMapCounter().getCounterStack().setPoints();
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
            unit.setTurnMoved(NextPhase.instance.getTurn());
            unit.getMapCounter().getCounterStack().shade();
            //unit.setCurrentMovement((int) (unit.getHexOccupy().getCalcMoveCost(0)));
          //  unit.getMapCounter().getCounterStack().setPoints();
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
            unit.setTurnMoved(NextPhase.instance.getTurn());
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
            unit.setTurnMoved(NextPhase.instance.getTurn());
            unit.getMapCounter().getCounterStack().shade();
            //unit.setCurrentMovement((int) (unit.getHexOccupy().getCalcMoveCost(0)));
          //  unit.getMapCounter().getCounterStack().setPoints();
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

    /**
     *
     * @param unit
     * @param startHex
     * @param endHex
     * @param checkTerrain
     * @param checkCommand
     * @param thread
     * @return cost for first of float and 1 if zoc for second
     */
    public static float[] cost(Unit unit, Hex startHex, Hex endHex,  boolean checkTerrain,boolean checkCommand, int thread) {
        if (endHex.xTable == 11 && endHex.yTable == 26 &&
           startHex.xTable == 12 && startHex.yTable == 26) {
            int br = 0;
        }
        float[] cost = new float[2];
        isRiverCrossed = false;
//        if (endHex.xTable == 16 && endHex.yTable == 10) {
//              int bg = 0;
//         }
        cost[0] = 1; // assume clear
        cost[1] = 0; // no zoc


        /**
         *  check if target hex has a unit
         *  or in case of fakeAI check if hex has been set
         *
         */
        boolean isEnemyInHex = false;
        if ((unit.isAllies && endHex.isRussianOccupied[thread] || (!unit.isAllies && endHex.isAlliedOccupied[thread]))) {
            cost[0] = 999;
        }
        /**
         *  add case for check command range and ezoc is covered by same side unit
         */
        if ((unit.isAllies && endHex.isRussianZOC[thread]) || !unit.isAllies && endHex.isAlliedZOC[thread]) {
            if (checkCommand) {
                if ((unit.isRussian && endHex.isRussianOccupied[thread]) || (unit.isAllies && endHex.isAlliedOccupied[thread])) {
                    cost[0] = 0;
                }
            } else {
                cost[1] = 1;
            }
        }
        boolean isRoadB = false;
        boolean isPathB = false;
        if (startHex.isRoad && endHex.isRoad) {
            if (Hex.isRoadConnection(startHex, endHex)) {
                isRoadB = true;
                /**
                 *  march mode logic not done here
                 */
                //cost[0] += .5F;
            }
        }
        if (startHex.isPath && endHex.isPath) {
            if (Hex.isPathConnection(startHex, endHex)) {
                isPathB = true;
                //cost[0] += 1;
            }
        }






        if (startHex.isStreamBank() && endHex.isStreamBank() && checkTerrain) {
            if (Bridge.isBridge(startHex, endHex)){
              // nothing
            }else
            {
                if (Hex.isStreamAcross(startHex, endHex)) {
                    cost[0] += 1; // an aditional
                }
            }
        }
        if (endHex.isForest() && !(isPathB || isRoadB) && checkTerrain){
            if (unit.isCalvary || unit.isHorseArtillery){
                cost[0] +=3;                ;
            }else{
                cost[0] +=1;
            }
        }
        if (endHex.isRiverBank && startHex.isRiverBank){
            if (Hex.isAcrossRiver(endHex,startHex)){
                if (Bridge.isBridge(endHex,startHex)){
                    // do nothing
                }else{
                    cost[0] = 997; // river
                }
            }
        }

        if (isEnemyInHex) {
            cost[0] = 999;
        }
 //       if (endHex.xTable == 24 && endHex.yTable == 8){
 //           int bg =0;
 //       }
        return cost;

    }

}
