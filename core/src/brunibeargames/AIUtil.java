package brunibeargames;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class AIUtil
{
	static public ArrayList<Hex> arrSafe = new ArrayList<Hex>();
	static public ArrayList<Hex> arrEnemy = new ArrayList<Hex>();



/**
 *  Get safe Area that can not be attacked
 * @param
 * @param
 * @param
 * @return
 */

	public static void RemoveDuplicateHex(ArrayList<Hex> arrHexIn) 
	{
		Set<Hex> aSet =  new HashSet<Hex>();
		aSet.addAll(arrHexIn);
		arrHexIn.clear();
		arrHexIn.addAll(aSet);
		return;
	}
	/**
	 * Sort by x and y 
	 * @param arrHex
	 * @return
	 */
	static public ArrayList<Hex> SortArray(ArrayList<Hex> arrHex)
	{
		ArrayList<Hex> arrReturn = new ArrayList();
		for (Hex hex:arrHex)
		{
			if (arrReturn.size() == 0)
			{
				arrReturn.add(hex);
			}
			else
			{
				boolean isAdded = false;
				for (int i= 0; i <arrReturn.size(); i++)
				{
					Hex hex2 = arrReturn.get(i);
					if (hex.xTable > hex2.xTable)
					{
						arrReturn.add(i, hex);
						isAdded =true;
						break;
					}
					else if (hex.xTable == hex2.xTable)
					{
						if (hex.yTable > hex2.yTable)
						{
							arrReturn.add(i, hex);
							isAdded =true;
							break;
						}
					}
				}
				if (!isAdded)
				{
					arrReturn.add(hex);
				}
			}
		}
		return arrReturn;
	}


	/**
	 *  reduce the area by 1 layer
	 * @param arrHexIn
	 * @return reduced 
	 */
	public static ArrayList<Hex> RemoveBarb(ArrayList<Hex> arrHexIn)
	{
		ArrayList<Hex> arrReturn = new ArrayList();
		arrReturn.addAll(arrHexIn);
		int xMax =0, yMax = 0, xMin = 99, yMin=99;
		for (Hex hex:arrHexIn)
		{
			if (hex.xTable > xMax)
			{
				xMax = hex.xTable;
			}
			if (hex.xTable < xMin)
			{
				xMin = hex.xTable;
			}
			if (hex.yTable > yMax)
			{
				yMax = hex.yTable;
			}
			if (hex.yTable < yMin)
			{
				yMin = hex.yTable;
			}

		}
		/**
		 * shave top if not at max
		 */
		if (Hex.xEnd - 1 > xMax  )
		{
			for (Hex hex:arrHexIn)
			{
				if (hex.xTable == xMax)
				{
					arrReturn.remove(hex);
				}
			}
			
		}
		/**
		 * shave Bottom if not at max
		 */
		if (xMin > 0  )
		{
			for (Hex hex:arrHexIn)
			{
				if (hex.xTable == xMin)
				{
					arrReturn.remove(hex);
				}
			}
			
		}
		/**
		 * shave right if not at max
		 */
		if (Hex.yEnd - 1 > yMax  )
		{
			for (Hex hex:arrHexIn)
			{
				if (hex.yTable == yMax)
				{
					arrReturn.remove(hex);
				}
			}
		}
		/**
		 * shave Left if not at min
		 */
		if (yMin > 0  )
		{
			for (Hex hex:arrHexIn)
			{
				if (hex.yTable == yMin)
				{
					arrReturn.remove(hex);
				}
			}
		}

		return arrReturn;
	}

	/**
	 *  Get a square from max points 
	 * @param arrHex
	 * @return
	 */
	static public ArrayList<Hex> GetSquareFromPoints(ArrayList<Hex> arrHex)
	{
		int xMin = 99 ;
		int xMax = 0;
		int yMin = 99;
		int yMax = 0;
		for (Hex hex:arrHex)
		{
			if (hex.xTable > xMax)
			{
				xMax = hex.xTable;
			}
			if (hex.xTable < xMin)
			{
				xMin = hex.xTable;
			}
			if (hex.yTable > yMax)
			{
				yMax = hex.yTable;
			}
			if (hex.yTable < yMin)
			{
				yMin = hex.yTable;
			}
		}
		ArrayList<Hex> arrReturn = new ArrayList();
		for (int x =xMin ;x <= xMax; x++)
		{
			for (int y =yMin ;y <= yMax; y++)
			{
				arrReturn.add(Hex.hexTable[x][y]);
			}
		}
		return arrReturn;
	}


}
/**
 *  Temporary Class to take threat envelopes and apply analysis to them
 * @author Casey
 *
 */
class AIThreatAnalysis
{
	static public ArrayList<AIThreatAnalysis> arrAnalysis = new ArrayList<AIThreatAnalysis>();
	Hex hex;
	float score;
	public AIThreatAnalysis(Hex hexIn)
	{
		hex =hexIn;
	}
	private void  UpdateScore(float cnt)
	{
		score += cnt;
	}
	static public AIThreatAnalysis Find(Hex hex)
	{
		for (AIThreatAnalysis aiThreatAnalysis:arrAnalysis)
		{
			if (aiThreatAnalysis.hex == hex)
			{ 
				return aiThreatAnalysis;
			}
		}
		return null;
	}
	static public ArrayList<Hex> GetAllHex()
	{
		ArrayList<Hex>arrReturn = new ArrayList<Hex>(); 
		for (AIThreatAnalysis aiThreatAnalysis:arrAnalysis)
		{
			arrReturn.add(aiThreatAnalysis.hex);
		}
		return arrReturn;
		
	}
}

