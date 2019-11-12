package com.example.pokedex;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokedex.db.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class PokemonListFragment extends Fragment
        implements PokemonRecyclerViewAdapter.DisplayPokemonCardCallback{

    private RecyclerView recyclerView;
    private PokemonRecyclerViewAdapter adapter;
    private int columnCount = 3;
    private View root;
    private boolean toggleVal;
    private DisplayPokemonInfo mCallback;

    interface DisplayPokemonInfo {
        void onPokemonClick(int pokemonId, String name);
    }

    public PokemonListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (DisplayPokemonInfo) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the DisplayPokemonInfo Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pokemon_list, container, false);
        Toolbar toolbar = (Toolbar)root.findViewById(R.id.toolbar_main);
        setHasOptionsMenu(true);

        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        toggleVal = sp.getBoolean("toggleVal", false);

        recyclerView = (RecyclerView) root.findViewById(R.id.rvPokemonList);
        return root;
    }

    /**
     * Creates a new adapter and passes in a blank array list along with call back variable
     * sets our column count (can be done dynamically for different devices)
     * Queries our database through AllCoursesViewModel and creates a new list with updated db vals
     */
    @Override
    public void onResume() {
        super.onResume();

        Context context = getContext();
        adapter = new PokemonRecyclerViewAdapter(new ArrayList<Pokemon>(), this);
        if(columnCount  <= 1){
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }


        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false); //dynamic data!!

        ViewModelProviders.of(this)
                .get(AllPokemonViewModel.class)
                .getPokemonList(getContext())
                .observe(this, new Observer<List<Pokemon>>() {
                    @Override
                    public void onChanged(@Nullable List<Pokemon> pokemonList) {
                        if(pokemonList != null){
                            adapter.addItems(pokemonList);
                            adapter.setShinyBool(toggleVal);
                        }
                    }});

    }

    /**
     * notifies main activity of what view was clicked
     * from recycler view
     * @param pokemonId pokemon id from recycler view that was clicked on
     * @param name name of pokemon clicked on
     */
    @Override
    public void onPokemonCardClickCallback(int pokemonId, String name) {
        mCallback.onPokemonClick(pokemonId, name);
    }

    /**
     * notifies adapter that the displayed sprite has changed
     * @param b state of the toggle switch to be passed into
     */
    public void notifyAdapterForBoolChange(final boolean b){
        adapter.setShinyBool(b);

        //setting our boolean value for how we display shiny images
        SharedPreferences sp = getActivity().getPreferences( Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean("toggleVal", b);
        spEdit.apply();
    }
}
