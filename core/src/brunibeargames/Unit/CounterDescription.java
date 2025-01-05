package brunibeargames.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import brunibeargames.Hex;
import brunibeargames.Map;
import brunibeargames.UI.WinToolTip;

public class CounterDescription {
    Counter counter;
    ArrayList<Stack> arrCounters = new ArrayList<>();
    static final int size =80;
    WinToolTip winToolTip;

    boolean isCancel = false;
    CounterDescription(final Counter counter){
        this.counter = counter;
        final Counter counterDisplay = counter;

        final Hex hex = counter.unit.getHexOccupy();
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               showWindow(counterDisplay.getUnit());
                           }
                       }, .6F
        );
        final Counter checkCounter = counter;
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               checkCounter.checkDescGone();
                           }
                       }, 6F
        );
    }

    private void showWindow(Unit unit) {
        if (isCancel){
            return;
        }
        Vector2 v2 = Map.ConvertToScreen(unit.getHexOccupy());
        int y = Gdx.graphics.getHeight();
        int x = Gdx.graphics.getWidth();
        String str = " ";
        winToolTip = new WinToolTip(str);
        if (v2.y > y - 50){
            v2.y -=100;
        }
        if (v2.x > x - 100){
            v2.x -= 100;
        }
        winToolTip.show(v2);
    }

    public void end() {
        isCancel = true;
        if (winToolTip != null) {
            winToolTip.remove();
        }
    }
}
