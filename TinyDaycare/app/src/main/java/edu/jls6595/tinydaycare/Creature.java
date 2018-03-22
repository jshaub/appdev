package edu.jls6595.tinydaycare;

// Class definition for Creature objects

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

public class Creature {

    public enum CreatureType {
        SMALL(250),
        MEDIUM(500),
        LARGE(1000);

        private int stepsNeeded;

        CreatureType(int stepsNeeded) {
            this.stepsNeeded = stepsNeeded;
        }

        public int getStepsNeeded() {
            return stepsNeeded;
        }
    }

    private int currentSteps;
    private boolean hatched;
    private CreatureType type;
    private ImageView iView;

    // Constructor
    public Creature(Context context, String type) {
        hatched = false;
        currentSteps = 0;

        switch(type) {
            case "small":
                //this.type = CreatureType.SMALL;
                break;
            case "medium":
                //this.type = CreatureType.MEDIUM;
                break;
            case "large":
                //this.type = CreatureType.LARGE;
                break;
        }

        // Create view to display
        iView = new ImageView(context);
        Log.d("viewID", "Created ImageView");
        iView.setId(ImageView.generateViewId());
        iView.setImageResource(R.drawable.egg_sprite);
        Log.d("viewID", "iView ID = " + iView.getId());
    }

    // when currentSteps = stepsToHatch, call this function
    public void hatch() {
        iView.setImageResource(R.drawable.creature_small);
    }

    // Update currentSteps
    public void updateSteps() {

    }

    // Play with creature
    public void play() {

    }

    // Feed creature
    public void feed() {

    }

    public ImageView getImageView() {
        return iView;
    }
}
