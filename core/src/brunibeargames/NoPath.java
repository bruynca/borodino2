package brunibeargames;

import java.util.ArrayList;

public class NoPath {
    static ArrayList<NoPath> arrSave  = new ArrayList<NoPath>();
    Hex hex;
    public ArrayList<Hex> arrHexesNoPath = new ArrayList<Hex>();
    NoPath(Hex in1, Hex in2){
        NoPath np =find(in1);
        if (np == null){
            hex = in1;
            arrHexesNoPath.add(in2);
            arrSave.add(this);
        }else{
            np.arrHexesNoPath.add(in2);
        }
    }
    static NoPath find(Hex hexIn){
        for (NoPath np:arrSave){
            if (np.hex == hexIn){
                return np;
            }
        }
        return null;
    }
    static public void clear(){
        arrSave.clear();
    }

}
