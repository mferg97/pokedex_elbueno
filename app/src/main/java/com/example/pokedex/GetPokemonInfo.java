package com.example.pokedex;


import android.os.AsyncTask;
import android.util.Log;

import com.example.pokedex.db.Pokemon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;

public class GetPokemonInfo extends AsyncTask<String, Integer, String> {

    private String rawJSON;
    private int pokemonId;
    private String name;
    private GetPokemonInfo.OnPokemonInfoComplete mCallback;

    public interface OnPokemonInfoComplete{
        void processPokemonInfo(Pokemon pokemon);
    }

    public void setOnPokemonInfoComplete(GetPokemonInfo.OnPokemonInfoComplete listener){
        mCallback = listener;
    }

    /**
     * Async tasks class that runs code on serperate thread
     * Makes our request to our poke API
     * @param strings values passed in from .execute param
     * @return rawJSON results
     */
    @Override
    protected String doInBackground(String... strings) {
        try{
            pokemonId = Integer.parseInt(strings[0]);
            name = strings[1];
            //set our https connection and accessing our first element sent
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + strings[0]);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            //set our http verb and send our token through header using "Authorization", and "Bearer "
            //set other header info here
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();

            switch(status){
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    rawJSON = bufferedReader.readLine();
                    break;
            }
        } catch (MalformedURLException e) {
            Log.d("test", "BAD URL, unable to connect");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("test", "Unable to connect, I/O issue");
            e.printStackTrace();
        }
        return rawJSON;
    }

    /**
     * after doInBackground is done, we parse our JSON
     * and put them into new assignment objects
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Pokemon pokemon;
        try{
            pokemon = parseJson(result);
            Log.d("pjason", "onPostExecute: After parseJSON");
            if(pokemon  != null){
                //send our courses to our DB
                if(mCallback != null && mCallback instanceof GetPokemonInfo.OnPokemonInfoComplete){
                    mCallback.processPokemonInfo(pokemon);
                }else{
                    throw new Exception("Must implement OnPokemonListComplete interface");
                }
            }
        }catch (Exception e){
            Log.d("test", e.getMessage());
        }
    }

    /**
     * UserDefined class that parses our JSON with gson and gsonBuilder
     * Drills down from master object class to get desired info
     * @param r raw JSON
     * @return a list of assignments on success
     */
    private Pokemon parseJson(String r){
        Pokemon newPokemon = null;

        //master Json object
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonTree = jsonParser.parse(r);
        JsonObject infoObj = jsonTree.getAsJsonObject();

        try{
            //pokemon can have 1-2 types, parsing is required to make pokemon object
            List<Pokemon.Type> typeList = parseJsonForTypes(infoObj);
            String type1, type2 = null;
            if(typeList.size() == 2){
                type1 = typeList.get(1).getType();
                type2 = typeList.get(0).getType();
            }else{
                type1 = typeList.get(0).getType();
            }

            //abilites can be 1-3 so parsing is required to make pokemon object
            String ability1, ability2 = null, ability3 = null;
            List<Pokemon.Ability> abilityList = parseJsonForAbilities(infoObj);
            if(abilityList.size() == 1){
                ability1 = abilityList.get(0).getName();
            }else if(abilityList.size() ==2){
                ability1 = abilityList.get(0).getName();
                ability2 = abilityList.get(1).getName();
            }else {
                ability1 = abilityList.get(0).getName();
                ability2 = abilityList.get(1).getName();
                ability3 = abilityList.get(2).getName();
            }
            Pokemon.Stats pokeStats = parseJsonForStats(infoObj);
            Pokemon.Attributes pokeAttr = parseJsonForAttributes(infoObj);

            //Build new pokemon!!
            newPokemon = new Pokemon(
                    pokemonId, name, pokeAttr.getHeight(), pokeAttr.getWeight(),
                    pokeStats.getHp(), pokeStats.getAttack(), pokeStats.getDefense(),
                    pokeStats.getSpAttack(), pokeStats.getSpDefense(), pokeStats.getSpeed(),
                    type1, type2, ability1, ability2, ability3
            );

        }catch (Exception e){
            Log.d("jsonarrayerorr", e.getMessage());
        }

        return newPokemon;
    }

    /**
     * handles json drilling for attributes
     * @param infoObj master object from get request
     * @return pokemon.attribute object
     */
    private Pokemon.Attributes parseJsonForAttributes(JsonObject infoObj) {
        //getting attribute objects from Json -> height and weight
        JsonElement heightEle = infoObj.get("height");
        JsonElement weightEle = infoObj.get("weight");

        String h = heightEle.toString();
        String w = weightEle.toString();

        return new Pokemon.Attributes(h, w);
    }


    /**
     * handles type json and creates a list of pokemon.type object
     * @param infoObj master object from get request
     * @return list of types
     */
    private List<Pokemon.Type> parseJsonForTypes(JsonObject infoObj){
        List<Pokemon.Type> pTypeList= new ArrayList<>();

        //getting types object
        JsonElement typesEle = infoObj.get("types");
        JsonArray typesArray = typesEle.getAsJsonArray();

        //checking each index of the json for our types objects and creates out objects
        for(int i = 0; i < typesArray.size(); i++){
            JsonElement typesSubEle = typesArray.get(i);
            JsonObject typesSubObj = typesSubEle.getAsJsonObject();

            //gets type object
            JsonElement typeEle = typesSubObj.get("type");
            JsonObject nameObj = typeEle.getAsJsonObject();

            //gets name key value
            String typeName = nameObj.get("name").toString();
            typeName = sanitizeString(typeName);

            pTypeList.add(new Pokemon.Type(typeName));
        }

        return pTypeList;
    }

    /**
     * handles stats from json drilling
     * @param infoObj master object from get request
     * @return returns Pokemon.Stat object
     */
    private Pokemon.Stats parseJsonForStats(JsonObject infoObj){
        Pokemon.Stats pokemonStats = new Pokemon.Stats();
        //getting basestats object
        JsonElement statsEle = infoObj.get("stats");
        JsonArray statsArray = statsEle.getAsJsonArray();

        //checking each index of the json for our types objects and creates our objects
        for(int i = 0; i < statsArray.size(); i++) {
            //each of our index's for our object array -> then we get base_stats
            JsonObject statsObject = statsArray.get(i).getAsJsonObject();
            String statNum = statsObject.get("base_stat").toString();

            //into stats object to find the name of the stat
            JsonElement statsSubEle = statsArray.get(i);
            JsonObject statsSubObj = statsSubEle.getAsJsonObject();

            //gets stat key value
            JsonElement statEle = statsSubObj.get("stat");
            JsonObject nameObj = statEle.getAsJsonObject();
            String statName = nameObj.get("name").toString();

            pokemonStats.setStat(sanitizeString(statName), statNum);
        }

        return pokemonStats;
    }

    /**
     * handles abilities drilling from json get
     * @param infoObj master object from get request
     * @return returns a list of Pokemon.abilitys
     */
    private List<Pokemon.Ability> parseJsonForAbilities(JsonObject infoObj) {
        List<Pokemon.Ability> abilityList = new ArrayList<>();
        //getting types object
        JsonElement abilitiesEle = infoObj.get("abilities");
        JsonArray abilitiesArray = abilitiesEle.getAsJsonArray();

        //checking each index of the json for our types objects and creates out objects
        for(int i = 0; i < abilitiesArray.size(); i++){
            //gets index info
            JsonElement abilitySubEle = abilitiesArray.get(i);
            JsonObject abilitySubObj = abilitySubEle.getAsJsonObject();

            //gets ability object
            JsonElement abilityEle = abilitySubObj.get("ability");
            JsonObject abilitiesObj = abilityEle.getAsJsonObject();

            //gets name key value
            JsonElement nameEle = abilitiesObj.get("name");
            String name = sanitizeString(nameEle.toString());
            name = name.replaceAll("[\\s\\-()]", " ");
            name = capitalize(name);
            abilityList.add(new Pokemon.Ability(name));
        }
        return abilityList;
    }

    /**
     * removes all unwanted characters in a string like "
     * @param toBeCleaned string to be sanitized
     * @return sanitized string
     */
    private String sanitizeString(String toBeCleaned){
        return toBeCleaned.replaceAll("^\"|\"$", "");
    }

    /**
     * capitalizes first letter for every word in a string
     * @param capString string to be affected
     * @return capitalized version of string
     */
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }


}