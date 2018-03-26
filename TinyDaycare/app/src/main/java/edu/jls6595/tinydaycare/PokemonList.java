package edu.jls6595.tinydaycare;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PokemonList {
    private final int MAX_SIZE = 15;
    private static Pokemon[] list;
    private static PokemonList pList;
    private static int currentIndex;
    private static TextView currentStepsView;
    public static int indexOfSelectedPokemon;
    public static Pokemon currentPokemon;


    private PokemonList(TextView currentStepsView) {
        list = new Pokemon[MAX_SIZE];
        currentIndex = 0;

        this.currentStepsView = currentStepsView;
    }

    // Should only be called on Home Screen at start of application
    public static synchronized PokemonList getInstance(TextView currentStepsView) {
        if(pList == null) {
            pList = new PokemonList(currentStepsView);
        }

        return pList;
    }

    // Called at every other point in the application to get the instance of the PokemonList
    public static synchronized PokemonList getInstance() {
        return pList;
    }

    public static synchronized Pokemon[] getList() {
        return list;
    }

    public TextView getCurrentStepsView() {
        return currentStepsView;
    }
    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getMaxSize() {
        return MAX_SIZE;
    }

    public void addPokemon(Pokemon p, Context context) {
        // If list is empty, then this pokemon should be set as "selected"
        if(currentIndex == 0) {
            indexOfSelectedPokemon = 0;
            currentPokemon = p;
        }

        if(currentIndex < MAX_SIZE) {
            list[currentIndex] = p;
            currentIndex = currentIndex + 1;
        }

        return;
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


}
