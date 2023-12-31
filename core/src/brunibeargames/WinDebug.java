package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class WinDebug
{
	TextButton textButton;
	Stage guiStage;
	Stage mapStage;
	Skin skin;
	public static WinDebug instance;
	public WinDebug(Stage inStage, Stage guiStage, Skin inSkin)
	{
		if (instance != null)
		{
			return;
		}
		mapStage = inStage;
		this.guiStage = guiStage;
		skin = inSkin;
		instance = this;
		textButton =  new TextButton("Debug",skin);
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
//		textButton.remove();
		textButton.setPosition(x, y);
		guiStage.addActor(textButton);
	}
	boolean clicked = true;
	public void ClickButton(InputEvent event, float xIn, float yIN)
	{
		if (clicked)
		{
			DoShowAll();
//			DoShowPath();
//			DoShowRivers();

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


	}
	ArrayList<Actor> arrActors =  new ArrayList();

	private void DoShowAll() {
		arrActors.clear();

		for (int y = 0; y < Hex.yEnd; y++) {
			for (int x = 0; x < Hex.xEnd; x++) {
				Hex hex = Hex.hexTable[x][y];
				if (x == 0 && y == 22){
					int b=0;
				}
//				if (hex.isForest || hex.isRoad || hex.isPath || hex.isBridge) {
				if (hex.isBridge) {
					String str = "";
				/**	if (hex.isRoad) {
						str += " R";
					}
					if (hex.isForest) {
						str += " F";
					}
					if (hex.isPath) {
						str += " P";
					}
					if (hex.npath != null) {
						str += "n";
					}

					if (hex.isBridge) {
						str += " B";
					}*/
					str += "\n" + x + "," + y;
					Label label = new Label(str, skin);
					label.setScale(20f);
					label.setColor(Color.YELLOW);
					label.setFontScale(2f);
					Vector2 vector2 = hex.GetDisplayCoord();
					label.setPosition(vector2.x + 35, vector2.y + 25);
					mapStage.addActor(label);
					arrActors.add(label);
				}
			}

		}
	}


	private void DoShowRivers()
	{
		arrActors.clear();
		int i =0;
		String stream;
		for (River river:River.arrRivers)
		{
			if (river.isStream)
			{
				stream = "S";
			}
			else
			{
				stream = "R";
			}
				
			for (Hex hex:river.arrAbank)
			{
				
				Label label = new Label(i+stream+"a",skin);
				label.setScale(10f);
				label.setColor(Color.RED);
				Vector2 vector2 =  hex.GetDisplayCoord();
				label.setPosition(vector2.x,vector2.y);
				mapStage.addActor(label);
				arrActors.add(label);
			}
			for (Hex hex:river.arrBbank)
			{
				Label label = new Label(i+stream+"b",skin);
				label.setScale(10f);
				label.setColor(Color.RED);
				Vector2 vector2 =  hex.GetDisplayCoord();
				label.setPosition(vector2.x,vector2.y);
				mapStage.addActor(label);
				arrActors.add(label);
			}
			i++;
			
		}

	}

	/**
	private void DoSelectedRegions()
	{
		ArrayList<Hex> arrHex = new ArrayList<Hex>();
		AIRegion aiRegion = AIRegionFactory.instance.topOfMapInDanger;
		AIRegion aiRegionNew = AIRegionFactory.instance.GetPreviousXRegion(aiRegion);
		arrHex.addAll(aiRegionNew.GetRightBorderHex(3));
//		arrHex.addAll(AIRegionFactory.instance.middleOfMapInDanger.arrHex);
//		arrHex.addAll(AIRegionFactory.instance.bottomOfMapInDanger.arrHex);
		HiliteHex.instance.Activate(arrHex,Type.Debug.GetValue(), false);
		clicked = false;
	}
	private void DoAIRegion(int i, int j) 
	{
		
		AIRegion ai = AIRegionFactory.instance.AIRegionTable[i][j];
		ArrayList<Hex> arrHex = ai.arrHex;
		HiliteHex.instance.Activate(arrHex, HiliteHex.Type.Debug.GetValue(), false);
		clicked = false;

		
	}
	public void DoRoadMoveCost()
	{
		// use main thread
		Hex.InitMoveCost(0);
		ArrayList<Hex> arrHexes = new ArrayList<Hex>();
		for (int y = 0; y < Hex.yEnd; y++)
		{
			for (int x = 0; x< Hex.xEnd; x++)
			{
				Hex hex = Hex.hexTable[x][y];
				if (hex.road != null)
				{
					hex.cntMoveCost[0] =hex.road.arrNextRoad.size();
				}
				arrHexes.add(hex);
//				if (hex.isRoad)
//				{
//					hex.cntMoveCost++;
//					arrHexes.add(hex);
//					hex.cntMoveCost += hex.road.arrNextRoad.size();
//				}
			}
			
		}
		HiliteHex.instance.Activate(arrHexes, HiliteHex.Type.Debug.GetValue(), false);
		clicked = false;

	}
	public void DoBridge()
	{
		// use main thread
		Hex.InitMoveCost(0);
		ArrayList<Hex> arrHexes = new ArrayList<Hex>();
		for (int y = 0; y < Hex.yEnd; y++)
		{
			for (int x = 0; x< Hex.xEnd; x++)
			{
				Hex hex = Hex.hexTable[x][y];
				if (hex.isBridge)
				{
				arrHexes.add(hex);
				}
//				if (hex.isRoad)
//				{
//					hex.cntMoveCost++;
//					arrHexes.add(hex);
//					hex.cntMoveCost += hex.road.arrNextRoad.size();
//				}
			}
			
		}
		HiliteHex.instance.Activate(arrHexes, HiliteHex.Type.Debug.GetValue(), false);
		clicked = false;

	}
	public void DoOccupied()
	{
		// use main thread
		Hex.InitMoveCost(0);
		ArrayList<Hex> arrHexes = new ArrayList<Hex>();
		for (int y = 0; y < Hex.yEnd; y++)
		{
			for (int x = 0; x< Hex.xEnd; x++)
			{

				Hex hex = Hex.hexTable[x][y];
				if (hex.occHexAllies[Settings.instance.threadAI])
				{
					arrHexes.add(hex);
				}
//				if (hex.isRoad)
//				{
//					hex.cntMoveCost++;
//					arrHexes.add(hex);
//					hex.cntMoveCost += hex.road.arrNextRoad.size();
//				}
			}
			
		}
		HiliteHex.instance.Activate(arrHexes, HiliteHex.Type.Debug.GetValue(), false);
		clicked = false;

	}

	public void Chosen(Hex hex) 
	{
		int i=1;
	}

*/
}



