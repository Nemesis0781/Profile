package fr.nemesis07.profile.manage;

import java.util.UUID;

public class Coins extends AbstractData {
    private long coins;

    public Coins(UUID uuid) {
        this.uuid = uuid;
    }

    public void setCoins(long coins) {
        if(coins < 0)
            coins = 0;
        this.coins = coins;
    }

    public long getCoins() {
        return coins;
    }

    public void addCoins(long coins) {
        this.coins += coins;
    }

    public void removeCoins(long coins) {
        if(coins > this.coins)
            coins = this.coins;
        this.coins -= coins;
    }

    public boolean hasCoins(long coins) {
        if(coins <= 0)
            return true;
        return this.coins >= coins;
    }
}
