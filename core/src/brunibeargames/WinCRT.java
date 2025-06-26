package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class WinCRT {
TextureAtlas textureAtlas = SplashScreen.instance.effectsManager.get("effects/combat.txt");
TextureRegion close =  textureAtlas.findRegion("close");
    TextureRegion backHilite = textureAtlas.findRegion("crtback");

    TextTooltip.TextTooltipStyle tooltipStyle;
    Window window;
    Window windowNew;
    Stage stage;
    int cntCountersToProcess =0;
    I18NBundle i18NBundle;
    Attack attack;

    private Skin skin;
    private Label[][] cellLabels; // rows: 6 (die rolls), cols: 9 (odds)

    // Combat Results Table values
    private final String[] headers = {"1-5", "1-4", "1-3", "1-2", "2-1", "3-1", "4-1", "5-1", "6-1"};
    private final String[][] results = {
            {"Ar", "Ar", "Dr", "Dr", "Dr", "Dr", "De", "De", "De"},
            {"Ar", "Ar", "Ar", "Dr", "Dr", "Dr", "Dr", "De", "De"},
            {"Ae", "Ar", "Ar", "Ar", "Dr", "Dr", "Dr", "Dr", "De"},
            {"Ae", "Ae", "Ar", "Ar", "Ar", "Dr", "Dr", "Dr", "Dr"},
            {"Ae", "Ae", "Ae", "Ar", "Ar", "Ar", "Dr", "Dr", "Dr"},
            {"Ae", "Ae", "Ae", "Ar", "Ar", "Ar", "Ar", "Ex", "Ex"},
    };
    private EventListener hitOK;
    static public WinCRT instance;
    public WinCRT(){
        instance= this;
    }
    public void show(Attack attack, String dieResult){
        skin = Borodino.instance.skin;
        stage = Borodino.instance.guiStage;

        Gdx.app.log("WinCRT", "Create");
        createnewWindow();
        if (window != null){
            window.remove();
            window = null;
        }
        this.attack = attack;
        stage= Borodino.instance.guiStage;
        i18NBundle = GameMenuLoader.instance.localization;
        tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = new Label.LabelStyle(Fonts.getFont24(), WHITE);
        NinePatch np = new NinePatch(GameMenuLoader.instance.gameMenu.asset.get("tooltip"), 2, 2, 2, 2);
        tooltipStyle.background = new NinePatchDrawable(np);

        np = new NinePatch(UILoader.instance.unitSelection.asset.get("window"), 10, 10, 33, 6);
        Window.WindowStyle windowStyle = new Window.WindowStyle(Fonts.getFont24(), WHITE, new NinePatchDrawable(np));
        String title = i18NBundle.format("crtwindow");
        window = new Window(title, windowStyle);
        Label lab = window.getTitleLabel();
        lab.setAlignment(Align.center);
        Image image = new Image(close);
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                end();
            }
        });
