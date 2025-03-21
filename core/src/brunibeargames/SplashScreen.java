package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.VisUI;

import java.util.ArrayList;

import brunibeargames.Unit.Unit;


public class SplashScreen {
    static public SplashScreen instance;
    Image splashImage;
    Texture bruinbearLogo;
    Logo logo;
    I18NBundle i18NBundle;

    private  AssetManager assetManager;
    public AssetManager soundsManager;
    public AssetManager effectsManager;
    private AssetManager mapsManager;
    public  AssetManager unitsManager;
    public  AssetManager cardManager;
    private AssetManager UIManager;
    private SoundsLoader soundAssets;
    private TextureAtlas atlas;
    private TextureAtlas logoAtlas;
    private Texture map;
    Image imgBorodino;
    Image imgCannon;
    Image imgLoading;
    Image imgLoadBox;
    int xStartCannon;
    int yStartCannon= 0;

    private TextureRegion menuimage = null;
    boolean isInitialLoad = true;

    private boolean beenHere;
    public final BitmapFont font = new BitmapFont();
    private  Stage stage;
    private float screenHeight;
    private float screenWidth;
    private String status;
    private boolean oldBuild;
    private LanguageSupport languageSupport;
    private GameMenu gameMenu;
    private Array<Actor> arrActorsMenu;
    Sound sound;
    private AssetManager assetSplashScreenManager;
    float screenRatio;
    float heightDiff;
    int numTanksToMark;
    int cntCannon = 0;
    int cntLoaded  = 0;
    int percentNewCannon = 5;
    ArrayList<Image> arrImageCannon = new ArrayList<>();



    public SplashScreen() {
        Gdx.app.log("SplashScreen", "COnstructor");

        instance = this;
        this.stage = Borodino.instance.splashStage;
        stage.clear();
        loadSplash();
        loadAssets();

//        Gdx.input.setInputProcessor(stage);




    }

    TextureRegion cannon;
    TextureRegion tank;

    TextureRegion borodino;
    TextureRegion dot;
    TextureRegion loadbox;
    TextureRegion loading;
    ArrayList<Image> arrImage = new ArrayList<>();


    private void loadSplash() {
        /**
         *  load and dislplay the splashscreen
         *  anytime screen is resized will load
         */
        //        Gdx.input.setInputProcessor(stage);
        float x=0;
        float y=0;
        Stage stage = Borodino.instance.splashStage;
        stage.clear();
        if (isInitialLoad) {
            assetSplashScreenManager = new AssetManager();
            assetSplashScreenManager.load("splash/splash.txt", TextureAtlas.class);
            assetSplashScreenManager.finishLoading();

            TextureAtlas textureAtlas = assetSplashScreenManager.get("splash/splash.txt");
            borodino = textureAtlas.findRegion("splashimage");
            dot =  textureAtlas.findRegion("dot");
            loading =  textureAtlas.findRegion("loading");
            loadbox =  textureAtlas.findRegion("loadbox");
            cannon =  textureAtlas.findRegion("cannon");
            percentNewCannon = 05;
            isInitialLoad = false;
            assetSplashScreenManager.load("sounds/levictoireestanous.mp3", Sound.class);
            assetSplashScreenManager.finishLoading();
            sound = assetSplashScreenManager.get("sounds/levictoireestanous.mp3");

            float level =  GamePreferences.getVolume();
            sound.play(level);

            isInitialLoad = false;
        }
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
        screenRatio = (float)Gdx.graphics.getWidth()/1920;
        heightDiff = height - Borodino.instance.getCalcHeight();
        imgBorodino = new Image(borodino);
        imgLoading = new Image(loading);
        imgLoadBox = new Image(loadbox);
        imgCannon = new Image(cannon);
        float sticksWidth = imgBorodino.getWidth();
        imgBorodino.setScale(screenRatio);
        imgBorodino.setPosition(0,0);
        /**
         *  place loading
         */
        x  =   (int)((width -(imgLoading.getWidth() * screenRatio))/2);
        y = 193  * screenRatio;
        imgLoading.setScale(screenRatio);
        imgLoading.setPosition(x,y);

        /**
         *  place loadBox
         */
        x  =   (int)((width -(imgLoadBox.getWidth() * screenRatio))/2);
        y = 85*screenRatio;
        imgLoadBox.setScale(screenRatio);
        imgLoadBox.setPosition(x,y);



        xStartCannon = (int)x+5;
        yStartCannon = (int)y+5;




        stage.addActor(imgBorodino);
        stage.addActor(imgLoadBox);
        stage.addActor(imgLoading);
 /*       x +=5;
        y +=5;
        for (float x1 = 20;x1< 1920f; x1++){
            setCannon(x1,100);
        };
        setCannon(0, 0);
        setCannon(x, y);
        setCannon(x+80, y);

*/

        int i=0;

    }

