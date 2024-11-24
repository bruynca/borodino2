package brunibeargames;

import com.badlogic.gdx.utils.I18NBundle;

import brunibeargames.UI.EventPopUp;

public class HelpPage {
    I18NBundle i18NBundle;
    public static HelpPage instance;
    Phase phase;
    boolean isOther = false;
    String strOther;
    public HelpPage(){
        instance  =this;
        i18NBundle = GameMenuLoader.instance.localization;
    }


    public void nextPhase() {
        int iPhase = NextPhase.instance.getPhase();
        EventPopUp.instance.hideImmediate();
        if (iPhase == 0){
            int b=0;
        }
        phase = Phase.values()[iPhase];
        if (phase.isHelp()) {
            if (GamePreferences.getPhaseInfo(phase.toString())) {
                return;  // if its there we dont want to do a thing
            }
            String str = i18NBundle.format(phase.toString());
            EventPopUp.instance.showHelp(str);
        }
        isOther = false;

    }
    public void showOther(String strOther){
        this.strOther = strOther;
        isOther = true;
        if (!GamePreferences.getPhaseInfo(strOther)){
            String str = i18NBundle.format(strOther);
            EventPopUp.instance.showHelp(str);
        }
    }

    public void dontShowAgain() {
        if (isOther){
            GamePreferences.setOther(strOther);
        }else {
            GamePreferences.setPhaseInfo(phase);
        }
        isOther = false;
    }
}
