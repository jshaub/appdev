package edu.jls6595.tinydaycare;

// Class definition for Pokemon objects

// TODO: Load largest id in database into this.id otherwise id conflicts will occur

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Pokemon {

    private static final int TYPE_SMALL_VAL = 10;
    private static final int TYPE_MEDIUM_VAL = 15;
    private static final int TYPE_LARGE_VAL = 20;
    private static final int COST_SMALL_VAL = 500;
    private static final int COST_MEDIUM_VAL = 750;
    private static final int COST_LARGE_VAL = 1000;

    public enum PokemonType {
        SMALL(TYPE_SMALL_VAL),
        MEDIUM(TYPE_MEDIUM_VAL),
        LARGE(TYPE_LARGE_VAL);

        private int stepsNeeded;

        PokemonType(int stepsNeeded) {
            this.stepsNeeded = stepsNeeded;
        }

        int getStepsNeeded() {
            return stepsNeeded;
        }
    }

    public enum PokemonCost {
        COST_SMALL(COST_SMALL_VAL),
        COST_MEDIUM(COST_MEDIUM_VAL),
        COST_LARGE(COST_LARGE_VAL);

        private int cost;

        PokemonCost(int cost) { this.cost = cost; }

        int getCost() { return cost; }
    }

    private static int id = 0;
    private int UNIQUE_ID = 0;

    private int stepsNeededToHatch;
    private int currentSteps;
    private boolean current;
    private boolean hatched;
    private boolean readyToHatch;
    private PokemonType type;
    private PokemonCost cost;
    private int eggSprite;
    private int hatchedSprite;

    // Constructor
    public Pokemon(PokemonType type, PokemonDB database) {
        UNIQUE_ID = id;
        id = id + 1;
        current = false;
        hatched = false;
        readyToHatch = false;
        currentSteps = 0;
        stepsNeededToHatch = type.getStepsNeeded();

        Log.d("pokemon", "steps needed = " + stepsNeededToHatch);

        switch(type) {
            case SMALL:
                this.type = PokemonType.SMALL;
                eggSprite = R.drawable.egg_sprite_small;
                hatchedSprite = R.drawable.pokemon_small;
                this.cost = PokemonCost.COST_SMALL;
                break;
            case MEDIUM:
                this.type = PokemonType.MEDIUM;
                eggSprite = R.drawable.egg_sprite_medium;
                hatchedSprite = R.drawable.pokemon_medium;
                this.cost = PokemonCost.COST_MEDIUM;
                break;
            case LARGE:
                this.type = PokemonType.LARGE;
                eggSprite = R.drawable.egg_sprite_large;
                hatchedSprite = R.drawable.pokemon_large;
                this.cost = PokemonCost.COST_LARGE;
                break;
            default:
                Log.wtf("pokemon", "Fatal error in construtor switch case");
                break;
        }

        Log.d("pokemon", "pokemonType = " + this.type);
        Log.d("pokemon", "cost = " + this.cost);

        PokemonList.getInstance().addPokemon(this);
    }

    // This constructor is used to reinstatiate Pokemon on app restart
    public Pokemon(int current, int hatched, int readyToHatch, int currentSteps, int eggSprite,
                   int hatchedSprite, String type, int cost) {
        UNIQUE_ID = id;
        id++;

        if(current == 1) {
            this.current = true;
        }
        else {
            this.current = false;
        }

        if(hatched == 1) {
            this.hatched = true;
        }
        else {
            this.hatched = false;
        }

        if(readyToHatch == 1) {
            this.readyToHatch = true;
        }
        else {
            this.readyToHatch = false;
        }

        this.currentSteps = currentSteps;
        this.eggSprite = eggSprite;
        this.hatchedSprite = hatchedSprite;

        switch(type) {
            case "SMALL":
                this.type = PokemonType.SMALL;
                this.cost = PokemonCost.COST_SMALL;
                break;
            case "MEDIUM":
                this.type = PokemonType.MEDIUM;
                this.cost = PokemonCost.COST_MEDIUM;
                break;
            case "LARGE":
                this.type = PokemonType.LARGE;
                this.cost = PokemonCost.COST_LARGE;
                break;
            default:
                Log.wtf("pokemon", "fatal error setting type and cost after reinstatiating");
                break;
        }

        stepsNeededToHatch = this.type.getStepsNeeded();
        PokemonList.getInstance().addPokemon(this);
    }

    public int getId() {
        return UNIQUE_ID;
    }

    public void setCurrent(boolean c) {
        current = c;
    }

    public int getCurrent() {
        if(current) {
            return 1;
        }
        else {
            return 0;
        }
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

    // Increment steps
    public void incrementSteps() {
        Log.d("Pokemon", "currentSteps before incrementing = " + currentSteps);
        currentSteps = currentSteps + 1;
        Log.d("Pokemon", "currentSteps after incrementing = " + currentSteps);
        Log.d("Pokemon", "stepsNeeded = " + stepsNeededToHatch);

        if(currentSteps > stepsNeededToHatch) {
            Log.d("Pokemon", "steps equal");
            currentSteps = stepsNeededToHatch;
        }

        Log.d("Pokemon", "stepsNeeded = " + this.type.getStepsNeeded());
        Log.d("Pokemon", "currentSteps = " + currentSteps);

        if(currentSteps == stepsNeededToHatch) {
            readyToHatch = true;
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

    public String getTypeString() {
        return this.type.toString();
    }

    public int getCost() {
        return cost.getCost();
    }

    public boolean isReadyToHatch() {
        return readyToHatch;
    }

    public int getReadyToHatchInt() {
        if(isReadyToHatch()) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public boolean isHatched() {
        return hatched;
    }

    public int getHatchedInt() {
        if(isHatched()) {
            return 1;
        }
        else {
            return 0;
        }
    }

    // when currentSteps = stepsToHatch, call this function
    public void hatch(View view) {
        hatched = true;
        setImageView((ImageView)view);
    }

    // Play with pokemon
    public void play() {
        User.getInstance().addCareCoins(50);
    }

    // Feed pokemon
    public void feed() {
        User.getInstance().addCareCoins(100);
    }
}
