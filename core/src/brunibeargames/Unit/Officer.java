package brunibeargames.Unit;

import java.util.ArrayList;

public class Officer {
    public String name;
    public Corp corp;
    public boolean isAllied;
    public String map;
    int movement;

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
}
