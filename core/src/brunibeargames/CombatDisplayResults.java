package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Observable;

import brunibeargames.UI.MouseImage;

public class CombatDisplayResults extends Observable {

    static public CombatDisplayResults instance;
    private Image background;
    private Group group;
    private Label russianResults;
  //  private Label russianBrigade;
   // private Label alliedBrigade;

    private Label alliedResults;
    private Label results;

    private boolean visible;
    private Label title;
    private I18NBundle i18NBundle;
    Stage stage;
    Label.LabelStyle labelStyleDisplay;
    TextureAtlas textureAtlas = SplashScreen.instance.effectsManager.get("effects/combat.txt");
    TextureRegion tBackRussian =  textureAtlas.findRegion("combatresultsdisplayrussian");
    TextureRegion tBackAllied =  textureAtlas.findRegion("combatresultsdisplayfrench");
    int cntDefendLable=0;
    int cntAttackLable=0;
    int maxLable = 5;
    private Label[] battleDefendLabel = new Label[5];
    private String battleDefendString = "";
    private Label[] battleAttackLabel = new Label[5];
    private String battleAttacktring = "";
    public CombatDisplayResults(){
        labelStyleDisplay  =new Label.LabelStyle(FontFactory.instance.largeFontWhite, Color.YELLOW);

        this.instance = this;
        stage = Borodino.instance.guiStage;
        group = new Group();
        group.setVisible(false);
        i18NBundle = GameMenuLoader.instance.localization;

        initializeBackgroundImage();
        initializeAttackerResultsLabel();
        initializeResultsLabel();
        initializeTitleLabel();

        stage.addActor(group);
    }
    public void clearText(){
        alliedResults.setText("");
        russianResults.setText("");
        results.setText("");
    }
    public void updateResults(String strOdds, Attack attack){
        this.attack = attack;
        Gdx.app.log("CombatDisplayResults", "update Results"+strOdds);
        if (attack.isAllies()){
            background.setDrawable(new TextureRegionDrawable(tBackAllied));
        }else{
            background.setDrawable(new TextureRegionDrawable(tBackRussian));
        }
        StringBuffer strResult = new StringBuffer();
        int attackerLoses = 0;
        int defenderLoses = 0;
        cntDefendLable = 0;
        cntAttackLable = 0;

        for (int i=0; i<maxLable; i++){
            if (battleDefendLabel[i] != null){
                battleDefendLabel[i].remove();
            }
            if (battleAttackLabel[i] != null){
                battleAttackLabel[i].remove();
            }
        }
        battleDefendLabel = new Label[maxLable];
        battleAttackLabel = new Label[maxLable];

        int defenderRetreats;
        switch (strOdds) {
            case "Ex":
                attackerLoses = 0;
                strResult.append(i18NBundle.format("exchange", Integer.toString(attackerLoses)));
                strResult.append("  ");
                break;
            case "AR":
                strResult.append(i18NBundle.format("attackerlose", Integer.toString(attackerLoses)));
                strResult.append("  ");
                break;
            case "Dr":
              //  defenderLoses = Character.getNumericValue(strOdds.charAt(i + 1));
                strResult.append(i18NBundle.format("defenderretreat", Integer.toString(defenderLoses)));
                strResult.append("  ");

                break;
        }

        results.setText(strResult);
        results.pack();
        GlyphLayout layout = results.getGlyphLayout();
        float height = layout.height;
        float width = layout.width;
        results.setSize(width, height);
        float x = ((background.getWidth() - width)/2) +background.getX();
        results.setPosition(x , background.getY() + background.getHeight() - (height + 120));
        if (!group.isVisible()) {
            visible = true;
            group.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(0.5f)));
            setChanged();
            notifyObservers(new ObserverPackage(ObserverPackage.Type.CombatDisplayResults,null,0,0));
            CombatDisplay.instance.end();
        }

    }
    public void updateCombatResultsDefender(String message){
        Gdx.app.log("CombatDisplayResults", "updatCombatResultsDefender ");
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24Android();
        battleDefendLabel[cntDefendLable] = new Label(message, style);
        battleDefendLabel[cntDefendLable].setWrap(true); // Enable text wrapping
        battleDefendLabel[cntDefendLable].setAlignment(Align.center); // Center-align text
        float y = background.getY() + 140;
        if (cntDefendLable > 0) {
            y -= cntDefendLable * battleDefendLabel[0].getHeight();
        }
        battleDefendLabel[cntDefendLable].setPosition((background.getX()+15 + background.getWidth()/2 + 15),y);
        group.addActor(battleDefendLabel[cntDefendLable]);
        if (cntDefendLable > 0){

        }
        battleDefendString = message;
        cntDefendLable++;

    }
    Attack attack;
    public Attack getAttack(){
        return attack;
    }
    public void updateCombatResultsAttacker(String message){
        Gdx.app.log("CombatDisplayResults", "updatCombatResultsAttacker");
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24Android();
        battleAttackLabel[cntAttackLable] = new Label(message, style);
        battleAttackLabel[cntAttackLable].setWrap(true); // Enable text wrapping
        battleAttackLabel[cntAttackLable].setAlignment(Align.center); // Center-align text
        float y = background.getY() + 140;
        if (cntAttackLable > 0) {
            y -= cntAttackLable * battleDefendLabel[0].getHeight();
        }
        battleAttackLabel[cntAttackLable].setPosition(background.getX() + 20 , y);
        group.addActor(battleAttackLabel[cntAttackLable]);
        cntAttackLable++;



    }


    public void hide(){
        Gdx.app.log("CombatDisplayResult", "Hide");

        GamePreferences.setWindowLocation("combatdisplayresults", (int) background.getX(), (int) background.getY());
        for (int i=0; i<maxLable; i++){
            if (battleDefendLabel[i] != null){
                battleDefendLabel[i].remove();
                Gdx.app.log("CombatDisplayResult", "remove d ="+battleDefendLabel[i].getText());
            }
            if (battleAttackLabel[i] != null){
                battleAttackLabel[i].remove();
                Gdx.app.log("CombatDisplayResult", "remove a ="+battleAttackLabel[i].getText());
            }
        }
        battleDefendLabel = new Label[maxLable];
        battleAttackLabel = new Label[maxLable];
        WinCRT.instance.end();
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                visible = false;
            }
        });
        group.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.visible(false),run));
    }

    public boolean isDisplayed(){
        return visible;
    }


    private void   initializeBackgroundImage(){
        background = new Image(tBackAllied);
        Vector2 v2 = GamePreferences.getWindowLocation("combatdisplayresults");
        if (v2.x == 0 && v2.y == 0) {
            background.setPosition(0, (float) Gdx.graphics.getHeight() /2 - background.getHeight()/2);
        }else{
            background.setPosition(v2.x, v2.y);
        }

        background.setHeight(304);
        background.setWidth(650);
        final boolean[] isdragged = new boolean[1];
        background.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                group.moveBy(x - 20, y - 20);
                isdragged[0] = true;
                Gdx.app.log("CombatDisplay", "dragging");
                Gdx.app.log("CombatDisplay", "x+y="+background.getX()+"  "+background.getY());


            }
        });

        background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!event.getType().equals("touchUp")) {
                    Gdx.app.log("combatdisplayresults","clicked");
                    if (isdragged[0]) {
                        isdragged[0] = false;
                        return;
                    }
                    /*if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                    }*/

                    //hide();
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                MouseImage.instance.setIgnore(true);
                MouseImage.instance.setMouseHand();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                MouseImage.instance.setIgnore(false);
                MouseImage.instance.mouseImageReset();
            }
        });

        group.addActor(background);
    }

    private void initializeAttackerResultsLabel(){
        // Create a table to hold the label with a background
       // Table table = new Table();
       // table.setSize(270, 150); // Set size of the table (adjust as needed)
       // table.setPosition(background.getX() + 20 , background.getY() +10);
       // table.top();
        // Create the label

        // Add label to table with padding and width constraint
       // table.add(battleAttackLabel).width(250).pad(10).expandY().center(); // Constrain width for wrapping

   /*     Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24Android();
        russianResults = new Label("",style);
        russianResults.setSize(30, 20);
        russianResults.setPosition(background.getX() + 20 , background.getY() + background.getHeight() - 127);
        russianResults.setVisible(true);



        russianResults.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                        hide();
                    }


                    //  hide();
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                MouseImage.instance.setIgnore(true);
                MouseImage.instance.setMouseHand();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                MouseImage.instance.setIgnore(false);
                MouseImage.instance.mouseImageReset();
            }
        }); */


    }
    private void initializeResultsLabel() {
        results = new Label("",labelStyleDisplay);
        results.setSize(30, 20);
        results.setPosition(background.getX() + background.getWidth()/2 + 5 , background.getY() + background.getHeight() - 127);
        results.setVisible(true);

        results.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
               /*     if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                        hide();
                    } */
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                MouseImage.instance.setIgnore(true);
                MouseImage.instance.setMouseHand();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                MouseImage.instance.setIgnore(false);
                MouseImage.instance.mouseImageReset();
            }
        });
        group.addActor(results);
    }



    private void initializeTitleLabel(){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24Android();
        title = new Label(GameMenuLoader.instance.localization.get("combatresults"),style);
        title.pack();
        GlyphLayout layout = title.getGlyphLayout();
        float height = layout.height;
        float width = layout.width;
        title.setSize(30, 20);
        title.setPosition(background.getX() + background.getWidth()/2 - width/2 , background.getY() + background.getHeight() - 90);
        title.setVisible(true);

        title.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                /*    if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                        hide();
                    }*/
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                MouseImage.instance.setIgnore(true);
                MouseImage.instance.setMouseHand();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                MouseImage.instance.setIgnore(false);
                MouseImage.instance.mouseImageReset();
            }
        });

        group.addActor(title);
    }
}
