package com.example.pokedex;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends DialogFragment {

    private View root;
    private boolean toggleVal;
    private OnToggle mCallback;

    public interface OnToggle{
        void onToggleRecreateList(boolean v);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (OnToggle) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement the OnToggle Interface");
        }
    }

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar_settings);
        toolbar.hideOverflowMenu();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dismiss();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        //saves the toggle switch value to properly set checked when in settings fragment
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        toggleVal = sp.getBoolean("toggleVal", false);

        SwitchCompat toggleSprite = (SwitchCompat)root.findViewById(R.id.settings_switch);
        toggleSprite.setChecked(toggleVal);
        toggleSprite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleVal = !toggleVal;

                //save updated switch value
                SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor spEdit = sp.edit();
                spEdit.putBoolean("toggleVal", toggleVal);
                spEdit.apply();

                //notify RecyclerView that we are changing what sprites we are loading in views
                mCallback.onToggleRecreateList(toggleVal);
            }
        });

    }


}
