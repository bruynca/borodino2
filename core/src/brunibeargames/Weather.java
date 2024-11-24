package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Random;

import brunibeargames.UI.EventPopUp;

public class Weather {
    static public Weather instance;
    int[][] dieTable ={{10,11,12,13},{9,11,12,13},{8,10,12,13},{7,6,8,10,12},{6,8,10,12},{4,6,9,12},{2,4,8,12}};
    int[] bombers ={0,1,3,5};
    Type currentType;
    int currentBombers;
    private I18NBundle i18NBundle;

    public Weather(){
        instance = this;
        i18NBundle = GameMenuLoader.instance.localization;

        currentType = Type.Soup;
        if (NextPhase.instance.getTurn() != 1) {
            currentBombers = calculateBombers();
        }
    }
    public int getCurrentBombers(){
        return currentBombers;
    }

    public Type getCurrentType() {
        return currentType;
    }

    public void nextTurn(){
        /**
         *
         */
        int totDie= getDieRoll() + getDieRoll();
        int addToWeather = 0;
        if (totDie == 2){
            addToWeather =-2;
        }else if( totDie <= 4){
            addToWeather =-1;
        }else if(totDie <= 7){
            addToWeather = 0;
        }else if(totDie < 10){
            addToWeather =1;
        }else{
            addToWeather = 2;
        }
        int newWeather = currentType.ordinal() + addToWeather;
        if (newWeather < 0){
            newWeather = 0;
        }else if (newWeather > (Type.values().length -1)){
            newWeather = Type.values().length -1;
        }
        currentType = Type.values()[newWeather];
        String strWeather = i18NBundle.format("weather", currentType.toString() + "\n");
        Gdx.app.log("Weather", "Next Turn Weather ="+strWeather+" air =");




    }
    private int calculateBombers() {
        int tableToUse = currentType.ordinal();
        Gdx.app.log("Weather", "Table to Use ="+tableToUse);
        int totDie = getDieRoll() + getDieRoll();
        Gdx.app.log("Weather", "calculate Bombers tot dice ="+totDie);

        int ixBombers = dieTable[tableToUse].length -1;
        for (int i=0; i < dieTable[tableToUse].length; i++){
            if (totDie < dieTable[tableToUse][i]){
                ixBombers = i;
                break;
            }
        }
        if (ixBombers > 3){
            ixBombers = 3;
        }
        Gdx.app.log("Weather", "Bombers="+bombers[ixBombers]);

        return bombers[ixBombers];
    }

    public int getDieRoll()
    {
        Random diceRoller = new Random();
        int die = diceRoller.nextInt(6) + 1;
        return die;

    }

    public void loadWeather(int weather) {
        currentType = Type.values()[weather];
    }

    public void setCurrentBombers(int cntAllied) {
        currentBombers = cntAllied;
    }

    public enum Type {Soup, Overcast,Cloudy,Mist,Variable,Clear,Bright};
}

