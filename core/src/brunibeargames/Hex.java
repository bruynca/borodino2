package brunibeargames;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import brunibeargames.Unit.Counter;
import brunibeargames.Unit.Division;
import brunibeargames.Unit.Unit;

public class Hex {
	public static int stackMax=6;
	//
	// these constants are map area that has playable part
	// rest of map is informational 
	//   the hex table are goes x from 0 to 76 y from 0 to 21 
	//
	protected static int xStart = 1; //
	public static int xEnd = 45;
	protected static int yStart = 1;
	public static int yEnd = 31;
	public static Hex[][] hexTable;
	public static ArrayList<Hex> arrHexMap = new ArrayList<Hex>();
	public Hex[] tableSurroundHex = new Hex[6];
	//
	//       1  2
	//     0      3
	//       5  4

	private ArrayList<Hex> arrLeft = new ArrayList();
	private ArrayList<Hex> arrRight = new ArrayList();


	public int xTable; // whete in hexTable this hex is
	public int yTable; //
	private ArrayList<Unit> arrUnitsInHex = new ArrayList<>();
	/**
     * This array tracks whether each of the 20 potential axis positions is currently occupied.
     * An axis position is considered occupied if a game object (e.g., a block, a unit) is currently
     * residing on that axis.
     *
     * - `isAxisOccupied[i] == true` indicates that the axis position `i` is occupied.
     * - `isAxisOccupied[i] == false` indicates that the axis position `i` is free.
     *
     * The index `i` ranges from 0 to 19, representing the 20 possible axis positions.
     */
    boolean[] isRussianOccupied = new boolean[20];
	boolean[] isAlliedOccupied = new boolean[20];
	private boolean isRussianEntered;
	private int[] calcMoveCost = new int[20];
	private int range;
	boolean[] isAlliedZOC = new boolean[20];
	boolean[] isRussianZOC = new boolean[20];
	private boolean hasBeenAttackedThisTurn;
	private boolean isRussianZOCOccupied;
	private boolean isAlliedZOCOccupied;

	public Hex(int xIn, int yIn) {
		xTable = xIn;
		yTable = yIn;
		hexTable[xIn][yIn] = this;
	}

	public static void initOccupied() {
	}

	public static boolean isRoadConnection(Hex startHex, Hex endHex) {

		if (arrSpecialRoadCheck.contains(startHex) || arrSpecialRoadCheck.contains(endHex)) {
			return doSpecialRoad(startHex, endHex);
		}else{
			return true;
		}


	}

	private static boolean doSpecialRoad(Hex startHex, Hex endHex) {
		for (ArrayList arr: noRoadArr){
			if (arr.contains(startHex) && arr.contains(endHex)){
				return false;
			}
		}
		return true;
	}

	static public ArrayList<Hex> arrSpecialRoadCheck = new ArrayList<>();
	static public ArrayList<Hex> arrSpecialPathCheck = new ArrayList<>();
	static public void loadSpecialRoadCheck() {
		for (int[] road : specialRoads) {
			arrSpecialRoadCheck.add(hexTable[road[0]][road[1]]);
		}
	}
	static int[][] specialRoads = {{10,27},{11,26},{41,13},{42,14},
			{43,13},{44,13}};
	public static boolean isPathConnection(Hex startHex, Hex endHex) {
		if (startHex.npath == null || endHex.npath == null) {
			 return true;
		}
		if ((startHex.npath.arrHexesNoPath.contains(endHex)) &&
			(endHex.npath.arrHexesNoPath.contains(startHex))) {
			return false;
		}
		return true;
	}


	/**
	 * Use current Hex
	 *
	 * @return vector2 pointing at bottom right point
	 */
	public Vector2 GetDisplayCoord() {
		Polygon poly = Map.GetBackPoly(this);
		float[] vertices = poly.getVertices();
		Vector2 v1 = new Vector2(vertices[10], vertices[11]);
		Vector2 v2 = Map.BackToWorld(v1);
//		v2.y -= 30;
//		v2.x += 50;
		return v2;

	}

	public Vector2 GetMidPoint() {
		Polygon poly = Map.GetBackPoly(this);
		float[] vertices = poly.getVertices();
		Vector2 v1 = new Vector2();
		v1.y = (vertices[0] + vertices[6]) / 2;
		v1.x = vertices[0];
		return v1;

	}

	public ArrayList<Unit> getUnitsInHex() {
		ArrayList<Unit> arrRetrun = new ArrayList<>();
		arrRetrun.addAll(arrUnitsInHex);
		return arrRetrun;
	}

	public Vector2 getCounterPosition() {
		Vector2 pos = GetDisplayCoord();
		pos.x +=33;
		pos.y += 14;
		return pos;
	}

	public Vector2 GetDisplayCoordHex() {
		Polygon poly = Map.GetBackPoly(this);
		float[] vertices = poly.getVertices();
		Vector2 v1 = new Vector2(vertices[2], vertices[3]);
		Vector2 v2 = Map.BackToWorld(v1);
		v2.y -= 70; //65
		v2.x -= 6; //10

		return v2;

	}

	public String toString() {
		return new String(xTable + " " + yTable);
	}

	/**
	 * Get Hex pointed to by screen location
	 *
	 * @param x
	 * @param y
	 * @return Hex
	 */
	static public Hex GetHexFromScreenPosition(float x, float y) {
		Vector3 worldCoordinates = Screen.instance.GetCamera().unproject(new Vector3(x, y, 0));
		Vector2 world = new Vector2(worldCoordinates.x, worldCoordinates.y);
		Vector2 back = Map.WorldToBack(world);
		Gdx.app.log("Hex", "Get Hex looking at x =" + back.x + " y=" + back.y);

		Vector2 pHex = Map.ConvertToHex(back);
		int xInt = (int) pHex.x;
		int yInt = (int) pHex.y;
		Hex hex = Hex.hexTable[xInt][yInt];
		return hex;
	}

	public boolean isRoad = false;
	boolean isClear = true;
	boolean isTown = false;
	boolean isPath = false;
	boolean isBridge = false;
	boolean isStreamBank = false;
	int streamBank = 0;
	int riverBank = 0;
	ArrayList<Integer> arrMultiplStreamBank = new ArrayList<>();
	boolean isMultipleStreamBank = false;
	boolean isRiverBank = false;
	boolean isForest = false;
	NoPath npath = null;
	public ArrayList<Hex> arrSurroundHex = new ArrayList<Hex>();

	//	Stream stream;
//	River river;
	public ArrayList<Hex> GetSurround() {
		ArrayList<Hex> arrReturn = new ArrayList<Hex>();
		arrReturn.addAll(arrSurroundHex);
		return arrReturn;
	}

	public ArrayList<Hex> GetSurround(int length) {
		if (length == 0) {
			return GetSurround();
		}

		ArrayList<Hex>[] arrHexList = new ArrayList[length];
		ArrayList<Hex> arrReturn = new ArrayList<Hex>();

		for (int i = 0; i < length; i++) {
			if (i == 0) {
				arrHexList[i] = GetSurround();
			} else {
				arrHexList[i] = new ArrayList<Hex>();
				for (Hex hex : arrHexList[i - 1]) {
					arrHexList[i].addAll(hex.GetSurround());
				}
			}
			arrReturn.addAll(arrHexList[i]);
		}
		AIUtil.RemoveDuplicateHex(arrReturn);
		return arrReturn;
	}


