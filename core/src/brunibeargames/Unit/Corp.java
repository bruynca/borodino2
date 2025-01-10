package brunibeargames.Unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Corp {
    String name;
    public String number;
    ArrayList<Unit> arrUnits = new ArrayList<>();
    Leader leader;
    boolean isAllies;
    static ArrayList<Corp> alliedCorp  = new ArrayList<>();
    static ArrayList<Corp> russianCorp  = new ArrayList<>();
    Texture corpTex;

    static int[][] clrArray={{113,297,133}, {240,160,72}, {139,84,48},{ 113,207,122},
            {240,160,72},{254,240,154},{82,130,198},{240,227,211},{211,194,178},
            {200,172,133},{239,179,21},{218,255,127},{218,156,209},{255,127,127},
            {255,255,255},{255,28,127}};
    static int colorNum;
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


        Color color = new Color(clrArray[colorNum][0]/255f,clrArray[colorNum][1]/255f,clrArray[colorNum][2]/255f,255/255f );
        colorNum++;
        if (colorNum == clrArray.length){
            colorNum = 0;
        }
        corpTex = generateTexture(color);
    }
    public  Texture generateTexture(Color color){
        Pixmap pixmap = new Pixmap( 117, 118, Pixmap.Format.RGBA8888 );
        pixmap.setColor(color);
        pixmap.fillRectangle(0,0,117,24);
        Texture pixmaptex = new Texture( pixmap );
        return  pixmaptex;
    }
    public Texture getCorpTtexture(){
        return corpTex;
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
