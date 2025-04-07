package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.util.Observable;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenu;
import brunibeargames.GameMenuLoader;
import brunibeargames.ObserverPackage;
import brunibeargames.SplashScreen;

public class WinWarning extends Observable {
    private  Label label;
    TextureAtlas textureAtlas;
    TextButton textButtonOK;
    TextButton.TextButtonStyle textButtonStyle;
    VisWindow window;
    I18NBundle i18NBundle;
    Cell cell;
    float height = Gdx.graphics.getHeight();
    float width = Gdx.graphics.getWidth();
    Label labelTitle;
    Image imgWarning;

    public WinWarning(String title,String message){
        textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");
        TextureRegion warning = textureAtlas.findRegion("warning");
        imgWarning = new Image(warning);
        imgWarning.setSize(40,40);

        i18NBundle = GameMenuLoader.instance.localization;
        window = new VisWindow("Warning");
        window.setVisible(false);
        labelTitle = window.getTitleLabel();
        labelTitle.getStyle().font.setColor(Color.YELLOW);
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();
        window.centerWindow();
        Label.LabelStyle lab = new Label.LabelStyle();
        lab.font = Fonts.getFont24();
        lab.fontColor = Color.YELLOW;
        label = new Label("", lab);
        window.setModal(true);
        window.setVisible(true);
        window.toFront();

        labelTitle.setText(title);
        labelTitle.getStyle().font.setColor(Color.YELLOW);
        label.setText(message);
        window.add(imgWarning).size(40,40);
        window.row();
        window.add(label);
        addButtonOK();
        window.pack();
        window.centerWindow();
        Borodino.instance.guiStage.addActor(window);

    }
    public void addButtonOK() {
        textButtonStyle = GameMenu.instance.textButtonStyle;
        textButtonOK = new TextButton(i18NBundle.format("ok"), textButtonStyle);
        textButtonOK.setSize(100, 60);
        textButtonOK.setPosition((window.getWidth() - textButtonOK.getWidth()) / 2, 30);
        textButtonOK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!event.getType().equals("touchUp")) {
                    setChanged();
                    notifyObservers(new ObserverPackage(ObserverPackage.Type.OK,null,0,0));
                    window.remove();
                }

            }
        });
        window.row();
        window.add(textButtonOK);
    }


}
