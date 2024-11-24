package brunibeargames;

import java.util.ArrayList;

/**
 *  first hex in array is farthest away
 */
class RetreathPath {
    ArrayList<Hex> arrHexPath = new ArrayList<>();
    int points;
    RetreathPath(Hex hex){
        arrHexPath.add(hex);
    }
    public void addHex(Hex hex){
        arrHexPath.add(hex);
    }
    public void updatePoints(int pointsIn){
        points = pointsIn;
    }
}


