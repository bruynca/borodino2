package brunibeargames.Unit;

import java.util.ArrayList;

public class Corp {
    String name ="";
    ArrayList<Unit> arrUnits = new ArrayList<>();
    Leader leader;
    boolean isAllies;
    public Corp(String corpName, boolean isAllies){
        name = corpName;
        this.isAllies = isAllies;
    }

}
