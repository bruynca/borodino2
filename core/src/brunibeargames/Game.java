package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

import brunibeargames.UI.BottomMenu;
import brunibeargames.Unit.CounterStack;
import brunibeargames.Unit.Unit;


public class Game {
    NextPhase nextPhase;
    FontFactory fontFactory;
    private I18NBundle i18NBundle;


    static public Game instance;
    Game(String str, boolean isResume) {
        Gdx.app.log("Game", "COnstructor");

        instance = this;
        i18NBundle = GameMenuLoader.instance.localization;
        fontFactory = new FontFactory();
        Borodino.instance.hexStage.clear();
        Borodino.instance.guiStage.clear();
        Borodino.instance.mapStage.clear();
        Unit.loadTexture();
        CounterStack.loadTexture();
        HexHiliteDisplay.textTureLoad();


        //       Counter.clearStack();
        Hex.initHex();
  //      Hex.LoadBridges();
        Unit.resetID();
        WinDebug winDebug = new WinDebug();
        nextPhase = new NextPhase();
        System.gc();
        WinModal winModal = new WinModal();
        if (str.length() == 0) {
            ArrayList<Unit> arrAllies =Unit.getSetupUnits(true);
            Unit.resetID();
            Unit.loadUnits(arrAllies);
            ArrayList<Unit> arrAxis =Unit.getSetupUnits(false);
            Unit.loadUnits(arrAxis);
            TurnCounter turnCounter = new TurnCounter();
//            nextPhase.nextPhase();
            //TopMenu topMenu = new TopMenu();
            /**
             *
             */
            if (GameSetup.instance.isHotSeatGame()){
                TurnCounter.instance.updateText(i18NBundle.format("selectamerican"));
 //               WinCardsChoice winCardsChoice = new WinCardsChoice(true);
            }else if (GameSetup.instance.isRussianVersusAI()){
                //TurnCounter.instance.updateText(i18NBundle.format("selectgerman"));
 //               WinCardsChoice winCardsChoice = new WinCardsChoice(false);
            }else{
                TurnCounter.instance.updateText(i18NBundle.format("selectamerican"));
  //              WinCardsChoice winCardsChoice = new WinCardsChoice(false);
            }
        }else{
            LoadGame loadGame = new LoadGame(str, isResume);
            BottomMenu.instance.showBottomMenu();
            TopMenu topMenu = new TopMenu();
        }
    }

    public Scenario getScenario() {
        return null;
    }


    public enum Scenario {Short}
}
