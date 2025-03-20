package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.widget.VisWindow;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;

public class WinText {
    public static WinText instance;
    private final Label label;
    VisWindow window;
    I18NBundle i18NBundle;
    Cell cell;
    float height = Gdx.graphics.getHeight();
    float width = Gdx.graphics.getWidth();

    public WinText(){
        instance = this;
        i18NBundle = GameMenuLoader.instance.localization;
        window = new VisWindow("");
        window.setVisible(false);
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
//        window.setHeight(height -40);
//        window.setWidth(height - 40);
        window.setHeight(600);
        window.setWidth(600);
        window.centerWindow();
        window.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                int bt=0;
                MouseImage.instance.setIgnore(true);
                MouseImage.instance.setMouseHand();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                MouseImage.instance.setIgnore(false);
                MouseImage.instance.mouseImageReset();
            }
        });
        Label.LabelStyle lab = new Label.LabelStyle();
        lab.font = Fonts.getFont24();
        label = new Label("", lab);
        Borodino.instance.guiStage.addActor(window);

    }
    public void show(String keyText){
        window.setModal(true);
        window.setVisible(true);
        window.toFront();
        label.setText(i18NBundle.format(keyText));
        window.add(label);
    }
    public void hide(){
        window.setVisible(false);
        window.setModal(false);
        window.clearChildren();

    }


}
