package edu.jls6595.tinydaycare;

import android.os.Bundle;

public class User {
    private int numCareCoins;
    private final int DEFAULT_AMOUNT = 3000;
    private static User user;

    private User() {
        numCareCoins = DEFAULT_AMOUNT;
    }

    public static synchronized User getInstance() {
        if(user == null) {
            user = new User();
        }

        return user;
    }

    public synchronized int getNumCareCoins() {
        return numCareCoins;
    }

    public synchronized void setNumCareCoins(int amount) {
        numCareCoins = amount;
    }

    public synchronized int addCareCoins(int amount) {
        numCareCoins += amount;
        return numCareCoins;
    }

    public synchronized int removeCareCoins(int amount) {
        numCareCoins -= amount;
        return numCareCoins;
    }
}
