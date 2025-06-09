package brunibeargames.Unit;

import java.util.ArrayList;

import brunibeargames.Hex;

public class RoadMarch {
    static int move;
    public static ArrayList<Hex> getMovePossible(Unit unit) {
        ArrayList<Hex> arrReturn = new ArrayList<>();
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
            if (hex1.getRussianZoc(0) || hex1.checkRussianInHex() || hex1.checkAlliesInHex()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (hex1.getAlliedZoc(0) || hex1.checkAlliesInHex()|| hex1.checkRussianInHex()) {
                return false;
            } else {
                return true;
            }
        }
    }


}
