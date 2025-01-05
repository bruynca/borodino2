package brunibeargames.Unit;

public class Division {
    String name;
    Corp corp;
    boolean isAllies;
    private boolean isGuard;
    public Division(String divName, Corp corp, boolean isAllies) {
        name = divName;
        this.corp =corp;
        if (divName.contains("Guard")){
            isGuard = true;
        }
    }

    public boolean isGuard() {
        return isGuard;
    }
}
