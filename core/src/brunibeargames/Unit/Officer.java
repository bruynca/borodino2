package brunibeargames.Unit;

import java.util.ArrayList;

public class Officer {
    public String name;
    public Corp corp;
    public boolean isAllied;
    public String map;
    int movement;
    boolean isActivated = false;

    Unit unit;
    static ArrayList<Officer> arrOfficers = new ArrayList<>();
    static int officerRange = 3;
    public Officer(String name,Corp corp, boolean isAllied, String map, Unit unit){
        this.name = name;
        this.corp = corp;
        this.isAllied = isAllied;
        this.map = map;
        this.unit = unit;
        movement= 6;
        arrOfficers.add(this);

    }

    public static void initCommand() {
        for (Officer officer : arrOfficers) {
            officer.isActivated = false;
        }
    }

    public Unit getUnit() {
        return unit;
    }
    public static  Officer getOfficer(String name) {
        for (Officer officer : arrOfficers) {
            if (officer.name.contains(name)) {
                return officer;
            }
        }
        return null;


    }
}
