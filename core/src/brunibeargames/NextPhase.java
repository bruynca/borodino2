package brunibeargames;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;
import com.bruinbeargames.ardenne.AI.AIMain;
import com.bruinbeargames.ardenne.GameLogic.AdvanceAfterCombat;
import com.bruinbeargames.ardenne.GameLogic.Airplane;
import com.bruinbeargames.ardenne.GameLogic.ArtillerySet;
import com.bruinbeargames.ardenne.GameLogic.Barrage;
import com.bruinbeargames.ardenne.GameLogic.BarrageExplode;
import com.bruinbeargames.ardenne.GameLogic.BlowBridge;
import com.bruinbeargames.ardenne.GameLogic.CardHandler;
import com.bruinbeargames.ardenne.GameLogic.Combat;
import com.bruinbeargames.ardenne.GameLogic.DefendResult;
import com.bruinbeargames.ardenne.GameLogic.Division;
import com.bruinbeargames.ardenne.GameLogic.ExitWest;
import com.bruinbeargames.ardenne.GameLogic.FixBridge;
import com.bruinbeargames.ardenne.GameLogic.HooufGas;
import com.bruinbeargames.ardenne.GameLogic.LehrExits;
import com.bruinbeargames.ardenne.GameLogic.LehrHalts;
import com.bruinbeargames.ardenne.GameLogic.LimberArtillery;
import com.bruinbeargames.ardenne.GameLogic.MobileAssualt;
import com.bruinbeargames.ardenne.GameLogic.MoreAmmo;
import com.bruinbeargames.ardenne.GameLogic.MoreGermanAmmo;
import com.bruinbeargames.ardenne.GameLogic.Move;
import com.bruinbeargames.ardenne.GameLogic.Reinforcement;
import com.bruinbeargames.ardenne.GameLogic.SecondPanzerExits;
import com.bruinbeargames.ardenne.GameLogic.SecondPanzerHalts;
import com.bruinbeargames.ardenne.GameLogic.SecondPanzerLoses;
import com.bruinbeargames.ardenne.GameLogic.SignPost;
import com.bruinbeargames.ardenne.GameLogic.Supply;
import com.bruinbeargames.ardenne.GameLogic.Weather;
import com.bruinbeargames.ardenne.UI.BombardDisplay;
import com.bruinbeargames.ardenne.UI.BottomMenu;
import com.bruinbeargames.ardenne.UI.BridgeExplosion;
import com.bruinbeargames.ardenne.UI.CombatDisplay;
import com.bruinbeargames.ardenne.UI.CombatDisplayResults;
import com.bruinbeargames.ardenne.UI.DiceEffect;
import com.bruinbeargames.ardenne.UI.EventAI;
import com.bruinbeargames.ardenne.UI.EventPopUp;
import com.bruinbeargames.ardenne.UI.Explosions;
import com.bruinbeargames.ardenne.UI.FlyingShell;
import com.bruinbeargames.ardenne.UI.HelpPage;
import com.bruinbeargames.ardenne.UI.SoundSlider;
import com.bruinbeargames.ardenne.UI.TurnCounter;
import com.bruinbeargames.ardenne.UI.VictoryPopup;
import com.bruinbeargames.ardenne.UI.WinAIDisplay;
import com.bruinbeargames.ardenne.UI.WinBombard;
import com.bruinbeargames.ardenne.UI.WinCRT;
import com.bruinbeargames.ardenne.Unit.ClickAction;
import com.bruinbeargames.ardenne.Unit.CounterStack;
import com.bruinbeargames.ardenne.Unit.Unit;

import java.util.UUID;

public class NextPhase {

    static public NextPhase instance;
    private int phase = -1;
    public final  Phase[] Phases;
    public boolean isAlliedPlayer = true;
 //   private AIMain aiMain;
    private boolean isDebug = false;
    boolean isFirstTime = true;
    boolean isLocked =  false;
    Hex hexNull;
    int turn = 1;
  //  TurnCounter turnCounter;
//    public Weather weather;
//    WinCRT winCRT = new WinCRT();
    private I18NBundle i18NBundle;
    boolean isAIControl = false;
    boolean isInBarrage = false;

    String programUID = "";

