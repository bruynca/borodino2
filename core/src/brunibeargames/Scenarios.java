package brunibeargames;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import brunibeargames.Unit.Corp;
import brunibeargames.Unit.Unit;
import brunibeargames.Unit.UnitHex;

public class Scenarios {
    static  int[][] corp4s0Allies = {{193, 16, 12},{194,16,12},{195,16,12},{196,16,12}, {192, 15, 13}, {191, 15, 13}, {176, 15, 12},
            {172, 15, 12}, {174, 13, 11}, {178, 11, 10}, {179, 10, 9}, {177, 14, 12}, {185, 9, 7},
            {180, 6, 9}, {169, 4, 10}, {170, 2, 9}, {171, 1, 9},{173,14,12}};



    static  int[][] corp1s0Allies =   {{144,15,15},{147,15,15},{151,15,14},{148,14,14},{145,14,16},
                {146,14,16},{130,10,16},{129,8,17},{128,6,16},{131,5,16},{132,4,17},
                {134,2,18},{135,0,18},{150,16,14},{149,16,14},{136,0,18},
                {138,0,18},{137,0,18},{140,0,18},{141,0,18},{142,0,18},{143,0,18}};
    static  int[][] corp1Cs0Allies  ={{223,11,20},{221,12,15},{220,12,15},
                         {219,12,15},{218,12,15}};

    static int[][] corp5s0Allies = {{208,14,25},{197,14,25},{198,12,26},{199,12,26},
        {206,11,27},{203,8,27},{204,6,28},{205,5,28},{207,4,29}};

    static int[][] corp2Cs0Allies = {{230,10,19},{228,10,19},{229,11,18}};

    static int[][]corp3Cs01Allies = {{236,0,10},{231,0,10},{232,0,10},{238,0,18},{234,0,18}};

    static int[][]corp3s01Allies = {{152,0,18},{153,0,18},{154,0,18},{155,0,18},{156,0,18},
                                    {158,0,18},{159,0,18},{162,0,18},{166,0,18}};

    static int[][] corpYGs01Allies = {{119,0,23},{118,0,23},{120,0,23},{122,0,23},{123,0,23},
                                     {126,0,23},{124,1,22},{125,0,23},{117,0,23}};

     static int[][] corpOGs01Allies = {{107,0,23},{108,0,23},{109,0,23},{110,0,23},
                                     {111,0,23},{113,0,23},{112,0,23},{114,0,23}};


     static int[][] corp2s01Russian = {{2,26,9},{6,26,9},{3,29,7},{1,30,7},
                                       {0,29,7},{7,29,8},{4,28,8},{5,28,8}};

    static int[][] corp3s01Russian = {{11,30,6},{10,30,6},{9,30,6},
                                        {13,20,12},{14,20,12},{15,20,12}};

    static int[][] corp4s01Russian = {{18,27,8},{19,27,8},{17,27,8},{16,27,8},
                                     {20,27,9},{21,27,9},{22,27,9},{23,27,9}};

    static int[][] corp5s01Russian = {{26,22,11},{27,27,11},{24,27,12},
                                    {25,27,12},{29,27,12},{28,28,13},{33,28,12},
                                    {32,28,12},{31,28,12},{30,28,12}};

    static int[][] corp6s01Russian = {{34,23,11},{35,24,11},{41,24,10},{36,25,11},
                                {38,24,12},{44,24,12},{42,26,11},{40,26,11},{39,26,11}};


    static int[][] corp1Cs01Russian = {{45,34,9},{47,34,9},{48,34,9},{46,19,12}};

    static int[][] corp2Cs01Russian = {{53,19,11},{51,19,11},{50,19,11},{49,19,11}};


    static int[][] corp3Cs01Russian = {{54,18,9},{55,18,9},{56,27,10},{58,27,10}};

    static int[][] corp1As01Russian = {{66,26,13},{67,26,13},{68,27,13},{69,27,13},{70,26,13}};

    static int[][] corpCOs01Russian = {{59,19,12},{65,33,9},{62,33,9},{61,33,9},{60,33,9},{64,33,9}};


    static int[][] corp7s01Russian = {{71,23,13},{77,24,13},{75,24,13},{74,24,13},
                        {76,25,14},{73,16,21},{72,16,21}};

