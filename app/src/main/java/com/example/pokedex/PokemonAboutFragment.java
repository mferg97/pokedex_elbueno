package com.example.pokedex;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokedex.db.Pokemon;

public class PokemonAboutFragment extends Fragment {

    private View root;
    private Pokemon pokemon;

    public PokemonAboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_pokemon_about, container, false);

        //checks if our arguments are set or not
        if(getArguments() != null && getArguments().containsKey("pokemon")) {
            pokemon = (Pokemon) getArguments().getSerializable("pokemon");
        }

        return root;

    }

    @Override
    public void onResume() {
        super.onResume();

        TextView tvType1 = (TextView)root.findViewById(R.id.tvAbout_type1);
        TextView tvType2 = (TextView)root.findViewById(R.id.tvAbout_type2);
        TextView tvHeight = (TextView)root.findViewById(R.id.tvAbout_height);
        TextView tvWeight = (TextView)root.findViewById(R.id.tvAbout_weight);
        TextView tvAbilities = (TextView)root.findViewById(R.id.tvAbout_abilities);

        //sets textviews in layout based on types
        if (pokemon.getType_2() == null){
            tvType2.setVisibility(View.GONE);
            setTextviewColor(tvType1, pokemon.getType_1());
        }else{
            setTextviewColor(tvType1, pokemon.getType_1());
            setTextviewColor(tvType2, pokemon.getType_2());
        }

        double pokemonHeight = (Integer.parseInt(pokemon.getHeight()));
        tvHeight.setText(metersToFeetInchesAsString(pokemonHeight/10));

        double pokemonWeight = (Integer.parseInt(pokemon.getWeight()));
        tvWeight.setText(kgToLbsString(pokemonWeight/10));

        String tempAbilities = buildAbilityString(pokemon.getAbility_1(), pokemon.getAbility_2(), pokemon.getAbility_3());
        tvAbilities.setText(tempAbilities);
    }

    /**
     * Sets the background color of the type textviews
     * @param view textview to be colored
     * @param type type of pokemon, used to color textview
     */
    private void setTextviewColor(TextView view, String type){
        view.setTextColor(Color.WHITE);
        int typeColor = getResources().getColor(pokemon.getTypeColor(type));
        String hexTypeColor = "#"+Integer.toHexString(typeColor);
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(Color.parseColor(hexTypeColor));

        type = type.substring(0, 1).toUpperCase() + type.substring(1);
        view.setText(type.toString());
    }

    /**
     * Builds a string based based on number of meters passed in
     * @param hInMeters height in meters
     * @return returns string containing meters and feet-inches
     */
    private String metersToFeetInchesAsString(double hInMeters){
        double tempInches = hInMeters*39.37;
        int feet = (int) Math.floor(tempInches/12);
        tempInches -= 12 * feet;
        int inches = (int) Math.floor(tempInches);

        return hInMeters + " m " + " (" + feet + "\'" + String.format("%02d", inches) + "\"" + ")";
    }

    /**
     * Builds string that displays kg and pounds
     * @param wInKg weight in kilograms
     * @return returns build string of kg and pounds
     */
    private String kgToLbsString(double wInKg){
        double pounds = wInKg * 2.20462;
        return wInKg + " kg " + "(" + String.format("%.1f", pounds) + " lbs" + ")";
    }

    /**
     * Builds a sting based on abilities of the pokemon
     * Note that a pokemon has only up to 3 abilities
     * @param ability   pokemon ability 1
     * @param ability2  pokemon ability 2
     * @param ability3  pokemon ability 3
     * @return returns string based on ability values
     */
    private String buildAbilityString(String ability, String ability2, String ability3){
        if(ability3 != null){
            return ability + ", " + ability2 + ", " + ability3;
        }else if (ability2 != null) {
            return ability + ", " + ability2;
        }else {
            return ability;
        }
    }

}