   // DiceEffect diceEffect = new DiceEffect();
  //  Explosions explosions = new Explosions();




    public NextPhase() {
        Gdx.app.log("NextPhase", "Constructor");


        instance = this;
        Phases =  Phase.values();
        i18NBundle= GameMenuLoader.instance.localization;
        System.gc();



    }
    public void nextPhase() {
        /**
         *  Next Turn
         */
        /**
         *  show debugging how we got here
         */
        String calling3 = Thread.currentThread().getStackTrace()[2].getClassName().toString();
        String calling4 = Thread.currentThread().getStackTrace()[2].getMethodName().toString();
        if (phase != -1) {
            Gdx.app.log("nextPhase", " in Old =" + Phases[phase].toString());
            Gdx.app.log("nextPhase", " class invoking   =" + calling3);
            Gdx.app.log("nextPhase", " method invoking  =" + calling4);
        }
        //       cntDebug++;
        phase++;
        if (Phases[phase] == Phase.NEXT_TURN) {
            Gdx.app.log("nextPhase", "Next Turn=");

            /**
             *  check for end of game no exit
             */

            phase = 0;
            /**
             *  warning for exitted units
             */
//        setPhase();

        }
    }

    private void createProgramUID() {
        UUID uuid = UUID.randomUUID();
        programUID = UUID.randomUUID().toString().replace("-", "");
    }
    public String getProgramUID(){
        return programUID;
    }
    public void setProgramUID(String inUID){
        programUID = inUID;
    }


    public int getPhase() {
        return phase;
    }
    public void setTurn(int turnIn){
        turn = turnIn;
        if (turn > 1){
//            Unit.setZOCMobileArtillery();
        }
    }


    /**
     *  Bypass doSeaMove handling and set directly
     *  used by loadgame
     * @param
     */
    public void setPhaseDirect(int phaseIn){
        phase = phaseIn;
    }

