package brunibeargames;

import java.util.ArrayList;

import brunibeargames.UI.WinCommand;
import brunibeargames.Unit.ClickAction;
import brunibeargames.Unit.Commander;
import brunibeargames.Unit.Unit;

public class DoCommand {
    static public DoCommand instance;
    ArrayList<Commander> arrCommanders = new ArrayList<Commander>();
    ArrayList<WinCommand> arrWinCommand = new ArrayList<WinCommand>();

    DoCommand() {
        instance = this;
    }

    /**
     *
     */
    public void start() {
        arrCommanders.clear();
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
        for (Commander commander : arrCommanders) {
            Unit unit = commander.getUnit();
            unit.getMapCounter().getCounterStack().removeShade();
            unit.getMapCounter().getCounterStack().hilite();
            ClickAction clickAction = new ClickAction(unit, ClickAction.TypeAction.Command);
            WinCommand winCommand = new WinCommand(commander);
        }
    }
}
