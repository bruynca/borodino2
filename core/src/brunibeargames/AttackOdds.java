package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import brunibeargames.Unit.Unit;

public class AttackOdds {

        static public String[] result ={"Ae","Ar","Dr","Ex","De","D1","A1"};

        public String oddactualString;
        public String oddUnConverted;
        static public int ixTableView;
        int ixTable; // index into table
        Attack attack;
        int crtColumn = 0;
        String[] strResult;
        public float oddsCheck =0;
        AttackOdds(Attack attack){
            if (!attack.isAI) {
                Gdx.app.log("AttackOdds", "Constructor");
            }
            this.attack =attack;
            oddactualString = "0:0";
        }

        public void update() {
            int shiftCRT = 0;
            crtColumn = WinCRT.instance.getColindex(attack.getActualOdds());
            strResult = WinCRT.instance.getColumn(crtColumn);
        }
        String getResult(int dice1){
            int check = dice1 -1;

            return strResult[check];
        }

        private boolean isArmorEffect(ArrayList<Unit> arrAttackers, Hex hexAttack) {
            if (hexAttack.isForest()){
                // ok
            }else{
                return false;
            }
            float  cnt = 0;
            for (Unit unit:arrAttackers){
                if (unit.isMechanized){
                    cnt++;
                }
            }
            float  half = (float) arrAttackers.size()/2;
            if (cnt >= half){
                return true;
            }
            return false;
        }
  }


