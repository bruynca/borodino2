package brunibeargames;

import com.badlogic.gdx.utils.I18NBundle;

import java.util.Observable;
import java.util.Observer;

import brunibeargames.UI.EventPopUp;

public class Initiative implements Observer {
    ObserverPackage op;
    private I18NBundle i18NBundle;
    private boolean isAllies = false;

    public Initiative(){
        i18NBundle= GameMenuLoader.instance.localization;
        EventPopUp.instance.show(i18NBundle.format("determineinitiative"));
        EventPopUp.instance.addObserver(this);
    }
    @Override
    public void update(Observable observable, Object o) {
        op = (ObserverPackage) o;
        if (op.type == ObserverPackage.Type.EVENTPOPUPHIDE) {
            NextPhase.instance.endPhase();
        }
        return;

    }
    public boolean getIsAllies(){
        return isAllies;
    }
}
