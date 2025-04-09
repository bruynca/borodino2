package brunibeargames.Unit;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

import brunibeargames.Hex;
import brunibeargames.SplashScreen;

public class Commander {
    static TextureAtlas  textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");

    public String name;
    public boolean isAllied;
    public String map;
    static int commanderRange = 4;
    int canCommand;
    Unit unit;
    int movement;
    TextureRegion textureRegion;
    public int entryDay=0;
    public int entryTurn=0;
    public int entryArea=0;

    static public ArrayList<Commander> arrCommander = new ArrayList<>();
    public Commander(String name, boolean isAllied, String map, Unit unit, String  canCommand, String entry){
        this.name = name;
        this.isAllied = isAllied;
        this.map = map;
        this.unit = unit;
        if (name.equals("Kutuzov")) {
            movement = 3;
        }else{
            movement = 10;
        }
        String strName = name.toLowerCase();
        this.canCommand = Integer.valueOf(canCommand);
        textureRegion = textureAtlas.findRegion(strName);
        if (!entry.isEmpty()) {
            String[] work = entry.split(" ");
            entryDay = Integer.parseInt(work[0]);
            entryTurn = Integer.parseInt(work[1]);
            entryArea = Integer.parseInt(work[2]);
        }

        arrCommander.add(this);
    }

    public Unit getUnit() {
         return unit;
    }

    public TextureRegion getTextureRegion() {
         return textureRegion;
    }

    public ArrayList<Officer> getOfficerPossibleAvailable() {
        ArrayList<Officer> arrOfficer = new ArrayList<>();
        UnitMove unitMove = new UnitMove(unit, commanderRange, true, false,0);
        ArrayList<Hex> arrHex = unitMove.getMovePossible();
        for (Hex hex:arrHex) {
              if (!hex.getUnitsInHex().isEmpty()){
                  for (Unit unit:hex.getUnitsInHex()){
                      if (unit.isOfficer && unit.isAllies == isAllied && !unit.officer.getisActivated()){
                          arrOfficer.add(unit.officer);
                      }
                  }

              }
        }
  //      HiliteHex hiliteHex = new HiliteHex(arrHex, HiliteHex.TypeHilite.AI,null);
        return arrOfficer;

    }
    public ArrayList<UnitsInDivision> getDivisionPossibleAvailable() {
        ArrayList<UnitsInDivision> arrUDivision = new ArrayList<>();
        UnitMove unitMove = new UnitMove(unit, commanderRange, true, false,0);
        ArrayList<Hex> arrHex = unitMove.getMovePossible();
        for (Hex hex:arrHex) {
            if (!hex.getUnitsInHex().isEmpty()){
                ArrayList<UnitsInDivision> arrIn = new ArrayList<>();
                for (Unit unit:hex.getUnitsInHex()){
                    if (!unit.isOfficer &&
                        unit.isAllies == isAllied &&
                        !unit.isActivated()){
                        //Check if already have a unit in this division
                        for (UnitsInDivision ud:arrIn){
                            if (ud.getDivision() == unit.division){
                                ud.addUnit(unit);
                                break;
                            }
                        }
                        UnitsInDivision ud=new UnitsInDivision(unit,hex);
                        arrIn.add(ud);
                    }
                    arrUDivision.addAll(arrIn);
                }

            }
        }
        return arrUDivision;

    }

    public int getCanCommand() {
        return canCommand;
    }
    class UnitsInDivision{
        ArrayList<Unit> arrUnit = new ArrayList<>();
        Division division;
        Hex hex;
        public UnitsInDivision (Unit unit, Hex hex){
            arrUnit.add(unit);
            this.hex = hex;
            division = unit.division;
        }

        Division getDivision() {
            return division;
        }
        public void addUnit(Unit unit){
            arrUnit.add(unit);
        }



    }
}
