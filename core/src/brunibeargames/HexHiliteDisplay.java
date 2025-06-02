package brunibeargames;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import brunibeargames.UI.MouseImage;
import brunibeargames.Unit.Unit;

/**
 *  put hilite display on the map
 */
public class HexHiliteDisplay {
    static TextureAtlas textureAtlas;
    static TextureRegion backHilite;
    static TextureRegion backHiliteReinDisplay;
    static TextureRegion backHiliteCannonRange;
    static TextureRegion backHiliteMove;
    static Texture backHiliteMoveTexture;
    static TextureRegion backHiliteExit;
    static ArrayList<HexHiliteDisplay> arrHexHilite = new ArrayList<>();
    static Label.LabelStyle labelStyleName;

    Image image;
    Label label;
    Label label2;
    HiliteHex.TypeHilite type;
    static ArrayList<Unit> arrUnitsToShade = new ArrayList<>();
    Hex hex;
    public HexHiliteDisplay(Hex hex, HiliteHex.TypeHilite typeIn){
        Vector2 pos = hex.getCounterPosition();
       type = typeIn;
       this.hex = hex;
      if (pos == null){
            int brk = 0;
        }
        //String str = hex.xTable+ " "+hex.yTable;
        String azoc = "";
        String rzaoc= "";
        if (hex.isAlliedZOC[0]){
            azoc="AZ";
        }
        if (hex.isRussianZOC[0]){
            rzaoc="RZ";
        }
       // String str = hex.xTable+ " "+hex.yTable;
        String str = azoc+"  "+rzaoc;
        /**
         *  SPOT to change AISCORE
         */

        label = new Label(str,labelStyleName);
        label.setFontScale(.8f);
        label.setPosition(pos.x+ 60, pos.y+40);
     //   label2.setPosition(pos.x, pos.y+50);


        pos.x -= 36;  //-48
        pos.y -= 15; //22
        float xdiv = hex.xTable;
 //       float adjustx = (32/xdiv) * .3125f;
 /*       if (xdiv < 10) {
            pos.x -= 10;
        }else if (xdiv < 20){
            pos.x -=6;
        }else if (xdiv < 30){
        pos.x -=3;
        } */

        if (type == HiliteHex.TypeHilite.ReinforceDisplay){
            image = new Image(backHiliteReinDisplay);
        }else if (type == HiliteHex.TypeHilite.Range) {
            image = new Image(backHiliteCannonRange);
        }
        else if (type == HiliteHex.TypeHilite.Hilite){
                image = new Image(backHilite);

        }else if (type == HiliteHex.TypeHilite.Move){
            image = new Image(backHiliteMove.getTexture());
        }else if (type != HiliteHex.TypeHilite.Debug) {
            image = new Image(backHiliteMove);
        }else if (type == HiliteHex.TypeHilite.Debug) {
            image = null;
        }else if (type != HiliteHex.TypeHilite.AI) {
            image = new Image(backHiliteCannonRange);
        }else if (type != HiliteHex.TypeHilite.ShowSupply) {
            image = new Image(backHiliteCannonRange);

        }
        if (image != null) {
            image.setTouchable(Touchable.enabled);

            image.setPosition(pos.x, pos.y);
            if (typeIn == HiliteHex.TypeHilite.Supply) {
                addListner();
            }
        }

        if (typeIn == HiliteHex.TypeHilite.Move){
            image.addAction(Actions.fadeOut(.01f));
            image.addAction(Actions.fadeIn(.5f/(hex.getCalcMoveCost(0)+1)));
        }
        if (image != null) {
            Borodino.instance.hexStage.addActor(image);
        }
        if (typeIn == HiliteHex.TypeHilite.Debug) {

            Borodino.instance.hexStage.addActor(label);
        }

        arrHexHilite.add(this);
    }

    private void addListner() {
        image.addListener(new ClickListener() {

         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        }
    });
   }

    static void removeHexHilite(){
        for (HexHiliteDisplay hexHilite: arrHexHilite){
            if (hexHilite.image != null) {
                hexHilite.image.remove();
            }
            hexHilite.label.remove();
 //           hexHilite.label2.remove();
            if (hexHilite.type == HiliteHex.TypeHilite.Supply){
                MouseImage.instance.mouseImageReset();
            }
        }
        arrHexHilite.clear();
    }
    static public void textTureLoad(){
        textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");
        backHilite =  textureAtlas.findRegion("hilitehex");
        //   backHiliteReinDisplay =  textureAtlas.findRegion("hilitehexreindisplay");
      //  backHiliteCannonRange =  textureAtlas.findRegion("hilitehexcannonrange");
        backHiliteMove =  textureAtlas.findRegion("hilitehexmove3");
        backHiliteMoveTexture  = backHiliteMove.getTexture();

        //  backHiliteExit =  textureAtlas.findRegion("hilitehexexit");
        arrHexHilite = new ArrayList<>();
        labelStyleName
                = new Label.LabelStyle(FontFactory.instance.yellowFont, Color.YELLOW);

    }
    static public void updateTexture(Texture inTexture){
        backHiliteMoveTexture = inTexture;
    }

}
