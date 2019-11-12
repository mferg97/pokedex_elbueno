package com.example.pokedex;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pokedex.db.Pokemon;

import java.io.IOException;
import java.io.InputStream;


/**
 * Fragment represents pokemon info and built for a single pokemon that was clicked on
 * builds color and tab layout
 */
public class PokemonInfoFragment extends Fragment {

    private View root;
    private Pokemon pokemon;
    private Toolbar toolbar;
    private OnPokemonInfoClose mCallback;

    public interface OnPokemonInfoClose{
        void onPokemonInfoClose();
    }

    public PokemonInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (OnPokemonInfoClose) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the OnPokemonInfoClose Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_pokemon_info, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //checks if our arguments are set or not
        if(getArguments() != null && getArguments().containsKey("pokemon")) {
            pokemon = (Pokemon) getArguments().getSerializable("pokemon");
        }

        toolbar = (Toolbar) root.findViewById(R.id.toolbar_info);
        toolbar.hideOverflowMenu();
        toolbar.setTitle("");
        setHasOptionsMenu(true);

        //sets up our 'X' out button in the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        return root;
    }

    /**
     * creates entire tab layout and creates pager adapter
     * @param view default
     * @param savedInstanceState default
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(pokemon !=null){
            PokemonInfoPagerAdapter adapter;
            TabLayout tabLayout;
            ViewPager viewPager;

            viewPager = (ViewPager) root.findViewById(R.id.viewPager);
            tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);

            Bundle bundle = new Bundle();
            bundle.putSerializable("pokemon", pokemon);

            PokemonAboutFragment aboutFragment = new PokemonAboutFragment();
            aboutFragment.setArguments(bundle);

            PokemonBasestatsFragment basestatsFragment = new PokemonBasestatsFragment();
            basestatsFragment.setArguments(bundle);

            //adding tab fragments to pager adapter
            adapter = new PokemonInfoPagerAdapter(getChildFragmentManager());
            adapter.addFragment(aboutFragment, "About");
            adapter.addFragment(basestatsFragment, "Base Stats");

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mCallback.onPokemonInfoClose();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        LinearLayout layout = (LinearLayout) root.findViewById(R.id.layout_info);
        layout.setBackgroundResource(pokemon.getTypeColor(pokemon.getType_1()));

        TextView tvName = (TextView) root.findViewById(R.id.textView_pokemonInfo);
        TextView tvNumber = (TextView) root.findViewById(R.id.textView_pokemonNumber);
        ImageView pokemonImg = (ImageView)root.findViewById(R.id.imageView_pokemonInfo);

        toolbar.setBackgroundResource(pokemon.getTypeColor(pokemon.getType_1()));
        tvName.setText(pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1));
        tvNumber.setText("#" + String.format("%03d", pokemon.get_id()));
        pokemonImg.setImageBitmap(getSpriteDrawbleFromAssets(pokemon.get_id()));

    }

    /**
     * Builds path to asset of pokemon to be display
     * and returns bitmap of official pokemon
     * @param id pokemon id to build inputstream path
     * @return bitmap of the pokemon, used for official pokemon
     */
    private Bitmap getSpriteDrawbleFromAssets(int id) {
        AssetManager am =  getContext().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("official/" + pokemon.get_id() + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        ((BitmapFactory.Options) opts).inDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opts);

        return bitmap;
    }
}

