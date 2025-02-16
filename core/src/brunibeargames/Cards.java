package brunibeargames;

import com.badlogic.gdx.Gdx;

public class Cards {
    static public Cards instance;
    Cards(){
        instance = this;
    }
    public void     doAllies(){
        Gdx.app.log("Cards", "Do Allied");

        NextPhase.instance.endPhase();
    }
    public void     doRussians(){
        Gdx.app.log("Cards", "Do Russians");
        NextPhase.instance.endPhase();
    }

}
