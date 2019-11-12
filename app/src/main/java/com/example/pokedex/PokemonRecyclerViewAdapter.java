package com.example.pokedex;



import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pokedex.db.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<PokemonRecyclerViewAdapter.ViewHolder> {

    private final List<Pokemon> pokemonList;
    private DisplayPokemonCardCallback mCallback;
    private boolean displayShiny;

    interface DisplayPokemonCardCallback {
        void onPokemonCardClickCallback(int pokemonId, String name);
    }
    public PokemonRecyclerViewAdapter(List<Pokemon> pokemonList, DisplayPokemonCardCallback displayPokemonCard) {
        this.pokemonList = pokemonList;
        mCallback = displayPokemonCard;
    }

    /**
     * creates a view holder for our recycle item view
     * @param viewGroup default value
     * @param i default value
     * @return a new ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Sets our TextView's in recycle_item.xml to be populated
     * with our database info
     * @param viewHolder default
     * @param i default
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Pokemon pokemon = pokemonList.get(i);

        AssetManager am = viewHolder.view.getContext().getAssets();
        InputStream inputStream = null;

        //set stream to according on what sprites are to be displayed
        if(pokemon != null) {
            try {
                if(displayShiny) {
                    inputStream = am.open("shiny/" + pokemon.get_id() + ".png");
                }else {
                    inputStream = am.open("normal/"+pokemon.get_id()+".png");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //sets our pixel density for our bitmap
            BitmapFactory.Options opts = new BitmapFactory.Options();
            ((BitmapFactory.Options) opts).inDensity = DisplayMetrics.DENSITY_DEFAULT;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opts);

            viewHolder.pokemon = pokemon;
            viewHolder.txtLine.setText(pokemon.getName());
            viewHolder.image.setImageBitmap(bitmap);
        }

        //listens for when our card is clicked them calls back to main activity
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pokemon != null){
                    mCallback.onPokemonCardClickCallback(pokemon.get_id(), pokemon.getName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    /**
     * clears our old list, and adds all items
     * from passed in list to old arraylist
     * then notifies system that data has now changed!!
     * @param courseList new arraylist with updated values
     */
    public void addItems(List<Pokemon> courseList){
        pokemonList.clear();
        pokemonList.addAll(courseList);
        notifyDataSetChanged();
    }

    /**
     * sets boolean from our List fragment
     * @param b state of our toggle switch
     */
    public void setShinyBool(boolean b){
        this.displayShiny = b;
        notifyDataSetChanged();
    }

    /**
     * view holder class that initialzes our TextView's viewholder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtLine;
        public ImageView image;
        public LinearLayout layout;

        public Pokemon pokemon;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            txtLine = (TextView) itemView.findViewById(R.id.line1);
            image = (ImageView) itemView.findViewById(R.id.imgView);
            layout = (LinearLayout) itemView.findViewById(R.id.recycler_layout);

        }
    }
}
