package brunibeargames.Unit;

import java.util.ArrayList;

public class Commander {
    public String name;
    public boolean isAllied;
    public String map;
    static int commanderRange = 4;
    int canCommand;
    Unit unit;
    int movement;
    static ArrayList<Commander> arrCommander = new ArrayList<>();
    public Commander(String name, boolean isAllied, String map, Unit unit, String  canCommand){
        this.name = name;
        this.isAllied = isAllied;
        this.map = map;
        this.unit = unit;
        if (name.equals("Kutuzov")) {
            movement = 3;
        }else{
            movement = 10;
        }
        this.canCommand = Integer.valueOf(canCommand);
        arrCommander.add(this);
    }

}
