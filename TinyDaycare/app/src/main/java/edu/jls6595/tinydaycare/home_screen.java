package edu.jls6595.tinydaycare;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.jls6595.tinydaycare.TinyDaycareService.ServiceEventListener;

public class home_screen extends AppCompatActivity {

    private User user;
    private PokemonList pList;
    private Pokemon currentPokemon;
    private ImageView selectedPokemon;
    private TextView careCoinsView;
    private TextView currentStepsView;
    private TextView stepsNeededView;
    private boolean serviceBound = false;
    private TinyDaycareService tdService;
    private SQLiteDatabase database;

    // Callback for TinyDaycareService
    private ServiceConnection serviceConn;

    private ServiceEventListener serviceListener;

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

        careCoinsView = findViewById(R.id.user_carecoins);
        currentStepsView = findViewById(R.id.user_steps);
        stepsNeededView = findViewById(R.id.steps_required);

        user = User.getInstance();
        pList = PokemonList.getInstance();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem item = navigation.getMenu().findItem(R.id.navigation_home);
        item.setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("homescreen", "resuming");
        final String[] columns = {"id"};
        final String selection = "current = 1";

        super.onResume();



        // Get database instance
        PokemonDB.getInstance(this).getDatabase(new PokemonDB.OnDBReadyListener() {
            @Override
            public void onDBReady(SQLiteDatabase db) {
                database = db;
                Log.d("homescreen", "database ready");

                long numTuples = DatabaseUtils.queryNumEntries(database, "pokemon");
                if(numTuples > 0 && !pList.isLoaded()) {
                    Log.d("homescreen", "loading pokemon");
                    loadPokemonFromDB();
                }

                Cursor c = database.query("pokemon", columns, selection, null, null, null, null);
                if(c.moveToFirst()) {
                    currentPokemon = PokemonList.getList()[c.getInt(c.getColumnIndexOrThrow("id"))];
                    PokemonList.getInstance().updateCurrentPokemon(currentPokemon);
                }

                Log.d("homescreen", "currentPokemon = " + currentPokemon);
                Log.d("homescreen", "calling continueOnResume");
                continueOnResume();
            }
        });

