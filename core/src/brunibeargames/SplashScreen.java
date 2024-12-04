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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;


public class SplashScreen {
    static public SplashScreen instance;
    Image splashImage;
    Texture bruinbearLogo;
    Logo logo;
    I18NBundle i18NBundle;

    TextureRegion texFrenchCalvary;
    TextureRegion texRussianCalvary;
    TextureRegion borodino;
    TextureRegion dot;
    TextureRegion loadbox;
    TextureRegion loading;
    TextureRegion title;
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
    Image imgTitle;
    Image imgBorodino;
    Image imgRussianCalvary;
    Image imgFrenchCalvary;
    Image imgDot;
    Image imgLoading;
    Image imgLoadBox;
    int xStartTank;

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
    int cntHorse = 0;
    int cntLoaded  = 0;
    int percentNewTank;
    ArrayList<Image> arrImageTanks = new ArrayList<>();



    public SplashScreen() {
        Gdx.app.log("SplashScreen", "COnstructor");

        instance = this;
        this.stage = Borodino.instance.guiStage;
        stage.clear();
        loadSplash();
//        Gdx.input.setInputProcessor(stage);

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
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;




    }

    private void loadSplash() {
        /**
         *  load and dislplay the splashscreen
         *  anytime screen is resized will load
         */
        //        Gdx.input.setInputProcessor(stage);
        Stage stage = Borodino.instance.splashStage;
        stage.clear();
        if (isInitialLoad) {
            assetSplashScreenManager = new AssetManager();
            assetSplashScreenManager.load("splash/splash.txt", TextureAtlas.class);
            assetSplashScreenManager.finishLoading();
            TextureAtlas textureAtlas = assetSplashScreenManager.get("splash/splash.txt");
            title =  textureAtlas.findRegion("title");
            texRussianCalvary =  textureAtlas.findRegion("abrams");
            dot =  textureAtlas.findRegion("dot");
            loading =  textureAtlas.findRegion("loading");
            loadbox =  textureAtlas.findRegion("loadbox");
            borodino =  textureAtlas.findRegion("splashscreen");
            cntHorse = title.getTexture().getWidth() / title.getTexture().getWidth();
            percentNewTank = 05;
            isInitialLoad = false;
            assetSplashScreenManager.load("sounds/levictoireestanous.mp3", Sound.class);
            assetSplashScreenManager.finishLoading();
            sound = assetSplashScreenManager.get("sounds/levictoireestanous.mp3");
            float level =  GamePreferences.getVolume();
            sound.play(level);
        }
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
        screenRatio = (float)Gdx.graphics.getWidth()/1920;
        heightDiff = height - Borodino.instance.getCalcHeight();


        imgTitle = new Image(title);
        imgBorodino = new Image(borodino);
        imgRussianCalvary = new Image(texRussianCalvary);
        imgDot = new Image(dot);
        imgLoading = new Image(loading);
        imgLoadBox = new Image(loadbox);
        float markWidth = imgTitle.getWidth();
        float sticksWidth = imgBorodino.getWidth();
        /**
         *  place top mark
         */
        int x = (int)((width -(markWidth * screenRatio))/2);
        int y = (int)(height - (((imgTitle.getHeight() + 40)  * screenRatio)));
        int saveY =y;
        imgTitle.setScale(screenRatio);
        imgTitle.setPosition(x,y);
        /**
         *  place loading
         */
        x  =   (int)((width -(imgLoading.getWidth() * screenRatio))/2);
        y -= 400  * screenRatio;
        imgLoading.setScale(screenRatio);
        imgLoading.setPosition(x,y);

        /**
         *  place loadBox
         */
        x  =   (int)((width -(imgLoadBox.getWidth() * screenRatio))/2);
        y -= 100;
        imgLoadBox.setScale(screenRatio);
        imgLoadBox.setPosition(x,y);
        xStartTank = x + 6;




        /**
         *  place Sticks and Stones
         */
        x = (int)((width -(sticksWidth * screenRatio))/2);
        y = (int)(height - ((((imgTitle.getHeight() + 40) + (imgBorodino.getHeight() + 40)) * screenRatio)));

//        y = (int) ((int)((imgMark.getY() + imgMark.getHeight()) + 40)  * screenRatio);
        imgBorodino.setScale(screenRatio);
        imgBorodino.setPosition(x,y);
        imgBorodino.addAction(Actions.fadeOut(.001f));




        stage.addActor(imgTitle);
        stage.addActor(imgLoadBox);
        stage.addActor(imgLoading);
        stage.addActor(imgBorodino);


        int i=0;

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
            //logo.update(progress);
            //            music.setVolume(music.getVolume() + 0.01f);
        }else {
 //           font.setColor(Color.WHITE);
 //           font.draw(stateEngine.getBatch(), GamePreferences.getBuildNumber(), 20, 20);
        }
        if (mapsManager.update() && effectsManager.update() && soundsManager.update() && unitsManager.update() && UIManager.update() && !beenHere) {
            map = mapsManager.get("map/borodinoprod.jpg", Texture.class);
//            Game game = new Game();
            Screen screenGame = new Screen(Borodino.instance.mapStage);
            CenterScreen centerScreen = new CenterScreen();
            Hex.loadHexes();
            Map map = new Map();
            SoundsLoader soundsLoader = new SoundsLoader();
            map.InitializeHexSearch();
            UILoader.instance.assignAssets(UIManager);
            screenHeight= Gdx.graphics.getHeight();
            screenWidth = Gdx.graphics.getWidth();
            gameMenu = new GameMenu(stage, sound,atlas,screenHeight, screenWidth);
 //           gameMenu.addObserver(this);
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
    }
    public void sounOff(){
        sound.stop();
    }
}


