package edu.jls6595.tinydaycare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static edu.jls6595.tinydaycare.Creature.CreatureType.SMALL;

public class adopt_screen extends AppCompatActivity {

    SQLiteDatabase database;

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
        CreatureDB.getInstance(this).getDatabase(new CreatureDB.OnDBReadyListener() {
            @Override
            public void onDBReady(SQLiteDatabase db) {
                database = db;
            }
        });
    }

    public void btnAdoptClick(View view) {
        if(database == null) {
            Toast.makeText(this, "Creating Database", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues vals = new ContentValues();

            vals.put("type", String.valueOf(Creature.CreatureType.SMALL));
            long newRowId = database.insert("creatures", null, vals);

            Toast.makeText(this, "Please take care of it :)", Toast.LENGTH_SHORT).show();
        }
    }
}
