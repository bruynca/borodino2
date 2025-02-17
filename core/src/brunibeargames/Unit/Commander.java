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
    static public ArrayList<Commander> arrCommander = new ArrayList<>();
    public Commander(String name, boolean isAllied, String map, Unit unit, String  canCommand){
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
        arrCommander.add(this);
    }

    public Unit getUnit() {
         return unit;
    }

    public TextureRegion getTextureRegion() {
         return textureRegion;
    }

    public ArrayList<Officer> getOfficerAvailable() {
        ArrayList<Officer> arrOfficer = new ArrayList<>();
        UnitMove unitMove = new UnitMove(unit, commanderRange, true, false,0);
        ArrayList<Hex> arrHex = unitMove.getMovePossible();
        for (Hex hex:arrHex) {
              if (!hex.getUnitsInHex().isEmpty()){
                  for (Unit unit:hex.getUnitsInHex()){
                      if (unit.isOfficer && unit.isAllies == isAllied){
                          arrOfficer.add(unit.officer);
                      }
                  }

              }
        }
  //      HiliteHex hiliteHex = new HiliteHex(arrHex, HiliteHex.TypeHilite.AI,null);
        return arrOfficer;

    }
}
