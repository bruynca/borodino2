package brunibeargames.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.widget.VisWindow;

import brunibeargames.Borodino;
import brunibeargames.Fonts;
import brunibeargames.GameMenuLoader;

public class WinText {
    private final Label label;
    VisWindow window;
    I18NBundle i18NBundle;
    Cell cell;
    float height = Gdx.graphics.getHeight();
    float width = Gdx.graphics.getWidth();
    Label labelTitle;

    /**
     * Wintext will fit to largest string
     * @param title
     * @param message
     */
    public WinText(String title,String message){

        i18NBundle = GameMenuLoader.instance.localization;
        window = new VisWindow("Warning");
        window.setVisible(false);
        labelTitle = window.getTitleLabel();
        labelTitle.getStyle().font.setColor(Color.YELLOW);
//        float height = Gdx.graphics.getHeight();
//        float width = Gdx.graphics.getWidth();
//        window.setHeight(height -40);
//        window.setWidth(height - 40);
      //  window.setHeight(600);
      //  window.setWidth(600);
        window.centerWindow();
        window.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.addAction(Actions.sequence(
                        Actions.fadeOut( 0.3f), // Shrink over 0.3 seconds
                        Actions.run(() -> window.setVisible(false)), // Set invisible
                        Actions.run(() -> window.remove()) // Reset size to packed size.
                ));
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
        lab.fontColor = Color.YELLOW;
        label = new Label("", lab);
        window.setModal(true);
        window.setVisible(true);
        window.toFront();
        labelTitle.setText(title);
        labelTitle.getStyle().font.setColor(Color.YELLOW);
        label.setText(message);
        window.add(label);
        window.pack();
        window.centerWindow();
        Borodino.instance.guiStage.addActor(window);

    }


}
