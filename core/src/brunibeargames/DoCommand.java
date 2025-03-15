package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import brunibeargames.UI.WinCommand;
import brunibeargames.Unit.Commander;
import brunibeargames.Unit.Officer;

public class DoCommand {
    static public DoCommand instance;
    ArrayList<Commander> arrCommanders = new ArrayList<Commander>();
    ArrayList<WinCommand> arrWinCommand = new ArrayList<WinCommand>();
    ArrayList<CommanderOfficers> arrCommanderOfficers = new ArrayList<CommanderOfficers>();


    DoCommand() {
        instance = this;
    }

    /**
     *
     */
    public void start() {
        arrCommanders.clear();
        arrCommanderOfficers.clear();
        /**
         *  load all the Commanders for this part of Command after the commander set up by
         *  Determine Command
         */
        arrCommanders.addAll(DetermineCommand.instance.getArrCommand());
        /*
         *  more than 1 then we need to choose
         */
        if (arrCommanders.size() > 1) {
            setupCommandChoose();
            return;
        }
    }

    private void setupCommandChoose() {
        float offsett = 50;
        offsett = calculateInitialWindow(arrCommanders.size());
        float y = (Gdx.graphics.getHeight() - 500f) / 2f;
        float x = offsett;
        for (Commander commander : arrCommanders) {
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
        arrCommanderOfficers.add(commanderOfficers);
        arrCommanders.remove(commander);
        if (arrCommanders.isEmpty()) {
            end();
        }

    }

    void end() {

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
