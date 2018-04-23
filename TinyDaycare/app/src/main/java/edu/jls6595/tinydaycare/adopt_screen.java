package edu.jls6595.tinydaycare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static edu.jls6595.tinydaycare.Pokemon.*;
import static edu.jls6595.tinydaycare.Pokemon.PokemonType.LARGE;
import static edu.jls6595.tinydaycare.Pokemon.PokemonType.MEDIUM;
import static edu.jls6595.tinydaycare.Pokemon.PokemonType.SMALL;

public class adopt_screen extends AppCompatActivity {

    TextView careCoins;
    PokemonDB pokemonDB;
    SQLiteDatabase database;
    private PokemonList pList;
    private long numAdopted;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home_intent = new Intent(getBaseContext(), home_screen.class);
                    startActivity(home_intent);
                    break;
                case R.id.navigation_collection:
                    Intent collection_intent = new Intent(getBaseContext(), collection_screen.class);
                    startActivity(collection_intent);
                    break;
                case R.id.navigation_settings:
                    Intent settings_intent = new Intent(getBaseContext(), settings_screen.class);
                    startActivity(settings_intent);
                    break;
                default:
                    Log.e("error on bottom nav", "ItemID didn't match any bottom nav IDs");
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_screen);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem item = navigation.getMenu().findItem(R.id.navigation_adopt);
        item.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get database instance
        PokemonDB.getInstance(this).getDatabase(new PokemonDB.OnDBReadyListener() {
            @Override
            public void onDBReady(SQLiteDatabase db) {
                database = db;
                Log.d("adopt", "database ready");
            }
        });

        pList = pList.getInstance();

        // Set CareCoins
        careCoins = findViewById(R.id.user_carecoins);
        careCoins.setText(String.valueOf(User.getInstance().getNumCareCoins()));
    }

    /* Create a new Pokemon object and add it to the database */
    // TODO: Implement check to see if user has enough CareCoins
    public void eggClick(View view) {
        ContentValues values = new ContentValues();
        Pokemon pokemon = null;
        int eggType = view.getId();

        if (pList.getCurrentIndex() == pList.getMaxSize()) {
            Toast.makeText(this, "You cannot adopt any more Pokemon", Toast.LENGTH_SHORT).show();
            return;
        }

        switch(eggType) {
            case R.id.smallEgg:
                if(User.getInstance().getNumCareCoins() < PokemonCost.COST_SMALL.getCost()) {
                    displayToast(R.string.careCoins_error);
                    return;
                }

                Log.d("eggType", "small egg clicked");
                pokemon = new Pokemon(SMALL, pokemonDB);
                displayToast(R.string.toast_adopt_small);
                break;
            case R.id.mediumEgg:
                if(User.getInstance().getNumCareCoins() < PokemonCost.COST_MEDIUM.getCost()) {
                    displayToast(R.string.careCoins_error);
                    return;
                }

                Log.d("eggType", "medium egg clicked");
                pokemon = new Pokemon(MEDIUM, pokemonDB);
                displayToast(R.string.toast_adopt_medium);
                break;
            case R.id.largeEgg:
                if(User.getInstance().getNumCareCoins() < PokemonCost.COST_LARGE.getCost()) {
                    displayToast(R.string.careCoins_error);
                    return;
                }

                Log.d("eggType", "large egg clicked");
                pokemon = new Pokemon(LARGE, pokemonDB);
                displayToast(R.string.toast_adopt_large);
                break;
        }

        values.put("id", pokemon.getId());
        values.put("current", pokemon.getCurrent());
        values.put("hatched", pokemon.getHatchedInt());
        values.put("readyToHatch", pokemon.getReadyToHatchInt());
        values.put("currentSteps", pokemon.getCurrentSteps());
        values.put("eggSprite", pokemon.getEggSprite());
        values.put("hatchedSprite", pokemon.getHatchedSprite());
        values.put("type", pokemon.getTypeString());
        values.put("cost", pokemon.getCost());
        database.insert("pokemon", null, values);

        User.getInstance().removeCareCoins(pokemon.getCost());
        careCoins.setText(String.valueOf(User.getInstance().getNumCareCoins()));

        return;
    }

    private void displayToast(int toastStringId) {
        Log.d("adopt", "Displaying toast");
        int toastLength = Toast.LENGTH_SHORT;
        int gravity = Gravity.BOTTOM;
        int xOff = 0, yOff = 225;

        Toast toast = Toast.makeText(this, toastStringId, toastLength);
        toast.setGravity(gravity, xOff, yOff);
        toast.show();
    }
}
