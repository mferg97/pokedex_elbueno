package com.example.pokedex;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pokedex.db.Pokemon;

public class PokemonBasestatsFragment extends Fragment {

    private View root;
    private Pokemon pokemon;

    public PokemonBasestatsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_pokemon_basestats, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        //checks if our arguments are set or not
        if(getArguments() != null && getArguments().containsKey("pokemon")) {
            pokemon = (Pokemon) getArguments().getSerializable("pokemon");
        }

        if(pokemon != null){
            TextView tvHp = (TextView) root.findViewById(R.id.textView_basestats_hp);
            TextView tvAttack = (TextView) root.findViewById(R.id.textView_basestats_attack);
            TextView tvDefense = (TextView) root.findViewById(R.id.textView_basestats_defense);
            TextView tvSpAttack = (TextView) root.findViewById(R.id.textView_basestats_spattack);
            TextView tvsSpDefense = (TextView) root.findViewById(R.id.textView_basestats_spdefense);
            TextView tvSpeed = (TextView) root.findViewById(R.id.textView_basestats_speed);
            TextView tvTotal = (TextView) root.findViewById(R.id.textView_basestats_total);

            int hp = Integer.parseInt(pokemon.getHp());
            int attack = Integer.parseInt(pokemon.getAttack());
            int defense = Integer.parseInt(pokemon.getDefence());
            int spAttack = Integer.parseInt(pokemon.getSpAttack());
            int spDefense = Integer.parseInt(pokemon.getSpDef());
            int speed = Integer.parseInt(pokemon.getSpeed());
            int total = hp + attack + defense + spAttack + spDefense + speed;

            ProgressBar pbHp = (ProgressBar)root.findViewById(R.id.progressBar_hp);
            ProgressBar pbAttck = (ProgressBar)root.findViewById(R.id.progressBar_attack);
            ProgressBar pbDefense = (ProgressBar)root.findViewById(R.id.progressBar_defense);
            ProgressBar pbSpAttck = (ProgressBar)root.findViewById(R.id.progressBar_spattack);
            ProgressBar pbSpDefense = (ProgressBar)root.findViewById(R.id.progressBar_spdefense);
            ProgressBar pbSpeed = (ProgressBar)root.findViewById(R.id.progressBar_speed);

            tvHp.setText(pokemon.getHp());
            tvAttack.setText(pokemon.getAttack());
            tvDefense.setText(pokemon.getDefence());
            tvSpAttack.setText(pokemon.getSpAttack());
            tvDefense.setText(pokemon.getSpDef());
            tvSpeed.setText(pokemon.getSpeed());
            tvTotal.setText(total+"");

            setProgessAndColor(pbHp, pokemon.getHp(), pokemon.getTypeColor(pokemon.getType_1()));
            setProgessAndColor(pbAttck, pokemon.getAttack(), pokemon.getTypeColor(pokemon.getType_1()));
            setProgessAndColor(pbDefense, pokemon.getDefence(), pokemon.getTypeColor(pokemon.getType_1()));
            setProgessAndColor(pbSpAttck, pokemon.getSpAttack(), pokemon.getTypeColor(pokemon.getType_1()));
            setProgessAndColor(pbSpDefense, pokemon.getSpDef(), pokemon.getTypeColor(pokemon.getType_1()));
            setProgessAndColor(pbSpeed, pokemon.getSpeed(), pokemon.getTypeColor(pokemon.getType_1()));
        }

    }

    /**
     * sets progress bar passed in with the string value passed in
     * sets color to the color of the type passed
     * @param pBar progress bar to be set and colored
     * @param p string representation of progress amount
     * @param type type of pokemon, used to color progress bar
     */
    private void setProgessAndColor(ProgressBar pBar, String p, int type){
        int progress = Integer.parseInt(p);
        pBar.setMax(160);
        pBar.setProgress(progress);

        pBar.getProgressDrawable().setColorFilter(getResources().getColor(type), PorterDuff.Mode.SRC_IN);
    }
}
