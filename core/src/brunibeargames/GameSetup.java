package brunibeargames;


public class GameSetup {

    private boolean hotSeatGame;


    private boolean russianVersusAI;
    private boolean frenchVersusAI;
    private boolean easyRussian;
    private boolean easyFrench;

    private boolean balanced;
    private int commandMethod = CommandMethod.StandardNAW.ordinal();

    private Scenario scenario = Scenario.Sept5;
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

    public boolean isRussianVersusAI() {
        return russianVersusAI;
    }

    public void setRussianVersusAI(boolean russianVersusAI) {

        this.russianVersusAI = russianVersusAI;
    }


    public void setFrenchVersusAI(boolean isIn) {
        this.frenchVersusAI = isIn;
    }

    public boolean isEasyRussian() {
        return easyRussian;
    }

    public void setEasyRussian(boolean easyRussian) {
        this.easyRussian = easyRussian;
    }
    public void setScenario(Scenario scenario)
    {
    	this.scenario = scenario;
    }
    public Scenario getScenario()
    {
        return scenario;
    }
    public boolean isEasyFrench() {
        return easyFrench;
    }

    public void setEasyFrench(boolean easyFrench) {
        this.easyFrench = easyFrench;

    }

    public boolean isBalanced() {
        return balanced;
    }

    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }

    public boolean isFrenchVersusAI() {
        return frenchVersusAI;
    }
    public int getCommandMethod(){
        return commandMethod;
    }




    public enum Scenario{Sept5(6),Sept6(8),Sept7(9),Free(11);

            private int length;
            Scenario(int length){
                this.length = length;
            }
            public int getLength(){
                return length;
            }
    }
    public enum CommandMethod{StandardNAW}{

    }




}
