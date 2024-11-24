package brunibeargames;

import java.util.ArrayList;

public class River {

    public static Object instance;
    static ArrayList<River> arrRivers = new ArrayList();
	ArrayList<Hex> arrAbank = new ArrayList();
	ArrayList<Hex> arrBbank = new ArrayList();
	boolean isStream = true;
	public River(boolean isStream) 
	{
		arrRivers.add(this);
		this.isStream = isStream;
	}
	public void addAbank(Hex hex) 
	{
		arrAbank.add(hex);
		
	}
	public void addBbank(Hex hex) 
	{
		arrBbank.add(hex);
		
	}

}
