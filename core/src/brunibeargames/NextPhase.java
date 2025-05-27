package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;

import java.util.UUID;

import brunibeargames.UI.BottomMenu;
import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.CounterStack;
import brunibeargames.Unit.Officer;
import brunibeargames.Unit.Unit;

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
    Cards cards;
    Hex hexNull;
    int turn = 1;
  //  TurnCounter turnCounter;
//    public Weather weather;
//    WinCRT winCRT = new WinCRT();
    private I18NBundle i18NBundle;

    String programUID = "";
    Initiative initiative;
    DetermineCommand determineCommand;
    DoCommand doCommand;
    DoCommandDivision   doCommandDivision;
    DoCommandRandom   doCommandRandom;
    DiceEffect diceEffect;
    Move move;

    // DiceEffect diceEffect = new DiceEffect();
  //  Explosions explosions = new Explosions();




    public NextPhase() {
        Gdx.app.log("NextPhase", "Constructor");
        /**
         *  initialize OBJECTS
         */

        instance = this;
        Phases =  Phase.values();
        i18NBundle= GameMenuLoader.instance.localization;
        Cards cards = new Cards();
        determineCommand = new DetermineCommand();
        doCommand = new DoCommand();
        doCommandDivision = new DoCommandDivision();
        doCommandRandom = new DoCommandRandom();
        diceEffect = new DiceEffect();
        move = new Move();
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

        }
        setPhase();

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


        ClickAction.unLock();
        ClickAction.clearClickListeners();
        Hex.initOccupied(); // bug somewhere

        {
            Gdx.app.log("nextPhase","next  ="+Phases[phase].toString());
            BottomMenu.instance.setEnablePhaseChange(true);

            switch (Phases[phase]) {
                case NEXT_TURN:
                    Gdx.app.log("NextPhase", "NEXT_TURN");
                    initTurn();
                    break;
                case CARD_CLEANUP:
                   endPhase();
                    break;
                case DETERMINE_INITIATIVE:
                    initiative = new Initiative();
                    break;
                case PLAYER1_CARD:
                    if (initiative.getIsAllies()) {
                        Cards.instance.doAllies();
                    }else{
                        Cards.instance.doRussians();

                         }
                   break;

                case PLAYER2_CARD:
                    if (!initiative.getIsAllies()) {
                        Cards.instance.doRussians();
                    }else{
                        Cards.instance.doAllies();

                    }
                    break;
                case DETERMINE_COMMAND:
                    /*
                        doGetCommandArray will determine which player to get
                     */
                    DetermineCommand.instance.doGetCommandArray(initiative);
                    break;
                case DO_COMMAND:
                    /*
                        Command based on arrays from DtermineCommand
                     */
                    doCommand.instance.start();
                    break;
                case DO_COMMAND_RANDOM:
                    doCommandRandom.instance.start(initiative.getIsAllies());
                    break;


                case DO_COMMAND_DIVISION:
                    /*
                        Command based on arrays from DtermineCommand
                     */
                    doCommandDivision.instance.start();
                    break;
                case MOVEMENT:
                    move.doMovePhase(initiative.getIsAllies());
                     break;
                default:
                    Gdx.app.log("NexPhase", "Invalid Phase");
                    ErrorGame errorGame = new ErrorGame("invalid Phase", this);
            }

        }

    }

    private void initTurn() {
        Officer.initCommand();
        Unit.initCommand();

    }

    public void endPhase(){
        Gdx.app.log("NexPhase", "endPhase"+Phases[getPhase()].toString());


        CounterStack.removeAllHilites();
        CounterStack.removeAllShaded();


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Gdx.app.log("NextPhase", "Timer" );
                nextPhase();
            }

        }, .0065F);
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
        return false;
    }
    public boolean isAlliedPlayer(){
        return isAlliedPlayer;
    }
    public void continuePhaseFirstTime() {
    }
}