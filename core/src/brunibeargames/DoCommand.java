package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

import brunibeargames.UI.BottomMenu;
import brunibeargames.UI.WinCommand;
import brunibeargames.Unit.Commander;
import brunibeargames.Unit.Officer;

public class DoCommand {
    static public DoCommand instance;
    ArrayList<Commander> arrCommandersThisPhase = new ArrayList<Commander>();
    ArrayList<WinCommand> arrWinCommand = new ArrayList<WinCommand>();
    ArrayList<CommanderOfficers> arrCommanderOfficersThisPhase = new ArrayList<CommanderOfficers>();
    boolean needDivisions = false;
    boolean needRandom= false;
    ArrayList<Commander> arrCommanderProcessed = new ArrayList<>();
    ArrayList<CommanderOfficers> arrCommandOfficersProcessed = new ArrayList<>();
    I18NBundle i18NBundle;


    DoCommand() {
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
        BottomMenu.instance.showNextPhase();
        BottomMenu.instance.setEnablePhaseChange(true);
        BottomMenu.instance.showInquirNextPhase();
        BottomMenu.instance.showBackOut();
        BottomMenu.instance.setWarningPhaseChange(false);

        arrCommandersThisPhase.clear();
        arrCommanderOfficersThisPhase.clear();
        arrCommanderProcessed.clear();
        arrCommanderOfficersThisPhase.clear();
        /**
         *  load all the Commanders for this part of Command after the commander set up by
         *  Determine Command
         */
        arrCommandersThisPhase.addAll(DetermineCommand.instance.getArrCommand());
        /*
         *  more than 1 then we need to choose
         */
        if (arrCommandersThisPhase.size() > 1) {
            setupCommandChoose();
            BottomMenu.instance.setWarningPhaseChange(true);
            String message= i18NBundle.get("choosenocommand");
            String title = i18NBundle.get("nextphasebutton");
            BottomMenu.instance.setPhaseData(title, message);
            message=i18NBundle.get("commandphasehelp");
            title=i18NBundle.get("help");
            BottomMenu.instance.setHelpData(title, message);
            needDivisions = true;
            needRandom = true;
            return;
        }
        if (arrCommandersThisPhase.size() == 1) {
            Commander commander = arrCommandersThisPhase.get(0);
            //setupCommand(commander);
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
    public void takeOfficer(Officer officer) {
        for (WinCommand winCommand : arrWinCommand) {
            winCommand.deleteOfficer(officer);
        }
    }

    public void addOfficer(Officer officer) {
        for (WinCommand winCommand : arrWinCommand) {
            winCommand.addOfficer(officer);

        }
    }

    public void allocate(Commander commander, ArrayList<Officer> arrOfficerSelected) {
        CommanderOfficers commanderOfficers = new CommanderOfficers(commander, arrOfficerSelected);
        arrCommanderOfficersThisPhase.add(commanderOfficers);
        arrCommanderProcessed.add(commander);
        arrCommandersThisPhase.remove(commander);
        arrCommanderOfficersThisPhase.add(commanderOfficers);
        if (arrCommandersThisPhase.isEmpty()) {
            end();
        }

    }

    void end() {
        Gdx.app.log("DoCommand", "end");

    }
}
    class CommanderOfficers{
        Commander commander;
        ArrayList<Officer> arrOfficer;
        public CommanderOfficers(Commander commander, ArrayList<Officer> arrOfficer){
            this.commander = commander;
            this.arrOfficer = arrOfficer;
    }
}
