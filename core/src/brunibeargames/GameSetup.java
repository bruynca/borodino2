package brunibeargames;


public class GameSetup {

    private boolean hotSeatGame;


    private boolean germanVersusAI;
    private boolean alliedVersusAI;
    private boolean easyGerman;
    private boolean easyAmerican;

    private boolean balanced;

    private Scenario scenario = Scenario.Intro;
   static public GameSetup instance;
   public GameSetup(){
        instance = this;
    }
    public boolean isHotSeatGame() {
        return hotSeatGame;
    }

    public void setHotSeatGame(boolean hotSeatGame) {
        this.hotSeatGame = hotSeatGame;
    }

    public boolean isGermanVersusAI() {
        return germanVersusAI;
    }

    public void setGermanVersusAI(boolean germanVersusAI) {

        this.germanVersusAI = germanVersusAI;
    }


    public void setAlliedVersusAI(boolean isIn) {
        this.alliedVersusAI = isIn;
    }

    public boolean isEasyGerman() {
        return easyGerman;
    }

    public void setEasyGerman(boolean easyGerman) {
        this.easyGerman = easyGerman;
    }
    public void setScenario(Scenario scenario)
    {
    	this.scenario = scenario;
    }
    public Scenario getScenario()
    {
        return scenario;
    }
    public boolean isEasyAmerican() {
        return easyAmerican;
    }

    public void setEasyAmerican(boolean easyAmerican) {
        this.easyAmerican = easyAmerican;

    }

    public boolean isBalanced() {
        return balanced;
    }

    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }

    public boolean isAlliedVersusAI() {
        return alliedVersusAI;
    }




    public enum Scenario{Intro(6),SecondPanzer(8),Lehr(9),CounterAttack(11);

            private int length;
            Scenario(int length){
                this.length = length;
            }
            public int getLength(){
                return length;
            }
        }




}