    static int[][] corp8s01Russian = {{78,22,16},{80,22,16},{79,22,16},{83,22,15},
            {81,19,15},{88,20,17},{85,20,17},{84,20,17},{86,17,22}};

    static int[][] corp4Cs01Russian = {{89,21,16},{90,20,23},{91,19,17},
                               {94,17,17},{92,24,16},{93,24,16}};

    static int[][] corpKs01Russian = {{97,25,23},{96,25,23},{98,24,22},{95,24,22}};

    static int[][] corp2As01Russian = {{99,25,14},{100,19,17},{101,24,15},{102,24,15}};

    static int[][] corpMMs01Russian ={{103,29,17},{104,29,17},{105,29,17},{106,31,11}};
    static int[][] commanders= {{269,16,14},{268,27,10},{267,23,15},{266,32,12},
                               {270,10,18},{255,4,17}};

    static int[][] frenchOfficers = {{257,14,12},{259,12,15},
                        {260,8,17},{258,14,25},
                    {261,0,10}, {256,0,18},{262,0,18},{263,0,18},
                    {264,0,18},{265,0,18}};

    static int[][] russianofficers = {{240,30,7},{239,30,17},{241,31,5},
            {242,29,7},{243,29,12},
    {244,26,11},{245,35,7},{247,27,11},{246,19,11},{249,31,13},
    {248,34,9},{250,23,15},{251,19,17},{252,24,16},
    {253,24,21},{254,25,14}};
    static public ArrayList<Scenarios> arrScenarios = new ArrayList();
    ArrayList<Corp> alliedCorp = new ArrayList<>();
    ArrayList<Corp> russianCorp = new ArrayList<>();
    ArrayList<UnitHex> alliedUnitHexs = new ArrayList<>();
    ArrayList<UnitHex> russianUnitsHex = new ArrayList<>();
    int numScenario; // 0 is Shevardino DOB
    int startTurn=0;
    int startDay=0;



    Scenarios(){



    }
    public static void loadData(){
        /**
         *  Load 0 Schevardiono day of battle
         */
        // 4th Corp
        Scenarios sc = new Scenarios();
        sc.numScenario = 0;
        sc.startDay= 1;
        sc.startTurn = 11;

        loadCorpData("4",corp4s0Allies,sc);
        loadCorpData("1",corp1s0Allies,sc);
        loadCorpData("1C",corp1Cs0Allies,sc);
        loadCorpData("5",corp5s0Allies,sc);
        loadCorpData("2C",corp2Cs0Allies,sc);
        loadCorpData("3C",corp3Cs01Allies,sc);
        loadCorpData("3",corp3s01Allies,sc);
        loadCorpData("YG", corpYGs01Allies,sc);
        loadCorpData("OG", corpOGs01Allies,sc);

        loadCorpDataRussian("2",corp2s01Russian,sc);
        loadCorpDataRussian("3",corp3s01Russian,sc);
        loadCorpDataRussian("4",corp4s01Russian,sc);
        loadCorpDataRussian("5",corp5s01Russian,sc);
        loadCorpDataRussian("6",corp6s01Russian,sc);
        loadCorpDataRussian("1C",corp1Cs01Russian,sc);
        loadCorpDataRussian("2C",corp2Cs01Russian,sc);
        loadCorpDataRussian("3C",corp3Cs01Russian,sc);
        loadCorpDataRussian("1A",corp1As01Russian,sc);
        loadCorpDataRussian("CO",corpCOs01Russian,sc);
        loadCorpDataRussian("7",corp7s01Russian,sc);
        loadCorpDataRussian("8",corp8s01Russian,sc);
        loadCorpDataRussian("4C",corp4Cs01Russian,sc);
        loadCorpDataRussian("K",corpKs01Russian,sc);
        loadCorpDataRussian("2A",corp2As01Russian,sc);
        loadCorpDataRussian("MM",corpMMs01Russian,sc);

        loadCommanders(sc);
        loadFrenchOfficers(sc);
        loadRusssianOfficers(sc);

    }

