package brunibeargames;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import brunibeargames.Unit.Unit;


public class Borodino extends Observable implements ApplicationListener, GestureListener, InputProcessor
 {
	 SpriteBatch batch;
	 Texture img;
	 boolean isWriteTerrain = false;
	 boolean isPlaceUnit = true;
	 public Stage splashStage;

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
	 GamePreferences gamePreferences;


	 public Stage mainmenuStage;
	public Screen screen;
	public Texture texHex;
	public Skin skin;
	BitmapFont font;
	Enter enter;
	Settings settings;
//
	Loader loader;
	 private boolean isDesktop = false;
	 private boolean isAndroid = false;
	 private boolean isIOS = false;
	 private boolean inSplash = true;
	 private float calcHeight;
	 private Dimension scrnSize;

	 public static void setIsStopPan(boolean b) {
	 }

	public Borodino(){
		 super();
	}
	 @Override
	public void create () {
	    Gdx.app.log("Create", "Create");
		instance = this;
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		//skin = new Skin(Gdx.files.internal("uiskin.json")); // Make sure you have a basic skin
		 skin = new Skin(Gdx.files.internal("uiskin.json"));
		 if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
			 isDesktop = true;

		 }
		 if(Gdx.app.getType() == Application.ApplicationType.Android) {
			 isAndroid = true;
		 }
		 if(Gdx.app.getType() == Application.ApplicationType.iOS) {
			 isIOS = true;
		 }
		 setScreenSize();
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 Date date = new Date();

		 Gdx.app.log("SS", "Date Time="+dateFormat.format(date));
//		music = new MusicGame();
		 if (!GamePreferences.isInPackage) {
			 if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
//				DoRedirectConsole();
			 }
//			analytics.registerUncaughtExceptionHandler();
		 }
		 splashStage = new Stage(new ScreenViewport());
		 guiStage = new Stage(new ScreenViewport());
		 mapStage = new Stage(new ScreenViewport());
		 hexStage = new Stage(new ScreenViewport());
		 gd = new GestureDetector(this);
		 gamePreferences = new GamePreferences();
		 splashScreen = new SplashScreen();
		 batch = new SpriteBatch();
		 MyColor myColor = new MyColor();
		 Hex.loadHexes();
		 FontFactory fontFactory = new FontFactory();
		 CreateInputProcessors();

	 }
	 private void setScreenSize() {
		 Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
		 Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes(currMonitor);
		 Graphics.DisplayMode currMode = Gdx.graphics.getDisplayMode(currMonitor);


		 float height = currMode.height;
//		height = 1080; // test
		 float width = currMode.width;
		 calcHeight = 0;
		 float calcWidth = 0;
		 boolean isWindow = true;
		 Vector2 v2ScreenSize = GamePreferences.getWindowSize();
		 isWindow = GamePreferences.getFullScreen();
		 /**
		  *  if none saved then first time
		  *  check if we can do 1920 X 1080 windowed mode
		  */
//		v2ScreenSize.x =0; // test
		 if (v2ScreenSize.x == 0) {
			 isWindow = true;
			 if (height > 1080) {
				 calcHeight = 1080;
				 calcWidth = 1920;
			 } else if (height > 900) {
				 calcHeight = 900;
				 calcWidth = 1600;
			 } else {
				 calcHeight = 768;
				 calcWidth = 1366;
			 }
			 GamePreferences.putWindowSize(new Vector2(calcWidth, calcHeight));
		 } else {
			 calcWidth = v2ScreenSize.x;
			 calcHeight = v2ScreenSize.y;
		 }
		 Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
//			taskBarHeight = scrnSize.height - winSize.height;
		 Gdx.graphics.setFullscreenMode(mode);
		 Gdx.graphics.setUndecorated(false);
		 Gdx.graphics.setTitle("Borodino");
		 Gdx.graphics.setResizable(false);
//		Gdx.graphics.setWindowedMode((int)calcWidth, (int)calcHeight);
//		Gdx.graphics.setWindowedMode(1366,768);
		 Gdx.graphics.setWindowedMode(1920, 1080);
//		Gdx.graphics.setWindowedMode(2560,1440);
		 Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
		 Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

	 }

		 private void CreateInputProcessors() {
		im = new InputMultiplexer(this);
	    im.addProcessor(1, splashStage);
		im.addProcessor(2, guiStage);
		im.addProcessor(3, mapStage);
  		im.addProcessor(4, hexStage);
		im.addProcessor(5, gd);
		Gdx.input.setInputProcessor(im);

		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (inSplash){
			splashStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
			splashStage.draw(); // make sure done after sprite batch end
		}else {
			if (Screen.instance != null) {
				Screen.instance.render(batch);
			}

			hexStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
			hexStage.draw(); // make sure done after sprite batch end
			mapStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
			//findNullTextures((SnapshotArray<Actor>) mapStage.getActors());

			mapStage.draw(); // make sure done after sprite batch end
			guiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
			guiStage.draw(); // make sure done after sprite batch end
		}

		checkKeyPress();
		if (Screen.instance != null) {
			if (isUpdateExplosion || isScroll || isUpdateDice || isBridgeExplosion || isUpdateShell) {
				batch.setProjectionMatrix(Screen.instance.cameraBackGround.combined);
				batch.begin();
				if (isUpdateExplosion) {
//					Explosions.instance.update(batch);
				}
				if (isUpdateDice) { // changes viewpoint
					DiceEffect.instance.update(batch);
				}
				if (isBridgeExplosion) {
//					BridgeExplosion.instance.update(batch);
				}
				if (isScroll) {
//					CenterScreen.instance.update();
				}
				if (isUpdateShell){
//					FlyingShell.instance.update();
				}

				batch.end();
			}
		}

		SplashScreen.instance.checkLoad(batch);


	}
	 private void findNullTextures(SnapshotArray<Actor> actors) {
		 for (Actor actor : actors) {
			 // Check if the actor itself is an Image and its drawable/texture is null
			 if (actor instanceof Image) {
				 Image image = (Image) actor;
				 Drawable drawable = image.getDrawable();
				 if (drawable instanceof TextureRegionDrawable) {
					 TextureRegionDrawable trd = (TextureRegionDrawable) drawable;
					 TextureRegion region = trd.getRegion();
					 if (region == null) {
						 Gdx.app.error("NullTextureDebug", "Actor '" + actor.getName() + "' (Image) has a null TextureRegion in its Drawable!");
					 } else if (region.getTexture() == null) {
						 Gdx.app.error("NullTextureDebug", "Actor '" + actor.getName() + "' (Image) has a TextureRegion whose Texture is NULL!");
						 // You can set a breakpoint here to inspect `actor` and `region`
					 }
				 } else if (drawable == null) {
					 Gdx.app.error("NullTextureDebug", "Actor '" + actor.getName() + "' (Image) has a NULL Drawable!");
				 }
			 }
			 // You can extend this to check other actors that might have drawables, e.g., Buttons
			 else if (actor instanceof Button) {
				 Button button = (Button) actor;
				 Drawable upDrawable = button.getStyle().up;
				 checkDrawableForNullTexture(button.getName() + " (Button Up)", upDrawable);
				 Drawable downDrawable = button.getStyle().down;
				 checkDrawableForNullTexture(button.getName() + " (Button Down)", downDrawable);
				 // Check other states like checked, disabled etc. if relevant
			 }
			 // Labels also might have background drawables or font regions from their style
			 else if (actor instanceof Label) {
				 Label label = (Label) actor;
				 Drawable backgroundDrawable = label.getStyle().background;
				 checkDrawableForNullTexture(label.getName() + " (Label Background)", backgroundDrawable);
				 // Checking font textures is more complex but could be done if issue is with font rendering
			 }


			 // If the actor is a Group, recursively check its children
			 if (actor instanceof Group) {
				 findNullTextures(((Group) actor).getChildren());
			 }
		 }
	 }

	 /**
	  * Helper method to check a Drawable for a null Texture.
	  */
	 private void checkDrawableForNullTexture(String context, Drawable drawable) {
		 if (drawable instanceof TextureRegionDrawable) {
			 TextureRegionDrawable trd = (TextureRegionDrawable) drawable;
			 TextureRegion region = trd.getRegion();
			 if (region == null) {
				 Gdx.app.error("NullTextureDebug", "Drawable for '" + context + "' has a null TextureRegion!");
			 } else if (region.getTexture() == null) {
				 Gdx.app.error("NullTextureDebug", "Drawable for '" + context + "' has a TextureRegion whose Texture is NULL!");
				 // You can set a breakpoint here
			 }
		 } else if (drawable == null) {
			 Gdx.app.log("NullTextureDebug", "Drawable for '" + context + "' is NULL. This might be intentional.");
		 }
	 }

	 private void checkKeyPress() {
		 if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			 if (ScreenGame.instance != null) {
				 ScreenGame.instance.panCamera(+4, 0);
			 }
		 }
		 if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			 if (ScreenGame.instance != null) {
				 ScreenGame.instance.panCamera(-4, 0);
			 }
		 }
		 if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			 if (ScreenGame.instance != null) {
				 ScreenGame.instance.panCamera(+4, 0);
			 }
		 }
		 if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			 if (ScreenGame.instance != null) {
				 ScreenGame.instance.panCamera(-4, 0);
			 }
		 }
		 if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			 if (ScreenGame.instance != null) {
				 ScreenGame.instance.panCamera(0, +4);
			 }
		 }
		 if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			 if (ScreenGame.instance != null) {
				 ScreenGame.instance.panCamera(0, -4);
			 }
		 }
		// if (Gdx.input.isKeyPressed(Input.Keys.C)) {
		//	 MyColor.instance.show();
		// }


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
		Hex hex = null;
		if (Hex.hexTable != null && Screen.instance !=null) {
			hex = Hex.GetHexFromScreenPosition(screenX, screenY);
			Gdx.app.log("Mouse Event", "Hex clicked=" + hex.xTable+ " y-"+hex.yTable);
			if (hex != null) {
				fireHex(hex,button,screenX,screenY);
			}
		}
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
		 if (screen.instance != null) {
			 if (amountY < 0) {
				 Screen.instance.ZoomBigger();
			 } else if (amountY > 0) {
				 Screen.instance.ZoomSmaller();
			 }
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
		Hex hex = null;
		if (Screen.instance != null) {
			hex = Hex.GetHexFromScreenPosition(x, y);
		}else{
			return true;
		}
		Gdx.app.log("Mouse Event", "Hex clicked= x-" + hex.xTable+ " y-"+hex.yTable);
//		Image image = new Image(texHex);
//		Vector2 v2 =  hex.GetDisplayCoord();
//		image.setPosition(v2.x,v2.y);
//		mapStage.addActor(image);
//		WinDebug.instance.doShowStream(hex);
//		WinDebug.instance.doRiver(hex);
/*		if (isWriteTerrain)
		{
			
			TerrainWriter(hex);
		}
		if (isPlaceUnit){
			if (unitPlace != null){
				if (isFirstTime)
				{
					logger = Gdx.files.local("stream.dat");
					logger.writeString("Path Data", false);
					isFirstTime = false;
				}
				logger.writeString("{"+Integer.toString(unitPlace.ID)+","+Integer.toString(hex.xTable)+","+Integer.toString(hex.yTable)+"}",true);
				Gdx.app.log("PLACE{"+Integer.toString(unitPlace.ID)+","+Integer.toString(hex.xTable)+","+Integer.toString(hex.yTable)+"}","");
				unitPlace.getHexOccupy().leaveHex(unitPlace);
				unitPlace.getMapCounter().place(hex);

				unitPlace = null;
			} */
			return false;
	}
	 public void fireHex(Hex hex, int button, int screenX, int screenY) {
		 /**
		  * this needs to be reworked as dragging happens all the time
		  * and so we need to hit The MOA icon 2 times
		  if (isHandleHexFire && isDragged){
		  isHandleHexFire = false;
		  isDragged = false;
		  return;
		  } */
		 if (hex != null) {

			 Gdx.app.log("ardenne", "Hex Fired hex=" + hex);
		 }else{
			 Gdx.app.log("ardenne", "Hex Fired Null");

		 }

		 if (hex != null) {
			 if (button == Input.Buttons.LEFT) {
				 setChanged();
				 if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
					 notifyObservers(new ObserverPackage(ObserverPackage.Type.TouchUpShift, hex, screenX, screenY));
				 } else {
					 notifyObservers(new ObserverPackage(ObserverPackage.Type.TouchUp, hex, screenX, screenY));
				 }
			 }
			 if (button == Input.Buttons.MIDDLE) {
				 setChanged();
				 notifyObservers(new ObserverPackage(ObserverPackage.Type.TouchUpMiddle, hex, screenX, screenY));
			 }
			 setChanged();
			 //return true;
		 }
	 }
	public Unit unitPlace= null;
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
		Gdx.graphics.setTitle("Borodino Phase");
		if (screen.instance != null) {
			screen.instance.PanCamera(deltaX, deltaY);
		}
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

	 public float getCalcHeight() {
		 return calcHeight;
	 }

	 public void setInSplash(boolean inSplash) {
		 this.inSplash = inSplash;
	 }
	boolean isInHover= false;
	 public void setInHover(boolean set) {
		 isInHover = set;
	 }
	 public boolean isInHover() {
		 return isInHover;
	 }
 }
