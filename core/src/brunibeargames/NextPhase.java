package brunibeargames;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;

import java.util.UUID;

import brunibeargames.UI.BottomMenu;
import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.CounterStack;

public class NextPhase {

    static public NextPhase instance;
    public Weather weather;
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

        {
            isAIControl = false;
            Gdx.app.log("nextPhase","next  ="+Phases[phase].toString());
            BottomMenu.instance.setEnablePhaseChange(true);

            switch (Phases[phase]) {
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
    public boolean isAIControl(){
        return isAIControl;
    }
    public boolean isInBarrage(){
        return isInBarrage;
    }

    public void continuePhaseFirstTime() {
    }
}