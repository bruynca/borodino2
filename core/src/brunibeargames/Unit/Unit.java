package brunibeargames.Unit;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import java.util.ArrayList;

import brunibeargames.Borodino;
import brunibeargames.ErrorGame;
import brunibeargames.Game;
import brunibeargames.Hex;
import brunibeargames.SplashScreen;

/**
 * Unit represents the counters that you punch out on a  war game
 *
 * @author Casey
 *
 */


public class Unit {

	/**
	 * All the units loaded in the game
	 */
	private static int cntUnit=0;
	/**
	 *  STATIC ARRAY Fields
	 */
	public static ArrayList<Unit> arrGameCombatUnits = new ArrayList<>();
	private static ArrayList<Unit> arrGameOtherUnits = new ArrayList<>();
	static TextureAtlas textureAtlas;
	static TextureRegion tStar;
	static public Unit unitAirplane = null;
	public Officer officer;
	public Commander commander;
	public boolean isOfficer = false;
	public boolean isCommander;


	/**
	 *  Nationality fields
	 */
	public boolean isFrench;
	public boolean isAllies;
	public boolean isPolish;
	public boolean isBavarian;
	public boolean isWestphalian;
	public boolean isWurttenburg;
	public boolean isItalian;
	public boolean isItalianGuard;
	public boolean isGuard;
	public boolean isPolishGuard;
	public boolean isCossack;
	public boolean isRussian;
	/**
	 *  String descriptions
	 */
	public String brigade;
	private Corp corp;
	public Division division;

	public boolean isBrigade= false;
	public boolean isVedette= false;
	public boolean isInfantry;
	public boolean isCalvary;
	public boolean isArtllery;



	//public boolean isBattalion=false;
	//public boolean isAdHoc=false;

	public String type= "";
	/**
	 *  Processing fields
	 */
	public int ID;
	public boolean isGroundCombat;
	public boolean isMechanized;
	public boolean isHQ;

	public boolean isArtillery;
	public boolean isTransport;

	public int entryTurn;

	public int entryNum;

	public boolean canAttackThisTurn = true;
	public boolean hasBeenAttackedThisTurn = false;
	public boolean hasAttackedThisTurn = false;

	private int atStartStep;
	private int currentStep;
	private int currentAttackFactor;
//	private int currentDefenseFactor;
	private int currentMoveFactor;
	private int atStartAttackFactor;
//	private int atStartDefenseFactor;
	private int atStartMoveFactor;
	/**
	 * artillery
	 */
	private int range;
	private boolean isActivated =false;
	private boolean hasBeensActivatedThisTurn =false;
	public boolean isDelete = false;


	public static void loadTexture() {
		//String strFrench = SplashScreen.instance.unitsManager.get("units/frenchnew.xml");
		textureAtlas = SplashScreen.instance.unitsManager.get("counter/counter.txt");
	}

	int xSetup;
	int ySetup;

	private int turnMoved =0;
	private String variant; // Y - in variant   N - removed when variant played
	private Counter counter;
	private Hex hexOccupy = null;
	private Hex hexOccupyPrev = null;
	private boolean isOnBoard;
	private  boolean isInSupplyThisTurn = true;

	public static void shadeAllAllies() {
		for (Unit unit:getOnBoardAllied())
		{
			if (unit.isOnBoard  && unit.counter.getCounterStack() != null)
			{
				unit.counter.getCounterStack().shade();
			}
		}
	}
	public static void shadeAllAxis() {
		for (Unit unit:getOnBoardAxis())
		{
			if (unit.isOnBoard  && unit.counter.getCounterStack() != null)
			{
				unit.counter.getCounterStack().shade();
			}
		}
	}

	public static Unit getUnitByID(int id) {
		for (Unit unit:arrGameCombatUnits){
			if (unit.ID == id){
				return unit;
			}
		}
		return null;
	}

