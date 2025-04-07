package brunibeargames.Unit;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class Division {
    public String name;
    public Corp corp;
    boolean isAllies;
    private boolean isGuard;
    static public ArrayList<Division> arrDivisions = new ArrayList<>();
    public Division(String divName, Corp corp, boolean isAllies) {
        name = divName;
        this.isAllies = isAllies;
        this.corp =corp;
        if (divName.contains("Guard")){
            isGuard = true;
        }
        Gdx.app.log("Division", "name=" + name+" isAllies="+isAllies);

        arrDivisions.add(this);
    }

    public boolean isGuard() {
        return isGuard;
    }
}
