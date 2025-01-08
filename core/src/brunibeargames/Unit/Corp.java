package brunibeargames.Unit;

import java.util.ArrayList;

public class Corp {
    String name;
    public String number;
    ArrayList<Unit> arrUnits = new ArrayList<>();
    Leader leader;
    boolean isAllies;
    public Corp(String corpNumber, String corpName, boolean isAllies){
        name = corpName.toString();
        number = corpNumber.toString();
        this.isAllies = isAllies;
        if (number.contains("4G")){
            int b =0;
        }

    }

    public String getNumber() {
        return number;
    }
}
