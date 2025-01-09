package brunibeargames;

import java.util.ArrayList;

import brunibeargames.Unit.Corp;
import brunibeargames.Unit.Unit;
import brunibeargames.Unit.UnitHex;

public class Scenarios {
    static  int[][] corp4s0Allies = {{193, 15, 13}, {192, 15, 13}, {191, 15, 13}, {176, 15, 12},
            {172, 15, 12}, {174, 13, 11}, {178, 11, 10}, {179, 10, 9}, {177, 8, 8}, {185, 9, 7},
            {180, 6, 9}, {169, 4, 10}, {170, 2, 9}, {171, 1, 9}};



    static  int[][] corp1s0Allies =   {{144,15,15},{147,15,15},{151,15,14},{148,14,14},{145,14,16},
                {146,14,16},{130,10,16},{129,8,17},{128,6,16},{131,5,16},{132,4,17},
                {134,2,18},{135,0,18},{150,16,14},{149,16,14},{136,0,18},
                {138,0,18},{137,0,18},{140,0,18},{141,0,18},{142,0,18},{143,0,18}};
    static  int[][] corp1Cs0Allies  ={{223,11,20},{221,13,16},{222,13,16},{220,11,16},
                         {219,12,15},{218,12,15}};
    static public ArrayList<Scenarios> arrScenarios = new ArrayList();
    ArrayList<Corp> alliedCorp = new ArrayList<>();
    ArrayList<Corp> russianCorp = new ArrayList<>();
    ArrayList<UnitHex> alliedUnitHexs = new ArrayList<>();
    ArrayList<UnitHex> russianUnitsHex = new ArrayList<>();
    int numScenario; // 0 is Shevardino DOB



    Scenarios(){



    }
    public static void loadData(){
        /**
         *  Load 0 Schevardiono day of battle
         */
        // 4th Corp
        Scenarios sc = new Scenarios();
        sc.numScenario = 0;
        loadCorpData("4",corp4s0Allies,sc);
        loadCorpData("1",corp1s0Allies,sc);
        loadCorpData("1C",corp1Cs0Allies,sc);
    }

    private static void loadCorpData(String intcorp, int[][] corpData, Scenarios sc) {
        Corp corp =Corp.find(intcorp,true);
        sc.alliedCorp.add(corp);
        for (int i = 0; i< corpData.length; i++) {
            Unit unit = Unit.getUnitByID(corpData[i][0]);
            int x = corpData[i][1];
            int y = corpData[i][2];
            Hex hex = Hex.hexTable[x][y];
            sc.alliedUnitHexs.add(new UnitHex(unit, hex));
        }
        arrScenarios.add(sc);

    }

    public static void loadUnitsOnBoard(int scenarioNumber){
        Scenarios workScenario=null;
        for (Scenarios sc:arrScenarios){
            if (sc.numScenario == scenarioNumber)
                workScenario = sc;
        }
        for (int i=0 ; i<workScenario.alliedUnitHexs.size();i++){;
            UnitHex uh = workScenario.alliedUnitHexs.get(i);
            uh.unit.placeOnBoard(uh.hex);
        }
        for (int i=0 ; i<workScenario.russianUnitsHex.size();i++){;
            UnitHex uh = workScenario.russianUnitsHex.get(i);
            uh.unit.placeOnBoard(uh.hex);
        }

    }
}
