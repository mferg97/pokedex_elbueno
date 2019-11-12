package com.example.pokedex;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.pokedex.db.AppDatabase;
import com.example.pokedex.db.Pokemon;

import java.util.List;

/**
 * ViewModel that returns all of the entries in our database as
 * an arrayList -- used to populate ArrayList in out recycle view adapter
 * and then is displayed in ViewHolders
 */
public class AllPokemonViewModel extends ViewModel {
    private LiveData<List<Pokemon>> pokemonList;

    public LiveData<List<Pokemon>> getPokemonList(Context c){
        if(pokemonList != null){
            return pokemonList;
        }

        return pokemonList = AppDatabase.getInstance(c).pokemonDAO().getAll();
    }
}