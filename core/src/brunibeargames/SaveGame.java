package brunibeargames;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import brunibeargames.Unit.Unit;

public class SaveGame {


	public SaveGame(String fileName) {
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH-mm-ss");
		Date date = new Date();
		fileName = fileName + dateFormat.format(date);
		StringBuffer saveGame = appendData();
		FileHandle file = GamePreferences.getSaveGamesLocation(fileName + ".xml");
		file.writeString(saveGame.toString(), false);
	}
	public static boolean canSaveGame(){
//		if (NextPhase.instance.getPhase() <= Phase.GERMAN_CARD.ordinal()){
//			return false;
//		}

		return true;
	}

	public static StringBuffer appendData() {

			StringBuffer saveGame = new StringBuffer();
			saveGame.append("<game>");

			saveGame.append("<turn value=");
			int turn =     NextPhase.instance.turn;
			saveGame.append("\""+String.format("%02d", turn)+"\" />");
		    //int wType = Weather.instance.getCurrentType().ordinal();
			//saveGame.append("<weather value=");
			//saveGame.append("\""+String.format("%02d", wType)+"\" />");
			int phase = NextPhase.instance.getPhase();
			saveGame.append("<phase value=");
			saveGame.append("\""+String.format("%02d", phase)+"\" />");
			String uid = NextPhase.instance.getProgramUID();
			saveGame.append("<uid value=");
			saveGame.append("\""+uid+"\"/>");
			saveGame.append("<scenario value=");
			int  scenario = GameSetup.instance.getScenario().ordinal();
			saveGame.append("\""+String.format("%02d", scenario)+"\" />");
			//saveGame.append("<bombers value=");
			//int cntallied = Weather.instance.getCurrentBombers();
			//saveGame.append("\""+String.format("%02d", cntallied)+"\" />");
			saveGame.append("<cntdebug value=");
			int cntDebug = NextPhase.instance.cntDebug;
			saveGame.append("\""+String.format("%02d", cntDebug)+"\" />");
			/**
			 *  get both allied and axis
			 */
			ArrayList<Unit> arrUnits = Unit.getOnBoardAllied();
			arrUnits.addAll(Unit.getOnBoardAxis());
			arrUnits.addAll(Unit.getEliminated(true));
			arrUnits.addAll(Unit.getEliminated(false));

			saveGame.append(Unit.getXMLUnits(arrUnits));
			ArrayList<Hex> arrAttacked = Hex.getAttackedThisTurn();
			if (arrAttacked.size() > 0) {
				saveGame.append("<hexsattacked>");
				for (Hex hex : arrAttacked){
					saveGame.append("<hex>");
					saveGame.append("<xPos value=\"");
					saveGame.append(String.format("%02d", hex.xTable));
					saveGame.append("\"/>");
					saveGame.append("<yPos value=\"");
					saveGame.append(String.format("%02d", hex.yTable));
					saveGame.append("\"/>");
					saveGame.append("</hex>");
				}
				saveGame.append("</hexsattacked>");
			}
//			saveGame.append(Reinforcement.instance.saveRemoved());
			saveGame.append("<hotseat value=\"");
			if (GameSetup.instance.isHotSeatGame()){
				saveGame.append("true");
			}else{
				saveGame.append("false");
			}
			saveGame.append("\"/>");

			saveGame.append("<germanversusai value=\"");
			if (GameSetup.instance.isRussianVersusAI()){
				saveGame.append("true");
			}else{
				saveGame.append("false");
			}
			saveGame.append("\"/>");

			saveGame.append("<alliedversusai value=\"");
			if (GameSetup.instance.isFrenchVersusAI()){
				saveGame.append("true");
			}else{
				saveGame.append("false");
			}
			saveGame.append("\"/>");
		saveGame.append("<easygerman value=\"");
		if (GameSetup.instance.isEasyRussian()){
			saveGame.append("true");
		}else{
			saveGame.append("false");
		}
		saveGame.append("\"/>");

		saveGame.append("<easyamerican value=\"");
		if (GameSetup.instance.isEasyFrench()){
			saveGame.append("true");
		}else{
			saveGame.append("false");
		}
		saveGame.append("\"/>");
		saveGame.append("<balanced value=\"");
		if (GameSetup.instance.isBalanced()){
			saveGame.append("true");
		}else{
			saveGame.append("false");
		}
		saveGame.append("\"/>");
		saveGame.append("<initiative value=\"");
		if (NextPhase.instance.initiative.getIsAllies()){
			saveGame.append("true");
		}else{
			saveGame.append("false");
		}
		saveGame.append("\"/>");

		int scene = GameSetup.instance.getScenario().ordinal();
		saveGame.append("<scenario value=");
		saveGame.append("\""+String.format("%02d", scene)+"\" />");
		saveGame.append("</game>");





		return saveGame;
		}


	static public void zipDebugFiles(boolean isStart) throws IOException {
		ArrayList<File> arrFile = new ArrayList<>();
		File folder = GamePreferences.getSaveAIDebug().file();
		if (folder.listFiles() == null || folder.listFiles().length == 0) {
			return;
		}
		if (folder.listFiles() != null) {
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {

				} else {
					if (fileEntry.toString().indexOf("xml") >= 0) {
						arrFile.add(fileEntry);// do something interesting here
					}
				}
			}
		}

		String name;
		if (isStart) {
			name = "StartGameSave.zip";
		} else {
			name = "EndGameSave.zip";
		}

		FileOutputStream fos = new FileOutputStream(name);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (File filetoZip : arrFile) {
			FileInputStream fis = new FileInputStream(filetoZip);
			ZipEntry zipEntry = new ZipEntry(filetoZip.getName());
			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[10024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}
		zipOut.close();
		fos.close();
	}



	static public void SaveDebug(String filename, int cnt) {
		if (cnt == 1) { // start of game
			try {
				zipDebugFiles(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			File folder = GamePreferences.getSaveAIDebug().file();
			if (folder.listFiles() != null) {
				for (File fileEntry : folder.listFiles()) {
					if (fileEntry.isDirectory()) {

					} else {
						if (fileEntry.toString().contains("xml")) {
							fileEntry.delete();// do something interesting here
						}
					}
				}
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH-mm-ss");
		Date date = new Date();
		filename = filename + dateFormat.format(date)+".xml";
		StringBuffer saveGame = appendData();
		FileHandle file = GamePreferences.getSaveAIDebug(filename + ".xml");

		file.writeString(saveGame.toString(), false);


	}
	static public void SaveResume() {
		String filename = "resume"+".xml";
		StringBuffer saveGame = appendData();
		FileHandle file = GamePreferences.getSaveResume(filename + ".xml");

		file.writeString(saveGame.toString(), false);


	}

	static public void SaveLastPhase(String filename, int cnt) {
//		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH-mm-ss");
		/**
		 *  Will allow save phase but not save turn for ai
		 */
		if (canSaveGame()) {
			Date date = new Date();
			StringBuffer saveGame = appendData();
			FileHandle file = GamePreferences.getSaveGamesLocation(filename + ".xml");
			file.writeString(saveGame.toString(), false);
		}
	}


	public static FileHandle getResume() {
		String strReturn = null;
		String filename = "resume"+".xml";
		FileHandle file = GamePreferences.getSaveResume(filename + ".xml");
		if (file.exists()){
			return file;
		}
		return null;
	}
}


