package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import brunibeargames.UI.BottomMenu;
import brunibeargames.UI.WinCommandRandom;
import brunibeargames.UI.WinWarning;
import brunibeargames.Unit.Officer;
import brunibeargames.Unit.Unit;

public class DoCommandRandom implements Observer {
    /**
     * Random activations of commanders for a side
     */
    static public DoCommandRandom instance;
        ArrayList<Officer> arrAllOfficersPossible = new ArrayList<Officer>();
    I18NBundle i18NBundle;
    private WinWarning winWarning;
    int dieAboveRussian = 4;
    int dieAboveAllies = 3;
    int numDieRussian = 3;
    int numDieAllies = 3;
    boolean isAllies = true;

    DoCommandRandom(){
        instance = this;
        i18NBundle = GameMenuLoader.instance.localization;
        dieAboveRussian = GameSetup.instance.getDicerollsAboveRussian();
        dieAboveAllies = GameSetup.instance.getDicerollsAboveFrench();
        numDieRussian = GameSetup.instance.getRandomCommandersRussian();
        numDieAllies = GameSetup.instance.getRandomCommandersFrench();
   }
    public void start(boolean isAlliesIn){
        Gdx.app.log("DoCommandRandom", "start");

        this.isAllies = isAlliesIn;

        arrAllOfficersPossible.clear();
        for (Unit unit : Game.instance.arrOnUnitBoard) {
            if (unit.isAllies == isAllies && unit.isOfficer &&
                    !unit.officer.getisActivated())
            {
               arrAllOfficersPossible.add(unit.officer);
            }
        }
        if (arrAllOfficersPossible.isEmpty()) {

            winWarning = new WinWarning(i18NBundle.get("warnrandomofficers"), i18NBundle.get("commandphasenoommander"));
            winWarning.addObserver(this);
            return;
        }
        BottomMenu.instance.setWarningPhaseChange(true);
        String message= i18NBundle.get("choosenocommand");
        String title = i18NBundle.get("nextphasebutton");
        BottomMenu.instance.setPhaseData(title, message);
        message=i18NBundle.get("commandrandomphasehelp");
        title=i18NBundle.get("commandrandomphasehelptitle");
        BottomMenu.instance.setHelpData(title, message);
        float y = (Gdx.graphics.getHeight() - 500f) / 2f;
        float x = (Gdx.graphics.getWidth() - 400f) / 2f;
        Vector2 pos = new Vector2(x, y);
        WinCommandRandom winCommandRandom = new WinCommandRandom(arrAllOfficersPossible,400,500,pos);

        Gdx.app.log("DoCommandRandom", "end -start");


    }

    @Override
    public void update(Observable observable, Object o) {
        ObserverPackage oB = (ObserverPackage) o;
        Gdx.app.log("DoCommandRandom", "update type=" + oB.type.toString());

        /**
         *  Hex touched
         */
        if (oB.type == ObserverPackage.Type.GoBack) {
            BottomMenu.instance.deleteObserver(this);
            goBack();
        }else{
            if (oB.type == ObserverPackage.Type.OK){
                winWarning.deleteObserver(this);
                end();
            }
        }
        return;
    }

    private void goBack() {
        start(isAllies);
    }
    private void end() {
        Gdx.app.log("DoCommandRandom", "end");

        BottomMenu.instance.deleteObserver(this);
        NextPhase.instance.endPhase();
    }

}