//        window.getTitleTable().add(image);
        hitOK = new TextTooltip(
                i18NBundle.format("crttool"),
                tooltipStyle);
        window.addListener(hitOK);
        int height = 0;
        Label.LabelStyle labelStyleg =new Label.LabelStyle(FontFactory.instance.largeFontWhite,Color.LIGHT_GRAY);

        Label.LabelStyle labelStyle =new Label.LabelStyle(FontFactory.instance.largeFontWhite,Color.YELLOW);
        Label.LabelStyle labelStyle2 =new Label.LabelStyle(Fonts.getFont24(),Color.WHITE);
        TextureRegionDrawable hilite = new TextureRegionDrawable(backHilite);
        Label.LabelStyle labelStyleHi =new Label.LabelStyle(FontFactory.instance.largeFontWhite,Color.YELLOW);
        Label.LabelStyle labelStyle2Hi =new Label.LabelStyle(Fonts.getFont24(),Color.WHITE);
        labelStyle2Hi.background = hilite;
        labelStyleHi.background = hilite;
        String[] strTab = attack.getAttackResults();
        /**
         *  draw heading
         */

        Label label = new Label("RESULT",labelStyleg);

        label.setAlignment(Align.left);
        //          label.setFontScale(.5F);
        window.add(label).align(Align.left);
        label = new Label("Dice ",labelStyleg);
        label.setAlignment(Align.right);
        window.add(label).align(Align.right).expandX();
        window.row();
        label = new Label("",labelStyleg);

        label.setAlignment(Align.left);
        //          label.setFontScale(.5F);
        window.add(label).align(Align.left);
        label = new Label("",labelStyleg);
        label.setAlignment(Align.right);
        window.add(label).align(Align.right).expandX();
        window.row();

        int[][] diceTable = attack.getDice();
        for (int i=0; i< strTab.length; i++){
            if (strTab[i].contentEquals(dieResult)){
                label = new Label(strTab[i],labelStyleHi);
            }else {
                label = new Label(strTab[i], labelStyle);
            }
            label.setAlignment(Align.left);
  //          label.setFontScale(.5F);
            window.add(label).align(Align.left);
            height += label.getHeight();
            String dice = Integer.toString(diceTable[i][0])+".."+Integer.toString(diceTable[i][1]);
            if (diceTable[i][0] == 0 || diceTable[i][1] == 0){
                dice = "";
            }
            if (strTab[i].contentEquals(dieResult)){
                label = new Label(dice,labelStyle2Hi);
            }else {
                label = new Label(dice,labelStyle2);
            }
            label.setAlignment(Align.right);
            window.add(label).align(Align.right).expandX();
            window.row();
        }
        height += window.getTitleLabel().getHeight() + 100;

        window.setModal(false);
        window.setTransform(true);
        window.setHeight(height);
        window.setWidth(200);
        window.setPosition((Gdx.graphics.getWidth() - (window.getWidth() +160)), (Gdx.graphics.getHeight() - window.getHeight() - 400 ));
        Vector2 v2 = GamePreferences.getWindowLocation("CRT");
        if (v2.x == 0 && v2.y == 0) {
            window.setPosition((Gdx.graphics.getWidth() - (window.getWidth() +120)), (Gdx.graphics.getHeight() - window.getHeight() - 400 ));
        }else{
            window.setPosition(v2.x, v2.y);

        }
        stage.addActor(window);

    }


    public void end(){
        if (window != null) {
            int lastX = (int) window.getX();
            int lastY = (int) window.getY();
            GamePreferences.setWindowLocation("CRT", lastX, lastY);
            if (window != null){window.remove();}
        }
    }
    private void createnewWindow() {
        Window window = new Window("Combat Results Table", skin);
        window.setMovable(false);
        window.setResizable(false);
        window.pad(10);
        window.defaults().pad(2);

        Table outerTable = new Table(skin);
        outerTable.defaults().pad(2);

        // Title
        Label title = new Label("Combat Ratios", skin);
        title.setAlignment(Align.center);
        outerTable.add(title).colspan(10).center().padBottom(10);
        outerTable.row();

        // Header Row
        outerTable.add("Die Roll");
        for (String header : headers) {
            outerTable.add(header);
        }
        outerTable.row();

        // Results Rows
        cellLabels = new Label[6][9];

        for (int row = 0; row < 6; row++) {
            outerTable.add("" + (row + 1)).pad(2); // Die roll label
            for (int col = 0; col < 9; col++) {
                Label cell = new Label(results[row][col], skin);
                cellLabels[row][col] = cell;
                outerTable.add(cell).pad(2); // Correctly applies padding to the cell
            }
            outerTable.row();
        }



        // Explanations
        Label explanation = new Label(
                "Attacks executed at greater than 6-1 are treated as 6-1.\n" +
                        "Attacks executed at less than 1-5 are treated as 1-5.\n" +
                        "Attacks by artillery alone against non-adjacent units must be made at odds of 1-3 or greater.\n",
                skin);
        explanation.setWrap(true);
        outerTable.add(explanation).colspan(10).width(600).padTop(10);
        outerTable.row();

        Label legend = new Label(
                "Explanation Of Results:\n" +
                        "Ae = Attacker eliminated.\n" +
                        "Ar = Attacker retreats one hex.\n" +
                        "Ne = No effect (Ae and Ar barrage attack results are Ne).\n" +
                        "Dr = Defender retreat.\n" +
                        "De = Defender eliminated.",
                skin);
        legend.setWrap(true);
        outerTable.add(legend).colspan(10).width(600).padTop(10);
        outerTable.row();

        // Close Button
        TextButton close = new TextButton("Close", skin);
        close.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove(); // Just hides the CRT window
            }
        });
        outerTable.add(close).colspan(10).center().padTop(15);
        outerTable.row();

        // Listener on the whole window
        window.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Window clicked at: x=" + x + ", y=" + y);
            }
        });

        window.add(outerTable);
        window.pack();
        window.setPosition(
                (Gdx.graphics.getWidth() - window.getWidth()) / 2,
                (Gdx.graphics.getHeight() - window.getHeight()) / 2
        );

        stage.addActor(window);

        // Example Highlight
        highlight(3, 5); // 4th row, 6th odds column => (die roll = 4), (odds = 3-1)
    }

    public void highlight(int rowIndex, int colIndex) {
        for (int r = 0; r < cellLabels.length; r++) {
            cellLabels[r][colIndex].setColor(Color.YELLOW); // highlight odds column
        }
        cellLabels[rowIndex][colIndex].setColor(Color.RED); // highlight result cell
    }




}
