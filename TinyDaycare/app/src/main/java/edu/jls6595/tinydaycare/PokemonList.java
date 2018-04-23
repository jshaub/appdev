package edu.jls6595.tinydaycare;

import android.util.Log;
import android.widget.TextView;

public class PokemonList {
    private final int MAX_SIZE = 15;
    private static Pokemon[] list;
    private static PokemonList pList;
    private static int currentIndex;
    public static Pokemon currentPokemon;
    private boolean loaded;

    // Arrays for storing Pokemon information in a bundle when onSaveInstanceState is invoked
    private boolean[] array_hatched = new boolean[MAX_SIZE];
    private boolean[] array_readyToHatch = new boolean[MAX_SIZE];
    private int[] array_currentSteps = new int[MAX_SIZE];
    private int[] array_stepsNeeded = new int[MAX_SIZE];
    private int[] array_eggSprite = new int[MAX_SIZE];
    private int[] array_hatchedSprite = new int[MAX_SIZE];
    private int[] array_cost = new int[MAX_SIZE];
    private int[] array_pokemonIndex = new int[MAX_SIZE];
    private int[] array_type = new int[MAX_SIZE];

    private PokemonList() {
        list = new Pokemon[MAX_SIZE];
        currentIndex = 0;
        loaded = false;
    }

    // Should only be called on Home Screen at start of application
    public static synchronized PokemonList getInstance() {
        if(pList == null) {
            pList = new PokemonList();
        }

        return pList;
    }

    public static synchronized Pokemon[] getList() {
        return list;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getMaxSize() {
        return MAX_SIZE;
    }

    public void addPokemon(Pokemon p) {
        loaded = true;
        // If list is empty, then this pokemon should be set as "selected"
        array_hatched[currentIndex] = p.isHatched();
        array_readyToHatch[currentIndex] = p.isReadyToHatch();
        array_currentSteps[currentIndex] = p.getCurrentSteps();
        array_stepsNeeded[currentIndex] = p.getStepsNeeded();
        array_eggSprite[currentIndex] = p.getEggSprite();
        array_hatchedSprite[currentIndex] = p.getHatchedSprite();
        array_cost[currentIndex] = p.getCost();
        array_type[currentIndex] = p.getType().getStepsNeeded();
        array_pokemonIndex[currentIndex] = currentIndex;

        if(currentIndex == 0) {
            currentPokemon = p;
            currentPokemon.setCurrent(true);
        }

        if(currentIndex < MAX_SIZE) {
            list[currentIndex] = p;
            currentIndex = currentIndex + 1;
        }
    }

    public void updateCurrentPokemon(Pokemon p) {
        Log.d("PokemonList", "Updating current pokemon");
        Log.d("PokemonList", "pokemon passed in = " + p);
        Log.d("PokemonList", "current before = " + currentPokemon);
        currentPokemon = p;
        Log.d("PokemonList", "current after = " + currentPokemon);
    }

    public Pokemon findPokemonAtIndex(int i) {
        return list[i];
    }

    public boolean isLoaded() {
        return loaded;
    }

}
