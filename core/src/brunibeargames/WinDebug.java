package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import brunibeargames.Unit.Corp;
import brunibeargames.Unit.Division;
import brunibeargames.Unit.Unit;


public class WinDebug {
	TextButton textButton;
	Stage guiStage;
	Stage mapStage;
	public static WinDebug instance = null;
	Label.LabelStyle labelStyleName
			= new Label.LabelStyle(FontFactory.instance.yellowFont, Color.YELLOW);

	TextButton.TextButtonStyle tx = GameSelection.instance.textButtonStyle;
	HiliteHex hiliteHex;

	public WinDebug()
	{
		if (instance != null)
		{
			return;
		}
		mapStage = Borodino.instance.mapStage;
		guiStage = Borodino.instance.guiStage;
		instance = this;
		textButton =  new TextButton("Debug",tx);
		textButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				ClickButton(event, x, y);
			}
		});
		Display();

	}
	public void Display()
	{
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		// set position 3/4 of the width and at top
		float x =  (float) width * 1/10 + 100F;
		float y = (float) height - textButton.getHeight();
		textButton.remove();
		textButton.setPosition(x, y);
		guiStage.addActor(textButton);
		textButton.setVisible(true);
	}
	boolean isVisible = false;
	public void toggle(){
		if (isVisible){
			textButton.setVisible(false);
			isVisible = false;
		}else{
			textButton.setVisible(true);
			isVisible = true;
		}

	}
	boolean clicked = true;
	public void ClickButton(InputEvent event, float xIn, float yIN) {
		Gdx.app.log("Windebug", "Click Button");
		Unit test = null;
		doShowHexes();
		int i=1;
		if (i==1){
			return;
		}
		for (Corp corp:Corp.russianCorp) {
			Gdx.app.log("Corp Display", "Corp=" + corp.name);
			for (Division div : Division.arrDivisions) {
				if (div.corp.equals(corp)) {
					Gdx.app.log("Division Display=", "Division=" + div.name + " Entry Day=" + div.entryDay + " Entry Turn=" + div.entryTurn + " Entry Area=" + div.entryArea);

					for (Unit unit : Unit.arrGameCombatUnits) {
						if (unit.isOfficer || unit.isCommander) {
							continue;
						}
						if (unit.division.equals(div)) {
							Gdx.app.log("Unit Display", "Unit=" + unit.brigade + " ID=" + unit.ID);


						}
					}
				}
			}
		}





	/*	Scenarios.loadUnitsOnBoard(0);

		int y = 8;
		ArrayList<Unit> arrUnits = new ArrayList<>();
		arrUnits.addAll(Unit.getAllRussian());
		arrUnits.addAll(Unit.getAllAllied());
		int x = 25;
		int i = 0;
		for (Unit unit : arrUnits) {
			if (unit.brigade.contains("Valen")){
				int b=0;
			}
			if (unit.isOfficer && !unit.isAllies && unit.getHexOccupy() == null) {
				Hex hex = Hex.hexTable[x][y];
				Gdx.app.log("Windebug", "Click Button unit=" + unit.brigade + "i=" + i);
				unit.placeOnBoard(hex);
//				arrUnits.get(i).placeOnBoard(hex);
				x++;
				i++;
				if (x > 40) {
					x = 0;
					y++;
				}
			}
		}
//		if (i> 80){
//				break;
//		}


/*		i=0;
		y++;
		y++;
		arrUnits.clear();
		arrUnits.addAll(Unit.getAllRussian());
		x=0;
		for (Unit unit:arrUnits){
			Hex hex= Hex.hexTable[x][y];
			Gdx.app.log("Windebug", "Click Button Russian  unit="+unit.brigade+ " y="+y );

			arrUnits.get(i).placeOnBoard(hex);
			x++;
			i++;
			if (x>40){
				x=0;
				y++;
			} */


			Gdx.app.log("Windebug", "Click Button Ended");

	}

/*		if (clicked){
			if (hiliteHex != null){;
				hiliteHex.remove();
				hiliteHex = null;
				return;
			}
			//           doShowJunctions();
			doShowHexes();
			ArrayList<Hex> arrHexSupply = new ArrayList<>();
			return;
		}
		if (clicked)
		{


			//           DoShowPath();
			doShowHexes();

			clicked = false;
		}
		else
		{
			clicked = true;
			for (Actor actor:arrActors)
			{
				actor.remove();
			}
		}


	} */
	ArrayList<Actor> arrActors =  new ArrayList();
	private void doShowHexes(){
		ArrayList<Hex> arrHex = new ArrayList<>();
		for (int x=0; x< Hex.xEnd; x++)
		{
			for (int y=0; y < Hex.yEnd; y++)
			{
				arrHex.add(Hex.hexTable[x][y]);
			}
		}
		hiliteHex = new HiliteHex(arrHex, HiliteHex.TypeHilite.Debug,null);
	}

}

