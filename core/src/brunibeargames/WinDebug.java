package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import static brunibeargames.Hex.arrHexMap;

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
//			showAllStreams();

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

	private void DoShowAll() {
		arrActors.clear();
		for (int y = 0; y < Hex.yEnd; y++)
		{
			for (int x = 0; x< Hex.xEnd; x++)
			{
				Hex hex = Hex.hexTable[x][y];
				if (hex.isForest || hex.isRoad || hex.isPath|| hex.isBridge)
				{
					String str = "";
					if (hex.isRoad){
						str +=" R";
					}
					if (hex.isForest){
						str +=" F";
					}
					if (hex.isPath){
						str +=" P";
					}
					if (hex.npath != null){
						str +="n";
					}

					if (hex.isBridge){
						str +=" B";
					}
					str += "\n"+x+","+y;
					Label label = new Label(str,skin);
					label.setScale(20f);
					label.setColor(Color.YELLOW);
					label.setFontScale(2f);
					Vector2 vector2 =  hex.GetDisplayCoord();
					label.setPosition(vector2.x+35,vector2.y+25);
					mapStage.addActor(label);
					arrActors.add(label);
				}
			}

		}
	}
	public Label getHexLable(Hex hex){
		String str = hex.xTable+", "+hex.yTable;
		Label label = new Label(str,skin);
		label.setScale(20f);
		label.setColor(Color.YELLOW);
		label.setFontScale(2f);
		Vector2 vector2 =  hex.GetDisplayCoord();
		label.setPosition(vector2.x+35,vector2.y+25);
		return label;
	}

	ArrayList<Actor> arrActors =  new ArrayList();
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
	public void doShowStream(Hex hex){
		Gdx.app.log("Windebug", "Do Show Stream=" + hex);
		mapStage.clear();
		showAllStreams();
		if (!hex.isStreamBank){
			return;
		}
		Texture texHex = Borodino.instance.getTexHex();
		Image image = new Image(texHex);
		Vector2 v2 =  hex.GetDisplayCoord();
		image.setPosition(v2.x,v2.y);

		mapStage.addActor(image);
		mapStage.addActor(getHexLable(hex));
		ArrayList<Hex> arrHex = hex.findOtherSideStream();
		ArrayList<Hex> arrTest = hex.getSurround();

/*		for (Hex hex2:arrTest){
			image = new Image(texHex);
			v2 =  hex2.GetDisplayCoord();
			image.setPosition(v2.x,v2.y);
			mapStage.addActor(image);
			mapStage.addActor(getHexLable(hex2));

		}*/

		for (Hex hex2:arrHex){
			image = new Image(texHex);
			v2 =  hex2.GetDisplayCoord();
			image.setPosition(v2.x,v2.y);
			mapStage.addActor(image);
			mapStage.addActor(getHexLable(hex2));

		}
	}
	public void showAllStreams(){
		ArrayList<Hex> arrFound = new ArrayList<>();
		ArrayList<String> arrString = new ArrayList<>();
		int ix=0;
		for (Hex hex:arrHexMap){
			if (hex.isStreamBank){
				arrFound.add(hex);
				String str = String.valueOf(hex.streamBank);
				for (int in: hex.arrMultiplStreamBank){
					str+=" "+in;
				}
				arrString.add(str);
			}
		}
		for (int i=0; i<arrFound.size();i++){
			if (arrFound.get(i).xTable==22 & arrFound.get(i).yTable == 12){
				int br=0;
			}
			Label label = new Label(arrString.get(i),skin);
			label.setScale(20f);
			label.setColor(Color.YELLOW);
			Vector2 vector2 =  arrFound.get(i).GetDisplayCoord();
			label.setPosition(vector2.x+ 60,vector2.y+ 60);
			mapStage.addActor(label);
			arrActors.add(label);
		}
	}
	public void doRiver(Hex hex){
		Gdx.app.log("Windebug", "DoRiver=" + hex);
		mapStage.clear();
		showAllStreams();
		if (!hex.isRiverBank){
			return;
		}
		Texture texHex = Borodino.instance.getTexHex();
		Image image = new Image(texHex);
		Vector2 v2 =  hex.GetDisplayCoord();
		image.setPosition(v2.x,v2.y);

		mapStage.addActor(image);
		mapStage.addActor(getHexLable(hex));
		ArrayList<Hex> arrHex = Hex.showAllRiverCrossings(hex);

/*		for (Hex hex2:arrTest){
			image = new Image(texHex);
			v2 =  hex2.GetDisplayCoord();
			image.setPosition(v2.x,v2.y);
			mapStage.addActor(image);
			mapStage.addActor(getHexLable(hex2));

		}*/

		for (Hex hex2:arrHex){
			image = new Image(texHex);
			v2 =  hex2.GetDisplayCoord();
			image.setPosition(v2.x,v2.y);
			mapStage.addActor(image);
			mapStage.addActor(getHexLable(hex2));

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



