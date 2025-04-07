package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.UI.BottomMenu;
import brunibeargames.UI.WinCommand;
import brunibeargames.UI.WinWarning;
import brunibeargames.Unit.Commander;
import brunibeargames.Unit.Division;
import brunibeargames.Unit.Officer;
import brunibeargames.Unit.Unit;

public class DoCommandDivision implements Observer {
    static public DoCommandDivision instance;
    ArrayList<Commander> arrCommandersThisPhase = new ArrayList<Commander>();
    ArrayList<WinCommand> arrWinCommand = new ArrayList<WinCommand>();
    ArrayList<Division> arrCommanderDivisions = new ArrayList<Division>();
    ArrayList<Commander> arrCommanderProcessed = new ArrayList<>();
    ArrayList<Division> arrDivisionProcessed = new ArrayList<>();
    I18NBundle i18NBundle;
    private WinWarning winWarning;


    DoCommandDivision() {
        instance = this;
        i18NBundle = GameMenuLoader.instance.localization;

    }

    /**
     * Command can be
     * input is the array of Commander set by the previous step DETERMINE
     * if there are multiple commanders then
     * * 1. display all commanders and choose the officers to allocate command use wincommand
     *   2. display all divisions that can be activated and process
     *   3. display number of officers and see if we can throw dice to activate
     *
     *  If there is only one commander then
     *   1. display all commanders and choose the officers to allocate command use winc
     *   2. display all divisions that can be activated and process
     *
     */
    public void start() {
        Gdx.app.log("DoCommandDivision", "Start");
        BottomMenu.instance.showNextPhase();
        BottomMenu.instance.setEnablePhaseChange(true);
        BottomMenu.instance.showInquirNextPhase();
        BottomMenu.instance.showBackOut();
        BottomMenu.instance.setWarningPhaseChange(false);
        BottomMenu.instance.addObserver(this);
        TurnCounter.instance.updateText(i18NBundle.get("commanddivisionphase"));

        arrCommandersThisPhase.clear();
        arrCommanderDivisions.clear();
        arrCommanderProcessed.clear();
        arrDivisionProcessed.clear();
        /**
         *  load all the Commanders for this part of Command after the commander set up by
         *  Determine Command
         */
        arrCommandersThisPhase.addAll(DetermineCommand.instance.getArrCommand());
        if (arrCommandersThisPhase.isEmpty()) {
            end();
        }
        ArrayList<Commander> arrRemove = new ArrayList<Commander>();
        for (Commander commander:arrCommandersThisPhase) {
            if (commander.getDivisionPossibleAvailable().isEmpty()) {
                arrRemove.add(commander);
            }
        }
        arrCommandersThisPhase.removeAll(arrRemove);

        if (arrCommandersThisPhase.isEmpty()) {

            winWarning = new WinWarning(i18NBundle.get("commanddivisionphase"), i18NBundle.get("commanddivisionphasenoommander"));
            winWarning.addObserver(this);
            //end();
        }



        /*
         *  more than 1 then we need to choose
         */
        if (arrCommandersThisPhase.size() > 1) {
            setupCommandChoose();
            BottomMenu.instance.setWarningPhaseChange(true);
            String message= i18NBundle.get("choosenocommanddivision");
            String title = i18NBundle.get("nextphasebutton");
            BottomMenu.instance.setPhaseData(title, message);
            message=i18NBundle.get("commanddivisionphasehelp");
            title=i18NBundle.get("commanddivisionphasehelptitle");
            BottomMenu.instance.setHelpData(title, message);
            return;
        }
        if (arrCommandersThisPhase.size() == 1) {
            Commander commander = arrCommandersThisPhase.get(0);
            return;
        }
    }

    private void setupCommandChoose() {
        float offsett = 50;
        offsett = calculateInitialWindow(arrCommandersThisPhase.size());
        float y = (Gdx.graphics.getHeight() - 500f) / 2f;
        float x = offsett;
        for (Commander commander : arrCommandersThisPhase) {
            //           Unit unit = commander.getUnit();
            //           unit.getMapCounter().getCounterStack().removeShade();
            //           unit.getMapCounter().getCounterStack().hilite();
            //           ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.Command);

            Vector2 pos = new Vector2(x, y);
            WinCommand winCommand = new WinCommand(commander, 400, 500, pos);
            arrWinCommand.add(winCommand);
            x += 400f + offsett;
        }
    }

    private float calculateInitialWindow(int numberCommanders) {
        float offsett = 0;
        float winWidth = 400f;  //
        float totWidth = winWidth * numberCommanders;
        float winOffsetts = numberCommanders + 1;
        float sizeOffsetts = (Gdx.graphics.getWidth() - winWidth * numberCommanders) / winOffsetts;
        return sizeOffsetts;
    }


    /**
     * Take an Officers from the pool
     *
     * @param officer
     */
    public void activateOfficer(Officer officer) {
        for (WinCommand winCommand : arrWinCommand) {
            winCommand.deleteOfficer(officer);
            ArrayList<Unit> arrUnits = new ArrayList<Unit>();
            arrUnits.addAll(officer.getUnitsAvailable());
            for (Unit unit : arrUnits) {
                unit.getMapCounter().getCounterStack().hilite();
            }

        }
    }

    public void toPoolOfficer(Officer officer) {
        for (WinCommand winCommand : arrWinCommand) {
            winCommand.addOfficer(officer);
            ArrayList<Unit> arrUnits = new ArrayList<Unit>();
            arrUnits.addAll(officer.getUnitsAvailable());
            for (Unit unit : arrUnits) {
                unit.getMapCounter().getCounterStack().removeHilite();
            }


        }
    }

    public void allocate(Commander commander, ArrayList<Division> arrDivisionsSelected) {
        CommanderDivision commanderDivision = new CommanderDivision(commander, arrDivisionsSelected);
        arrCommanderDivisions.addAll(arrDivisionsSelected);
        arrCommanderProcessed.add(commander);
        arrCommandersThisPhase.remove(commander);
        if (arrCommandersThisPhase.isEmpty()) {
            BottomMenu.instance.setWarningPhaseChange(false);
            BottomMenu.instance.setEnablePhaseChange(true);
            end();
        }

    }

    void end() {
        Gdx.app.log("DoCommandDivision", "end");
        NextPhase.instance.endPhase();

    }

    public void goBack() {
        start();
    }

    @Override
    public void update(Observable observable, Object o) {
        ObserverPackage oB = (ObserverPackage) o;
        /**
         *  Hex touched
         */
        /**
         *  Hex touched
         */
        if (oB.type == ObserverPackage.Type.GoBack) {
            BottomMenu.instance.deleteObserver(this);
            goBack();
        }else{
            if (oB.type == ObserverPackage.Type.OK){
                winWarning.deleteObserver(this);
                end();
            }
        }
        return;

    }
    class CommanderDivision {
        Commander commander;
        ArrayList<Division> arrDivision = new ArrayList<Division>();

        public CommanderDivision(Commander commander, ArrayList<Division> arrDivision) {
            this.commander = commander;
            this.arrDivision.addAll(arrDivision);
        }
    }
}
