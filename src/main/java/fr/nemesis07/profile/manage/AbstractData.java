package fr.nemesis07.profile.manage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

abstract class AbstractData {
    protected UUID uuid;
    public String getUUID() { return uuid.toString(); }
    public UUID getRealUUID() { return uuid; }
    protected Player getPlayer() { return Bukkit.getPlayer(uuid); }

    private boolean hasYoutubeAccount;
    private boolean hasTwitterAccount;
    private boolean hasTwitchAccount;
    private boolean hasDiscordAccount;

    public boolean hasYoutubeAccount() {
        return hasYoutubeAccount;
    }

    public void setYoutubeAccount(boolean b) {
        this.hasYoutubeAccount = b;
    }

    public boolean hasTwitterAccount() {
        return hasTwitterAccount;
    }

    public void setTwitterAccount(boolean b) {
        this.hasTwitterAccount = b;
    }

    public boolean hasTwitchAccount() {
        return hasTwitchAccount;
    }

    public void setTwitchAccount(boolean b) {
        this.hasTwitchAccount = b;
    }

    public boolean hasDiscordAccount() {
        return hasDiscordAccount;
    }

    public void setDiscordAccount(boolean b) {
        this.hasDiscordAccount = b;
    }
}
