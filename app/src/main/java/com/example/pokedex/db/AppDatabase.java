package com.example.pokedex.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Pokemon.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //a single static variable to only have one instance to store
    private static AppDatabase instance;
    public static  AppDatabase getInstance(Context context){
        if(instance != null){
            return instance;
        }

        //factory that knows how to build a database -> .build it
        instance = Room.databaseBuilder(context, AppDatabase.class, "database").build();
        return instance;
    }

    public abstract PokemonDAO pokemonDAO();
}