	public static void initCommand() {
			for (Unit unit:arrGameCombatUnits){
				unit.isActivated = false;
				unit.hasBeensActivatedThisTurn = false;
			}
	}

	public int getArtAmmo() {
		return artAmmo;
	}

	public void setArtAmmo(int artAmmo) {
		this.artAmmo = artAmmo;
	}
	public void setCurrentAttackFactor(int attackFactor) {
		currentAttackFactor = attackFactor;
	}
	private int artAmmo;



	protected boolean isEliminated = false;
	private boolean isExertZOC = false;
	SupplyUnit supplyUnit;
	private boolean isDisorganized =false;

	/**
	 * Constructor
	 * All units are loaded at start of the game
	 */
	public Unit(boolean isAllies, String  strBrigade, Corp corp, Division division, Element xmlBrigade,boolean isCommander, boolean isOfficer)
	{
		/**
		 * update ID and increment so that each unit is unique
		 */
		ID = cntUnit;
		cntUnit++;
		this.corp = corp;
		this.division = division;
		/**
		 *  update nationality and set if axis or allies
		 */
		if (isAllies) {
			isRussian = false;
			this.isAllies = true;
		}else{
			this.isAllies = false;
			isRussian = true;
		}
		/**
		 *  xml can either be 1 long string of values or
		 *  a number of tags set defaults first and then update
		 */
		if (isAllies){
			isFrench = true;
		}
		if (isOfficer||(isOfficer && isCommander)) {
			this.isOfficer = true;
			this.corp = corp;
			this.currentMoveFactor = 6;
			isInfantry = false;
			String strList[] = strBrigade.split("\\s*,\\s*");
			this.brigade = strList[0];
			String entry="";
			if (strList.length > 6){
				entry = strList[6];
			}
			if (strList.length > 1) {
				officer = new Officer(strList[0], corp, isAllies, strList[1], this,entry);
			}
		}
		if (isCommander) {
			this.isCommander = true;
			this.currentMoveFactor = 10;
			this.brigade = " ";
			isInfantry = false;
			String strList[] = strBrigade.split("\\s*,\\s*");
			if (strList[0].equals("Kutuzov")) {
				currentMoveFactor = 3;
			}
			this.brigade = strList[0];
			String entry="";
			if (strList.length > 6){
				entry = strList[6];
			}
			if (strList.length > 1) {
				commander = new Commander(strList[0], isAllies, strList[1], this,strList[5],entry);
			}
		}
		if (!isCommander && !isOfficer){
				isInfantry = true;

				String strList[] = strBrigade.split("\\s*,\\s*");
			if (strList.length > 1) {
				setByString(strList);
			}else{
				setByXML(strList[0],xmlBrigade);
			}

		}
		arrGameCombatUnits.add(this);
		Gdx.app.log("Unit", "Constructor unit=" + this.brigade + "unitID=" + this.ID);
	}

	private void setByXML(String strBrigade, Element xmlBrigade) {
		brigade = strBrigade.toString();
		atStartMoveFactor = Integer.parseInt(xmlBrigade.getChildByName("mf").getAttribute("value"));
		currentMoveFactor = atStartMoveFactor;
		atStartAttackFactor = Integer.parseInt(xmlBrigade.getChildByName("cf").getAttribute("value"));
		currentAttackFactor =atStartAttackFactor;
		String str = xmlBrigade.getChildByName("or").getAttribute("value");
		setByValueOfString(str);
		str = xmlBrigade.getChildByName("type").getAttribute("value");
		setByValueOfString(str);
		if (xmlBrigade.getChildByName("delete") != null){
			isDelete = true;
		}
	}

	private void setByString(String[] strList) {
		brigade = strList[0].toString();
		atStartAttackFactor = Integer.parseInt(strList[1]);
		currentAttackFactor =atStartAttackFactor;
		currentMoveFactor = Integer.parseInt(strList[2]);
		atStartMoveFactor = currentMoveFactor;
		if (division.isGuard() && isInfantry){
			isGuard = true;
		}
		for (int i=3; i < strList.length;i++){
			setByValueOfString(strList[i]);
		}


	}

