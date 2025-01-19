package brunibeargames.Unit;

public class Officer {
    public String name;
    public Corp corp;
    public boolean isAllied;
    public String map;

    Unit unit;
    public Officer(String name,Corp corp, boolean isAllied, String map, Unit unit){
        this.name = name;
        this.corp = corp;
        this.isAllied = isAllied;
        this.map = map;
        this.unit = unit;

    }
}
