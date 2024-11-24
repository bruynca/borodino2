package brunibeargames.Unit;


import java.util.ArrayList;

import brunibeargames.Hex;

public class UnitHexes {
    public Unit unit;
    public ArrayList<Hex> arrHexes = new ArrayList<>();
    public UnitHexes(Unit unitIn, ArrayList<Hex> arrHexIn){
        unit = unitIn;
        arrHexes.addAll(arrHexIn);
    }

}
