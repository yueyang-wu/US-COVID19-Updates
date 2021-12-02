package com.cs5520.covid19;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class DataReadService {
    private final static JSONArray input = StatisticJsonData.stateJsonArray;
    private final static HashMap<String, Integer> stateMap = new HashMap<>();
    private final static HashMap<String, Integer> countyMap = new HashMap<>();
    private static JSONObject json_data;
    /**
     * Pass the input Json data base on the county and state info from spinner
     * @param state
     * @return
     * @throws JSONException
     */
    private static void recordStateJSONObj(String state) throws JSONException {
        if(state == ""){
            return;
        }

        for(int i = 0; i < input.length(); i++){
            JSONObject data = input.getJSONObject(i);
            String stateName = json_data.getString("state");

            if(stateName.equals(state)) {
                json_data = data;
                return;
            }
        }
    }

    private static void recordStateJSONObj(String state, String county) throws JSONException {
        if(state == "" || county == ""){
            return;
        }

        for(int i = 0; i < input.length(); i++){
            JSONObject data = input.getJSONObject(i);
            String stateName = json_data.getString("state");
            String countyName = json_data.getString("county");

            if(stateName.equals(state) && countyName.equals(county)) {
                json_data = data;
                return;
            }
        }
    }

    /**
     * Put Json Obj of the state and record datapointes in hashmap
     * @return
     * @throws JSONException
     */

    private static void recordStateData() throws JSONException {
        JSONObject actualCase = json_data.getJSONObject("actuals");
        int cases = actualCase.getInt("cases");
        int deaths = actualCase.getInt("deaths");
        int positiveTests = actualCase.getInt("positiveTests");
        int negativeTests = actualCase.getInt("negativeTests");
        int newCases = actualCase.getInt("newCases");
        int newDeaths = actualCase.getInt("newDeaths");

        stateMap.put("cases", cases);
        stateMap.put("deaths", deaths);
        stateMap.put("positiveTests", positiveTests);
        stateMap.put("negativeTests", negativeTests);
        stateMap.put("newCases", newCases);
        stateMap.put("newDeaths",  newDeaths);
    }

    private static void recordCountyData() throws JSONException{
        JSONObject actualCase = json_data.getJSONObject("actuals");
        int cases = actualCase.getInt("cases");
        int deaths = actualCase.getInt("deaths");
        int positiveTests = actualCase.getInt("positiveTests");
        int negativeTests = actualCase.getInt("negativeTests");
        int newCases = actualCase.getInt("newCases");
        int newDeaths = actualCase.getInt("newDeaths");

        countyMap.put("cases", cases);
        countyMap.put("deaths", deaths);
        countyMap.put("positiveTests", positiveTests);
        countyMap.put("negativeTests", negativeTests);
        countyMap.put("newCases", newCases);
        countyMap.put("newDeaths",  newDeaths);
    }

    private static Integer getStateCases() throws NullPointerException{

        if(stateMap.containsKey("cases")) {
            return stateMap.get("cases");
        }else{
            throw new NullPointerException();
        }
    }


    private static Integer getStateDeaths() throws NullPointerException{
        if(stateMap.containsKey("deaths")){
            return stateMap.get("deaths");
        }else{
            throw new NullPointerException();
        }
    }


    private static Integer getStateNewCases() throws NullPointerException{
        if(stateMap.containsKey("newCases")){
            return stateMap.get("newCases");
        }else{
            throw  new NullPointerException();
        }
    }

    private static Integer getCountyCases() throws NullPointerException{
        if(countyMap.containsKey("cases")) {
            return countyMap.get("cases");
        }else{
            throw new NullPointerException();
        }
    }


    private static Integer getCountyDeaths() throws NullPointerException{
        if(countyMap.containsKey("deaths")){
            return countyMap.get("deaths");
        }else{
            throw new NullPointerException();
        }
    }

    private Integer getCountyNewCases() throws NullPointerException{
        if(countyMap.containsKey("newCases")){
            return countyMap.get("newCases");
        }else{
            throw  new NullPointerException();
        }
    }

    public static void main(String[] args) {
        try {
            recordStateJSONObj("AK");
            recordStateData();
            int newStateCase = getStateNewCases();
            System.out.println(newStateCase);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
