package brunibeargames.Unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import brunibeargames.FontFactory;
import brunibeargames.SplashScreen;

public class CounterStack {
    Unit unit;
    Label labelName;
    Label labelSub;
    Image imgSil;
    Label labelCorp;
    Stack stack;
    Label labelPoints;
    Image hilite;
    Image moved;
    Image backGround;
    Image corp;
    Image step;
    static TextureRegion bavariancalvary;
    static TextureRegion bavarianline2;
    static TextureRegion     cannongame;
    static TextureRegion     cannongamerussian;
    static TextureRegion    cossack;
    static TextureRegion    russiancalvary;
    static TextureRegion    wurtenburgcavalry;
    static TextureRegion    wurtenburgline;
    static TextureRegion    counterwurtenburg;
    static TextureRegion    counterbavarian;
    static TextureRegion    counterwestphalian;
    static TextureRegion    counterfrenchguard;
    static TextureRegion    counterfrenchline;
    static TextureRegion    counteritalian;
    static TextureRegion    counterpolish;
    static TextureRegion    counterrussian;
    static TextureRegion    frechcalvaryguard;
    static TextureRegion    frenchcavalry;
    static TextureRegion    frenchgrenadier2;
    static TextureRegion    frenchline2;
    static TextureRegion    italiancalvary;
    static TextureRegion    italianline2;
    static TextureRegion    polishcalvary;
    static TextureRegion    polishline2;
    static TextureRegion    russianguard2;
    static TextureRegion    russianline2;
    static TextureRegion    westphaliancalvary;
    static TextureRegion    westphalianline;
    static TextureRegion movepic;
    static TextureRegion hilitePic
            ;


    boolean isHilited;
    boolean isShaded;
    static TextureAtlas textureAtlas;
    static Label.LabelStyle labelStyleName;

    static Label.LabelStyle labelStyleName2;
    static Label.LabelStyle labelStyleName3;
    private static Label.LabelStyle labelStyleCorp;

    static ArrayList<CounterStack> arrHilited = new ArrayList<>();
    static ArrayList<CounterStack> arrShaded = new ArrayList<>();

    ArrayList<Actor> arrActors = new ArrayList<>();
    static boolean isFirstCall = true;
    private float stackScale = 1.0f;

    CounterStack(Unit unit, Stack stack){


        this.unit = unit;
        this.stack = stack;
        if (!unit.isAllies){
            createRussianStack(stack);
        }else{
            createAllied(stack);
        }

    }
    static public void loadTexture(){
        textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");
        /**
         *  load the texture regions
         */
        bavariancalvary = textureAtlas.findRegion("bavariancalvary");
        bavarianline2 = textureAtlas.findRegion("bavarianline2");

        cannongame = textureAtlas.findRegion("cannongame");
        cannongamerussian = textureAtlas.findRegion("cannongamerussian");

        cossack = textureAtlas.findRegion("cossack");

        counterbavarian = textureAtlas.findRegion("counterbavarian");
        counterwestphalian = textureAtlas.findRegion("counterwestphalian");

        counterfrenchguard = textureAtlas.findRegion("counterfrenchguard");

        counterfrenchline = textureAtlas.findRegion("counterfrenchline");

        counteritalian = textureAtlas.findRegion("counteritalian");

        counterpolish = textureAtlas.findRegion("counterpolish");

        counterrussian = textureAtlas.findRegion("counterrussian");

        frechcalvaryguard = textureAtlas.findRegion("frechcalvaryguard");

        frenchcavalry = textureAtlas.findRegion("frenchcavalry");

        frenchgrenadier2 = textureAtlas.findRegion("frenchgrenadier2");

        frenchline2 = textureAtlas.findRegion("frenchline2");

        italiancalvary = textureAtlas.findRegion("italiancalvary");

        italianline2 = textureAtlas.findRegion("italianline2");

        polishcalvary = textureAtlas.findRegion("polishcalvary");

        polishline2 = textureAtlas.findRegion("polishline2");

        russianguard2 = textureAtlas.findRegion("russianguard2");

        russianline2 = textureAtlas.findRegion("russianline2");

        westphaliancalvary = textureAtlas.findRegion("westphaliancalvary");

        westphalianline = textureAtlas.findRegion("westphalianline");
        russiancalvary = textureAtlas.findRegion("russiancalvary");
        wurtenburgcavalry = textureAtlas.findRegion("wurtemburgcavalry");
        wurtenburgline = textureAtlas.findRegion("wurtemburgline");
        counterwurtenburg = textureAtlas.findRegion("counterwurtemburg");
        movepic = textureAtlas.findRegion("moved");
        hilitePic = textureAtlas.findRegion("hilite");

        labelStyleName
                = new Label.LabelStyle(FontFactory.instance.largeFont, Color.RED);
         labelStyleName2 = new Label.LabelStyle(FontFactory.instance.jumboFont, Color.WHITE);
        labelStyleName3 = new Label.LabelStyle(FontFactory.instance.largeFontWhite, Color.WHITE);
        labelStyleCorp = new Label.LabelStyle(FontFactory.instance.corpFont, Color.YELLOW);
        arrHilited.clear();
        arrShaded.clear();


    }

