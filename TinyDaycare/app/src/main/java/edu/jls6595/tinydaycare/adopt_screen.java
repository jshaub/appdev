package edu.jls6595.tinydaycare;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static edu.jls6595.tinydaycare.Pokemon.PokemonType.LARGE;
import static edu.jls6595.tinydaycare.Pokemon.PokemonType.MEDIUM;
import static edu.jls6595.tinydaycare.Pokemon.PokemonType.SMALL;

public class adopt_screen extends AppCompatActivity {

    PokemonDB pokemonDB;
    SQLiteDatabase database;
    private PokemonList pList;

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
        pList = pList.getInstance();
        pokemonDB = PokemonDB.getInstance(this);

        pokemonDB.getDatabase(new PokemonDB.OnDBReadyListener() {
            @Override
            public void onDBReady(SQLiteDatabase db) {
                database = db;
            }
        });
    }

    /* Create a new Pokemon object and add it to the database */
    // TODO: Implement check to see if user has enough CareCoins
    public void eggClick(View view) {
        int eggType = view.getId();

        if (pList.getCurrentIndex() == pList.getMaxSize()) {
            Toast.makeText(this, "You cannot adopt any more Pokemon", Toast.LENGTH_SHORT).show();
            return;
        }

        switch(eggType) {
            case R.id.smallEgg:
                Log.d("eggType", "small egg clicked");
                new Pokemon(SMALL, pokemonDB, this);
                Toast.makeText(this, "You adopted a small-sized Pokemon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mediumEgg:
                Log.d("eggType", "medium egg clicked");
                new Pokemon(MEDIUM, pokemonDB, this);
                Toast.makeText(this, "You adopted a medium-sized Pokemon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.largeEgg:
                Log.d("eggType", "large egg clicked");
                new Pokemon(LARGE, pokemonDB, this);
                Toast.makeText(this, "You adopted a large-sized Pokemon!", Toast.LENGTH_SHORT).show();
                break;
        }

        return;
    }

}
