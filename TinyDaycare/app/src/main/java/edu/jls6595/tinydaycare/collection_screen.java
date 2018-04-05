package edu.jls6595.tinydaycare;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import static edu.jls6595.tinydaycare.PokemonList.getInstance;

public class collection_screen extends AppCompatActivity {

    // Used to register a callback when the currently selected pokemon is changed
    public interface OnPokemonChange {
        void changeComplete();
    }

    private final int MAX_SIZE = 15;
    PokemonList pList;
    private static OnPokemonChange listener;
    static Pokemon tappedPokemon;
    ImageView tappedPokemonView;

    private static ImageView[] iViewArray;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home_intent = new Intent(getBaseContext(), home_screen.class);
                    startActivity(home_intent);
                    break;
                case R.id.navigation_adopt:
                    Intent adopt_intent = new Intent(getBaseContext(), adopt_screen.class);
                    startActivity(adopt_intent);
                    break;
                case R.id.navigation_settings:
                    Intent settings_intent = new Intent(getBaseContext(), settings_screen.class);
                    startActivity(settings_intent);
                    break;
                default:
                    Log.e("error on bottom nav", "ItemID didn't match any bottom nav IDs");
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_screen);

        iViewArray = new ImageView[MAX_SIZE];
        createImageViewArray(iViewArray);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem item = navigation.getMenu().findItem(R.id.navigation_collection);
        item.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int i;
        int pListIndex = getInstance().getCurrentIndex();
        //Log.d("collection", "Getting pList instance");
        pList = pList.getInstance();

        //Log.d("collection", "Starting for loop");
        for(i=0; i<pListIndex; i++) {
            //Log.d("collection", "i = " + i);
            pList.getList()[i].setImageView(iViewArray[i]);
            //Log.d("collection", "setImageView returned");
            //Log.d("collection", "setting visibility");
            iViewArray[i].setVisibility(View.VISIBLE);
            iViewArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pokemonPress(view);
                }
            });
        }
    }

    public void setOnPokemonChangeListener(OnPokemonChange listener) {
        this.listener = listener;
    }

    private void createImageViewArray(ImageView[] array) {
        Log.d("collection", "Creating ImageView Array");
        array[0] = findViewById(R.id.pokemon00);
        array[1] = findViewById(R.id.pokemon01);
        array[2] = findViewById(R.id.pokemon02);
        array[3] = findViewById(R.id.pokemon10);
        array[4] = findViewById(R.id.pokemon11);
        array[5] = findViewById(R.id.pokemon12);
        array[6] = findViewById(R.id.pokemon20);
        array[7] = findViewById(R.id.pokemon21);
        array[8] = findViewById(R.id.pokemon22);
        array[9] = findViewById(R.id.pokemon30);
        array[10] = findViewById(R.id.pokemon31);
        array[11] = findViewById(R.id.pokemon32);
        array[12] = findViewById(R.id.pokemon40);
        array[13] = findViewById(R.id.pokemon41);
        array[14] = findViewById(R.id.pokemon42);
    }

    // When user presses on a creature or egg, this method is invoked
    public void pokemonPress(View v) {
        int index = findIndexOfImageViewArray(v);
        Log.d("collectionscreen", "index = " + index);

        if(index == -1) {
            Log.wtf("collectionscreen", "fatal error finding index in iViewArray");
        }

        tappedPokemon = PokemonList.getInstance().findPokemonAtIndex(index);
        Log.d("collectionscreen", "tappedPokemon = " + tappedPokemon);
        //tappedPokemonView = (ImageView) v;

        DialogFragment switchDialog = new SwitchDialog();
        switchDialog.show(getFragmentManager(), "confirmSwitch");
    }

    public int findIndexOfImageViewArray(View v) {
        int i;

        for(i=0; i<MAX_SIZE; i++) {
            if(iViewArray[i].getId() == v.getId()) {
                return i;
            }
        }

        return -1;
    }

    // When a user taps on a creature or egg that is not currently selected, show alert dialog asking if they really want to switch
    public static class SwitchDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.switch_dialog_title);
            builder.setMessage(R.string.switch_dialog_message);

            builder.setPositiveButton(R.string.switch_dialog_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    positivePress();
                }
            });

            builder.setNegativeButton(R.string.switch_dialog_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // User pressed "No", don't need to do anything
                }
            });

            return builder.create();
        }

        public void positivePress() {
            Log.d("positivePress", "Dialog successfully called positivePress()");
            Log.d("collectionscreen", "tappedPokemon = " + tappedPokemon);

            PokemonList.getInstance().updateCurrentPokemon(tappedPokemon);
            listener.changeComplete();
            Log.d("collectionscreen", "current Pokemon = " + PokemonList.currentPokemon);
        }
    }

}