	private void setByValueOfString(String str) {
		switch(str){
			case "cossack":
				isCossack =true;
				isCalvary =true;
				isInfantry=false;
				break;
			case "calvary":
			case "calvry":
			case "Cavalry":
				isCalvary =true;
				isInfantry=false;
				break;
			case "infantry":
				isInfantry =true;
				if (division.isGuard()){
					isGuard = true;
				}
				break;
			case "french":
				isFrench =true;
				break;
			case "artillery":
            case "horseartllty":
			case "horseartillery":
                isArtillery = true;
				isInfantry = false;
				break;
            case "vedettes":
				isVedette = true;
				break;
			case "westphalian":
				isWestphalian = true;
				isFrench = false;
				break;
			case "wurtemburg":
				isWurttenburg = true;
				isFrench = false;
				break;
			case "polish":
				isPolish = true;
				isFrench = false;
				break;
			case "italian":
				isItalian = true;
				isFrench = false;
				break;
			case "frenchguardPolish":
			case "frenchguardDutch":
				isPolish = true;
				isFrench = false;
				break;
			case "frenchguardItalian":
				isItalianGuard = true;
				isFrench = false;
				break;
			case "frenchguard":
				isGuard = true;
				isFrench = true;
				break;
			case "bavarian":
				isBavarian = true;
				isFrench = false;
				break;
			case "D":
				isDelete = true;
				break;
			default:
				Gdx.app.log("Unit", "Select By Value Of String invalid=" + str);
				break;
		}
	}




	/**
	 *
	 */


