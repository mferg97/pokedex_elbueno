package com.example.pokedex;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pokedex.db.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetApiPokemon extends AsyncTask<String, Integer, String> {

    private String rawJSON;
    private GetApiPokemon.OnPokemonListComplete mCallback;
    private int numOfAPIPokemon = 151;

    public interface OnPokemonListComplete{
        void processPokemonList(Pokemon[] pokemonList);
    }

    public void setOnPokemonListComplete(GetApiPokemon.OnPokemonListComplete listener){
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
            //set our https connection and accessing our first element sent
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/?offset=" + 0 + "&limit=" + numOfAPIPokemon);
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
                    rawJSON = rawJSON.substring(104, rawJSON.length());
                    rawJSON = rawJSON.substring(0, rawJSON.length()-1);

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

        Pokemon[] pokemonArray;
        try{
            pokemonArray = parseJson(result);
            if(pokemonArray  != null){
                //send our courses to our DB
                if(mCallback != null && mCallback instanceof GetApiPokemon.OnPokemonListComplete){
                    mCallback.processPokemonList(pokemonArray);
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
     * @param r raw JSON
     * @return a list of assignments on success
     */
    private Pokemon[] parseJson(String r){
        //to parse gson, we need builder -> we then use builder to create gson object
        //then use gson to to parse our raw json and use that to create our courses.
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Pokemon[] pokemonArray = null;
        try{
            pokemonArray = gson.fromJson(r, Pokemon[].class);
        }catch (Exception e){
            Log.d("jsonarrayerorr", e.getMessage());
        }
        return pokemonArray;
    }
}