    private void createRussianStack(Stack stack) {
        if (unit.isRussian){
            backGround  = new Image(counterrussian);

        }
        stack.add(backGround);
        stack.setSize(Counter.size,Counter.size);
        imgSil = getRussianSilhouttes();
        imgSil.setTouchable(Touchable.disabled);
        stack.addActor(imgSil);
        String strBrigade = unit.brigade;
        labelName= new Label(strBrigade,labelStyleName3);
        labelName.setTouchable(Touchable.disabled);
        labelName.setAlignment(Align.top);
        labelName.setScale(Counter.scaleBrigade);
        stack.addActor(labelName);
        arrActors.add(imgSil);
        setCorp();
        stack.addActor(labelCorp);

        setPoints();
//        stack.setScale(stackScale);
        stack.addActor(labelPoints);

    }

    public void setPoints(){

        String strPoints = null;
        strPoints = " "+unit.getCurrentAttackFactor()+"    "+unit.getCurrentMoveFactor();
        if (labelPoints != null){
            stack.removeActor(labelPoints);
            labelPoints = null;
        }
        labelPoints = new Label(strPoints,labelStyleName2);
        labelPoints.setTouchable(Touchable.disabled);
        labelPoints.setAlignment(Align.bottom);

    }
    public void setCorp(){
        if (labelCorp != null){
            stack.removeActor(labelCorp);
        }

        labelCorp = new Label(unit.getCorp().number, labelStyleCorp);
        if (unit.getCorp().number.length() > 1){
            labelCorp.setFontScale(.6f);
        }else{
            labelCorp.setFontScale((.8f));
        }
        labelCorp.setTouchable(Touchable.disabled);
        labelCorp.setAlignment(Align.left);
 //       labelCorp.setFontScale(1.3f);
    }
    public void adjustFont(float adjust){
        labelPoints.setFontScale(adjust);
        if (labelName != null) { // allied
            labelName.setFontScale(adjust);
        }
    }
    private void createAllied(Stack stack) {
        backGround = createBackAllied();

        stack.add(backGround);
        stack.setSize(Counter.size,Counter.size);
        String strBrigade = unit.brigade;
        labelName= new Label(strBrigade,labelStyleName);
        labelName.setTouchable(Touchable.disabled);
        labelName.setAlignment(Align.top);
        labelName.setScale(Counter.scaleBrigade);

        stack.addActor(labelName);
        //       arrActors.add(labelName);
        imgSil = getAlliedilhouttes();
        imgSil.setTouchable(Touchable.disabled);
        stack.addActor(imgSil);
        arrActors.add(imgSil);

        setCorp();
        stack.addActor(labelCorp);
        String str = String.valueOf(unit.getCurrentStep())+" ";
//        labelSub= new Label(str,labelStyleName);
//        labelSub.setTouchable(Touchable.disabled);
//
//        labelSub.setAlignment(Align.right);
//        labelSub.setPosition(30,0);/
//        labelSub.setFontScale(.8f);
//        stack.addActor(labelSub);

//        arrActors.add(labelName);
        setPoints();
 //       stack.setScale(stackScale);

        stack.addActor(labelPoints);

//        arrActors.add(labelPoints);
    }

    private Image createBackAllied() {
        Image image = null;
        if (unit.isGuard){
            image = new Image(counterfrenchguard);
            return image;
        }
        if (unit.isFrench){
            image = new Image(counterfrenchline);
            return image;
        }
        if (unit.isItalian){
            image = new Image(counteritalian);
            return image;
        }
        if (unit.isPolish){
            image = new Image(counterpolish);
            return image;
        }
        if (unit.isBavarian){
            image = new Image(counterbavarian);
            return image;
        }
        if (unit.isWestphalian){
            image = new Image(counterwestphalian);
            return image;
        }
        if (unit.isWurttenburg){
            image = new Image(counterwurtenburg);
            return image;
        }

        image = new Image(frenchline2);
        return image;
    }

