package brunibeargames;

import java.util.ArrayList;

import brunibeargames.Unit.Commander;

public class DetermineCommand {
    static public DetermineCommand instance;
    private ArrayList<Commander> arrCommand = new ArrayList<Commander>();
//    private ArrayList<Officer> arrOfficer = new ArrayList<Officer>();
    boolean isAllied = false;

    DetermineCommand() {
        instance = this;
    }

    /*
        Create Command Array
        1. determine player
        2. based on commandMethod get commanders or officers  and put in Array
     */
    public void doGetCommandArray(Initiative initiative) {
        /**
         *  if empty first time
         */
        if (arrCommand.isEmpty()) {
            if (initiative.getIsAllies()) {
                isAllied = true;
            } else {
                isAllied = false;
            }
        }
        if (GameSetup.instance.getCommandMethod() == GameSetup.CommandMethod.StandardNAW.ordinal()) {
            populateAllCommanders(isAllied);
            NextPhase.instance.endPhase();
        }
    }

    private void populateAllCommanders(boolean isAllied) {
        if (isAllied) {
            for (Commander commander : Commander.arrCommander) {
                if (commander.isAllied) {
                    arrCommand.add(commander);
                }
            }
        } else {
            for (Commander commander : Commander.arrCommander) {
                if (!commander.isAllied) {
                    arrCommand.add(commander);
                }

            }
        }
    }

    public ArrayList<Commander> getArrCommand() {
        return arrCommand;
    }

}

