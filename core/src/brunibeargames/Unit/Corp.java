package brunibeargames.Unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Corp {
    public String name;
    public String number;
    Color color;
    ArrayList<Unit> arrUnits = new ArrayList<>();
    Leader leader;
    boolean isAllies;
    static public ArrayList<Corp> alliedCorp  = new ArrayList<>();
    static public ArrayList<Corp> russianCorp  = new ArrayList<>();
    Texture corpTex;

    static int[][] clrArray={{113,297,133}, {240,160,72}, {139,84,48},{ 113,207,122},
            {240,160,72},{254,240,154},{82,130,198},{240,227,211},{211,194,178},
            {200,172,133},{239,179,21},{218,255,127},{218,156,209},{255,127,127},
            {255,255,255},{255,28,127}};
    static int colorNum;
    static ArrayList<Color> arrColor = new ArrayList<>();
    static ArrayList<CorpColor> arrCorpColors =  new ArrayList();
    public Corp(String corpNumber, String corpName, boolean isAllies){
        if (arrColor.size() == 0){
            loadColorTable();
            loadColorTable2();
        }
        name = corpName.toString();
        number = corpNumber.toString();
        this.isAllies = isAllies;
        if (number.contains("1A")){
            int b =0;
        }
        if (isAllies){
            alliedCorp.add(this);
            }else{
            russianCorp.add(this);
        }

 //       for (CorpColor ccol:arrCorpColors){
 //           if (number.equals(ccol.corpNumber)){
 //               color = ccol.color;
 //           }
 //       }
        color = arrColor.get(colorNum);
        colorNum++;
        if (colorNum == arrColor.size()){
            colorNum = 0;
        }
        corpTex = generateTexture(color);
    }

    private void loadColorTable2() {
        CorpColor corpColor = new CorpColor(Color.valueOf("1EA88C"),"4");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("A8A41E"),"1");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("551EA9"),"1C");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("A8431E"),"5");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("3B2B53"),"2C");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("19A895"),"3C");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("A88A19"),"3");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("77A689"),"YG");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.valueOf("A69477"),"OG");
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.CHARTREUSE,"2",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.CORAL,"3",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.CYAN,"4",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.LIGHT_GRAY,"5",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.LIME,"6",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.MAGENTA,"1C",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.NAVY,"2C",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.FOREST,"3C",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.LIME,"1A",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.PINK,"CO",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.ORANGE,"7",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.OLIVE,"8",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.OLIVE,"4C",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.FIREBRICK,"K",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.TAN,"2A",true);
        arrCorpColors.add(corpColor);
        corpColor = new CorpColor(Color.YELLOW,"MM",true);
        arrCorpColors.add(corpColor);


    }

    private static  void loadColorTable() {
        arrColor.add(Color.CORAL);
        arrColor.add(Color.LIME);
        arrColor.add(Color.CHARTREUSE);
        arrColor.add(Color.ORANGE);
        arrColor.add(Color.CYAN);
        arrColor.add(Color.SLATE);
//        arrColor.add(Color.GOLD);
        arrColor.add(Color.PINK);
        for (int i=0; i <clrArray.length; i++){
            Color color = new Color(clrArray[i][0]/255f,clrArray[i][1]/255f,clrArray[i][2]/255f,255/255f );
            arrColor.add(color);
        }
        arrColor.add(Color.CORAL);
        arrColor.add(Color.LIME);
        arrColor.add(Color.CHARTREUSE);
        arrColor.add(Color.ORANGE);
        arrColor.add(Color.CYAN);
        arrColor.add(Color.SLATE);
//        arrColor.add(Color.GOLD);
        arrColor.add(Color.PINK);
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
    class CorpColor{
        Color color;
        String corpNumber;
        boolean isAllies;
        CorpColor(Color colorin, String corpNumberIn){
            color = colorin;
            corpNumber = corpNumberIn.toString();
            isAllies = true;
        }
        CorpColor(Color colorin, String corpNumberIn, boolean isRussian){
            color = colorin;
            corpNumber = corpNumberIn.toString();
            isAllies = !isRussian;
        }

    }
}