    private Image getRussianSilhouttes() {
        Image image;
        if (unit.isInfantry){
            if (unit.isGuard){
                image = new Image(russianguard2)   ;
            }else{
                image = new Image(russianline2);
            }
            return image;
        }
        if (unit.isCalvary){
            if (unit.isCossack){
                image = new Image(cossack);
            }else{
                image = new Image(russiancalvary);
            }
            return image;
        }
        image = new Image(cannongamerussian);
        return image;
    }


/*
    private Image getGermanHQs(TextureAtlas tex) {
        Image image = new Image();
        switch (unit.designation) {
            case "VG 276":
                image = new Image(hqvg276);
                break;
            case "VG 352":
                image = new Image(hqvg352);
                break;

            default:
                image = new Image(silstug);
                break;

        }
        return image;
    }*/

    private Image getAlliedilhouttes() {
        Image image = null;
        if (unit.isArtillery){
            image = new Image(cannongame);
            return image;
        }
        if (unit.isGuard){
            if (unit.isInfantry) {
                image = new Image(frenchgrenadier2);
            }else if(unit.isCalvary){
                image = new Image(frechcalvaryguard);
            }
            return image;
        }
        if (unit.isItalian){
            if (unit.isInfantry) {
                image = new Image(italianline2);
            }else if(unit.isCalvary){
                image = new Image(italiancalvary);
            }
            return image;
        }
        if (unit.isPolish){
            if (unit.isInfantry) {
                image = new Image(polishline2);
            }else if(unit.isCalvary){
                image = new Image(polishcalvary);
            }
            return image;
        }
        if (unit.isBavarian) {
            if (unit.isInfantry) {
                image = new Image(bavarianline2);
            } else if (unit.isCalvary) {
                image = new Image(bavariancalvary);
            }
            return image;
        }
        if (unit.isWestphalian) {
            if (unit.isInfantry) {
                image = new Image(westphalianline);
            } else if (unit.isCalvary) {
                image = new Image(westphaliancalvary);
            }
            return image;
        }
        if (unit.isWurttenburg) {
            if (unit.isInfantry) {
                image = new Image(wurtenburgline);
            } else if (unit.isCalvary) {
                image = new Image(wurtenburgcavalry);
            }
            return image;
        }

        if (unit.isFrench){
            if (unit.isInfantry) {
                image = new Image(frenchline2);
            }else if(unit.isCalvary){
                image = new Image(frenchcavalry);
            }
            return image;
        }

        return null;

    }

    private void placeLabelName(Stack stack) {
        float stackX = stack.getX();
        float stackY = stack.getY();
        int x = (int)stackX + 6; // font size
        labelName.setPosition(x,stackY+80);
    }

    private void placeSil(Stack stack) {
        float stackX = stack.getX();
        float stackY = stack.getY();
        int x = (int)stackX + 15; // font size
        imgSil.setPosition(x,stackY+55);
    }
    public Stack getStack(){
        return stack;
    }


    private void placeLabelPoints(Stack stack) {
        float stackX = stack.getX();
        float stackY = stack.getY();
        int x = (int)stackX + 10; // font size
        labelPoints.setPosition(x,stackY+10);

    }
    public void shade()
    {
//		Gdx.app.log("Counter", "Shadeunit="+getUnit().toString());

        if (moved == null) {
            moved = new Image(movepic);
        }
        stack.add(moved);
        arrShaded.add(this);
        removeHilite(); // just in case
        isShaded = true;
    }

    /**
     *  unblacken the counter to show this counter is in play
     */
    public void removeShade()
    {
        stack.removeActor(moved);
        isShaded = false;
        arrShaded.remove(this);
    }
    /**
     *  Hilite with purpose for map counters
     */
    public void removeHilite() {
        stack.removeActor(hilite);
        arrHilited.remove(this);
        isHilited = false;
    }
    public void hilite() {
        if (hilite == null)
        {
            hilite = new Image(hilitePic);
        }
        stack.add(hilite);
        isHilited = true;
        arrHilited.add(this);
    }

    public static TextureRegion getHilite() {
        return hilitePic;
    }

    public boolean isHilited(){
        return isHilited;
    }
    public boolean isShaded(){
        return isShaded;
    }
    public static void removeAllHilites(){
        ArrayList<CounterStack> arrWork = new ArrayList<>();
        arrWork.addAll(arrHilited);
        for (CounterStack cs:arrWork){
            cs.removeHilite();
        }
        arrHilited.clear();

    }
    public static void removeAllShaded(){
        ArrayList<CounterStack> arrWork = new ArrayList<>();
        arrWork.addAll(arrShaded);
        for (CounterStack cs:arrWork){
            cs.removeShade();
        }
        arrShaded.clear();

    }

    public void removeBack() {
        if (backGround != null) {
            backGround.setVisible(false);
        }
    }
    public void addBack() {
        if (backGround != null) {
            backGround.setVisible(true);
        }
    }

    public void setInvisible() {
        this.stack.setVisible(false);
    }
}
