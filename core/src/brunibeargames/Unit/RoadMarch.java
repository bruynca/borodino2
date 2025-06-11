package brunibeargames.Unit;

import java.util.ArrayList;

import brunibeargames.Hex;

public class RoadMarch {
    static int move;
    static ArrayList<Hex> arrForward = new ArrayList<>();
    static ArrayList<Hex> arrBackward = new ArrayList<>();
    public static ArrayList<Hex> getMovePossible(Unit unit) {
        ArrayList<Hex> arrReturn = new ArrayList<>();
        arrForward.clear();
        arrBackward.clear();
        move = unit.getCurrentMoveFactor() * 2;
        Hex hex = unit.getHexOccupy();
        if (hex == null){
            return arrReturn;
        }
        if (!hex.isRoad){
            return arrReturn;
        }

        ArrayList<Hex> arrWork = new ArrayList<>();
        ArrayList<Hex> arrSurround = hex.getSurround();
        for (Hex hex1:arrSurround){
            if (hex1.isRoad){
                if (Hex.isRoadConnection(hex1,hex)&& validMM(hex1, unit)) {
                    arrWork.add(hex1);
                }
            }
        }
        arrReturn.addAll(arrWork);
        arrReturn.add(hex);
        /**
         *  create 2 now forward and backward
         */
        ArrayList<Hex> arrWorkForward = new ArrayList<>();
        ArrayList<Hex> arrWorkBackward = new ArrayList<>();
        if (!arrWork.isEmpty()){
            arrWorkForward.add(arrWork.get(0));
        }
        if (arrWork.size() > 1){
            arrWorkBackward.add(arrWork.get(1));
        }
        int i=1;
        while (!arrWorkForward.isEmpty()){
            ArrayList<Hex> arrSaveNextStep = new ArrayList<>();
            for (Hex hexCheck:arrWorkForward){
                arrSurround = hexCheck.getSurround();
                for (Hex hexSurround1:arrSurround) {
                    if (hexSurround1.isRoad){

                        if (Hex.isRoadConnection(hexCheck,hexSurround1) && validMM(hexSurround1, unit)) {
                            if (!arrReturn.contains(hexSurround1)) {
                                arrSaveNextStep.add(hexSurround1);
                            }
                        }
                    }
                }
            }
            arrWorkForward.clear();
            arrWorkForward.addAll(arrSaveNextStep);
            arrReturn.addAll(arrSaveNextStep);
            arrForward.addAll(arrSaveNextStep);
            i++;
            if (i == move){
                break;
            }

      }
        i = 10;
        while (!arrWorkBackward.isEmpty()){
            ArrayList<Hex> arrSaveNextStep = new ArrayList<>();
            for (Hex hexCheck:arrWorkBackward){
                arrSurround = hexCheck.getSurround();
                for (Hex hexSurround1:arrSurround) {
                    if (hexSurround1.isRoad){
                        if (Hex.isRoadConnection(hexCheck,hexSurround1) && validMM(hexSurround1, unit)) {
                            if (!arrReturn.contains(hexSurround1)) {
                                arrSaveNextStep.add(hexSurround1);
                            }
                        }
                    }
                }
            }
            arrWorkBackward.clear();
            arrWorkBackward.addAll(arrSaveNextStep);
            arrReturn.addAll(arrSaveNextStep);
            arrBackward.addAll(arrSaveNextStep);

            i++;
            if (i == move){
                break;
            }

        }
        return arrReturn;

    }

    private static boolean validMM(Hex hex1, Unit unit) {
        if (hex1.xTable == 18 && hex1.yTable == 25) {
            int b= 0;
        }

        if (unit.isAllies) {
            if (hex1.getRussianZoc(0) || hex1.checkRussianInHex() || hex1.checkAlliesCombatInHex()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (hex1.getAlliedZoc(0) || hex1.checkAlliesInHex()|| hex1.checkRussianCombatInHex()) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * add road march to end of the array
     *
     * @param arrMove the orignal array to add
     * @param unit the unit that is moving
     * @param hex final hex of the move
     */

    public static void addRoadMarchHexes(ArrayList<Hex> arrMove, Unit unit, Hex hex) {
        getMovePossible(unit);
        arrMove.addAll(arrForward);
        ArrayList<Hex> arrWork = new ArrayList<>();
        // which one to use
        //
         if (arrBackward.contains(hex)){
            arrWork.addAll(arrBackward);

        }else{
            arrWork.addAll(arrForward);
        }
         // remove any hexes after the final hex of move in new array keep
        ArrayList<Hex> arrKeep = new ArrayList<>();
        int i=0;
         for (i=0; i < arrWork.size();i++){
             arrKeep.add(arrWork.get(i));
             if (arrWork.get(i) == hex) {
                break;
             }
         }
         // add keep to array of the move
        for (Hex hex1:arrKeep){
            if (!arrMove.contains(hex1)){
                arrMove.add(hex1);
            }
        }
    }
}
