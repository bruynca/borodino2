package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import brunibeargames.UI.BottomMenu;
import brunibeargames.UI.WinCommandRandom;
import brunibeargames.UI.WinWarning;
import brunibeargames.Unit.Officer;
import brunibeargames.Unit.Unit;

public class DoCommandRandom implements Observer {
    /**
     * Random activations of commanders for a side
     * Once the button has been pressed on winrandom
     * we can not go back
     * */
    static public DoCommandRandom instance;
        ArrayList<Officer> arrAllOfficersPossible = new ArrayList<Officer>();
        ArrayList<Officer> arrOfficerSelected = new ArrayList<Officer>();
    I18NBundle i18NBundle;
    private WinWarning winWarning;
    int dieAboveRussian = 4;
    int dieAboveAllies = 3;
    int numDieRussian = 3;
    int numDieAllies = 3;
    boolean isAllies = true;
    private WinCommandRandom winCommandRandom;
    private Officer officer;
    private boolean isActivated;
    private boolean isInAnimation = false;

    DoCommandRandom(){
        instance = this;
        i18NBundle = GameMenuLoader.instance.localization;
        dieAboveRussian = GameSetup.instance.getDicerollsAboveRussian();
        dieAboveAllies = GameSetup.instance.getDicerollsAboveFrench();
        numDieRussian = GameSetup.instance.getRandomCommandersRussian();
        numDieAllies = GameSetup.instance.getRandomCommandersFrench();
   }
    public void start(boolean isAlliesIn){
        Gdx.app.log("DoCommandRandom", "start");
        isInAnimation = false;
        this.isAllies = isAlliesIn;

        arrAllOfficersPossible.clear();
        arrOfficerSelected.clear();
        for (Unit unit : Game.instance.arrOnUnitBoard) {
            if (unit.isAllies == isAllies && unit.isOfficer &&
                    !unit.officer.getisActivated())
            {
               arrAllOfficersPossible.add(unit.officer);
            }
        }
        if (arrAllOfficersPossible.isEmpty()) {

            winWarning = new WinWarning(i18NBundle.get("warnrandomofficers"), i18NBundle.get("commandphasenoommander"));
            winWarning.addObserver(this);
            return;
        }
        BottomMenu.instance.setWarningPhaseChange(true);
        String message= i18NBundle.get("choosenocommand");
        String title = i18NBundle.get("nextphasebutton");
        BottomMenu.instance.setPhaseData(title, message);
        message=i18NBundle.get("commandrandomphasehelp");
        title=i18NBundle.get("commandrandomphasehelptitle");
        BottomMenu.instance.setHelpData(title, message);
        float y = (Gdx.graphics.getHeight() - 500f) / 2f;
        float x = (Gdx.graphics.getWidth() - 400f) / 2f;
        Vector2 pos = new Vector2(x, y);
        BottomMenu.instance.addObserver(this);
        TurnCounter.instance.updateText(i18NBundle.get("commandrandomphase"));


        winCommandRandom = new WinCommandRandom(arrAllOfficersPossible,400,500,pos);

        Gdx.app.log("DoCommandRandom", "end -start");


    }
    private void rollForOfficers(ArrayList<Officer> arrOfficerSelected) {
        if (arrOfficerSelected.isEmpty()) {
            winCommandRandom.removeButton();
            winCommandRandom.displayEffect(officer);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    winCommandRandom.end();
                }
            }, 2f); // The delay in seconds (0.1f for 0.1 seconds)

            // nextphase?
            return;
        }
        officer = arrOfficerSelected.get(0);
        arrOfficerSelected.remove(0);
        int getDie = getDieRoll();
        if (getDie > dieAboveAllies && isAllies) {
            Gdx.app.log("DoCommandRandom", "Officer Activated="+officer.getName());
            officer.setActivated(true);
        }
        DiceEffect.instance.addObserver(this);
        DiceEffect.instance.rollRedDice(getDie);

    }
    public int getDieRoll()
    {
        Random diceRoller = new Random();
        int die = (int)(Math.random()*6) + 1;
        return die;

    }


    @Override
    public void update(Observable observable, Object o) {
        ObserverPackage oB = (ObserverPackage) o;
        Gdx.app.log("WinCommandRandom", "update type=" + oB.type.toString());
        if (oB.type == ObserverPackage.Type.GoBack) {
            BottomMenu.instance.deleteObserver(this);
            goBack();
        }else{
            if (oB.type == ObserverPackage.Type.OK || oB.type == ObserverPackage.Type.NextPhase){
                if (winWarning != null) {
                    winWarning.deleteObserver(this);
                }
                BottomMenu.instance.deleteObserver(this);
                end();
            }
        }

        if (oB.type == ObserverPackage.Type.DiceRollFinished) {
            DiceEffect.instance.deleteObserver(this);
            winCommandRandom.displayEffect(officer);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    rollForOfficers(arrOfficerSelected);
                }
            }, 1.8f); // The delay in seconds (0.1f for 0.1 seconds)

            return;
        }
        return;
    }


    private void goBack() {
        if (!isInAnimation){
            start(isAllies);
        }
    }

    /**
     * Natural End after die roll
     * @param arrOfficerSelected
     */

    /**
     * forced end
     */
    public void end() {
        BottomMenu.instance.deleteObserver(this);
        DiceEffect.instance.deleteObserver(this);
        NextPhase.instance.endPhase();
    }

    public void doRandom(ArrayList<Officer> arrOfficer) {
        isInAnimation = true;
        arrOfficerSelected.addAll(arrOfficer);
        rollForOfficers(arrOfficerSelected);
    }
}
