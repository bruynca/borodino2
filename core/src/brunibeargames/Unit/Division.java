package brunibeargames.Unit;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class Division {
    public String name;
    public Corp corp;
    public String mN;
    boolean isAllies;
    private boolean isGuard;
    public int entryDay=0;
    public int entryTurn=0;
    public int entryArea=0;
    static public ArrayList<Division> arrDivisions = new ArrayList<>();
    public Division(String divName, Corp corp, boolean isAllies, String entry, String divMN) {
        name = divName;
        mN = divMN;
        this.isAllies = isAllies;
        this.corp =corp;
        if (divName.contains("Guard")){
            isGuard = true;
        }
        Gdx.app.log("Division", "name=" + name+" isAllies="+isAllies);
        if (!entry.isEmpty()) {
            String[] work = entry.split(" ");
            entryDay = Integer.parseInt(work[0]);
            entryTurn = Integer.parseInt(work[1]);
            entryArea = Integer.parseInt(work[2]);
        }
        arrDivisions.add(this);
    }

    public boolean isGuard() {
        return isGuard;
    }
}
