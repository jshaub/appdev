package edu.jls6595.tinydaycare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class settings_screen extends AppCompatActivity {

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
                case R.id.navigation_adopt:
                    Intent adopt_intent = new Intent(getBaseContext(), adopt_screen.class);
                    startActivity(adopt_intent);
                    break;
                default:
                    Log.e("bottomNav", "ItemID didn't match any bottom nav IDs");
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        //getActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem item = navigation.getMenu().findItem(R.id.navigation_settings);
        item.setChecked(true);
    }

    // Displays a toast when each setting is enabled or disabled
    protected void displayToast(int resID) {
        //Log.d("displayToast", "displayToast() called");
        Toast.makeText(this, resID, Toast.LENGTH_SHORT).show();
        //Log.d("toast displayed", "Toast has been displayed");
    }

    // Code that executes when settings are toggled
    public void settingsToggle(View view) {
        Log.d("settingsToggle", "settingsToggle() called");

        Switch toggle = (Switch) view;

        switch (toggle.getId()) {
            // Push notifications toggled
            case R.id.push_notifications:
                if (toggle.isChecked()) {
                    Log.d("pushNotif", "push notifications toggled on");
                    displayToast(R.string.notif_enabled);
                } else {
                    Log.d("pushNotif", "push notifications toggled off");
                    displayToast(R.string.notif_disabled);
                }
                break;
            // Sounds toggled
            case R.id.sound:
                if (toggle.isChecked()) {
                    Log.d("sounds", "sounds toggled on");
                    displayToast(R.string.sound_enabled);
                } else {
                    Log.d("sounds", "sounds toggled off");
                    displayToast(R.string.sound_disabled);
                }
                break;
            // Vibrations toggled
            case R.id.vibrate:
                if (toggle.isChecked()) {
                    Log.d("vibrations", "vibrations toggled on");
                    displayToast(R.string.vibrate_enabled);
                } else {
                    Log.d("vibrations", "vibrations toggled off");
                    displayToast(R.string.vibrate_disabled);
                }
                break;
            // Something was toggled that shouldn't have been???
            default:
                Log.d("no matching id", "No toggle ID was matched");
                displayToast(R.string.setting_toast_error);
                break;
        }
    }

}