    public static ArrayList<Unit> getOnBoardAlliedNotEliminated() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:getOnBoardAllied()){
			if (!unit.isEliminated){
				arrReturn.add(unit);
			}
		}
		return arrReturn;
    }



    /**
	 * Place a unit on the game board
	 * All logic for placement driven here
	 * Invoking routine should have verified that the unit can be placed on this hex
	 * Logged at this point for saved games
	 * @param hex
	 */
	public void placeOnBoard(Hex hex) {
		if (isOnBoard)
		{
			Gdx.app.log("Unit", "placeOnBoard on board already unit="+this.brigade+" id="+this.ID);
			int fail = 9/0;
		}
		Game.instance.addUnit(this);
		placeLogic(hex);
		// log this for saved game

	}
	public void removeFromBoard(){
		if (isOnBoard){
			hexOccupy.leaveHex(this);
			counter.getCounterStack().getStack().remove();

//			counter = null;
			isOnBoard = false;
		}
	}
	public void fadeOut(){
		counter.getCounterStack().getStack().addAction(Actions.fadeIn((.15f)));
		counter.getCounterStack().getStack().addAction(Actions.fadeOut(.75f));

	}
	public boolean isOnBoard(){
		return isOnBoard;
	}
	public void placeOnBoard() {
		if (isOnBoard || hexOccupy == null)
		{
			Gdx.app.log("Unit", "placeOnBoard error unit="+this.toString());
			int fail = 9/0;
		}
		placeLogic(hexOccupy);
	}
	public Counter getMapCounter()
	{
		return counter;
	}

	private void placeLogic(Hex hexPlace) {
		hexOccupyPrev = hexOccupy;
		hexOccupy = hexPlace;
		isOnBoard = true;
		counter = new Counter(this, Counter.TypeCounter.MapCounter);
		counter.place(hexPlace);
		hexPlace.enterHex(this);
	}

	/**
	 * DO NOT USE THIS to Move a unit
	 * @param hexTO
	 */
	public void moveForHexProcessing(Hex hexTO)
	{
		/**
		 *  check if there is a counter
		 *   for some processing
		 */
		if (counter != null) {
			counter.place(hexTO);
		}
		hexOccupyPrev = hexOccupy;
		hexOccupy = hexTO;
	}
	public static  void  removeFromBoard(Unit unit) {
		Gdx.app.log("Unit", "removeFromBoard ="+unit);
		if (unit.hexOccupy != null) {
			unit.hexOccupy.leaveHex(unit);
		}
		unit.isOnBoard = false;
		if (unit.counter != null) {
			unit.counter.remove();
		}
		unit.counter = null;
		if (unit.hexOccupy != null) {
			Counter.rePlace(unit.hexOccupy);
		}
		arrGameCombatUnits.remove(unit);

	}

	public boolean canStepLoss() {
		if (currentStep == 1 ){
			return false;
		}
		return true;
	}
	public int getCurrentStep()
	{
		return currentStep;
	}

	public void eliminate(boolean isExit){
        Gdx.app.log("Unit","eliminate ="+this);
        /**
         *  changed to remove isEliminated out of timer
         *   other program logic depends on it being set immediatly
         */
		if (!isExit) {
		}
		isEliminated = true;
		float x=0; float y=0;
		if (counter != null) {
			x = counter.getCounterStack().getStack().getX();
			y = counter.getCounterStack().getStack().getY();
		}
		Hex hex=hexOccupy;
		hex.leaveHex(this);
		removeFromBoard(this);
		if (x > 0 && y>0) {

			Counter work = new Counter(this, Counter.TypeCounter.GUICounter);
			final Stack stack = work.getCounterStack().getStack();
			stack.setPosition(x, y);
			stack.addAction(Actions.fadeOut(1.5f));
			Borodino.instance.mapStage.addActor(stack);
			Timer.schedule(new Timer.Task() {
							   @Override
							   public void run() {
								   //                              cntProcess++;
								   //                              final int copy = cntProcess;
								   stack.remove();
							   }
						   }
					, 1f                    //    (delay)
			);
		}

	}

	public void revive(){
		isEliminated =false;
	}
	public void undoSetup() {
		removeFromBoard(this);
		// log before we change hexOccupy
//		com.brnsft.yobowargames.krim.Utilities.Logger.instance.undoplaceUnit(this, hexOccupy);
		hexOccupy = hexOccupyPrev;

	}
	/**
	 * Reduce the unit by 1 step
	 */
	public void reduceStep() {
		Gdx.app.log("Unit","reduceStep="+this);
		if (counter != null) {
			getHexOccupy().moveUnitToFront(this);
			Counter.rePlace(getHexOccupy());
		}
		currentStep--;
		if (currentStep == 0){
			ErrorGame errorGame=  new ErrorGame("Trying to reduce past 1",this);
		}
		float attack = currentAttackFactor;
		currentAttackFactor = (int) ((attack/ 2) +.5);

		attack = atStartAttackFactor;
		atStartAttackFactor = (int) ((attack/ 2) +.5);
		if (currentAttackFactor < 0){
			currentAttackFactor =0;
		}
		if (atStartAttackFactor < 1){
			atStartAttackFactor =1;
		}
		if (counter != null) {
			Stack stack = counter.getCounterStack().stack;
			counter.getCounterStack().setPoints();

			final Image image = new Image(tStar);
			image.setPosition(stack.getX()+10,stack.getY()+10);
			image.addAction(Actions.fadeIn((.05f)));
			image.addAction(Actions.fadeOut(.5f));
			Borodino.instance.mapStage.addActor(image);
			Timer.schedule(new Timer.Task(){
							   @Override
							   public void run() {
								   //                              cntProcess++;
								   //                              final int copy = cntProcess;
								   image.remove();
							   }
						   }
					, 2f        			//    (delay)
			);

		}

	}

	public Hex getHexOccupy() {
		return hexOccupy;
	}


	public void setCurrentMovement(int newMove) {
		currentMoveFactor = newMove;
		if (currentMoveFactor < 0){
			currentMoveFactor = 0;
		}
	}







	/**
	 * Create xml string of units
	 * string will include
	 *
	 * ID
	 * Current Hex
	 * CurrentStep
	 *
	 * This will need to be modified
	 *
	 * @param arrUnitsIn
	 * @return
	 */
	public static String getXMLUnits(ArrayList<Unit> arrUnitsIn)
	{
		final String strUnits = "<units>";
		final String strUnitsTerm = "</units>";
		final String strUnit = "<unit>";
		final String strUnitTerm = "</unit>";
		final String strHexX = "<hexX value=\"";
		final String strHexY = "<hexY value=\"";
		final String strCanAttack = "<canattack value=\"";
		final String strAttacked = "<attacked value=\"";
		final String strBeenAttacked = "<beenattacked value=\"";
		final String strEliminated = "<eliminated value=\"";
		final String strTerm = "\"/>";
		final String strCurrentStep = "<currentStep value=\"";
		final String strCurrentMove = "<currentMove value=\"";
		final String strCurrentAttack = "<currentAttack value=\"";
		final String strCurrentDefense = "<currentDefense value=\"";
		final String strAtStartAttack = "<atstartAttack value=\"";
		final String strAtStartDefense = "<atstartDefense value=\"";
		final String strLimbered = "<limber value=\"";

		final String strMoveTurn = "<turnMoved value=\"";
		final String strOTTurn = "<turnOT value=\"";
		final String strOperationalSupply = "<supply value=\"";
		final String strThisTurnSupply = "<turnsupply value=\"";
		final String strDG = "<dg value=\"";

		final String strID = "<ID value=\"";


		StringBuffer sXML =  new StringBuffer();
		sXML.append(strUnits);
		for (Unit unit:arrUnitsIn)
		{
			sXML.append(strUnit);
			sXML.append(strID);
			sXML.append(String.format("%02d", unit.ID));
			sXML.append(strTerm);
			sXML.append(strHexX);
//			if (unit.getHexOccupy() == null){
//				unit.hexOccupy =  Hex.hexTable[0][0];
//				int i =0;
//			}
			if (unit.ID == 128){
				int b=0;
			}
			sXML.append(unit.hexOccupy.xTable);
			sXML.append(strTerm);
			sXML.append(strHexY);
			sXML.append(unit.hexOccupy.yTable);
			sXML.append(strTerm);
			sXML.append(strCurrentStep);
			sXML.append(String.format("%02d", unit.currentStep));
			sXML.append(strTerm);
			sXML.append(strCurrentMove);
			sXML.append(String.format("%02d", unit.getCurrentMovement()));
			sXML.append(strTerm);
			sXML.append(strCurrentAttack);
			sXML.append(String.format("%02d", unit.getCurrenAttackFactor()));
			sXML.append(strTerm);
			sXML.append(strCurrentDefense);
			sXML.append(String.format("%02d", unit.getCurrentDefenseFactor()));
			sXML.append(strTerm);
			sXML.append(strAtStartAttack);
			sXML.append(String.format("%02d", unit.atStartAttackFactor));
			sXML.append(strTerm);
			sXML.append(strAtStartDefense);
	//		sXML.append(String.format("%02d", unit.atStartDefenseFactor));
			sXML.append(strTerm);
			sXML.append(strMoveTurn);
			sXML.append(String.format("%02d", unit.turnMoved));
			sXML.append(strTerm);

			sXML.append(strCanAttack);
			if (unit.canAttackThisTurn) {
				sXML.append("true");
			}else{
				sXML.append("false");
			}
			sXML.append(strTerm);
			sXML.append(strAttacked);
			if (unit.hasAttackedThisTurn) {
				sXML.append("true");
			}else{
				sXML.append("false");
			}
			sXML.append(strTerm);
			sXML.append(strBeenAttacked);
			if (unit.hasBeenAttackedThisTurn) {
				sXML.append("true");
			}else{
				sXML.append("false");
			}

			sXML.append(strTerm);
			sXML.append(strEliminated);
			if (unit.isEliminated) {
				sXML.append("true");
			}else{
				sXML.append("false");
			}

			sXML.append(strTerm);

			sXML.append(strThisTurnSupply);
			if (unit.isInSupplyThisTurn) {
				sXML.append("true");
			}else{
				sXML.append("false");
			}
			sXML.append(strTerm);

			sXML.append(strDG);
			if (unit.isDisorganized) {
				sXML.append("true");
			}else{
				sXML.append("false");
			}

			sXML.append(strTerm);

			sXML.append(strUnitTerm);
		}
		sXML.append(strUnitsTerm);
		return sXML.toString();
	}

	private int getCurrentDefenseFactor() {
		return currentAttackFactor;
	}

	private int getCurrenAttackFactor() {
		return currentAttackFactor;
	}

	/**
	 * Load Unit data  that was saved with getXMLUnits
	 * @param xmlSetup - string of xml
	 */
	public static void loadUnitHex(String xmlSetup) {
		XmlReader reader = new XmlReader();
		Element root = reader.parse(xmlSetup);
		Array<Element> xmlUnitAll = root.getChildrenByName("unit");
		for (Element xmlunit: xmlUnitAll)
		{
			int ID  = Integer.parseInt(xmlunit.getChildByName("ID").getAttribute("value"));
//			Unit unit=getUnitByID(ID);
			String strHex = xmlunit.getChildByName("hex").getAttribute("value");
			Hex  hex = null;
//			unit.hexOccupy = hex;

		}
	}



	/**
	 *  KRIM
	 */
	public static void loadAllUnits() {
		arrGameOtherUnits = new ArrayList<>();
		arrGameCombatUnits = new ArrayList<>();
		/**
		 *  Load all Unit xml files for this game
		 */
		FileHandle fileHandle;
		fileHandle = Gdx.files.internal("units/russiannew.xml");
		loadNationUnits(fileHandle, false);
		fileHandle = Gdx.files.internal("units/frenchnew.xml");
		loadNationUnits(fileHandle, true);
		fileHandle = Gdx.files.internal("units/leader.xml");
		loadLeaderUnits(fileHandle, true);
	}

	private static void loadLeaderUnits(FileHandle fileHandle, boolean b) {
		XmlReader reader = new XmlReader();
		Element root = reader.parse(fileHandle);
		Array<Element> xmlLeaderAll = root.getChildrenByName("officer");
		for (Element xmlLeader : xmlLeaderAll) {
			Array<Element> xmlAllStrings = xmlLeader.getChildrenByName("string");
			for (Element xmlStrinf : xmlAllStrings) {
				String strLeader = xmlStrinf.getAttribute("value");
				String strList[] = strLeader.split(",");

				boolean isAllies;
				boolean isCommander = false;
				boolean isOfficer = false;
				if (strList[4].equals("false")) {
					isAllies = false;
				} else {
					isAllies = true;
				}
				Corp corp = null;
				if (strList[2].equals("officer")) {
					isOfficer = true;
					corp = Corp.find(strList[3], isAllies);
				}
				if (strList[2].equals("both")) {
					isOfficer = true;
					isCommander = true;
					corp = Corp.find(strList[3], isAllies);
				}
				if (strList[2].equals("commander")) {
					isCommander = true;
				}


				Unit unit = new Unit(isAllies, strLeader, corp, null, null, isCommander, isOfficer);
				//					String strList[] = strBrigade.split(",");
				//					this.isAllies = isAllies;
				//					if (strList.length >= 1){

				//};

			}
		}
	}

	private static  void loadNationUnits(FileHandle fileHandle,boolean isAllies)
	{
		XmlReader reader = new XmlReader();
		Element root = reader.parse(fileHandle);
		Array<Element> xmlCorpAll = root.getChildrenByName("corp");
		Corp corp;
		for (Element xmlcorp: xmlCorpAll)
		{
			String corpName = xmlcorp.getChildByName("name").getAttribute("value");
			String corpNum = " ";
			if (xmlcorp.hasChild("abname")) {
				corpNum = xmlcorp.getChildByName("abname").getAttribute("value");
			}
			corp = new Corp(corpNum,corpName, isAllies);
//			if (xmlcorp.hasChild("commander")){
//				String strCommander = xmlcorp.getChildByName("commander").getAttribute("value");
//				Unit unit = new Unit(isAllies, strCommander, corp, null, null, true, false);
//			}
			Array<Element> xmlDiv = xmlcorp.getChildrenByName("division");
			for (Element xmldivision: xmlDiv){
				String divName = xmldivision.getChildByName("name").getAttribute("value");
				String entry="";
				if (xmldivision.hasChild("entry")){
					entry = xmldivision.getChildByName("entry").getAttribute("value");
				}
				Division div = new Division(divName,corp,isAllies,entry);
				Array<Element> xmlBrig = xmldivision.getChildrenByName("brigade");
				for (Element xmlBrigade:xmlBrig){
					String strBrigade= xmlBrigade.getAttribute("value");
					Unit unit= new Unit(isAllies, strBrigade,corp,div,xmlBrigade, false, false);
//					String strList[] = strBrigade.split(",");
//					this.isAllies = isAllies;
//					if (strList.length >= 1){

					//};
				}
			}
		}

	}
	/**
	 * Get the Setup units
	 * This will select Units that have an entry value of zero(on Board at start)
	 * and is the the appropriate side
	 * @param wantAllied
	 * @return
	 */
	public static ArrayList<Unit> getSetupUnits(boolean wantAllied) {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.entryNum == 1 && unit.isAllies == wantAllied) // must be on map at start
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	public static ArrayList<Unit> getUnitsForTurn(int turn) {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.entryNum == turn) // must be on map at start
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	public static ArrayList<Unit> getReinforcements() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.entryNum > 1)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	/**
	 * Get all onboard allied combat units
	 * @return
	 */
	public static ArrayList<Unit> getOnBoardAllied() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.isOnBoard  && unit.isAllies&& !unit.isEliminated)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	public static ArrayList<Unit> getAllAllied() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.isAllies&& !unit.isEliminated)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	public static ArrayList<Unit> getAllRussian() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (!unit.isAllies&& !unit.isEliminated)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	/**
	 * Get all onboard allied combat units
	 * @return
	 */
	public static ArrayList<Unit> getAllied() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.isAllies) {
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	/**
	 * Get all onboard axis combat units
	 * @return
	 */

	public static ArrayList<Unit> getOnBoardAxis() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.isOnBoard  && !unit.isAllies && !unit.isEliminated)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	public static ArrayList<Unit> getAxis() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (!unit.isAllies)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	public static ArrayList<Unit> getOnBoardAllies() {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.isOnBoard  && unit.isAllies && !unit.isEliminated)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;
	}
	public static void  loadUnits(ArrayList<Unit> arrLoad){
		for (Unit unit:arrLoad){
			Hex hex = Hex.hexTable[unit.xSetup][unit.ySetup];
			unit.placeLogic(hex);
		}

	}




	public static ArrayList<Unit> getEliminated(boolean isAllies) {
		ArrayList<Unit> arrReturn = new ArrayList<>();
		for (Unit unit:arrGameCombatUnits)
		{
			if (unit.isEliminated && unit.isAllies == isAllies)
			{
				arrReturn.add(unit);
			}
		}
		return arrReturn;

	}




	public static void initCanAttack()
	{
		for (Unit unit:arrGameCombatUnits){
			unit.canAttackThisTurn = true;
//			unit.hasBeenAttackedThisTurn = false;
		}
	}

	public static void initUnShade()
	{
		for (Unit unit:arrGameCombatUnits){
			if (unit.getMapCounter() != null){
				unit.getMapCounter().getCounterStack().removeShade();
			}
		}
	}

	public static void initShade(boolean isAllies)
	{
		for (Unit unit:arrGameCombatUnits){
			if (unit.isAllies == isAllies) {
				if (unit.getMapCounter() != null) {
					unit.getMapCounter().getCounterStack().shade();
				}
			}
		}
	}

	public static void reIntializeBoard()
	{

		for (Unit unit:arrGameCombatUnits){
			unit.removeFromBoard(unit);
			unit = null;
		}
		for (Unit unit:arrGameOtherUnits){
			unit.removeFromBoard(unit);
			unit = null;
		}
		arrGameCombatUnits.clear();
		arrGameOtherUnits.clear();
		cntUnit = 0;
		Hex.loadHexes();
		/**
		 *  just in case want to play another game
		 *  set setting to vanilla not what was in the loadgame
		 *
		 */

	}


	int disorganizedAttackSave;
	int disOrganizedDefendSave;
	int disOrganizedMove;
	public void setDisorganized() {
		if (!isEliminated) {
			isDisorganized = true;
			disorganizedAttackSave = currentAttackFactor;
			currentAttackFactor = 0;
			disOrganizedMove = currentMoveFactor;
			float move = currentMoveFactor;
			isExertZOC = false;
			currentMoveFactor = (int) ((move / 2F) + .5f);
			if (getHexOccupy() != null) {
				getHexOccupy().setZOCs();
			}
			if (getMapCounter() != null) {
				getMapCounter().getCounterStack().setPoints();
			}
		}
	}

	public void setCanAttackThisTurnOff() {
		canAttackThisTurn = false;
	}

	public void setHasbeenAttackedThisTurn() {
		hasBeenAttackedThisTurn = true;
	}
	public void setHasbeenAttackedThisTurnOff() {
		hasBeenAttackedThisTurn = false;
	}
	public boolean getHasbeenAttackedThisTurn(){
		return hasBeenAttackedThisTurn;
	}
	public void setHasAttackedThisTurn() {
		hasAttackedThisTurn = true;
	}
	public void setHasAttackedThisTurnOff() {
		hasAttackedThisTurn = false;
	}
	public boolean getHasAttackedThisTurn() {
		return hasAttackedThisTurn;
	}

	public void setStep(int stepNum) {
		currentStep = stepNum;
	}


	public boolean isDisorganized() {
		return isDisorganized;
	}
	public static void resetID(){
		cntUnit = 0;
	}


	public void setAtStartAttackFactor(int attack) {
		atStartAttackFactor = attack;
	}


	public int getEntryX() {
		return xSetup;
	}

	public int getEntryY() {
		return ySetup;
	}

	public void resetAttack() {
		currentAttackFactor = atStartAttackFactor;
	}

	public void setMovedThisTurn(int movedThisTurn) {
		this.movedThisTurn = movedThisTurn;
	}
	int movedThisTurn =0;

	public int getMovedThisTurn() {
		return movedThisTurn;
	}

	public boolean isEliminated() {
		return isEliminated;
	}
	private boolean isElimited = false;

	public int getCurrentAttackFactor() {
		return currentAttackFactor;
	}

	public int getCurrentMoveFactor() {
		return currentMoveFactor;
	}

	public int getMovedLast() {
		return turnMoved;
	}

	public Corp getCorp() {
		return corp;
	}

	public Officer getOfficer() {
		return officer;
	}

	public boolean isExertZOC() {
		if (isCommander || isOfficer){
			return false;
		}
		return true;
	}

    public boolean isHasBeensActivatedThisTurn() {
        return hasBeensActivatedThisTurn;
    }

    public void setHasBeensActivatedThisTurn(boolean hasBeensActivatedThisTurn) {
        this.hasBeensActivatedThisTurn = hasBeensActivatedThisTurn;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
		if (isActivated) {
			counter.getCounterStack().activate();
		}else{
			counter.getCounterStack().removeActivate();
		}
    }

	public int getCurrentMovement() {
		return currentMoveFactor;

	}
}



