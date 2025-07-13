package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
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

    public CombatDisplayResults(){
        labelStyleDisplay  =new Label.LabelStyle(FontFactory.instance.largeFontWhite, Color.YELLOW);

        this.instance = this;
        stage = Borodino.instance.guiStage;
        group = new Group();
        group.setVisible(false);
        i18NBundle = GameMenuLoader.instance.localization;

        initializeBackgroundImage();
        initializeDefenderResultsLabel();
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
    public void updateResults(String strOdds){
        Gdx.app.log("CombatDisplayResults", "update Results"+strOdds);
        if (attack.isAllies()){
            background.setDrawable(new TextureRegionDrawable(tBackAllied));
        }else{
            background.setDrawable(new TextureRegionDrawable(tBackRussian));
        }
        StringBuffer strResult = new StringBuffer();
        int attackerLoses = 0;
        int defenderLoses = 0;
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
            case "DR":
              //  defenderLoses = Character.getNumericValue(strOdds.charAt(i + 1));
                strResult.append(i18NBundle.format("defenderlose", Integer.toString(defenderLoses)));
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
       }

    }
    public void updateCombatResultsDefender(ArrayList<CombatResults> combatResults, boolean isAttackerAllies, Attack attack){
        Gdx.app.log("CombatDisplayResults", "updatCombatResultsDefender");

        this.attack = attack;
        isDefenseDisplayed = true;
        String defender = "";
        String strBrigade =   " ";
        if (combatResults.size() > 0) {
            for (int i = 0; i < combatResults.size(); i++) {
                CombatResults combatResult = combatResults.get(i);
                strBrigade = combatResult.getUnit().brigade;
                String paddedShort = String.format("%-" + 11 + "s", strBrigade);

                //String paddedRightFormat = String.format("%-" + 12 + "s", str);
                if (combatResult.isDestroyed()) {
                    defender += i18NBundle.format("destroyed", paddedShort) + "\n";
                    //defender += combatResult.getUnitName() + " was destroyed" + "\n";
                }
                if (combatResult.isStepLosses()) {
                    defender += i18NBundle.format("loststep") + "\n";
                    //defender += combatResult.getUnitName() + " lost step" + "\n";
                }

            }
            if (combatResults.get(0).getHexesRetreated() > 0) {
                defender += i18NBundle.format("defenderretreated", combatResults.get(0).getHexesRetreated()) + "\n";
                //defender += "Defending forces retreated " + combatResults.get(0).getHexesRetreated() + " hexes";
            }
        }else{
            defender += i18NBundle.format("noresult");
        }
        if (!isAttackerAllies){
            alliedResults.setText(defender);
            setAlliedLabel();
        }else {
            russianResults.setText(defender);
            setAxisLabel();
        }

    }
    Attack attack;
    public Attack getAttack(){
        return attack;
    }
    public void updateCombatResultsAttacker(ArrayList<CombatResults> combatResults, boolean isAttackerAllies, Attack attack){
        Gdx.app.log("CombatDisplayResults", "updatCombatResultsAttacker");

        this.attack = attack;
        isAttackDisplayed = true;
        String attacker = "";
        boolean isjustOneAdvance = false;
        boolean isjustMover = false;
        if (combatResults.size() > 0) {

            for (int i = 0; i < combatResults.size(); i++) {
                CombatResults combatResult = combatResults.get(i);
                String strBrigade = combatResult.getUnit().brigade;
                String paddedShort = String.format("%-" + 11 + "s", strBrigade);

                if (combatResult.isDestroyed()) {
                    attacker += i18NBundle.format("destroyed",paddedShort)+ "\n";
                    //attacker += combatResult.getUnitName() + " was destroyed" + "\n";
                }
                if (combatResult.isStepLosses()) {
                    attacker += i18NBundle.format("loststep")+ "\n";
                    //attacker += combatResult.getUnitName() + " lost step" + "\n";
                }
                if (combatResult.isCanContinueMovement()) {
                    if (!isjustMover) {
                        attacker += i18NBundle.get("cancontinuemoving");
                        isjustMover = true;
                    }
                }
                if (combatResult.isCanAdvance()) {
                    if (!isjustOneAdvance) {
                        attacker += i18NBundle.get("canadvance");
                        isjustOneAdvance = true;
                    }
                    //attacker += "Attacking Units can Advance";
                }
                if (combatResults.get(0).getHexesRetreated() > 0) {
                    attacker += i18NBundle.format("attackerretreated", combatResults.get(0).getHexesRetreated()) + "\n";
                    //attacker += "Defending forces retreated " + combatResults.get(0).getHexesRetreated() + " hexes";
                }

            }
        }else{
            attacker += i18NBundle.format("noresult");
        }
        if (!isAttackerAllies){
            russianResults.setText(attacker);
            setAxisLabel();
            return;
        }else {
            alliedResults.setText(attacker);
            setAlliedLabel();
        }

    }
    boolean isAttackDisplayed = false;
    boolean isDefenseDisplayed = false;
    private void setAxisLabel(){
        russianResults.pack();
        GlyphLayout layout = russianResults.getGlyphLayout();
        float height = layout.height;
        float width = layout.width;
        russianResults.setSize(width, height);
        russianResults.setPosition(background.getX() + 20 , background.getY() + background.getHeight() - (height + 160));
        if (!group.isVisible()) {
            visible = true;
            group.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(0.5f)));
            setChanged();
            notifyObservers(new ObserverPackage(ObserverPackage.Type.CombatDisplayResults,null,0,0));
        }

    }
    private void setAlliedLabel(){
        alliedResults.pack();
        GlyphLayout layout = alliedResults.getGlyphLayout();
        float height = layout.height;
        float width = layout.width;
        alliedResults.setSize(width, height);
        alliedResults.setPosition(background.getX() + background.getWidth()/2 + 20 , background.getY() + background.getHeight() - (height + 160));
        if (!group.isVisible()) {
            visible = true;
            group.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(0.5f)));
            setChanged();
            notifyObservers(new ObserverPackage(ObserverPackage.Type.CombatDisplayResults,null,0,0));
        }

    }


    public void hide(){
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
        background.setHeight(304);
        background.setWidth(650);
        background.setPosition((Gdx.graphics.getWidth()/2 - background.getWidth()/2), (Gdx.graphics.getHeight()/2 - background.getHeight()/2));

        background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!event.getType().equals("touchUp")) {
                    Gdx.app.log("combatdisplayresults","clicked");
                    if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                    }
                    hide();
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
        Label.LabelStyle style = new Label.LabelStyle();
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
        });


        group.addActor(russianResults);
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
                    if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                        hide();
                    }
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


        private void initializeDefenderResultsLabel(){
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Fonts.getFont24Android();
        alliedResults = new Label("",style);
        alliedResults.setSize(30, 20);
        alliedResults.setPosition(background.getX()+15 + background.getWidth()/2 + 5 , background.getY() + background.getHeight() - 127);
        alliedResults.setVisible(true);

        alliedResults.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                        hide();
                    }
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

        group.addActor(alliedResults);
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
                    if (isAttackDisplayed && isDefenseDisplayed){
                        CombatDisplay.instance.end();
                        Gdx.app.log("combatdisplayresults","call attack");
                        attack.afterDisplay(this);
                        hide();
                    }
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
