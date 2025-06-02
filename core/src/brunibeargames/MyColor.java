package brunibeargames;

import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

import javax.swing.JColorChooser;

public class MyColor {
    public static MyColor instance;
    int i=0;
    MyColor(){
        instance = this;
    }
    public void show() {
   //     VisTextButton colorPickerButton = new VisTextButton("Pick Color");
    //    VisTable root = new VisTable();
    //    root.setFillParent(true);
    //    Borodino.instance.guiStage.addActor(root);
        i++;
        if (i>1){
            return;
        }

        ColorPicker colorPicker = new ColorPicker("Choose a Color", new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                i=0;
                //Gdx.app.log("ColorPicker", "Selected color: " + newColor.toString());
                // You can use the newColor value for whatever you need (e.g., set button color)
                //colorPickerButton.setColor(newColor);
            }
        });

       /* colorPickerButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                Borodino.instance.guiStage.addActor(colorPicker.fadeIn());
            }
            return true;
        }); */

     //   root.add(colorPickerButton).pad(10);
        Borodino.instance.guiStage.addActor(colorPicker.fadeIn());
    }
    public void other(){
        java.awt.Color selectedColor = JColorChooser.showDialog(null, "Choose a color", java.awt.Color.RED);
        if (selectedColor != null) {
            System.out.println("Selected color: " + selectedColor);
            int i=0;
        }
    }

}
