package edu.jls6595.tinydaycare;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
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

public class collection_screen extends AppCompatActivity {

    ConstraintLayout cLayout;
    ConstraintSet set = new ConstraintSet();
    final int MAX_CAPACITY = 9;
    Creature creatureList[] = new Creature[MAX_CAPACITY];

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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_screen);

        //getActionBar().hide();

        cLayout = findViewById(R.id.collection_layout);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem item = navigation.getMenu().findItem(R.id.navigation_collection);
        item.setChecked(true);
    }

    // When user presses on a creature or egg, this method is invoked
    public void creaturePress(View v) {
        DialogFragment switchDialog = new SwitchDialog();
        switchDialog.show(getFragmentManager(), "confirmSwitch");
    }

    public void buyEgg(View v) {
        Creature c = new Creature(getBaseContext(), "small");
        Log.d("viewID", "Creature ID = " + c.getImageView().getId());

        cLayout.addView(c.getImageView());

        // Set width and height of image view
        set.constrainHeight(c.getImageView().getId(), 450);
        set.constrainWidth(c.getImageView().getId(), 450);

        // Set start and top constraints of image view
        set.connect(c.getImageView().getId(), ConstraintSet.START, R.id.collection_layout, ConstraintSet.START, 10);
        set.connect(c.getImageView().getId(), ConstraintSet.TOP, R.id.collection_layout, ConstraintSet.TOP, 10);

        // Apply changes to layout
        set.applyTo(cLayout);
        Log.d("view", "Applied ConstraintSet to Layout");

        // Set onClick behavior
        c.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creaturePress(view);
            }
        });
    }

}
