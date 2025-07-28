package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public class CombatDisplay {
    TextureAtlas textureAtlas = SplashScreen.instance.effectsManager.get("effects/combat.txt");
    TextureRegion tBridge =  textureAtlas.findRegion("bridge");
    TextureRegion tMechAttack =  textureAtlas.findRegion("mechattack");
    TextureRegion tRiver =  textureAtlas.findRegion("river");
    TextureRegion tTown =  textureAtlas.findRegion("town");
    TextureRegion tTrees =  textureAtlas.findRegion("trees");
    TextureRegion tBackRussian =  textureAtlas.findRegion("backgroundrussiann");
    TextureRegion tBackAllied =  textureAtlas.findRegion("backgroundallied");
    TextureRegion tVillage =  textureAtlas.findRegion("village");
    TextureRegion tSurpise =  textureAtlas.findRegion("surprise");


    private Image background;
    private Image bridge;  //forest
    private Image mechattack;  //bridge
    private Image river; // village
    private Image odds;
    private Image town;
    private Image trees;
    private Image village;
    private Image surprise;
    private Label germanCombatFactors;
    private Label alliedCombatFactors;
    private Label lossesHeader;
    private Group group;
    private Group hexGroup;
    private I18NBundle i18NBundle;
    private final TextTooltip.TextTooltipStyle tooltipStyle;

    static public CombatDisplay instance;
    Stage stage;
    WinCRT winCRT;
    Attack attack;

    public CombatDisplay() {
        Gdx.app.log("CombatDisplay", "Constructor");

        instance = this;
        stage = Borodino.instance.guiStage;
        group = new Group();
        group.setVisible(false);
        hexGroup = new Group();
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(Fonts.getFont24(), Color.WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);

        initializeBackgroundImage();
        initializeOddsImage();
        initializeGermanCombatFactorsLabel();
        initializeRussianCombatFactorsLabel();
        initializeBridgeImage();
        initializeMechAttackImage();
        initializeRiverImage();
        initializeTownImage();
        initializeTreesImage();
        initializeVillageImage();
        initializeSurpriseImage();


        group.addActor(hexGroup);
        stage.addActor(group);
    }


    public void updateCombat(Attack attack, String odds) {
       this.attack = attack;

        group.setVisible(true);
        hexGroup.clear();
        String dOds = odds.replaceAll(":","to") ;
        TextureRegion  tOdds = textureAtlas.findRegion(dOds);
        this.odds.setDrawable(new TextureRegionDrawable(tOdds));
        //this.odds = new Image(tOdds);
        if (attack.isAllies()){
            background.setDrawable(new TextureRegionDrawable(tBackAllied));
        }else{
            background.setDrawable(new TextureRegionDrawable(tBackRussian));
        }

        int yposition = showIcons(attack.getHexTarget(),  attack);
        germanCombatFactors.setText((int)attack.getAttackStrength() + "");
        alliedCombatFactors.setText((int)attack.getDefenseStrength() + "");



    }
    public Attack getAttack(){
        return attack;
    }
    boolean haBeenDragged = false;
    public void end(){
        group.setVisible(false);
        //WinCRT.instance.end();
        haBeenDragged = false;
        GamePreferences.setWindowLocation("combatdisplay", (int) background.getX(), (int) background.getY());

        if (hexGroup != null) {
            //hexGroup.clear();
        }
    }


    private int showIcons(Hex hexTarget, Attack attack) {

        // Clear up first
        hexGroup.clear();
        // Now re-add if required
        int xPosition = 10;
        int xPositionDefend  = 171;

        int yPositionAttack = 165;
        int yPositionDefend = 165;

        /**
         hexTarget.setFortfication(true);
         hexTarget.setSevastopol(true);
         hexTarget.setMarsh(true);
         hexTarget.setHills(true);
         hexTarget.setBroken(true);
         hexTarget.setLargeTown(true);
         hexTarget.setEntrenchment(true);
         */
        if (attack.isTown()) {
            town.setPosition(background.getX() + xPositionDefend, background.getY() + background.getHeight() - yPositionDefend);
            hexGroup.addActor(town);
            yPositionDefend += 25;
        }
        if (attack.isTrees()) {
            trees.setPosition(background.getX() + xPositionDefend, background.getY() + background.getHeight() - yPositionDefend);
            hexGroup.addActor(trees);
            yPositionDefend += 25;
        }
        if (attack.isVillage()) {
            village.setPosition(background.getX() + xPositionDefend, background.getY() + background.getHeight() - yPositionDefend);
            hexGroup.addActor(village);
            yPositionDefend += 25;
        }
        if (attack.isRiver()) {
            river.setPosition(background.getX() + xPosition, background.getY() + background.getHeight() - yPositionAttack);
            hexGroup.addActor(river);
            yPositionAttack += 25;
        }

        if (attack.isBridge()) {
            bridge.setPosition(background.getX() + xPosition, background.getY() + background.getHeight() - yPositionAttack);
            hexGroup.addActor(bridge);
            yPositionAttack += 25;
        }
        if (attack.isMechAttack()) {
            mechattack.setPosition(background.getX() + xPosition, background.getY() + background.getHeight() - yPositionAttack);
            hexGroup.addActor(mechattack);
           yPositionAttack += 25;
        }
        if (!attack.isAllies() && NextPhase.instance.getTurn() <= 2){
            surprise.setPosition(background.getX() + xPosition, background.getY() + background.getHeight() - yPositionAttack);
            hexGroup.addActor(surprise);
            yPositionAttack += 25;
       }


        int linePosition = 0;
        if (yPositionAttack > 165 || yPositionDefend > 165) {
            if (yPositionAttack > yPositionDefend) {
                linePosition = yPositionAttack - 10;
            } else {
                linePosition = yPositionDefend - 10;
            }
            Image line = new Image(new TextureRegion(UILoader.instance.combatDisplay.asset.get("line")));
            line.setHeight(2 / (1));
            line.setWidth(220 / 1);
            line.setPosition(background.getX() + 10, background.getY() + background.getHeight() - line.getHeight() - linePosition);
            linePosition += 10;
            hexGroup.addActor(line);
        }else {
            linePosition = 155;
        }
        WinCRT.instance.end();
        WinCRT.instance.show(attack, "");
        return linePosition;
    }

    private void initializeBackgroundImage(){

        background = new Image(tBackRussian);
        background.setHeight(360);
        background.setWidth(236);
        Vector2 v2 = GamePreferences.getWindowLocation("combatdisplay");
        if (v2.x == 0 && v2.y == 0) {
            background.setPosition(Gdx.graphics.getWidth()-(background.getWidth()+10),
                    Gdx.graphics.getHeight()-(background.getHeight()-10));
        }else{
            background.setPosition(v2.x, v2.y);
        }

        //background.setPosition((Gdx.graphics.getWidth() - (background.getWidth() +160)), (Gdx.graphics.getHeight() - background.getHeight() - 16 ));

        background.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                group.moveBy(x - 20, y - 20);
                haBeenDragged = true;
                Gdx.app.log("CombatDisplay", "dragging");
                Gdx.app.log("CombatDisplay", "x+y="+background.getX()+"  "+background.getY());


            }
        });

        group.addActor(background);
    }

    private void initializeOddsImage(){

        odds = new Image(textureAtlas.findRegion("0to0"));
        odds.setHeight(50);
        odds.setWidth(110);
        odds.setPosition(background.getX() + 63 , background.getY() + background.getHeight() - 110);

        group.addActor(odds);
    }

    private void initializeTownImage(){

        town = new Image(tTown);
        town.setHeight(24);
        town.setWidth(51);
        town.setPosition((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2));

        town.addListener(new TextTooltip(
                i18NBundle.get("ltown"),
                tooltipStyle));
    }

    private void initializeVillageImage() {

        village = new Image(tVillage);
 //       village.setHeight(24);
 //       village.setWidth(51);
        village.setPosition((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2));

        village.addListener(new TextTooltip(
                i18NBundle.get("village"),
                tooltipStyle));
    }
    private void initializeSurpriseImage() {

        surprise = new Image(tSurpise);
        //       village.setHeight(24);
        //       village.setWidth(51);
        surprise.setPosition((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2));

        surprise.addListener(new TextTooltip(
                i18NBundle.get("surprise"),
                tooltipStyle));
    }

    private void initializeTreesImage() {
        trees = new Image(tTrees);
  //      trees.setHeight(24);
  //      trees.setWidth(51);
        trees.setPosition((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2));

        trees.addListener(new TextTooltip(
                i18NBundle.get("trees"),
                tooltipStyle));

    }

    private void initializeRiverImage() {
        river = new Image(tRiver);
   //     river.setHeight(24);
   //     river.setWidth(51);
        river.setPosition((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2));

        river.addListener(new TextTooltip(
                i18NBundle.get("river"),
                tooltipStyle));

    }

    private void initializeMechAttackImage() {
        mechattack = new Image(tMechAttack);
//        mechattack.setHeight(24);
//        mechattack.setWidth(51);
        mechattack.setPosition((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2));

        mechattack.addListener(new TextTooltip(
                i18NBundle.get("mechanized"),
                tooltipStyle));

    }

    private void initializeBridgeImage() {
        bridge = new Image(tBridge);
  //      bridge.setHeight(24);
 //       bridge.setWidth(51);
        bridge.setPosition((Gdx.graphics.getWidth() / 2), (Gdx.graphics.getHeight() / 2));

        bridge.addListener(new TextTooltip(
                i18NBundle.get("bridge"),
                tooltipStyle));

    }


    private void initializeGermanCombatFactorsLabel(){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24();
        germanCombatFactors = new Label("0",style);
        germanCombatFactors.setAlignment(Align.center);
        germanCombatFactors.setSize(30, 20);
        germanCombatFactors.setPosition(background.getX() + 21 , background.getY() + background.getHeight() - 127);
        germanCombatFactors.setVisible(true);

        group.addActor(germanCombatFactors);
    }

    private void initializeRussianCombatFactorsLabel(){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24();
        alliedCombatFactors = new Label("0",style);
        alliedCombatFactors.setAlignment(Align.center);
        alliedCombatFactors.setSize(30, 20);
        alliedCombatFactors.setPosition(background.getX() + 187 , background.getY() + background.getHeight() - 127);
        alliedCombatFactors.setVisible(true);

        group.addActor(alliedCombatFactors);
    }

    public boolean checkOutsideWindow(int x, int y) {
    /*        float winStartx = background.getX();
            float winEndx = background.getX() + background.getWidth();
            float winStarty = background.getY();
            float winEndy = background.getY() + background.getHeight();
            int reverse = Gdx.graphics.getHeight() - y;
            if (x < winStartx || x > winEndx || reverse < winStarty || reverse > winEndy) {
                return true;
            } else {
                return false;
            } */
            if (haBeenDragged){
                haBeenDragged = false;
                return false;
            }else{
                return true;
            }
    }
}



