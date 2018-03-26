package edu.jls6595.tinydaycare;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class home_screen extends AppCompatActivity {

    private PokemonDB pokemonDB;
    private SQLiteDatabase database;
    private Sensor stepTracker;
    private SensorManager sManager;
    private SensorEventListener stepEventListener;
    private static PokemonList pList;
    private Pokemon currentPokemon;
    private TextView currentStepsView;
    private TextView stepsNeededView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_collection:
                    Intent collection_intent = new Intent(getBaseContext(), collection_screen.class);
                    startActivity(collection_intent);
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
        setContentView(R.layout.activity_home_screen);

        currentStepsView = findViewById(R.id.user_steps);
        stepsNeededView = findViewById(R.id.steps_required);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem item = navigation.getMenu().findItem(R.id.navigation_home);
        item.setChecked(true);

        Log.d("homescreen", "Setting listener for step event");
        sManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        stepEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d("homescreen", "sensor event received");

                switch(sensorEvent.sensor.getType()) {
                    case Sensor.TYPE_STEP_DETECTOR:
                        if(currentPokemon != null && !currentPokemon.isHatched()) {
                                currentPokemon.updateCurrentSteps((Button)findViewById(R.id.hatch_button));
                                currentStepsView.setText((String.valueOf(currentPokemon.getCurrentSteps())));
                        }
                        break;
                    default:
                        Log.wtf("homescreen", "Fatal error in SensorEventListener onSensorChanged()");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // no operations
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView selectedPokemon = findViewById(R.id.selected_creature);
        PokemonList.getInstance((TextView)findViewById(R.id.user_steps));
        pokemonDB = PokemonDB.getInstance(this);

        pokemonDB.getDatabase(new PokemonDB.OnDBReadyListener() {
            @Override
            public void onDBReady(SQLiteDatabase db) {
                database = db;
            }
        });

        // Start detecting sensor events
        sManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if (sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            stepTracker= sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            sManager.registerListener(stepEventListener, stepTracker, SensorManager.SENSOR_DELAY_GAME);
        }

        Log.d("homescreen", "loading current pokemon, if it exists");
        // Load current pokemon
        if(PokemonList.getInstance().getCurrentIndex() > 0) {
            currentPokemon = PokemonList.currentPokemon;
            currentPokemon.setImageView(selectedPokemon);

            currentStepsView.setText(String.valueOf(currentPokemon.getCurrentSteps()));
            stepsNeededView.setText(String.valueOf(currentPokemon.getStepsNeeded()));

            if(currentPokemon.getCurrentSteps() == currentPokemon.getStepsNeeded() && !currentPokemon.isHatched()) {
                findViewById(R.id.hatch_button).setVisibility(View.VISIBLE);
            }

            findViewById(R.id.new_player_message).setVisibility(View.GONE);
            findViewById(R.id.selected_creature).setVisibility(View.VISIBLE);

            if(!currentPokemon.isHatched()) {
                findViewById(R.id.steps_viewGroup).setVisibility(View.VISIBLE);
                findViewById(R.id.button_viewGroup).setVisibility(View.GONE);
            }
            else {
                findViewById(R.id.steps_viewGroup).setVisibility(View.GONE);
                findViewById(R.id.button_viewGroup).setVisibility(View.VISIBLE);
            }

        }
        else {
            findViewById(R.id.new_player_message).setVisibility(View.VISIBLE);
            findViewById(R.id.selected_creature).setVisibility(View.GONE);
        }
    }

    public void buttonHatchClick(View view) {
        Log.d("homescreen", "Hatch button clicked");

        findViewById(R.id.steps_viewGroup).setVisibility(View.GONE);
        findViewById(R.id.hatch_button).setVisibility(View.GONE);
        findViewById(R.id.button_viewGroup).setVisibility(View.VISIBLE);

        currentPokemon.hatch(findViewById(R.id.selected_creature));
    }

}
