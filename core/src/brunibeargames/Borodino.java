package brunibeargames;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Observable;


public class Borodino extends Observable implements ApplicationListener, GestureListener, InputProcessor
 {
	 SpriteBatch batch;
	 Texture img;
	 boolean isWriteTerrain = true;
	 public Stage guiStage;
	 public  Stage mapStage;
	 public  Stage hexStage;
	 //	public Stage mainmenuStage;
	 public FontFactory fontFactory;
	 public SplashScreen splashScreen;
	 //	TNLog tnlog;
	 com.badlogic.gdx.Game game;
	 boolean isMainMenu = false;
	 public boolean isNoInput = true;
	 private boolean isSaveGame = false;
	 InputMultiplexer im;
	 GestureDetector gd;
	 static public Borodino instance;
	 MusicGame music;
	 ShapeRenderer shapeRenderer;
	 SoundEffects soundEffects;
	 Map map;
	 public boolean isUpdateDice = false;
	 public boolean isUpdateExplosion = false;
	 public boolean isUpdateShell = false;
	 public boolean isBridgeExplosion = false;
	 public boolean isScroll = false;
	 private I18NBundle i18NBundle;
	 private boolean aiRender = false;
	 private int cntAiRender =0;
	 static boolean isResumed = false;

	public Stage mainmenuStage;
	public Screen screen;
	public Texture texHex;
	Skin skin;
	BitmapFont font;
	Enter enter;
	Settings settings;
//
	Loader loader;

	 public static void setIsStopPan(boolean b) {
	 }


	 @Override
	public void create () {
	    Gdx.app.log("Create", "Create");

		instance = this;
		font = new BitmapFont(Gdx.files.internal("scene2d/default.fnt"));
	    skin = new Skin(Gdx.files.internal("scene2d/uiskin.json"));
	    texHex = new Texture(Gdx.files.internal("hexhilite.png"));
		Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setFullscreenMode(mode);
//		Gdx.graphics.setWindowedMode((int)GamePreferences.getWindowSize().x, (int)GamePreferences.getWindowSize().y - 75);
		Gdx.graphics.setUndecorated(false);
		Gdx.graphics.setTitle("Borodino");
		Gdx.graphics.setWindowedMode(1920, 1080);
		//		Gdx.graphics.setResizable(false);
//			Gdx.graphics.setWindowedMode(1366, 600);



		settings = new Settings();
		shapeRenderer = new ShapeRenderer();
		soundEffects = new SoundEffects();
//		VisUI.load();
//		VisUI.setDefaultTitleAlign(Align.center);
//   	VisUI.load(SkinScale.X2);
		batch = new SpriteBatch();
		fontFactory = new FontFactory();
//		img = new Texture(Gdx.files.internal("data/RedMenaceSplash.png"));
//		hexStage = new Stage(new ScreenViewport());
		guiStage = new Stage(new ScreenViewport());
		mapStage = new Stage(new ScreenViewport());
		mainmenuStage = new Stage(new ScreenViewport());
//		mainMenu = new MainMenu(mainmenuStage);
		loader = new Loader();
//		screen = new Screen(mapStage);
		gd = new GestureDetector(this);
		Messages messages = new Messages();
//		SetMainMenu();
		Randomizer randomizer = new Randomizer();
		batch = new SpriteBatch();
		screen = new Screen(mapStage);
		Hex.loadHexes();
		map = new Map();
		Map.InitializeHexSearch();
		CreateInputProcessors();
		WinDebug winDebug = new WinDebug(mapStage,guiStage, skin);
	}

	private void CreateInputProcessors() {
		im = new InputMultiplexer(this);
		im.addProcessor(1, guiStage);
		im.addProcessor(2, mapStage);
		im.addProcessor(3, gd);
		Gdx.input.setInputProcessor(im);

		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screen.instance.Render(batch);
		mapStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
	    mapStage.draw(); // make sure done after sprite batch end
		guiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
	    guiStage.draw(); // make sure done after sprite batch end

	}
	
	@Override
	public void dispose () {
	    Gdx.app.log("Red Menance","Dispose");
		batch.dispose();
//		VisUI.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	 @Override
	 public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		 return false;
	 }

	 @Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	 @Override
	 public boolean scrolled(float amountX, float amountY) {
//		 Gdx.app.log("Mouse Event", "Scrolled amountx="+amountX+" Amounty="+amountY);

		 if (isMainMenu) {
			 return false;
		 }
		 if (amountY < 0) {
			 Screen.instance.ZoomBigger();
		 } else if (amountY > 0) {
			 Screen.instance.ZoomSmaller();
		 }
		 return false;
	 }

//	 @Override
	public boolean scrolled(int amount) {
		Gdx.app.log("Mouse Event", "Scrolled");

		if (isMainMenu) {
			return false;
		}
		if (amount < 0) {
			Screen.instance.ZoomBigger();
		} else if (amount > 0) {
			Screen.instance.ZoomSmaller();
		}
		return false;

	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		Gdx.app.log("Mouse Event", "Click at " + x+ " y-"+y); 
		Hex hex = Hex.GetHexFromScreenPosition(x, y); 
		Gdx.app.log("Mouse Event", "Hex clicked= x-" + hex.xTable+ " y-"+hex.yTable);
//		Image image = new Image(texHex);
//		Vector2 v2 =  hex.GetDisplayCoord();
//		image.setPosition(v2.x,v2.y);
//		mapStage.addActor(image);
//		WinDebug.instance.doShowStream(hex);
		WinDebug.instance.doRiver(hex);
		if (isWriteTerrain)
		{
			
			TerrainWriter(hex);
		}
		
		 
		return true;
	}
	boolean isFirstTime = true;
	FileHandle logger;
	StringBuffer logBuffer;
	private void TerrainWriter(Hex hex)
	{
		Gdx.app.log("TerrainWriter", "Hex =" + hex.xTable+ " y-"+hex.yTable); 

		if (isFirstTime)
		{
			logger = Gdx.files.local("stream.dat");
			logger.writeString("Path Data", false);
			isFirstTime = false;
		}

		logger.writeString("{"+Integer.toString(hex.xTable)+","+Integer.toString(hex.yTable)+"},"
				+ "", true); 
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Gdx.app.log("Borodino","Pan"); 

		screen.instance.PanCamera(deltaX, deltaY);
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		guiStage.getViewport().update(width, height, true);
		mapStage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	 public Texture getTexHex() {
		 return texHex;
	 }
 }
