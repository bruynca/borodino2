package brunibeargames.Unit;

import java.util.ArrayList;

import brunibeargames.Hex;

public class Officer {
    public String name;
    public Corp corp;
    public boolean isAllied;
    public String map;
    int movement;
    private boolean isActivated = false;
    private boolean hasBeenActivatedThisTurn =false;

    Unit unit;
    public int entryDay=0;
    public int entryTurn=0;
    public int entryArea=0;
    static ArrayList<Officer> arrOfficers = new ArrayList<>();
    static int officerRange = 3;
    public Officer(String name,Corp corp, boolean isAllied, String map, Unit unit, String entry){
        this.name = name;
        this.corp = corp;
        this.isAllied = isAllied;
        this.map = map;
        this.unit = unit;
        movement= 6;
        arrOfficers.add(this);
        if (!entry.isEmpty()) {
            String[] work = entry.split(" ");
            entryDay = Integer.parseInt(work[0]);
            entryTurn = Integer.parseInt(work[1]);
            entryArea = Integer.parseInt(work[2]);
        }

    }

    public static void initCommand() {
        for (Officer officer : arrOfficers) {
            officer.isActivated = false;
        }
    }

    public Unit getUnit() {
        return unit;
    }
    public ArrayList<Unit> getUnitsAvailable() {
        ArrayList<Unit> arrUnits = new ArrayList<>();
        UnitMove unitMove = new UnitMove(unit, officerRange, true, false, 0);
        ArrayList<Hex> arrHex = unitMove.getMovePossible();
        for (Hex hex : arrHex) {
            if (!hex.getUnitsInHex().isEmpty()) {
                for (Unit unit : hex.getUnitsInHex()) {
                    if (!unit.isOfficer && unit.isAllies == isAllied && !unit.isCommander){
                        if (unit.getCorp() == corp) {
                            arrUnits.add(unit);
                        }
                    }
                }

            }
        }
        return arrUnits;
    }

    public static  Officer getOfficer(String name) {
        for (Officer officer : arrOfficers) {
            if (officer.name.contains(name)) {
                return officer;
            }
        }
        return null;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
        if (isActivated) {
            unit.getMapCounter().getCounterStack().activate();
        }else{
            unit.getMapCounter().getCounterStack().removeActivate();
        }
    }
    public boolean getisActivated() {
        return isActivated;
    }

    public String getName() {
        return unit.brigade;
    }
}
