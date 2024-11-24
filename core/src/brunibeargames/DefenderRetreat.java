package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import brunibeargames.Unit.Unit;
import brunibeargames.Unit.UnitMove;

class DefenderRetreat {
    /**
     * need to check stacking
     */
    int losses;
    ArrayList<Hex> arrRetreatPath = new ArrayList<>();

    DefenderRetreat(Attack attack) {
        Gdx.app.log("DefenderRetreat", "Constructor");

        /**
         * get an array of 3 hexes without checkin terrain
         */
        if (attack.arrDefenders.size() < 1) {
            ErrorGame errorGame = new ErrorGame("No Defenders to retreat", this);
            return;
        }
        Unit unitRetreatBase = attack.arrDefenders.get(0);
        UnitMove unitMove = new UnitMove(unitRetreatBase, attack.defenderRetreats, false, false, 0);
        ArrayList<Hex> arrUnitCanGo = unitMove.getMovePossible();
        ArrayList<Hex> arrHexPossible = HexHelper.getSurroundinghexes(attack.hexTarget, attack.defenderRetreats);
        arrHexPossible.retainAll(arrUnitCanGo); // intersection

        ArrayList<RetreathPath> arrRetreats = createRetreatPaths(attack.defenderRetreats, arrHexPossible);
        scoreRetreatPath(arrRetreats, attack);
        ArrayList<RetreathPath> arrSorted = sortRetreat(arrRetreats);
        if (arrSorted.size() == 0) { // no retreats possible
            losses += attack.defenderRetreats;
        } else {
            /**
             *  take first from sorted
             */
            losses += attack.defenderRetreats - arrSorted.get(0).arrHexPath.size();
            if (arrSorted.get(0).points > 100) {
                losses++;
            }
            for (int i = arrSorted.get(0).arrHexPath.size() - 1; i >= 0; i--) {
                arrRetreatPath.add(arrSorted.get(0).arrHexPath.get(i));
            }
        }
        Gdx.app.log("DefenderRetreat", "Losses=" + losses);

    }

    public int getLosses() {
        return losses;

    }

    private void scoreRetreatPath(ArrayList<RetreathPath> arrRetreats, Attack attack) {
        /**
         * update for distance to Supply
         */
        for (RetreathPath rep : arrRetreats) {
            float dist = 9999;
            rep.updatePoints((int) dist);
        }
        /**
         * update for going through zoc
         */
        for (RetreathPath rep : arrRetreats) {
            for (Hex hex : rep.arrHexPath) {
                if (attack.arrDefenders.get(0).isAllies) {
                    if (hex.getAxisZoc(0)) {
                        rep.points += 100;
                    }
                } else {
                    if (hex.getAlliedZoc(0)) {
                        rep.points += 100;
                    }
                }
            }
        }
    }


    private ArrayList<RetreathPath> createRetreatPaths ( int defenderRetreats, ArrayList<
    Hex > arrHexPossible){
        ArrayList<RetreathPath> arrReturn = new ArrayList<>();
        int num = defenderRetreats;
        /**
         *  get all hexes that are number away
         */
        ArrayList<Hex> arrDest = new ArrayList<>();

        for (Hex hex : arrHexPossible) {
            if (hex.getRange() == num) {
                arrDest.add(hex);
            }
        }
        num--;
        /**
         *  max is retreat 2
         */
        for (Hex hex : arrDest) {
            if (num > 0) {
                for (Hex hex2 : hex.getSurround()) {
                    if (arrHexPossible.contains(hex2) && hex2.getRange() == num) {
                        RetreathPath retreathPath = new RetreathPath(hex);
                        retreathPath.addHex(hex2);
                        arrReturn.add(retreathPath);
                    }
                }
            } else {
                RetreathPath retreathPath = new RetreathPath(hex);
                arrReturn.add(retreathPath);
            }

        }
        return arrReturn;
    }

    private ArrayList<RetreathPath> sortRetreat (ArrayList < RetreathPath > arrRetreats) {
        ArrayList<RetreathPath> arrReturn = new ArrayList();
        int i;
        int points = 99999;
        for (RetreathPath rp : arrRetreats) {
            for (i = 0; i < arrReturn.size(); i++) {
                if (rp.points < arrReturn.get(i).points) {
                    break;
                }
            }
            arrReturn.add(i, rp);
        }
        return arrReturn;
    }


}