        /*
        selectedPokemon = findViewById(R.id.selected_creature);

        // Set CareCoins
        careCoinsView.setText(String.valueOf(user.getNumCareCoins()));

        Log.d("homescreen", "loading current pokemon, if it exists");

        // Load current pokemon
        if(currentPokemon != null) {
            currentPokemon = PokemonList.currentPokemon;
            currentPokemon.setImageView(selectedPokemon);

            findViewById(R.id.new_player_message).setVisibility(TextView.GONE);

            if(currentPokemon.isHatched()) {
                findViewById(R.id.hatch_button).setVisibility(Button.GONE);
                findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.GONE);
                findViewById(R.id.button_viewGroup).setVisibility(ViewGroup.VISIBLE);
            }
            else if(currentPokemon.isReadyToHatch()) {
                findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.GONE);
                findViewById(R.id.hatch_button).setVisibility(Button.VISIBLE);
            }
            else {
                currentStepsView.setText(String.valueOf(currentPokemon.getCurrentSteps()));
                stepsNeededView.setText(String.valueOf(currentPokemon.getStepsNeeded()));

                findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.VISIBLE);

                // Start tracking steps
                startStepService();
            }
        }
        else {
            currentStepsView.setText(String.valueOf(0));
            findViewById(R.id.new_player_message).setVisibility(TextView.VISIBLE);
            findViewById(R.id.selected_creature).setVisibility(ImageView.GONE);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Stop TinyDaycareService
        if(tdService != null) {
            tdService.stopStepTracker();
            unbindService(serviceConn);
        }
    }

    void continueOnResume() {
        selectedPokemon = findViewById(R.id.selected_creature);

        // Set CareCoins
        careCoinsView.setText(String.valueOf(user.getNumCareCoins()));

        Log.d("homescreen", "loading current pokemon, if it exists");

        // Load current pokemon
        if(currentPokemon != null) {
            currentPokemon = PokemonList.currentPokemon;
            currentPokemon.setImageView(selectedPokemon);

            findViewById(R.id.new_player_message).setVisibility(TextView.GONE);

            if(currentPokemon.isHatched()) {
                findViewById(R.id.hatch_button).setVisibility(Button.GONE);
                findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.GONE);
                findViewById(R.id.button_viewGroup).setVisibility(ViewGroup.VISIBLE);
            }
            else if(currentPokemon.isReadyToHatch()) {
                findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.GONE);
                findViewById(R.id.hatch_button).setVisibility(Button.VISIBLE);
            }
            else {
                currentStepsView.setText(String.valueOf(currentPokemon.getCurrentSteps()));
                stepsNeededView.setText(String.valueOf(currentPokemon.getStepsNeeded()));

                findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.VISIBLE);

                // Start tracking steps
                startStepService();
            }
        }
        else {
            currentStepsView.setText(String.valueOf(0));
            findViewById(R.id.new_player_message).setVisibility(TextView.VISIBLE);
            findViewById(R.id.selected_creature).setVisibility(ImageView.GONE);
        }
    }

    void loadPokemonFromDB() {
        int current, hatched, readyToHatch, currentSteps, eggSprite, hatchedSprite, cost;
        String type;
        String[] columns = {"current, hatched", "readyToHatch", "currentSteps", "eggSprite", "hatchedSprite",
                            "type", "cost"};

        Cursor c = database.query("pokemon", columns, null, null, null, null, null);
        while(c.moveToNext()) {
            current = c.getInt(c.getColumnIndexOrThrow("current"));
            hatched = c.getInt(c.getColumnIndexOrThrow("hatched"));
            readyToHatch = c.getInt(c.getColumnIndexOrThrow("readyToHatch"));
            currentSteps = c.getInt(c.getColumnIndexOrThrow("currentSteps"));
            eggSprite = c.getInt(c.getColumnIndexOrThrow("eggSprite"));
            hatchedSprite = c.getInt(c.getColumnIndexOrThrow("hatchedSprite"));
            type = c.getString(c.getColumnIndexOrThrow("type"));
            cost = c.getInt(c.getColumnIndexOrThrow("cost"));

            new Pokemon(current, hatched, readyToHatch, currentSteps, eggSprite, hatchedSprite, type, cost);
        }
        c.close();
    }

    void startStepService() {
        Log.d("homescreen", "setting serviceConn");
        serviceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d("homescreen", "onServiceConnected callback received");
                TinyDaycareService.LocalBinder binder = (TinyDaycareService.LocalBinder) iBinder;
                tdService = ((TinyDaycareService.LocalBinder) iBinder).getService();
                tdService.setServiceEventListener(serviceListener);
                tdService.startStepTracker();
                serviceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceBound = false;
            }
        };

        Log.d("homescreen", "setting serviceListener");
        serviceListener = new ServiceEventListener() {
            @Override
            public void onServiceEvent() {
                ContentValues values = new ContentValues();
                int currentSteps = currentPokemon.getCurrentSteps();
                int neededSteps = currentPokemon.getStepsNeeded();
                int id = currentPokemon.getId();

                Log.d("homescreen", "callback to onServiceEvent received");
                Log.d("homescreen", "updating current steps");

                Log.d("homescreen", "currentSteps = " + currentPokemon.getCurrentSteps());
                currentStepsView.setText(String.valueOf(currentPokemon.getCurrentSteps()));

                String selection = "id = " + id;
                values.put("currentSteps", currentPokemon.getCurrentSteps());
                database.update("pokemon", values, selection, null);

                if (currentSteps == neededSteps) {
                    findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.GONE);
                    findViewById(R.id.hatch_button).setVisibility(ViewGroup.VISIBLE);
                    tdService.stopStepTracker();
                }
            }
        };

        // Start TinyDaycareService
        Log.d("homescreen", "starting service");
        if(!serviceBound) {
            Intent intent = new Intent(this, TinyDaycareService.class);
            bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
        }

        Log.d("homescreen", "service bound");
    }

    public void buttonHatchClick(View view) {
        Log.d("homescreen", "Hatch button clicked");

        findViewById(R.id.steps_viewGroup).setVisibility(ViewGroup.GONE);
        findViewById(R.id.hatch_button).setVisibility(Button.GONE);
        findViewById(R.id.button_viewGroup).setVisibility(ViewGroup.VISIBLE);

        currentPokemon.hatch(findViewById(R.id.selected_creature));

        // Update database
        ContentValues values = new ContentValues();
        int id = currentPokemon.getId();
        String selection = "id = " + id;
        values.put("hatched", 1);
        database.update("pokemon", values, selection, null);
    }

    public void buttonPlayClick(View view) {
        Log.d("homescree", "Play button clicked");

        currentPokemon.play();
        careCoinsView.setText(String.valueOf(user.getNumCareCoins()));
    }

    public void buttonFeedClick(View view) {
        Log.d("homescreen", "Feed button clicked");

        currentPokemon.feed();
        careCoinsView.setText(String.valueOf(user.getNumCareCoins()));
    }
}
