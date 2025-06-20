package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;

import java.util.HashMap;
import java.util.UUID;

import brunibeargames.Unit.Unit;

public class AccessInternet {
    static public AccessInternet instance;
    String ipAddress = new String("");
    static public String strIPAddress = "none";

    static String strResponse;
    AccessInternet(){
            instance = this;
    }
    static  public String getIPAddress() {
        if (strIPAddress.compareToIgnoreCase("none") != 0) {
            return strIPAddress;
        }
        //      String UUID = getUUID(strCheck);
        UUID uuid = UUID.randomUUID();
        String strIpAddress = UUID.randomUUID().toString().replace("-", "");
        return strIpAddress;
    }


        /**
         * Update bruinbeargame website tracker
         * @param isDevelopment
         */
    static public void registerGame(boolean isDevelopment){
        Gdx.app.log("AccessInternet", "register Game");
        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setUrl("https://bruinbeargames.com/ardenne/php/initprogram.php");
        HashMap params = new HashMap<String, String>();

        String ip = GamePreferences.getIpAddress();
        params.put("ip_address", ip);
//        params.put("key", "Key1");
        params.put("version", GamePreferences.getBuildNumber());
        String formEncodedContent = HttpParametersUtils.convertHttpParameters(params);
        httpPost.setContent(formEncodedContent);
        Gdx.app.log("AccessInternet", "About to Request sent="+formEncodedContent);
        Gdx.net.sendHttpRequest (httpPost, new Net.HttpResponseListener() {


            @Override
            /**
             *  does website want to check if browser needs to be opne
             */
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                strResponse = httpResponse.getResultAsString();
                Gdx.app.log("AccessInternet", "Response="+strResponse);

                if (strResponse.contains("sendbrowsermessage")){
                    Gdx.app.log("AccessInternet", "open webpage");

                    Gdx.net.openURI("https://bruinbeargames.com");

                }
            }

            public void failed(Throwable t) {
                Gdx.app.log("AccessInternet", "Failure"+t.toString());

                int i=0;
                //do stuff here based on the failed attempt
            }

            @Override
            public void cancelled() {

            }
        });
    }

    /**
     *  update the game done on every turn
     */
    static public void updateGame(int turn,String strWinner){
        Gdx.app.log("AccessInternet", "register Game");
        Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPost.setUrl("https://bruinbeargames.com/ardenne/php/updateprogram.php");
        HashMap params = new HashMap<String, String>();

        String ip = GamePreferences.getIpAddress();
        params.put("ip_address", ip);
//        params.put("key", "Key1");
        params.put("program_uid", NextPhase.instance.getProgramUID());
        String sb = Integer.toString(turn);
        params.put("turn", sb);
        String type="";
             if (GameSetup.instance.isRussianVersusAI()){
                type="AI";
            }else{
                type="HotSet";
            }
            params.put("type", type);
            sb = Integer.toString(GameSetup.instance.getScenario().ordinal());
            params.put("scenario", sb);
            sb = Integer.toString(Unit.getOnBoardRussians().size());
            params.put("german_cnt", sb);
            sb = Integer.toString(Unit.getOnBoardAllies().size());
            params.put("allied_cnt", sb);
        String strKey="";
 //       ArrayList<CardsforGame> arrCards = new ArrayList<>();
 //       arrCards.addAll(CardHandler.instance.getArrCardsChosenAllied());
 //       arrCards.addAll(CardHandler.instance.getArrCardsChosenGerman());
 //       arrCards.addAll(CardHandler.instance.getArrCardsPlayedAllied());
 //       arrCards.addAll(CardHandler.instance.getArrCardsPlayedGerman());
        for (int i = 1; i <= 8; i++){
            switch(i){
                case 1:
                    strKey="card1";
                    break;
                case 2:
                    strKey="card2";
                    break;
                case 3:
                    strKey="card3";
                    break;
                case 4:
                    strKey="card4";
                    break;
                case 5:
                    strKey="card5";
                    break;
                case 6:
                    strKey="card6";
                    break;
                case 7:
                    strKey="card7";
                    break;
                case 8:
                    strKey="card8";
                    break;
            }
        }
        if (strWinner.length() > 0) {
            params.put("winner", strWinner);
        }else{
            params.put("winner","unkown");
        }

        String formEncodedContent = HttpParametersUtils.convertHttpParameters(params);
        httpPost.setContent(formEncodedContent);
        Gdx.app.log("AccessInternet", "About to Request sent="+formEncodedContent);
        Gdx.net.sendHttpRequest (httpPost, new Net.HttpResponseListener() {


            @Override
            /**
             *  does website want to check if browser needs to be opne
             */
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                strResponse = httpResponse.getResultAsString();
                Gdx.app.log("AccessInternet", "Response="+strResponse);

                if (strResponse.contains("sendbrowsermessage")){
                    Gdx.app.log("AccessInternet", "open webpage");

                    Gdx.net.openURI("https://bruinbeargames.com");

                }
            }

            public void failed(Throwable t) {
                Gdx.app.log("AccessInternet", "Failure"+t.toString());

                int i=0;
                //do stuff here based on the failed attempt
            }

            @Override
            public void cancelled() {

            }
        });
    }

}
