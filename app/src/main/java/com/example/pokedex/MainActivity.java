package com.example.pokedex;

import android.content.Context;
import android.content.SharedPreferences;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pokedex.db.AppDatabase;
import com.example.pokedex.db.Pokemon;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements PokemonListFragment.DisplayPokemonInfo,
    PokemonInfoFragment.OnPokemonInfoClose,
    SettingsFragment.OnToggle{

    private FragmentManager fragMan;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //allows us to use splash screen on normal boots
        setTheme(R.style.AppTheme);
        //doesn't move on until pokemon list is drawn
        reportFullyDrawn();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragMan = getSupportFragmentManager();
        SharedPreferences sp = getPreferences( Context.MODE_PRIVATE);
        boolean coldBoot = sp.getBoolean("cold_boot", true);

        //migrates our db on initial download
        if(coldBoot){
            onColdBoot();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_menu_settings:
                SettingsFragment settingsFrag = new SettingsFragment();
                fragMan.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, settingsFrag)
                        .addToBackStack("settingsFrag")
                        .commit();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets pokemon list if this is the first time
     * the app has been booted up and adds pokemon to db
     */
    private void onColdBoot() {
        GetApiPokemon task =  new GetApiPokemon();
        task.setOnPokemonListComplete(new GetApiPokemon.OnPokemonListComplete() {
            @Override
            public void processPokemonList(Pokemon[] pList) {
                //Log.d("pokemon", "processPokemonList: " + pList[1].toString());
                if(pList != null){
                    final ArrayList<Pokemon> pokemonList = new ArrayList<>();
                    for(Pokemon p: pList){
                        pokemonList.add(p);
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getInstance(getApplicationContext())
                                    .pokemonDAO()
                                    .insertPokemonList(pokemonList);
                        }
                    }).start();
                }
            }
        });

        task.execute("");

        //setting our boolean flag that we have now hit a cold boot operation
        SharedPreferences sp = getPreferences( Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean("cold_boot", false);
        spEdit.apply();
    }

    /**
     * Implemented interface From Pokemon list that passes
     * pokemon name and id to pokemonInfo
     * @param pokemonId
     * @param name
     */
    @Override
    public void onPokemonClick(final int pokemonId, final String name) {
        final GetPokemonInfo task = new GetPokemonInfo();
        task.setOnPokemonInfoComplete(new GetPokemonInfo.OnPokemonInfoComplete() {
            @Override
            public void processPokemonInfo(final Pokemon p) {
                PokemonInfoFragment infoFragment = new PokemonInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("pokemon", p);
                infoFragment.setArguments(bundle);

                fragMan.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, infoFragment)
                        .addToBackStack("infoFrag")
                        .commit();
            }
        });
        task.execute(pokemonId+"" , name);
    }

    /**
     * Implemented interface from PokemonInfo
     * Called when back arrow is clicked to remove fragment
     */
    @Override
    public void onPokemonInfoClose() {
        fragMan.popBackStack();
    }


    /**
     * Implemented interface from settings fragment
     * notifies PokemonInfo that sprites will be changed
     * @param v boolean switch value
     */
    @Override
    public void onToggleRecreateList(boolean v) {
        PokemonListFragment pfrag = (PokemonListFragment) fragMan.findFragmentById(R.id.pokemonListFrag);
        if(pfrag != null){
            pfrag.notifyAdapterForBoolChange(v);
        }
    }
}
