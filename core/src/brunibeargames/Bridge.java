package brunibeargames;

import java.util.ArrayList;


public class Bridge
{
	static public ArrayList<Bridge> arrBridges = new ArrayList();
	Hex hex1;
	Hex hex2;
	/**
	 *  should be run only after rivers and streams  created 
	 */
/*	static public void CreateBridges()
	{
		for (River river:River.arrRivers)
		{
			for (Hex hex:river.arrAbank)
			{
				if (hex.isPath || hex.isRoad)
				{
					ArrayList<Hex> arrHexWork = hex.GetSurround();
					for (Hex hexb:river.arrBbank)
					{
						if (hexb.isPath || hexb.isRoad)
						{
							if (arrHexWork.contains(hexb))
							{
								Bridge bridge = new Bridge(hex, hexb);
							}
						}
					}
				}
			}
		}

	}*/
	/**
	 *  Check all bridges to see if we have a match 
	 * @param hexa
	 * @param hexb
	 * @return
	 */
	static boolean isBridge(Hex hexa, Hex hexb)
	{
		for (Bridge bridge:arrBridges)
		{
			if ((bridge.hex1 == hexa && bridge.hex2 == hexb)|| (bridge.hex1 == hexb && bridge.hex2 == hexa))
			{
				return true;
			}
		}
		return false;
		
	}
	

}