    public void setPhase() {
        if (turn > GameSetup.instance.getScenario().getLength()){
            return;
        }


        isAIControl = true;
        isInBarrage = false;
        ClickAction.unLock();
        ClickAction.clearClickListeners();
        Hex.initOccupied(); // bug somewhere
        if (aiMain.isHandleAlliedPhase(phase)) {
            Gdx.app.log("nextPhase","AI in New ="+Phases[phase].toString());
            aiMain.handlePhase(phase);
            return;
        } else if (aiMain.isHandleAxisPhase(phase)) {
            Gdx.app.log("nextPhase","AI in New ="+Phases[phase].toString());
            aiMain.handlePhase(phase);
            return;
        }else {
            isAIControl = false;
            Gdx.app.log("nextPhase","next  ="+Phases[phase].toString());
            BottomMenu.instance.setEnablePhaseChange(true);

            switch (Phases[phase]) {
                case CARD_CLEANUP:
                    CardHandler.instance.cleanLastTurn(turn);
                    break;
                case ALLIED_CARD:
                    isAlliedPlayer = true;
                    CardHandler.instance.alliedCardPhase(turn);
                    break;
                case GERMAN_CARD:
                    isAlliedPlayer = false;
                    CardHandler.instance.germanCardPhase(turn);
                    break;
                case GERMAN_ROLL_BRIDGE:
                    isAlliedPlayer = false;
                    AccessInternet.updateGame(NextPhase.instance.turn, "");
                    FixBridge.instance.display(null);
                    break;
                case GERMAN_PRE_MOVEMENT:
                    isAlliedPlayer = false;
                    LimberArtillery.instance.initializeLimber(false,false);
                    break;
                case GERMAN_MOVEMENT:
                    isAlliedPlayer = false;
                    Move.instance.intializeMove(false, false, false);
  //                  endPhase(getPhase());
                    break;
                case GERMAN_POST_MOVEMENT:
                    EventPopUp.instance.hide();
                    isAlliedPlayer = false;
                    Move.instance.endMove(false, false);
//                    Hex.checkStacking();
 //                   Hex.checkStacking();
 //                   OverStacking.instance.check();
                    nextPhase();
                    break;
                case US_BARRAGE_DEFENSE:
                    isInBarrage = true;
                    isAlliedPlayer = true;
                    Barrage.instance.intialize(true,false);
 //                   endPhase(getPhase());
                    break;
                case GERMAN_BARRAGE_ATTACK:
                    isInBarrage = true;
                    isAlliedPlayer = false;
                    EventPopUp.instance.hide();
  //                  Unit.initUnShade();
                    Barrage.instance.intialize(false,false);
                    break;
                case GERMAN_BARRAGE_RESOLVE:
                    isInBarrage = true;

                    isAlliedPlayer = false;
                    //                  Unit.initUnShade();
                    BarrageExplode barrageExplode = new BarrageExplode();
                    break;
                case GERMAN_BARRAGE_END:
                    isInBarrage = true;

                    isAlliedPlayer = false;
                    BarrageExplode.End();
                    Barrage.clearTargets();
                    nextPhase();
                    break;
                case GERMAN_COMBAT:
                    isAlliedPlayer = false;
                    Combat.instance.Intialize(false, false);
                    break;
                case GERMAN_COMBAT_END:
                    isAlliedPlayer = false;
                    EventPopUp.instance.hide();
                    Combat.instance.cleanup(true);
                    nextPhase();
                    break;
                case GERMAN_EXPLOTATION:
                    Move.instance.intializeMove(false, false, true);
                    break;
                case GERMAN_POST_EXPLOTATION:
                    EventPopUp.instance.hide();
                    Move.instance.endMove(false, false);
                    Hex.checkStacking();
                    if (LehrHalts.instance.isLehrHalted()){
                        LehrHalts.instance.restore();
                    }
                    if (SecondPanzerHalts.instance.is2NDPanzerHalted()){
                        SecondPanzerHalts.instance.restore();
                    }
                    if (noExitCheck()){
                        return;
                    }
                    nextPhase();
                    break;
                case GERMAN_SUPPLY:
                    if (turn == GameSetup.instance.getScenario().getLength()) {
                        if (GameSetup.instance.getScenario().ordinal() > 0){
                            String str = i18NBundle.format("exitinsupply");
                            EventPopUp.instance.show(str);
                            if (SecondPanzerExits.instance.unitExit1.size() > 0){
                                CenterScreen.instance.start(SecondPanzerExits.instance.hexExit1);
                            }else{
                                CenterScreen.instance.start(SecondPanzerExits.instance.hexExit1);
                            }
                        }
                    }

                    isAlliedPlayer = false;
                    Unit.initOutOfSupplyThisTurn(false);
                    Supply.instance.doGermanSupply();
                    break;
                case GERMAN_SUPPLY_END:
                    isAlliedPlayer = false;
                    Supply.instance.EndSupplyGerman();
                    if (turn == GameSetup.instance.getScenario().getLength()) {
                        if (GameSetup.instance.getScenario().ordinal() > 0){
                            VictoryPopup.instance.announceVictorAtEnd();
                            return;
                        }
                    }

// done in Supply                    nextPhase();
                    break;
                case GERMAN_END:
                    isAlliedPlayer = false;
                    Unit.initUnShade();
                    SecondPanzerExits.unShadeAll();
                    ClickAction.cancelAll();
                    if (turn == GameSetup.instance.getScenario().getLength()) {
                        if (GameSetup.instance.getScenario().ordinal() > 0){
                            VictoryPopup.instance.announceVictorAtEnd();
                            return;
                        }
                    }
                    nextPhase();
                    break;
                case ALLIED_PRE_MOVEMENT:
                    isAlliedPlayer = true;
                    LimberArtillery.instance.initializeLimber(true,false);
                    break;
                case ALLIED_REINFORCEMENT:
                    isAlliedPlayer = true;
                    if (getTurn() == 3){
                        Supply.instance.loadOtherUSSupply();
                    }
                    if (Reinforcement.instance.getReinforcementsAvailable(turn).size() > 0){
                        Reinforcement.instance.showWindow(false);
                    }else{
                        nextPhase();
                    }
                    break;

                case ALLIED_MOVEMENT:
                    isAlliedPlayer = true;
                    Move.instance.intializeMove(true, false,false);
                    break;
                case ALLIED_POST_MOVEMENT:
                    isAlliedPlayer = true;
                    EventPopUp.instance.hide();
                    Move.instance.endMove(false, false);
                    Hex.checkStacking();
                    nextPhase();
                    break;
                case BRIDGE_GERMAN:
                    HooufGas.instance.checkHooufgas();

                    nextPhase();
                    break;
                case BRIDGE_ALLIED:
                    nextPhase();
                    break;
                case GERMAN_BARRAGE_DEFEND:
                    isInBarrage = true;

                    isAlliedPlayer = false;

                    Barrage.instance.intialize(false,false);
                    break;
                case ALLIED_BARRAGE_ATTACK:
                    isInBarrage = true;

                    isAlliedPlayer = true;
                    EventPopUp.instance.hide();
                    Barrage.instance.intialize(true,false);
                    break;
                case ALLIED_BARRAGE_RESOLVE:
                    isInBarrage = true;

                    isAlliedPlayer = true;
                    barrageExplode = new BarrageExplode();
                    break;
                case ALLIED_BARRAGE_END:
                    isInBarrage = true;

                    isAlliedPlayer = true;
                    BarrageExplode.End();
                    Barrage.clearTargets();

                    nextPhase();
                    break;
                case ALLIED_COMBAT:
                    isAlliedPlayer = true;
                    Combat.instance.Intialize(true,false);
                    break;
                case ALLIED_COMBAT_END:
                    isAlliedPlayer = true;
                    EventPopUp.instance.hide();
                    Combat.instance.cleanup(true);
                    nextPhase();
                    break;
                case ALLIED_EXPLOTATION:
                    Move.instance.intializeMove(true, false,true);
                    break;
                case ALLIED_POST_EXPLOTATION:
                    EventPopUp.instance.hide();
                    Move.instance.endMove(false, false);
                    Hex.checkStacking();
                    nextPhase();
                    break;
                case ALLIED_SUPPLY:
                    isAlliedPlayer = true;
                    Unit.initOutOfSupplyThisTurn(true);
                    Supply.instance.doAlliedSupply();
                    break;

                case ALLIED_END:
                    isAlliedPlayer = true;
                    Supply.instance.endSupplyUS();
                    nextPhase();
                    break;
                case NEXT_TURN:
                    break;
                default:
                    Gdx.app.log("NexPhase", "Invalid Phase");
                    ErrorGame errorGame = new ErrorGame("invalid Phase", this);
            }

        }

    }
    public void endPhase(int phaseToEnd){
        Gdx.app.log("NexPhase", "endPhase"+Phases[getPhase()].toString());

        if (phaseToEnd != getPhase()){
            ErrorGame errorGame = new ErrorGame("EndPhase and current Phase do not Match",this);
        }
        CounterStack.removeAllHilites();
        CounterStack.removeAllShaded();


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Gdx.app.log("NextPhase", "Timer" );
                nextPhase();
            }

        }, .065F);
   }

    private boolean noExitCheck(){
        boolean iswarned = false;
        if (GameSetup.instance.getScenario().ordinal() > 0) {
            if (SecondPanzerExits.instance.checkExits()){
                String winner = VictoryPopup.instance.announceVictorAtEnd();
                BottomMenu.instance.setEnablePhaseChange(false);
                AccessInternet.updateGame(turn, winner);
                iswarned = true;
                return true;
            }
        }
        if (GameSetup.instance.getScenario().ordinal() > 1 && !iswarned) {
            if (LehrExits.instance.checkExits()){
                String winner = VictoryPopup.instance.announceVictorAtEnd();
                BottomMenu.instance.setEnablePhaseChange(false);
                AccessInternet.updateGame(turn, winner);
                return true;
            }
        }
        return false;

    }


    public void setDebug() {
        isDebug = true;
        cntDebug = 0;
        isLocked = true;
    }

    public int getTurn(){
        return turn;
    }
    int cntDebug = 0;

    public boolean isArtillery() {
        if (phase == Phase.GERMAN_PRE_MOVEMENT.ordinal() ||  phase ==Phase.ALLIED_PRE_MOVEMENT.ordinal()){
            return true;
        }
        return false;
    }
    public boolean isAlliedPlayer(){
        return isAlliedPlayer;
    }
    public AIMain getAiMain(){
        return aiMain;
    }
    public boolean isAIControl(){
        return isAIControl;
    }
    public boolean isInBarrage(){
        return isInBarrage;
    }
}