    private void setCannon(float x, float y) {

        Image image = new Image(cannon);
        image.setScale(screenRatio);
        image.setPosition(x,y);
        stage.addActor(image);
        arrImage.add(image);
    }

    boolean isLoaded = false;
    public void checkLoad(Batch batch) {
         batch.begin();
        float progress = (mapsManager.getProgress() + effectsManager.getProgress() + unitsManager.getProgress() + UIManager.getProgress() + soundsManager.getProgress()+cardManager.getProgress())/5;
        if (!beenHere) {
 /*           Gdx.app.log("SplashScreen", "Progress ="+progress);
            Gdx.app.log("SplashScreen", "mapsManage ="+mapsManager.getProgress());
            Gdx.app.log("SplashScreen", "effectsMan ="+effectsManager.getProgress());
            Gdx.app.log("SplashScreen", "unitMKan   ="+unitsManager.getProgress());
            Gdx.app.log("SplashScreen", "UIManage   ="+UIManager.getProgress());
            Gdx.app.log("SplashScreen", "Sounds     ="+soundsManager.getProgress());
*/
        font.setColor(Color.WHITE);

        font.draw(batch, "Version:" + GamePreferences.getBuildNumber() + " Loading at " + Math.round((progress * 100)-18) + "%", 10,50 );
        int profDisplay =  Math.round((progress * 100)-18);
        int percentNewCannon = 05;  // every 5 percent need new cannon
        if (profDisplay > (cntLoaded+1)*percentNewCannon){
//            cntLoaded = (int) (profDisplay + percentNewCannon - 1);
            cntLoaded++;
            Gdx.app.log("SplashScreen", "Displaying next Progress="+profDisplay+ " num cannons="+cntLoaded);
            Image imgCannonD = new Image(cannon);
            imgCannonD.setScale(screenRatio);
            imgCannonD.setPosition(xStartCannon, yStartCannon);
      //      imgCannonD.setPosition(0,0);
            stage.addActor(imgCannonD);
            arrImageCannon.add(imgCannonD);
            xStartCannon += (imgCannonD.getWidth() * screenRatio);
        }
        font.setColor(Color.WHITE);
        font.draw(batch, "Version:" + GamePreferences.getBuildNumber() + " Loading at " + Math.round((progress * 100)-18) + "%", 10,50 );
        //logo.update(progress);
        //            music.setVolume(music.getVolume() + 0.01f);
    }

        if (mapsManager.update() && effectsManager.update() && soundsManager.update() && unitsManager.update() && UIManager.update() && !beenHere) {
            map = mapsManager.get("map/borodinoprod.jpg", Texture.class);
            /**
             *  fadeout and fade in
             */

            for (Image img:arrImageCannon){
                img.addAction(Actions.fadeOut(.4f));
            }
            //          VisUI.load();            //	VisUI.load(VisUI.SkinScale.X2);
            //VisUI.load();
            VisUI.load(VisUI.SkinScale.X2);
            VisUI.setDefaultTitleAlign(Align.center);



            imgLoadBox.addAction(Actions.fadeOut(.4f));
            imgLoading.addAction(Actions.fadeOut(.4f));


//            Game game = new Game();
            Screen screen = new Screen(Borodino.instance.mapStage);
            CenterScreen centerScreen = new CenterScreen();
            Hex.loadHexes();
            Map map = new Map();
            SoundsLoader soundsLoader = new SoundsLoader();
            map.InitializeHexSearch();
            UILoader.instance.assignAssets(UIManager);
            Unit.loadAllUnits();

            Scenarios.loadData();

            screenHeight= Gdx.graphics.getHeight();
            screenWidth = Gdx.graphics.getWidth();
 //           MainMenu mainMenu = new MainMenu(stage, sound,atlas,screenHeight, screenWidth);
            gameMenu = new GameMenu(stage, sound,atlas,screenHeight, screenWidth);
//            gameMenu.addObserver(this);
            languageSupport = new LanguageSupport();
 //           languageSupport.addObserver(this);
            beenHere = true;
/**            if (ardenne.isResumed){
                Gdx.app.log("SplashScreen", "inIsResume");

                String strResume = "Does Not Matter";
                i18NBundle = GameMenuLoader.instance.localization;
                if (SaveGame.getResume() != null)  {
                    end();
                    Game game = new Game(strResume, true);
                    EventPopUp.instance.show(i18NBundle.format("resumegame"));
                }
            } */
        }
        batch.end();
    }
    /**
     *  load all assets associated with the the game  see checkLoad for completion check
     */
    private void loadAssets() {
        assetManager = new AssetManager();

        // Load splash screen and music
        assetManager.load("logo/logo.txt", TextureAtlas.class);
        assetManager.finishLoading();
        logoAtlas = assetManager.get("logo/logo.txt");
        assetManager.finishLoading();

        AssetManager gameMenuManager = new AssetManager();
        gameMenuManager.load("menus/gamemenu.txt", TextureAtlas.class);
        gameMenuManager.load("menus/flags.txt", TextureAtlas.class);
        gameMenuManager.load("menus/victory.txt", TextureAtlas.class);
        gameMenuManager.load("fonts/chinese24.fnt", BitmapFont.class);
        gameMenuManager.load("fonts/chinese24A.fnt", BitmapFont.class);
        String language = "i18n/"+GamePreferences.getLanguage();
        gameMenuManager.load(language, I18NBundle.class);
//        gameMenuManager.load("console/uiskin.atlas", TextureAtlas.class);
        //       gameMenuManager.load("menus/gameselectionbuttons.txt", TextureAtlas.class);

        gameMenuManager.finishLoading();
        GameMenuLoader.instance.assignAssets(gameMenuManager);
        atlas = gameMenuManager.get("menus/gamemenu.txt");
//        language = "i18n/"+GamePreferences.getLanguage();
        GameMenuLoader.instance.localization = gameMenuManager.get(language, I18NBundle.class);

        soundsManager = new AssetManager();
        effectsManager = new AssetManager();
        mapsManager = new AssetManager();
        unitsManager = new AssetManager();
        UIManager = new AssetManager();
        cardManager = new AssetManager();
        mapsManager.load("map/borodinoprod.jpg", Texture.class);
        effectsManager.load("effects/attackarrows.txt", TextureAtlas.class);
        effectsManager.load("effects/dicefronts.txt", TextureAtlas.class);
        effectsManager.load("effects/dicefrontsblue.txt", TextureAtlas.class);
//        effectsManager.load("effects/combatdisplay.txt", TextureAtlas.class);
        effectsManager.load("effects/attackarrows.txt", TextureAtlas.class);
//        effectsManager.load("effects/dicerolling.txt", TextureAtlas.class);
//        effectsManager.load("effects/explosion.txt", TextureAtlas.class);
        UIManager.load("menus/bottommenu.txt", TextureAtlas.class);
        UIManager.load("menus/buttons.txt", TextureAtlas.class);
        UIManager.load("menus/topmenu.txt", TextureAtlas.class);
        UIManager.load("menus/unitselection.txt", TextureAtlas.class);
        UIManager.load("menus/gamemenu.txt", TextureAtlas.class);
        UIManager.load("menus/unitselection.txt", TextureAtlas.class);
        UIManager.load("menus/victory.txt", TextureAtlas.class);
        soundsManager.load("sounds/die_roll.mp3", Sound.class);
        soundsManager.load("sounds/backgroundbattle.mp3", Sound.class);
        soundsManager.load("sounds/artillery.mp3", Sound.class);
        soundsManager.load("sounds/trucks.mp3", Sound.class);
        soundsManager.load("sounds/combat.mp3", Sound.class);
        soundsManager.load("sounds/movement.mp3", Sound.class);
        soundsManager.load("sounds/bridgeblow.mp3", Sound.class);
        soundsManager.load("sounds/limber.mp3", Sound.class);
        soundsManager.load("sounds/march.mp3", Sound.class);
        soundsManager.load("sounds/stuka.mp3", Sound.class);
        soundsManager.load("sounds/mortar.mp3", Sound.class);
        soundsManager.load("sounds/tada.mp3", Sound.class);
        unitsManager.load("counter/counter.txt", TextureAtlas.class);
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;


    }

    public Texture getMap(){
        return map;

    }
    public void reStart(){
        stage.addActor(splashImage);
        gameMenu = new GameMenu(stage, sound,atlas,screenHeight, screenWidth);
        System.gc();
    }
    public void end() {
        arrActorsMenu = stage.getActors();
        stage.clear();
        sound.stop();
        Borodino.instance.setInSplash(false);
    }
    public void sounOff(){
        sound.stop();
    }

    public float getScreenRatio() {
        return screenRatio;
    }
}


