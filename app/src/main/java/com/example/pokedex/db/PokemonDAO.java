package com.example.pokedex.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PokemonDAO {

    @Query("SELECT * FROM Pokemon")
    LiveData<List<Pokemon>> getAll();

    @Query("SELECT name from Pokemon WHERE _id = :pokemonId")
    String getPokemonName(int pokemonId);

    @Query("SELECT hp from Pokemon WHERE _id = :pokemonId")
    String getPokemonHp(int pokemonId);

    @Query("SELECT * from Pokemon WHERE _id = :pokemonId")
    Pokemon getPokemonWithId(int pokemonId);

    @Update
    void update(Pokemon pokemon);

    //batch insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonList(List<Pokemon> list);

}
