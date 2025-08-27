package brunibeargames.Unit;

import java.util.ArrayList;

import brunibeargames.Hex;
import brunibeargames.HexHelper;

public class UnitHexToAttack {
    Unit unit;
    ArrayList<Hex> arrToAttack = new ArrayList<>();
    static ArrayList<UnitHexToAttack> arrMaster = new ArrayList<>();
    private UnitHexToAttack(Unit unit, Hex hexAttack) {
        UnitHexToAttack uh= findUnitInt(unit);
        if (uh == null){
           this.unit = unit;
           arrToAttack.add(hexAttack);
           arrMaster.add(this);
        }else{
            uh.arrToAttack.add(hexAttack);
            HexHelper.removeDupes(arrToAttack);

        }
    }

    public static  void  startProcess(){
        arrMaster.clear();
    }
    public Unit getUnit() {
        return unit;
    }
    public static UnitHexToAttack findUnitInt(Unit unit) {
        for (UnitHexToAttack unitInt : arrMaster){
            if (unitInt.getUnit() == unit){
                return unitInt;
            }
        }
        return null;
    }
    public static UnitHexToAttack addUnitHex(Unit unit,Hex hexAttack){
        UnitHexToAttack unitHexToAttack  = findUnitInt(unit);
        if (unitHexToAttack == null){
            unitHexToAttack = new UnitHexToAttack(unit,hexAttack);
            return unitHexToAttack;
        }else{
            unitHexToAttack.arrToAttack.add(hexAttack);
            HexHelper.removeDupes(unitHexToAttack.arrToAttack);
        }
        return unitHexToAttack;
    }

}


