package edu.jls6595.tinydaycare;

// Class definition for Pokemon objects

// TODO: Load largest id in database into this.id otherwise id conflicts will occur

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Pokemon {

    public enum PokemonType {
        SMALL(10),
        MEDIUM(15),
        LARGE(20);

        private int stepsNeeded;

        PokemonType(int stepsNeeded) {
            this.stepsNeeded = stepsNeeded;
        }

        int getStepsNeeded() {
            return stepsNeeded;
        }
    }

    private final int UNIQUE_ID;
    private int stepsNeededToHatch;
    private int currentSteps;
    private boolean hatched;
    private PokemonType type;
    private int eggSprite;
    private int hatchedSprite;
    private int viewId;

    private static int id = 0;

    // Constructor
    public Pokemon(PokemonType type, PokemonDB database, Context context) {
        //Log.d("pokemon", "setting unique id");
        //Log.d("pokemon", "id = " + id);
        UNIQUE_ID = id;
        id = id + 1;
        //Log.d("pokemon", "unique id = " + UNIQUE_ID);
        //Log.d("pokemon", "id = " + id);
        hatched = false;
        currentSteps = 0;
        stepsNeededToHatch = type.getStepsNeeded();

        Log.d("pokemon", "steps needed = " + stepsNeededToHatch);

        switch(type) {
            case SMALL:
                //Log.d("pokemon", "setting pokemon type small");
                this.type = PokemonType.SMALL;
                eggSprite = R.drawable.egg_sprite_small;
                hatchedSprite = R.drawable.pokemon_small;
                //Log.d("pokemon", "pokemonType = " + this.type);
                break;
            case MEDIUM:
                //Log.d("pokemon", "setting pokemon type medium");
                this.type = PokemonType.MEDIUM;
                eggSprite = R.drawable.egg_sprite_medium;
                hatchedSprite = R.drawable.pokemon_medium;
                //Log.d("pokemon", "pokemonType = " + this.type);
                break;
            case LARGE:
                //Log.d("pokemon", "setting pokemon type large");
                this.type = PokemonType.LARGE;
                eggSprite = R.drawable.egg_sprite_large;
                hatchedSprite = R.drawable.pokemon_large;
                //Log.d("pokemon", "pokemonType = " + this.type);
                break;
            default:
                Log.wtf("pokemon", "Fatal error in construtor switch case");
                break;
        }

        PokemonList.getInstance().addPokemon(this, context);
        database.addPokemonToDB(this);

    }

    public void setViewId(int id) {
        viewId = id;
    }

    public int getEggSprite() {
        return eggSprite;
    }

    public int getHatchedSprite() {
        return hatchedSprite;
    }

    public void setImageView(ImageView view) {
        Log.d("viewID", "Setting ImageView");

        int sprite;

        if(!isHatched()) {
            sprite = getEggSprite();
        }
        else {
            sprite = getHatchedSprite();
        }

        switch(type) {
            case SMALL:
                view.setImageResource(sprite);
                break;
            case MEDIUM:
                view.setImageResource(sprite);
                break;
            case LARGE:
                view.setImageResource(sprite);
                break;
        }

    }

    // Update currentSteps
    public void updateCurrentSteps(Button hatchButton) {
        if(currentSteps == stepsNeededToHatch) {
            Log.d("pokemon", "current steps = " + currentSteps);
            Log.d("pokemon", "needed steps = " + stepsNeededToHatch);
            hatchButton.setVisibility(View.VISIBLE);
            Log.d("pokemon", "set hatch button visible");
            hatchButton.invalidate();
        }
        else {
            Log.d("pokemon", "pokemon id = " + this);
            Log.d("pokemon", "updating steps");
            currentSteps = currentSteps + 1;
            Log.d("pokemon","current steps = " + currentSteps);
        }
    }

    public int getCurrentSteps() {
        return currentSteps;
    }

    public int getStepsNeeded() {
        return this.type.getStepsNeeded();
    }

    public PokemonType getType() {
        return type;
    }

    public boolean isHatched() {
        return hatched;
    }

    // when currentSteps = stepsToHatch, call this function
    public void hatch(View view) {
        hatched = true;
        setImageView((ImageView)view);
    }

    // Play with pokemon
    public void play() {

    }

    // Feed pokemon
    public void feed() {

    }
}