    private static void loadRusssianOfficers(Scenarios sc) {
        for (int i = 0; i< russianofficers.length; i++) {
            Unit unit = Unit.getUnitByID(russianofficers[i][0]);
            int x = russianofficers[i][1];
            int y = russianofficers[i][2];
            Hex hex = Hex.hexTable[x][y];
            sc.alliedUnitHexs.add(new UnitHex(unit, hex));
        }
        arrScenarios.add(sc);


    }

    private static void loadFrenchOfficers(Scenarios sc) {
        for (int i = 0; i< frenchOfficers.length; i++) {
            Unit unit = Unit.getUnitByID(frenchOfficers[i][0]);
            int x = frenchOfficers[i][1];
            int y = frenchOfficers[i][2];
            Hex hex = Hex.hexTable[x][y];
            sc.alliedUnitHexs.add(new UnitHex(unit, hex));
        }
        arrScenarios.add(sc);

    }

    private static void loadCommanders(Scenarios sc) {
        for (int i = 0; i< commanders.length; i++) {
            Unit unit = Unit.getUnitByID(commanders[i][0]);
            int x = commanders[i][1];
            int y = commanders[i][2];
            Hex hex = Hex.hexTable[x][y];
            sc.alliedUnitHexs.add(new UnitHex(unit, hex));
        }
        arrScenarios.add(sc);

    }

    private static void loadCorpData(String intcorp, int[][] corpData, Scenarios sc) {
        Corp corp =Corp.find(intcorp,true);
        sc.alliedCorp.add(corp);
        for (int i = 0; i< corpData.length; i++) {
            Unit unit = Unit.getUnitByID(corpData[i][0]);
            int x = corpData[i][1];
            int y = corpData[i][2];
            Hex hex = Hex.hexTable[x][y];
            Gdx.app.log("LoadCorpData", "Corp="+intcorp+" Unit="+unit.brigade+" ID="+unit.ID+" Hex="+hex.toString() );

            sc.alliedUnitHexs.add(new UnitHex(unit, hex));
        }
        arrScenarios.add(sc);

    }
    private static void loadCorpDataRussian(String intcorp, int[][] corpData, Scenarios sc) {
        Corp corp =Corp.find(intcorp,false);
        sc.russianCorp.add(corp);
        for (int i = 0; i< corpData.length; i++) {
            Unit unit = Unit.getUnitByID(corpData[i][0]);
            int x = corpData[i][1];
            int y = corpData[i][2];
            Hex hex = Hex.hexTable[x][y];
            sc.russianUnitsHex.add(new UnitHex(unit, hex));
        }
        arrScenarios.add(sc);

    }

    public static void loadUnits(int scenarioNumber){
        Scenarios workScenario=null;
        for (Scenarios sc:arrScenarios){
            if (sc.numScenario == scenarioNumber)
                workScenario = sc;
        }
        for (int i=0 ; i<workScenario.alliedUnitHexs.size();i++){;
            UnitHex uh = workScenario.alliedUnitHexs.get(i);
            Gdx.app.log("LoadUnits", "Unit="+ uh.unit.brigade+" ID="+uh.unit.ID);
            if (i==261)
            {
                int b=0;
            }
            if (uh.unit.ID == 255){
                int b=0;
            }

            if (uh.unit.isCommander) {
                if (!uh.unit.isAllies || uh.unit.commander.entryTurn < workScenario.startTurn &&
                        uh.unit.commander.entryDay == workScenario.startDay ) {
                    uh.unit.placeOnBoard(uh.hex);
                }
                continue;
            }
            if (uh.unit.isOfficer) {
                if (!uh.unit.isAllies || (uh.unit.officer.entryTurn < workScenario.startTurn &&
                        uh.unit.officer.entryDay == workScenario.startDay)) {
                    uh.unit.placeOnBoard(uh.hex);
                }
                continue;
            }
            if (uh.unit.division.entryTurn < workScenario.startTurn &&
                        uh.unit.division.entryDay == workScenario.startDay){

                    uh.unit.placeOnBoard(uh.hex);
            }
            //uh.unit.placeOnBoard(uh.hex);

        }
        for (int i=0 ; i<workScenario.russianUnitsHex.size();i++){;
            UnitHex uh = workScenario.russianUnitsHex.get(i);
            uh.unit.placeOnBoard(uh.hex);
        }

    }
}
