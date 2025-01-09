package brunibeargames.Unit;

import java.util.ArrayList;

public class Corp {
    String name;
    public String number;
    ArrayList<Unit> arrUnits = new ArrayList<>();
    Leader leader;
    boolean isAllies;
    static ArrayList<Corp> alliedCorp  = new ArrayList<>();
    static ArrayList<Corp> russianCorp  = new ArrayList<>();

    public Corp(String corpNumber, String corpName, boolean isAllies){
        name = corpName.toString();
        number = corpNumber.toString();
        this.isAllies = isAllies;
        if (number.contains("4G")){
            int b =0;
        }
        if (isAllies){
            alliedCorp.add(this);
            }else{
            russianCorp.add(this);
        }

    }

    public static Corp find(String number, boolean isAllies) {
        ArrayList<Corp> arrSearch = null;
        if (isAllies){
            arrSearch = alliedCorp;
        }else{
            arrSearch = russianCorp;
        }
        for (Corp corp:arrSearch){
            if (corp.number.equals(number)){
                return corp;
            }
        }
        return null;
    }

    public String getNumber() {
        return number;
    }
}