	static int[][] roads = {{0, 18}, {1, 17}, {2, 18}, {3, 17}, {4, 17}, {5, 16}, {6, 16}, {7, 16}, {8, 17},
			{9, 16}, {10, 16}, {11, 15}, {12, 15}, {13, 14}, {14, 14}, {15, 14}, {16, 14}, {17, 13},
			{18, 13}, {19, 12}, {20, 12}, {21, 11}, {22, 11}, {23, 10}, {24, 11}, {25, 10}, {26, 10},
			{27, 10}, {28, 10}, {29, 10}, {30, 11}, {31, 11}, {32, 12}, {33, 12}, {34, 12}, {35, 12},
			{36, 13}, {37, 12}, {38, 13}, {39, 12}, {40, 13}, {41, 13}, {42, 14}, {43, 13}, {44, 13},
			{44, 13}, {44, 14}, {43, 14}, {42, 15}, {41, 14}, {40, 15}, {39, 15}, {38, 16},
			{37, 15}, {36, 16}, {35, 16}, {34, 16}, {33, 16}, {32, 16}, {31, 16}, {30, 17}, {29, 17},
			{28, 18}, {27, 18}, {26, 19}, {25, 19}, {24, 20}, {23, 20}, {22, 21}, {21, 21}, {21, 22},
			{20, 23}, {19, 23}, {19, 24}, {18, 25}, {17, 24}, {16, 25}, {15, 24}, {14, 25}, {13, 25},
			{12, 26}, {11, 26}, {11, 27}, {10, 27}, {9, 27}, {8, 27}, {7, 27}, {6, 28}, {5, 28},
			{4, 29}, {3, 28},
			{2, 29}, {1, 28}, {0, 29}};
	static int[][] path1 = {{0, 23}, {1, 23}, {2, 24}, {3, 23}, {4, 23}, {5, 22}, {6, 22}, {7, 22}, {8, 23}, {8, 24}, {8, 25}, {8, 26},
			{7, 21}, {8, 22}, {9, 21}, {10, 21}, {11, 20}, {12, 21}, {12, 22}, {13, 22}, {13, 23}, {14, 24}, {15, 24}, {16, 25},
			{16, 26}, {15, 23}, {15, 22}, {15, 21}, {16, 21}, {16, 20}, {16, 19}, {15, 19}, {14, 19}, {13, 19}, {12, 20}, {10, 20},
			{10, 19}, {9, 18}, {9, 17}, {8, 17}, {17, 18}, {16, 18}, {16, 17}, {15, 16}, {14, 16}, {13, 15}, {12, 15}, {12, 14},
			{11, 13}, {10, 14}, {9, 13}, {8, 13}, {7, 12}, {6, 12}, {7, 11}, {8, 11}, {9, 10}, {9, 9}, {9, 8}, {10, 9},
			{8, 8}, {7, 8}, {6, 9}, {5, 9}, {4, 10}, {3, 9}, {2, 9}, {1, 9}, {0, 10}, {11, 9}, {11, 10}, {12, 11},
			{13, 11}, {14, 12}, {15, 12}, {15, 13}, {16, 14}, {16, 13}, {16, 12}, {16, 11}, {17, 10}, {18, 11}, {19, 11}, {20, 12},
			{20, 13}, {20, 14}, {20, 15}, {20, 16}, {19, 16}, {20, 17}, {21, 17}, {22, 17}, {23, 17}, {24, 18}, {25, 18}, {25, 19},
			{25, 20}, {24, 21}, {24, 22}, {24, 23}, {25, 23}, {24, 24}, {23, 24}, {22, 24}, {21, 24}, {20, 24}, {28, 26}, {28, 25},
			{27, 24}, {26, 24}, {26, 23}, {27, 22}, {28, 23}, {28, 22}, {29, 23}, {30, 24}, {31, 23}, {32, 24}, {33, 24}, {34, 25},
			{34, 26}, {34, 24}, {35, 23}, {36, 23}, {37, 23}, {38, 23}, {39, 22}, {40, 22}, {41, 21}, {42, 22}, {41, 22}, {40, 23},
			{39, 23}, {38, 24}, {38, 25}, {38, 26}, {40, 21}, {40, 20}, {39, 19}, {39, 18}, {39, 17}, {39, 16}, {38, 16}, {32, 16},
			{33, 16}, {33, 17}, {33, 18}, {33, 19}, {34, 20}, {35, 20}, {35, 21}, {35, 22}, {28, 21}, {29, 20}, {30, 20}, {30, 19},
			{30, 18}, {30, 17}, {31, 16}, {32, 16}, {31, 15}, {30, 16}, {29, 16}, {28, 16}, {27, 15}, {26, 15}, {25, 14}, {24, 15},
			{24, 16}, {24, 17}, {25, 17}, {23, 14}, {22, 14}, {21, 14}, {23, 13}, {22, 13}, {21, 12}, {21, 11}, {17, 9}, {18, 9},
			{18, 8}, {19, 7}, {20, 7}, {19, 6}, {19, 5}, {18, 5}, {17, 4}, {16, 5}, {15, 5}, {14, 5}, {15, 4}, {16, 4},
			{13, 5}, {12, 6}, {11, 6}, {11, 7}, {10, 8}, {44, 13}, {44, 12}, {44, 11}, {44, 10}, {44, 9}, {44, 8}, {43, 7},
			{42, 7}, {42, 6}, {42, 5}, {41, 4}, {40, 5}, {39, 4}, {38, 4}, {43, 13}, {42, 13}, {42, 12}, {41, 11}, {40, 11},
			{39, 10}, {38, 10}, {37, 9}, {36, 9}, {36, 10}, {37, 10}, {37, 11}, {37, 12}, {35, 8}, {34, 9}, {33, 9}, {32, 9},
			{31, 9}, {30, 10}, {29, 10}, {29, 9}, {28, 9}, {27, 8}, {27, 9}, {26, 8}, {26, 7}, {28, 8}, {29, 7}, {30, 7},
			{30, 6}, {31, 5}, {32, 5}, {31, 4}, {30, 4}, {32, 4}, {25, 7}, {25, 6}, {24, 8}, {24, 9}, {23, 9}, {23, 10},
			{30, 6}, {32, 3}, {32, 2}, {31, 1}, {30, 1}, {29, 0}, {28, 1}, {27, 1}, {27, 2}, {28, 3}, {29, 3}, {36, 3},
			{37, 3}, {36, 4}, {36, 5}, {36, 6}, {24, 3}, {25, 3}, {27, 14},
			{36, 7}, {36, 8}, {18, 0}, {18, 1}, {17, 1}, {16, 2}, {26, 6},
			{15, 2}, {15, 3}, {15, 4}, {31, 14}, {31, 13}, {26, 3}, {26, 4}, {26, 5},
			{34, 25}, {19, 30}, {20, 30}, {21, 29}, {22, 30}, {23, 29}, {24, 29}, {25, 29},
			{26, 29}, {27, 28}, {28, 28}, {29, 27}, {30, 27}, {29, 26}, {31, 27}, {32, 28},
			{33, 27}, {34, 28}, {34, 27}, {35, 27}, {36, 27}, {37, 26}, {38, 27}, {39, 27}, {40, 28},
			{41, 28}, {42, 28}, {43, 28}, {44, 28}, {19, 29}, {19, 28},
			{23, 02}, {22, 03}, {21, 03}, {20, 03}, {19, 03}, {18, 03}, {17, 03}, {18, 28}, {17, 27}, {16, 27}};
	static int[][] stream1A = {{01, 9}, {02, 10}, {03, 9}, {04, 10}, {05, 9}, {06, 10}, {07, 10}, {8, 11}, {9, 11},
			{10, 12}, {11, 12}, {12, 12}, {13, 12}, {13, 13}, {13, 14}, {14, 15}, {15, 15},{16,16},{17,16},{18,17}
	};
	static int[][] stream1B = {{01, 10}, {02, 11}, {03, 10}, {04, 11}, {05, 10}, {06, 11}, {07, 11}, {8, 12}, {9, 12}, {10, 13},
			{11, 13}, {12, 13}, {12, 14}, {12, 15}, {13, 15}, {14, 16},{15,16},{16,17},{17,17}};
	static int[][] stream2a = {{33, 8}, {27, 02}, {26, 03}, {26, 04}, {26, 05}, {27, 05}, {27, 06}, {27, 07}
			, {26, 06}, {25, 06}, {25, 07}, {25, 8}, {24, 9}, {00, 00}, {23, 9}, {23, 10}, {22, 11},
	};
	static int[][] stream2b = {{28, 03}, {27, 03}, {27, 04}, {00, 00}, {28, 06}, {28, 07}, {28, 8}, {27, 8}, {26, 8}, {26, 07}, {26, 9}, {25, 9}, {24, 10}, {24, 11}, {23, 11},{22,12}};
	static int[][] stream3a = {{23, 11}, {24, 11}, {25, 10}, {26, 10}, {27, 10}, {28, 11}, {29, 10}, {0, 0}, {30, 11}, {31, 11}, {32, 12}, {33, 12}, {33, 13}, {32, 14}};
	static int[][] stream3b = {{22, 12}, {23, 12}, {24, 12}, {25, 11}, {26, 11}, {27, 11}, {28, 12}, {29, 11}, {30, 12}, {31, 12}, {32, 13}};
	static int[][] stream4a = {{28, 12}, {28, 13}};
	static int[][] stream4b = {{27, 11}, {27, 12}, {27, 13}};
	static int[][] stream5a = {{22, 12}, {22, 13}, {23, 13}, {24, 14}, {25, 14}};
	static int[][] stream5b = {{21, 12}, {21, 13}, {22, 14}, {23, 14}, {24, 15}};
	static int[][] stream6a = {{0, 22}, {1, 21}, {2, 22}, {3, 21}, {4, 21}, {5, 20}, {6, 20}, {7, 20}, {8, 20}, {9, 19}, {10, 19}, {11, 18}, {11, 17}, {12, 17}, {13, 16},
			{14, 16}, {15, 15}, {15, 14}, {16, 14}, {17, 14}, {18, 14}, {18, 13}, {19, 12}, {20, 12}, {21, 11}};
	static int[][] stream6B = {{0, 23}, {1, 22}, {2, 23}, {3, 22}, {4, 22}, {5, 21}, {6, 21}, {7, 21}, {8, 21}, {9, 20}, {10, 20}, {11, 19}, {12, 19}, {12, 18}, {13, 17}, {14, 17}, {15, 16}, {16, 16}, {16, 15}, {17, 15}, {18, 15}, {19, 14}, {19, 13}, {20, 13}, {21, 12}, {22, 12}};
	static int[][] stream7a = {{0, 15}, {1, 15}, {2, 16}, {3, 16}, {4, 16}, {5, 16}, {6, 17}, {7, 16}, {8, 17}, {9, 17}, {9, 18}, {10, 19},
	};
	static int[][] stream7b = {{0, 16}, {1, 16}, {2, 17}, {3, 17}, {4, 17}, {5, 17}, {6, 18}, {7, 17}, {8, 18}, {8, 19}, {9, 19}};
	static int[][] stream8a = {{0, 4}, {1, 3}, {2, 4}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 2}, {8, 3}, {9, 2}, {10, 3}, {11, 2}, {12, 2}, {12, 1}, {13, 1}, {13, 2}
			, {13, 3}, {13, 4}, {13, 5}, {14, 6}, {15, 6}, {16, 7}, {17, 7}, {18, 8}, {19, 8}, {20, 8}, {20, 9}, {20, 10}, {21, 10}, {21, 11},
	};
	static int[][] stream8b = {{0, 3}, {1, 2}, {2, 3}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, {7, 1}, {8, 2}, {9, 1}, {10, 2}, {11, 1}, {11, 0}, {12, 0}, {13, 0}, {14, 1}, {14, 2}, {14, 3}, {14, 4}, {14, 5}
			, {15, 5}, {16, 6}, {17, 6}, {18, 7}, {19, 7}, {20, 7}, {21, 7}, {21, 8}, {21, 9}, {22, 10}, {22, 11},};
	static int[][] stream9a = {{10, 21},{10,20},{11,19}, {11, 21}, {11, 22}, {12, 23}, {12, 24}, {13, 24}, {14, 25},
	};
	static int[][] stream9b = {{12,19},{12,20},{11, 20}, {12, 21}, {12, 22}, {13, 22}, {13, 23}, {14, 24},};
	static int[][] river1a = {{25, 0}, {24, 1}, {24, 2}, {25, 2}, {26, 3}, {27, 2}, {28, 2}, {29, 1}, {30, 2}, {30, 3}, {31, 3}, {31, 4}, {32, 5}, {33, 4}, {34, 4}, {35, 3}, {33, 5}, {33, 6}, {34, 7}, {35, 7}, {36, 7}, {37, 6}, {37, 7}, {37, 8}, {38, 9}, {39, 8}, {39, 7}, {39, 6}, {40, 8}, {41, 8}, {42, 9}, {43, 9}, {44, 9}, {44, 8}, {44, 7},
	};
	static int[][] river1b = {{27, 0}, {26, 0},{25,1}, {26, 1}, {26, 2}, {27, 1}, {28, 1}, {29, 0}, {30, 1}, {31, 1}, {0, 0}, {31, 2}, {32, 3}, {32, 4}, {33, 3}, {34, 3}, {35, 2}, {0, 0}, {36, 3}, {36, 4}, {35, 4}, {34, 5}, {34, 6}, {35, 6}, {36, 6}, {37, 5}, {38, 6}, {38, 7}, {38, 8}, {39, 5}, {40, 5}, {40, 6}
			, {40, 7}, {41, 7}, {42, 8}, {43, 8}, {43, 7}, {43, 6}, {44, 6}, {0, 0}
	};

	static int[][][] nopath =
			{{{36, 8}, {36, 9}}, {{36, 10}, {37, 9}}, {{30, 10}, {29, 9}}, {{28, 8}, {28, 9}}, {{28, 9}, {27, 9}}, {{26, 07}, {26, 06}}, {{25, 07}, {26, 07}}, {{26, 04}, {25, 03}},
					{{31, 05}, {31, 04}}, {{31, 04}, {32, 04}}, {{15, 05}, {15, 04}}, {{16, 05}, {16, 04}}, {{19, 07}, {19, 06}},
					{{16, 13}, {15, 13}}, {{16, 13}, {15, 12}}, {{10, 9}, {10, 8}}, {{9, 9}, {10, 9}},
					{{07, 12}, {07, 11}}, {{07, 22}, {07, 21}}, {{07, 22}, {8, 22}}, {{10, 21}, {10, 20}}, {{12, 21}, {12, 20}}
					, {{15, 23}, {14, 24}}, {{16, 19}, {16, 18}}, {{24, 18}, {25, 17}}, {{24, 17}, {24, 18}},
					{{24, 24}, {24, 23}}, {{26, 24}, {26, 23}}, {{28, 23}, {28, 22}}, {{29, 27}, {29, 26}}, {{33, 27}, {34, 27}},
					{{34, 25}, {34, 24}}, {{35, 27}, {34, 27}}, {{35, 22}, {36, 23}}, {{38, 27}, {38, 26}},
					{{38, 24}, {38, 23}}, {{39, 22}, {39, 23}}, {{40, 22}, {40, 23}}, {{40, 21}, {40, 22}},
					{{41, 21}, {41, 22}}, {{41, 22}, {40, 22}}, {{20, 16}, {20, 17}}, {{20, 14}, {21, 14}},
					{{28, 16}, {28, 15}}, {{31, 16}, {30, 16}}, {{30, 16}, {30, 17}}, {{30, 14}, {31, 14}},
					{{23, 14}, {23, 13}}, {{36, 04}, {37, 03}}, {{15, 03}, {16, 04}}, {{20, 13}, {21, 12}},{{15,12},{16,12}}};
	static int[][][] noRoad =
			{{{10, 27}, {11, 26}}, {{41, 13}, {41, 14}}, {{41, 13}, {41,14}},
			{{42, 14}, {41,14}}, {{42,14}, {42,15}}};

	static int[][] forest = {{9, 18}, {10, 5}, {8, 4}, {0, 0}, {1, 0}, {0, 1}, {2, 0}, {3, 0}, {4, 0},
			{5, 0}, {6, 0}, {7, 0}, {8, 0}, {7, 1}, {7, 2}, {8, 3}, {9, 3},
			{9, 2}, {10, 3}, {11, 2}, {12, 2}, {13, 2}, {12, 3}, {13, 3}, {12, 4},
			{11, 3}, {11, 4}, {10, 4}, {11, 5}, {12, 6}, {12, 7}, {11, 6}, {10, 6},
			{9, 6}, {9, 5}, {9, 4}, {8, 5}, {7, 4}, {7, 3}, {6, 4}, {6, 5},
			{5, 4}, {4, 4}, {3, 3}, {2, 3}, {1, 2}, {0, 3}, {0, 4}, {1, 3},
			{2, 4}, {0, 5}, {1, 4}, {2, 5}, {3, 4}, {4, 5}, {0, 6}, {1, 5},
			{2, 6}, {3, 5}, {4, 6}, {5, 5}, {6, 6}, {7, 5}, {8, 6}, {7, 6},
			{6, 7}, {5, 6}, {5, 7}, {4, 7}, {3, 6}, {2, 7}, {1, 6}, {0, 7},
			{0, 8}, {0, 9}, {0, 10}, {0, 11}, {1, 10}, {1, 11}, {0, 12}, {0, 13},
			{1, 12}, {1, 13}, {2, 15}, {2, 14}, {2, 13}, {2, 12}, {2, 11}, {3, 10},
			{3, 11}, {4, 11}, {5, 10}, {5, 11}, {4, 12}, {3, 12}, {3, 13}, {2, 15},
			{3, 14}, {4, 15}, {4, 14}, {5, 14}, {6, 14}, {6, 15}, {7, 15}, {7, 14},
			{7, 13}, {6, 13}, {8, 13}, {8, 14}, {8, 15}, {9, 15}, {9, 14}, {8, 12},
			{8, 11}, {9, 10}, {8, 10}, {7, 9}, {9, 9}, {10, 9}, {10, 8}, {10, 7},
			{11, 7}, {11, 8}, {11, 9}, {11, 10}, {11, 11}, {10, 12}, {10, 11}, {10, 10},
			{12, 11}, {13, 11}, {13, 10}, {13, 9}, {12, 10}, {12, 9}, {12, 8}, {13, 7},
			{14, 7}, {14, 8}, {15, 7}, {16, 7}, {16, 5}, {17, 7}, {17, 8}, {16, 8},
			{16, 9}, {15, 8}, {15, 9}, {14, 9}, {14, 10}, {15, 10}, {16, 11}, {15, 11},
			{14, 11}, {16, 12}, {17, 11}, {18, 12}, {17, 12}, {16, 6}, {16, 4}, {16, 3},
			{17, 2}, {18, 2}, {19, 2}, {20, 3}, {19, 3}, {18, 3}, {18, 4}, {18, 5},
			{21, 3}, {22, 3}, {23, 2}, {23, 1}, {24, 2}, {24, 1}, {24, 0}, {26, 1},
			{27, 0}, {0, 0}, {28, 6}, {29, 5}, {28, 5}, {29, 4}, {28, 4}, {29, 3},
			{0, 0}, {32, 0}, {32, 1}, {33, 1}, {33, 0}, {0, 0}, {34, 0}, {34, 1},
			{35, 0}, {35, 1}, {36, 1}, {36, 2}, {36, 0}, {37, 0}, {37, 1}, {37, 2},
			{38, 2}, {38, 1}, {38, 0}, {39, 0}, {40, 0}, {40, 1}, {40, 2}, {39, 2},
			{39, 3}, {38, 3}, {41, 0}, {42, 1}, {43, 0}, {42, 0}, {44, 0}, {44, 1},
			{44, 2}, {44, 3}, {44, 4}, {43, 3}, {43, 2}, {43, 1}, {42, 2}, {42, 3},
			{42, 4}, {41, 3}, {40, 4}, {40, 3}, {39, 4}, {39, 5}, {37, 3}, {36, 4},
			{36, 5}, {30, 8}, {31, 8}, {31, 9}, {30, 9}, {29, 8}, {28, 9}, {29, 9},
			{30, 10}, {31, 10}, {32, 11}, {35, 10}, {35, 11}, {36, 11}, {36, 12}, {37, 10},
			{37, 11}, {37, 12}, {38, 12}, {38, 11}, {39, 11}, {40, 12}, {41, 12}, {42, 12},
			{41, 11}, {40, 11}, {41, 10}, {41, 9}, {42, 10}, {42, 14}, {43, 15}, {43, 16},
			{43, 17}, {42, 17}, {42, 16}, {42, 15}, {41, 14}, {41, 15}, {41, 16}, {41, 17},
			{40, 18}, {40, 17}, {40, 16}, {40, 15}, {39, 15}, {39, 16}, {39, 17}, {39, 18},
			{38, 15}, {38, 16}, {38, 17}, {38, 18}, {38, 19}, {38, 20}, {38, 21}, {39, 20},
			{39, 19}, {37, 14}, {37, 15}, {37, 16}, {37, 17}, {37, 18}, {37, 19}, {37, 20},
			{37, 21}, {36, 14}, {36, 15}, {36, 16}, {36, 17}, {36, 18}, {36, 19}, {36, 20},
			{36, 21}, {35, 14}, {35, 15}, {35, 16}, {35, 17}, {35, 18}, {35, 19}, {35, 21},
			{34, 16}, {34, 17}, {34, 18}, {34, 19}, {33, 16}, {33, 17}, {33, 18}, {33, 19},
			{33, 20}, {34, 21}, {33, 21}, {32, 17}, {32, 18}, {32, 14}, {32, 15}, {33, 14},
			{33, 13}, {32, 19}, {32, 20}, {32, 21}, {31, 17}, {31, 18}, {31, 19}, {31, 20},
			{31, 21}, {30, 18}, {30, 19}, {30, 20}, {30, 21}, {29, 18}, {29, 19}, {29, 20},
			{29, 21}, {28, 20}, {28, 21}, {30, 16}, {30, 15}, {30, 14}, {29, 14}, {28, 14},
			{28, 15}, {29, 15}, {30, 16}, {28, 16}, {27, 21}, {25, 21}, {28, 18}, {27, 17},
			{26, 17}, {25, 16}, {25, 17}, {26, 18}, {24, 18}, {24, 17}, {23, 18}, {23, 19},
			{22, 19}, {22, 20}, {22, 21}, {21, 15}, {21, 14}, {21, 13}, {21, 12}, {21, 21},
			{21, 20}, {21, 19}, {21, 18}, {20, 19}, {20, 20}, {20, 21}, {0, 0}, {19, 19},
			{19, 20}, {19, 21}, {18, 19}, {18, 20}, {18, 21}, {17, 19}, {17, 20}, {17, 21},
			{16, 19}, {16, 20}, {15, 17}, {14, 17}, {13, 17}, {14, 18}, {15, 18}, {15, 19},
			{14, 19}, {13, 18}, {14, 20}, {15, 20}, {14, 21}, {13, 21}, {0, 20}, {0, 21},
			{1, 20}, {1, 19}, {2, 20}, {2, 19}, {3, 18}, {3, 19}, {4, 19}, {4, 20},
			{5, 18}, {5, 19}, {6, 20}, {6, 19}, {7, 18}, {7, 19}, {18, 21}, {18, 21},
			{0, 25}, {0, 26}, {0, 27}, {0, 28}, {1, 25}, {1, 26}, {1, 27}, {2, 26},
			{2, 27}, {3, 27}, {3, 26}, {3, 25}, {4, 25}, {4, 26}, {4, 27}, {5, 26},
			{5, 25}, {5, 24}, {6, 24}, {7, 24}, {7, 25}, {6, 25}, {6, 26}, {6, 27},
			{1, 30}, {2, 30}, {3, 29}, {3, 30}, {4, 30}, {5, 29}, {5, 30}, {6, 29},
			{6, 30}, {7, 29}, {7, 30}, {8, 30}, {9, 30}, {9, 29}, {9, 28}, {8, 29},
			{10, 30}, {10, 29}, {10, 28}, {9, 27}, {9, 26}, {9, 25}, {9, 24}, {9, 23},
			{9, 22}, {10, 22}, {11, 22}, {12, 23}, {12, 22}, {13, 22}, {13, 23}, {14, 22},
			{11, 23}, {11, 24}, {10, 25}, {11, 25}, {10, 26}, {11, 26}, {10, 27}, {11, 27},
			{11, 28}, {11, 29}, {12, 30}, {12, 29}, {12, 28}, {12, 27}, {12, 26}, {12, 25},
			{12, 24}, {13, 24}, {13, 25}, {13, 26}, {13, 27}, {13, 28}, {14, 28}, {14, 27},
			{15, 27}, {15, 26}, {16, 26}, {17, 26}, {17, 25}, {17, 22}, {18, 22}, {18, 23},
			{18, 24}, {18, 25}, {18, 26}, {18, 27}, {19, 22}, {19, 23}, {19, 24}, {19, 25},
			{19, 26}, {19, 27}, {20, 22}, {20, 23}, {20, 24}, {20, 25}, {20, 26}, {20, 27},
			{21, 28}, {21, 27}, {21, 26}, {21, 25}, {21, 24}, {21, 23}, {21, 22}, {22, 22},
			{22, 23}, {22, 24}, {22, 25}, {22, 26}, {22, 27}, {22, 28}, {23, 26}, {23, 25},
			{23, 24}, {24, 22}, {26, 22}, {28, 22}, {27, 24}, {28, 24}, {28, 25}, {27, 27},
			{28, 28}, {28, 29}, {29, 29}, {29, 28}, {30, 29}, {29, 27}, {29, 26}, {29, 25},
			{29, 24}, {29, 23}, {30, 24}, {30, 25}, {30, 26}, {30, 27}, {30, 28}, {31, 28},
			{31, 27}, {31, 26}, {31, 25}, {31, 24}, {32, 26}, {32, 27}, {32, 28}, {33, 26},
			{30, 23}, {30, 22}, {29, 22}, {32, 23}, {31, 22}, {32, 22}, {31, 23}, {33, 22},
			{34, 22}, {38, 22}, {38, 23}, {37, 23}, {38, 24}, {38, 25}, {36, 25}, {37, 25},
			{38, 26}, {39, 25}, {37, 26}, {37, 27}, {36, 27}, {36, 26}, {36, 25}, {41, 23},
			{41, 24}, {42, 24}, {43, 23}, {43, 24}, {44, 25}, {44, 24}, {44, 23}, {0, 0},
			{42, 29}, {42, 30}, {41, 29}, {40, 30}, {40, 29}, {39, 28}, {39, 29}, {38, 30},
			{38, 29}, {37, 29}, {14, 25}, {41, 2}, {19, 18}, {41, 1}, {39, 1}, {13, 8}, {9, 11}};
	static int[][] town = {{2, 9}, {6, 12}, {32, 12}, {9, 8}, {14, 5}, {20, 7}, {25, 6}, {32, 5}, {36, 9}, {38, 6}, {36, 3}, {32, 3}, {43, 7}, {26, 3}, {44, 14},
			{42, 22}, {39, 27}, {34, 28}, {35, 23}, {25, 23}, {25, 19}, {25, 14}, {27, 10}, {23, 10}, {22, 11}
			, {17, 18}, {15, 16}, {16, 14}, {8, 17}, {4, 17}, {0, 18}, {6, 22}, {5, 28}, {0, 29}
			, {11, 20}, {15, 24}};
	static int[][] bridges = {{13, 5}, {14, 5}, {7, 11}, {8, 11}, {12, 15}, {13, 14}, {4, 17}, {5, 16}, {10, 19}, {10, 20}, {14, 16},
			{15, 16}, {21, 11}, {22, 11}, {22, 13}, {21, 12}, {32, 13}, {33, 12}, {33, 12}, {36, 7}, {36, 6},
			{32, 4}, {32, 5}, {27, 2}, {27, 1}, {44, 8}, {43, 7}, {12, 15},
			{18, 8}, {19, 7}, {10, 21}, {11, 20}, {25, 6}, {23, 10}, {25, 7}, {24, 11},
			{32, 13}, {26, 7}, {24, 15}, {25, 14}, {20, 12}, {20, 13}};

	public static void loadHexes() {
		hexTable = new Hex[xEnd][yEnd];
		for (int x = 0; x < xEnd; x++) {
			for (int y = 0; y < yEnd; y++) {
				hexTable[x][y] = new Hex(x, y);
				arrHexMap.add(hexTable[x][y]);
			}
		}
		LoadSurround();  // need surround for river and stream processing

		LoadRoads();
		LoadPaths();
		LoadTowns();
		LoadForest();
		LoadRiversStream();
		LoadBridges();

	}


	private static void LoadSurround() {
		for (int x=0; x< xEnd; x++)
		{
			for (int y=0; y < yEnd; y++)
			{
				Hex hex = hexTable[x][y];
				hex.arrSurroundHex = new ArrayList<>();
				for (Hex hexs:HexHandler.getSurround(hex)){
					if (hexs == null){
						int b=0;
					}else {
						hex.arrSurroundHex.add(hexs);
					}

				}
			}
		}
	}
	static ArrayList<Hex> arrStream10 = new ArrayList<>();
	static ArrayList<Hex> arrStream11 = new ArrayList<>();
	static ArrayList<Hex> arrStream20 = new ArrayList<>();
	static ArrayList<Hex> arrStream21 = new ArrayList<>();
	static ArrayList<Hex> arrStream30 = new ArrayList<>();
	static 	ArrayList<Hex> arrStream31 = new ArrayList<>();
	static ArrayList<Hex> arrStream40 = new ArrayList<>();
	static ArrayList<Hex> arrStream41 = new ArrayList<>();
	static ArrayList<Hex> arrStream50 = new ArrayList<>();
	static ArrayList<Hex> arrStream51 = new ArrayList<>();
	static ArrayList<Hex> arrStream60 = new ArrayList<>();
	static ArrayList<Hex> arrStream61 = new ArrayList<>();
	static ArrayList<Hex> arrStream70 = new ArrayList<>();
	static ArrayList<Hex> arrStream71 = new ArrayList<>();
	static ArrayList<Hex> arrStream80 = new ArrayList<>();
	static ArrayList<Hex> arrStream81 = new ArrayList<>();
	static ArrayList<Hex> arrStream90 = new ArrayList<>();
	static ArrayList<Hex> arrStream91 = new ArrayList<>();
	static ArrayList<Hex> arrRiver10 = new ArrayList<>();
	static ArrayList<Hex> arrRiver11 = new ArrayList<>();

	private static void LoadRiversStream() {
		for (int i = 0; i < stream1A.length; i++) {
			int x = stream1A[i][0];
			int y = stream1A[i][1];
			Hex hex = hexTable[x][y];
			hex.isStreamBank = true;
			hex.streamBank = 10;
			hex.arrMultiplStreamBank.add(10);
			arrStream10.add(hex);
		}
		for (int i = 0; i < stream1B.length; i++) {
			int x = stream1B[i][0];
			int y = stream1B[i][1];
			Hex hex = hexTable[x][y];
			hex.isStreamBank = true;
			hex.streamBank = 11;
			hex.arrMultiplStreamBank.add(11);
			arrStream11.add(hex);
		}

		for (int i = 0; i < stream2a.length; i++) {
			int x = stream2a[i][0];
			int y = stream2a[i][1];
			Hex hex = hexTable[x][y];
			arrStream20.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(20);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 20;
			}
		}
		for (int i = 0; i < stream2b.length; i++) {
			int x = stream2b[i][0];
			int y = stream2b[i][1];
			Hex hex = hexTable[x][y];
			arrStream21.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(21);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 21;
			}

		}
		for (int i = 0; i < stream3a.length; i++) {
			int x = stream3a[i][0];
			int y = stream3a[i][1];
			Hex hex = hexTable[x][y];
			arrStream30.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(30);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 30;

			}
		}
		for (int i = 0; i < stream3b.length; i++) {
			int x = stream3b[i][0];
			int y = stream3b[i][1];
			Hex hex = hexTable[x][y];
			arrStream31.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(31);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 31;
			}

		}
		for (int i = 0; i < stream4a.length; i++) {
			int x = stream4a[i][0];
			int y = stream4a[i][1];
			Hex hex = hexTable[x][y];
			arrStream40.add(hex);

			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(40);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 40;
			}

		}
		for (int i = 0; i < stream4b.length; i++) {
			int x = stream4b[i][0];
			int y = stream4b[i][1];
			if (x==27 && y ==11){
				int b=0;
			}

			Hex hex = hexTable[x][y];
			arrStream41.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(41);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 41;
			}
		}
		for (int i = 0; i < stream5a.length; i++) {
			int x = stream5a[i][0];
			int y = stream5a[i][1];
			Hex hex = hexTable[x][y];
			arrStream50.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(50);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 50;
			}

		}
		for (int i = 0; i < stream5b.length; i++) {
			int x = stream5b[i][0];
			int y = stream5b[i][1];
			Hex hex = hexTable[x][y];
			arrStream51.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(51);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 51;
			}
		}
		for (int i = 0; i < stream6a.length; i++) {
			int x = stream6a[i][0];
			int y = stream6a[i][1];
			Hex hex = hexTable[x][y];
			arrStream60.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(60);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 60;
			}

		}

		for (int i = 0; i < stream6B.length; i++) {
			int x = stream6B[i][0];
			int y = stream6B[i][1];
			Hex hex = hexTable[x][y];
			arrStream61.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(61);
				hex.isMultipleStreamBank = true;
			}else {
				hex.streamBank = 61;

				hex.isStreamBank = true;
			}

		}
		for (int i = 0; i < stream7a.length; i++) {
			int x = stream7a[i][0];
			int y = stream7a[i][1];
			Hex hex = hexTable[x][y];
				arrStream70.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(70);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 70;
			}
		}
		for (int i = 0; i < stream7b.length; i++) {
			int x = stream7b[i][0];
			int y = stream7b[i][1];
			Hex hex = hexTable[x][y];
			arrStream71.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(71);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 71;
			}
		}
		for (int i = 0; i < stream8a.length; i++) {
			int x = stream8a[i][0];
			int y = stream8a[i][1];
			Hex hex = hexTable[x][y];
			arrStream80.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(80);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 80;
			}
		}
		for (int i = 0; i < stream8b.length; i++) {
			int x = stream8b[i][0];
			int y = stream8b[i][1];
			Hex hex = hexTable[x][y];
			arrStream81.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(81);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 81;
			}
		}
		for (int i = 0; i < stream9a.length; i++) {
			int x = stream9a[i][0];
			int y = stream9a[i][1];
			Hex hex = hexTable[x][y];
			arrStream90.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(90);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 90;
			}
		}
		for (int i = 0; i < stream9b.length; i++) {
			int x = stream9b[i][0];
			int y = stream9b[i][1];
			Hex hex = hexTable[x][y];
			arrStream91.add(hex);
			if (hex.isStreamBank){
				hex.arrMultiplStreamBank.add(91);
				hex.isMultipleStreamBank = true;
			}else {
				hex.isStreamBank = true;
				hex.streamBank = 91;
			}
		}

		for (int i = 0; i < river1a.length; i++) {
			int x = river1a[i][0];
			int y = river1a[i][1];
			Hex hex = hexTable[x][y];
			hex.isRiverBank = true;
			hex.riverBank = 10;
			arrRiver10.add(hex);
		}
		for (int i = 0; i < river1b.length; i++) {
			int x = river1b[i][0];
			int y = river1b[i][1];
			Hex hex = hexTable[x][y];
			hex.isRiverBank = true;
			hex.riverBank =11;
			arrRiver11.add(hex);

		}


	}
	public static boolean isAcrossRiver(Hex hexFrom, Hex hexTo){
		if (hexTo.isRiverBank && hexFrom.isRiverBank){
			//
		}else{
			return false;
		}
		if (hexTo.riverBank != hexFrom.riverBank){
			return true;
		}else{
			return false;
		}
	}
	public static ArrayList<Hex> showAllRiverCrossings(Hex hex){
		ArrayList<Hex> arrReturn = new ArrayList<>();
		if (!hex.isRiverBank){
			return arrReturn;

		}
		ArrayList<Hex> arrWork = new ArrayList<>();
		arrWork.addAll(hex.getSurround());
		for (Hex hex2:arrWork){
			if (isAcrossRiver(hex, hex2)){
				arrReturn.add(hex2);
			}
		}
		return arrReturn;
	}

	/**
	 *  check if there is a stream from this hex to passed hex
	 * @param hexTo
	 * @return
	 */
	public static boolean isStreamAcross(Hex hexfrom, Hex hexTo){
		//Gdx.app.log("Hex isstreamaccros", "From=" + hexfrom + " To="+hexTo);


		if (hexfrom.isStreamBank && hexTo.isStreamBank ){
			//return false;
		}else{
		//	Gdx.app.log("Hex isstreamaccros", "Return no Bank");

			return false;
		}
		ArrayList<Hex> arrToTest = new ArrayList<>();
		arrToTest.addAll(getStreamBanksToCheck(hexfrom));
		//Gdx.app.log("Hex isStreamaccross", "Array=" + arrToTest);

		if (hexfrom.isMultipleStreamBank){
			arrToTest.addAll(getMultiStreamBanksToCheck(hexfrom));
		//	Gdx.app.log("Hex isStreamaccross", "Multipl Array=" + arrToTest);

		}
		if (arrToTest == null){
			return false;
		}
		for(Hex hex:arrToTest){
			if (hex == hexTo){
				return true;
			}
		}
		return false;
	}

	private static ArrayList<Hex> getMultiStreamBanksToCheck(Hex hexfrom) {
		ArrayList<Hex> arrTurn = new ArrayList<>();
		ArrayList<Hex> arrWork = new ArrayList<>();
		for (Integer in:hexfrom.arrMultiplStreamBank) {
			arrWork.clear();
			switch (in) {
				case 10:
					arrWork.addAll(arrStream11); // opposite side
					break;
				case 11:
					arrWork.addAll(arrStream10); // opposite side
					break;
				case 20:
					arrWork.addAll(arrStream21); // opposite side
					break;
				case 21:
					arrWork.addAll(arrStream20); // opposite side
					break;
				case 30:
					arrWork.addAll(arrStream31); // opposite side
					break;
				case 31:
					arrWork.addAll(arrStream30); // opposite side
					break;
				case 40:
					arrWork.addAll(arrStream41); // opposite side
					break;
				case 41:
					arrWork.addAll(arrStream40); // opposite side
					break;
				case 50:
					arrWork.addAll(arrStream51); // opposite side
					break;
				case 51:
					arrWork.addAll(arrStream50); // opposite side
					break;
				case 60:
					arrWork.addAll(arrStream61); // opposite side
					break;
				case 61:
					arrWork.addAll(arrStream60); // opposite side
					break;
				case 70:
					arrWork.addAll(arrStream71); // opposite side
					break;
				case 71:
					arrWork.addAll(arrStream70); // opposite side
					break;
				case 80:
					arrWork.addAll(arrStream81); // opposite side
					break;
				case 81:
					arrWork.addAll(arrStream80); // opposite side
					break;
				case 90:
					arrWork.addAll(arrStream91); // opposite side
					break;
				case 91:
					arrWork.addAll(arrStream90); // opposite side
					break;
				default:
					arrWork = null; // opposite side
					break;
			}
			arrTurn.addAll(arrWork);

		}
		return arrTurn;


	}

	private static ArrayList<Hex> getStreamBanksToCheck(Hex hexfrom) {
		//Gdx.app.log("Hex getStreamBankToCheck", "Hex=" + hexfrom);

		ArrayList<Hex> arrWork = new ArrayList<>();
		//Gdx.app.log("Hit", "on ="+ hexfrom.streamBank);

		switch (hexfrom.streamBank){
			case 10:
				arrWork.addAll(arrStream11); // opposite side
				break;
			case 11:
				arrWork.addAll(arrStream10); // opposite side
				break;
			case 20:
				arrWork.addAll(arrStream21); // opposite side
				break;
			case 21:
				arrWork.addAll(arrStream20); // opposite side
				break;
			case 30:
				arrWork.addAll(arrStream31); // opposite side
				break;
			case 31:
				arrWork.addAll(arrStream30); // opposite side
				break;
			case 40:
				arrWork.addAll(arrStream41); // opposite side
				break;
			case 41:
				arrWork.addAll(arrStream40); // opposite side
				break;
			case 50:
				arrWork.addAll(arrStream51); // opposite side
				break;
			case 51:
				arrWork.addAll(arrStream50); // opposite side
				break;
			case 60:
				arrWork.addAll(arrStream61); // opposite side
				break;
			case 61:
				arrWork.addAll(arrStream60); // opposite side
				break;
			case 70:
				arrWork.addAll(arrStream71); // opposite side
				break;
			case 71:
				arrWork.addAll(arrStream70); // opposite side
				break;
			case 80:
				arrWork.addAll(arrStream81); // opposite side
				break;
			case 81:
				arrWork.addAll(arrStream80); // opposite side
				break;
			case 90:
				arrWork.addAll(arrStream91); // opposite side
				break;
			case 91:
				arrWork.addAll(arrStream90); // opposite side
				break;
			default:
				arrWork = null; // opposite side
				break;
		}
		return arrWork;

	}

	/**
	 *  find all hexes across stream from this hex
	 * @return an array of hexes across from this hex
	 */
	public ArrayList<Hex> findOtherSideStream(){
		Gdx.app.log("Hex findOtherSideStream", "Hex=" + this);
		Gdx.app.log("Hex findOtherSideStream", "Hex=" + this);
		Gdx.app.log("Hex findOtherSideStream", "Surrond=" + this.getSurround());
		Gdx.app.log("Hex findOtherSideStream", "Hex=" + this);

		ArrayList<Hex> arrReturn = new ArrayList<>();
		for (Hex hex:getSurround()){
			Gdx.app.log("Hex findOtherSideStream", "Trying Surround=" + hex);
			if (isStreamAcross(this, hex)){
				Gdx.app.log("Hex findOtherSideStream", "hit=" + hex);
				arrReturn.add(hex);
			}
		}
		return arrReturn;
	}



	private static void LoadForest() {
		for (int i = 0; i < forest.length; i++) {
			int x = forest[i][0];
			int y = forest[i][1];
			Hex hex = hexTable[x][y];
			hex.isForest = true;
		}


	}


	private static void LoadTowns() {
		for (int i = 0; i < town.length; i++) {
			int x = town[i][0];
			int y = town[i][1];
			Hex hex = hexTable[x][y];
			hex.isTown = true;
		}

	}


	private static void LoadPaths() {
		for (int i = 0; i < path1.length; i++) {
			int x = path1[i][0];
			int y = path1[i][1];
			Hex hex = hexTable[x][y];
			hex.isPath = true;
		}
		NoPath.clear();
		for (int i = 0; i < nopath.length; i++) {
			;
			int x = nopath[i][0][0];
			int y = nopath[i][0][1];
			Hex hex1 = hexTable[x][y];
			x = nopath[i][1][0];
			y = nopath[i][1][1];
			Hex hex2 = hexTable[x][y];
			hex1.npath = new NoPath(hex1, hex2);
			hex2.npath = new NoPath(hex2, hex1);
		}
		for (int i = 0; i < noRoad.length; i++) {
			;
			int x = noRoad[i][0][0];
			int y = noRoad[i][0][1];
			Hex hex1 = hexTable[x][y];
			x = noRoad[i][1][0];
			y = noRoad[i][1][1];
			Hex hex2 = hexTable[x][y];
			ArrayList<Hex> arrHex = new ArrayList<>();
			arrHex.add(hex1);
			arrHex.add(hex2);
			noRoadArr[i] = arrHex;
		}

	}
	static public ArrayList noRoadArr[] = new ArrayList[noRoad.length];

	public boolean isPathTo(Hex hex) {
		if (npath == null) {
			return true;
		}
		if (!npath.arrHexesNoPath.contains(hex)) {
			return true;
		}
		return false;
	}

	private static void LoadBridges() {
		for (int i = 0; i < bridges.length; i++) {
			int x = bridges[i][0];
			int y = bridges[i][1];
			Hex hex1 = hexTable[x][y];
			hex1.isBridge = true;
		}

	}


	private static void LoadRoads() {
		for (int i = 0; i < roads.length; i++) {
			int x = roads[i][0];
			int y = roads[i][1];
			Hex hex = hexTable[x][y];
			hex.isRoad = true;
		}
		loadSpecialRoadCheck();

	}


	public boolean checkAlliesInHex() {
		for (Unit unit : arrUnitsInHex) {
			if (unit.isAllies){
				return true;
			}
		}
		return false;
	}
	public boolean checkAlliesCombatInHex() {
		for (Unit unit : arrUnitsInHex) {
			if (unit.isAllies && unit.isGroundCombat){
				return true;
			}
		}
		return false;
	}

	public boolean checkRussianInHex() {
		for (Unit unit : arrUnitsInHex) {
			if (unit.isRussian){
				return true;
			}

		}
		return false;

	}
	public boolean checkRussianCombatInHex() {
		for (Unit unit : arrUnitsInHex) {
			if (unit.isRussian && unit.isGroundCombat){
				return true;
			}

		}
		return false;

	}

	public boolean leaveHex(Unit unit) {
		//       Gdx.app.log("Hex","leaveHex unit="+unit+" Hex= "+this+" id="+unit.getID());
		if (xTable == 25 && yTable == 2) {
			int bk = 0;
		}
		if (arrUnitsInHex.contains(unit)) {
			arrUnitsInHex.remove(unit);
		} else {
			if (unit.isEliminated()) {
			} else {
				return false;
			}
		}
		establishOccupied();
		setZOCs();
		return true;
	}

	public void setZOCs() {
		ArrayList<Hex> arrHexCheck = getSurround();
		arrHexCheck.add(this);
		for (Hex hexCheck:arrHexCheck){
			ArrayList<Hex> arrSurround = hexCheck.getSurround();
			hexCheck.isAlliedZOC[0] = false;
			hexCheck.isRussianZOC[0] = false;
			hexCheck.isRussianZOCOccupied = false;
			hexCheck.isAlliedZOCOccupied = false;

			for (Hex hex:arrSurround){
				if ((hexCheck.isStreamBank() && hex.isStreamBank()) &&
						!Bridge.isBridge(hexCheck,hex) && Hex.isStreamAcross(hex,hexCheck)){
					// do nothing
				}else{
					for (Unit unit:hex.getUnitsInHex()) {
						if (unit.isAllies && unit.isExertZOC()) {
							hexCheck.isAlliedZOC[0] = true;
						}
						if (unit.isRussian && unit.isExertZOC()) {
							hexCheck.isRussianZOC[0] = true;
						}
					}
				}
			}
		}
		for (Unit unit:arrUnitsInHex){
			if (unit.isRussian && isAlliedZOC[0]){
				isAlliedZOCOccupied = true;
			}else if(unit.isAllies && isRussianZOC[0]){
				isRussianZOCOccupied = true;
			}
		}

	}

	boolean isStreamBank() {
		return isStreamBank;
	}

	private void establishOccupied() {
		/**
		 *  No default
		 */
		isRussianOccupied[0] = false;
		isAlliedOccupied[0] = false;
		if (checkRussianInHex()){
			isRussianOccupied[0] = true;
			isAlliedOccupied[0] = false;
		}
		if (checkAlliesInHex()){
			isAlliedOccupied[0] = true;
			isRussianOccupied[0] = false;
		}


	}

	public void enterHex(Unit unit) {
		/**
		 * Add unit as first entry in the array
		 * It has to match the display first unit will be on top
		 * Changes for Mobile Assualt
		 */
		//      Gdx.app.log("Hex","enterHex unit="+unit+" Hex= "+this+" id="+unit.getID());
		if (!unit.isAllies) {
			isRussianEntered = true;
		}
		if (unit.isAllies) {
			isRussianEntered = false;
		}
		arrUnitsInHex.add(0, unit);
		if (!unit.isAllies && !checkAlliesInHex()) {
			isRussianOccupied[0] = true;
			isAlliedOccupied[0] = false;
		}
		if (unit.isAllies && !checkRussianInHex()) {
			isAlliedOccupied[0] = true;
			isRussianOccupied[0] = false;
		}
		moveUnitToBack(unit);
		if (xTable == 40 && yTable == 22) {
			int bg = 0;
		}
		setZOCs();

//        this is the hard move for unit
		unit.moveForHexProcessing(this);
	}

	public void moveUnitToBack(Unit unit) {
		if (!unit.isEliminated() && !arrUnitsInHex.contains(unit)) {
			new ErrorGame("Move To Back unit that is not present", this);
		}
		arrUnitsInHex.remove(unit);
		arrUnitsInHex.add(unit);

	}
	public void moveUnitToFront(Unit unit) {
		if (!unit.isEliminated()){
			if (!arrUnitsInHex.contains(unit)) {
				new ErrorGame("Move To Front unit that is not present", this);
			}
		}
		arrUnitsInHex.remove(unit);
		arrUnitsInHex.add(0,unit);
		Counter.rePlace(this);

	}
	public static void loadCalcMoveCost(int thread) {
		for (int x=0; x< xEnd; x++)
		{
			for (int y=0; y < yEnd; y++)
			{
				Hex hex = hexTable[x][y];
				for (int i=0; i <11; i++) {
					hex.calcMoveCost[thread] = 0;
				}
			}
		}
	}
	public void setRange(int range) {
		this.range = range;
	}
	public ArrayList<Hex> getSurround(){
		ArrayList<Hex> arrReturn = new ArrayList<Hex>();
		arrReturn.addAll(arrSurroundHex);
		return arrReturn;
	}

	/**
	 *  get all surrounding hexes to range supplied
	 * @param range
	 * @return an array of hexes and distance from passed hex.
	 */

	public  Hex[][] getSurround(int range){
		ArrayList<HexInt> arrReturn = new ArrayList<>();
		Hex[][] retTable; // jagged array to contain hexes for surround
		// if within 7 of henderson copy the rolled version
		//		if (startHex == Hex.hendersonField && iRange == 7 && doneHenderson)
		//		{
		//			retTable = Hex.sevenHendersonHex;
		//			return retTable;
		//		}
		//		else
		//		{
		//			doneHenderson = true;
		//		}
		retTable = new Hex[range][]; // set up internal jagged array
		//  1 = first surround, 2 = next etc.
		retTable[0] = HexHandler.getSurround(this); // get initial surrounding hexes
		// go through the entire range creating surrond hexTable
		ArrayList<Hex> workList = new ArrayList<Hex>();
		ArrayList<Hex> totList = new ArrayList<Hex>();
		int prevTableLevel, prevTableSize;
		Hex[] workHexTab;
		for (int i = 1; i < range; i++)
		{
			prevTableLevel = i - 1; // get preceeding level
			prevTableSize = retTable[prevTableLevel].length; // get preceeding level size
			workList.clear(); // clear the array list
			for (int  j = 0; j < prevTableSize; j++)
			{
				workHexTab = HexHandler.getSurround(retTable[prevTableLevel][j]);
				for (int k = 0; k < 6; k++)
				{
					if (workHexTab[k] == null)
					{
						//                           workList.Add(null); //keep track of nulls
						//                           totList.Add(null);
					}
					else
					{
						if (!totList.contains(workHexTab[k]))
						// if not in list
						{
							workList.add(workHexTab[k]); // add it to it
							totList.add(workHexTab[k]); // add to our dupe checker
							//                              MapDisplay.Character("p", Color.Coral, workHexTab[k].xTable, workHexTab[k].yTable);
						}
					}
				}
			}
			// everyting in array list lets move it to hexTable
			retTable[i] = new Hex[workList.size()];
			for (int l = 0; l < workList.size(); l++)
			{
				retTable[i][l] =  workList.get(l);
			}
		} // next table level

		return  retTable;
	}
	public static ArrayList<Hex> getSurroundMapArr(Hex startHex, int iRange)
	{
		ArrayList<Hex> arrReturn = new ArrayList<Hex>();
		Hex[][] hexTemp = startHex.getSurround(iRange);
		for (int i = 0; i < hexTemp.length; i++)
		{
			for (int j = 0; j < hexTemp[i].length; j++)
			{
				if (!arrReturn.contains(hexTemp[i][j])&& hexTemp[i][j] != null)
					arrReturn.add(hexTemp[i][j]);
			}
		}
		AIUtil.RemoveDuplicateHex(arrReturn);
		return arrReturn;
	}
	public int getStacksIn() {
		int stackTotal = 0;
		for (Unit unit:arrUnitsInHex){
			stackTotal += unit.getCurrentStep();
		}
		return stackTotal;
	}
	public float getCalcMoveCost(int thread) {
		return calcMoveCost[thread];
	}
	public void setCalcMoveCost(float calcMoveCost, int thread) {
		this.calcMoveCost[thread] = (int) calcMoveCost;
	}


	public int getAiScore() {
		return 0;
	}


	public boolean getAlliedZoc(int thread){
		return isAlliedZOC[thread];
	}
	public boolean getRussianZoc(int thread){
		return isRussianZOC[thread];
	}



	public boolean isForest() {
		return isForest;
	}
	public void setHasBeenAttackedThisTurn(boolean hasBeenAttackedThisTurn) {
		this.hasBeenAttackedThisTurn = hasBeenAttackedThisTurn;
	}
	public static void initCombatFlags(){
		for (int x=0; x< xEnd; x++)
		{
			for (int y=0; y < yEnd; y++)
			{
				hexTable[x][y].hasBeenAttackedThisTurn = false;
			}
		}
	}
	public static  ArrayList<Hex> getAttackedThisTurn(){
		ArrayList<Hex> arrReturn = new ArrayList<>();
		for (int x=0; x< xEnd; x++)
		{
			for (int y=0; y < yEnd; y++)
			{
				if (hexTable[x][y].hasBeenAttackedThisTurn){
					arrReturn.add(hexTable[x][y]);
				}
			}
		}
		return arrReturn;
	}


	public boolean isHasBeenAttackedThisTurn() {
		return hasBeenAttackedThisTurn;
	}

	/**
	 *  check if unit can occupy this hex
	 *  only 2 combat units or 3 units belonging to same division
	 * @param unit
	 * @return
	 */
	public boolean canOccupy(Unit unit) {
		if (unit.isAllies && checkRussianInHex()){
			return false;
		}
		if (!unit.isAllies && checkAlliesInHex()){
			return false;
		}
		if (!unit.isGroundCombat){
			return true;
		}
		int countersInHex =  0;
		ArrayList<Division> arrDivs = new ArrayList<>();
		for (Unit unitch:getUnitsInHex()){
			if (unitch.isGroundCombat){
				countersInHex++	;
				if (!arrDivs.contains(unitch.getDivision())) {
					arrDivs.add(unitch.getDivision());
				}
			}
		}
		if (countersInHex > 2){
			return false;
		}
		if (countersInHex == 2){
			if (arrDivs.size() > 1){
				return false;
			}else{
				if (!arrDivs.contains(unit.getDivision())){
					return false;
				}
			}
		}
		return true;
	}
	public int getRange() {
		return range;
	}

	public boolean isPath() {
		return false;
	}

	public boolean isCity() {
		return false;
	}

	public boolean isTown() {
		return false;
	}
	public void cycleUnits() {
		if (arrUnitsInHex.size() <= 1) {
			return;
		}
		Unit unit = arrUnitsInHex.get(0);
		arrUnitsInHex.remove(0);
		arrUnitsInHex.add(unit);
		Counter.rePlace(this);

	}
	public void bringOfficersToTop(){
		if (arrUnitsInHex.size() <= 1) {
			return;
		}
		Unit unitOfficer = null;
		for (Unit unit:arrUnitsInHex) {
			if (unit.isOfficer) {
			   unitOfficer = unit;
			   break;
			}
		}
		if (unitOfficer != null) {
			arrUnitsInHex.remove(unitOfficer);
			arrUnitsInHex.add(0, unitOfficer);
			Counter.rePlace(this);
		}
	}
	public void bringCommanderToTop(){
		if (arrUnitsInHex.size() <= 1) {
			return;
		}
		Unit unitCommander = null;
		for (Unit unit:arrUnitsInHex) {
			if (unit.isCommander) {
				unitCommander = unit;
				break;
			}
		}

		if (unitCommander != null) {
			arrUnitsInHex.remove(unitCommander);
			arrUnitsInHex.add(0, unitCommander);
			Counter.rePlace(this);
		}
	}
	public static void initHex(){
		for (Hex hex:arrHexMap){
			hex.arrUnitsInHex = new ArrayList<>();
			hex.isAlliedOccupied[0] = false;
			hex.isRussianOccupied[0] = false;
			hex.isRussianZOC[0] = false;
			hex.isAlliedZOC[0] = false;
		}
	}
}

