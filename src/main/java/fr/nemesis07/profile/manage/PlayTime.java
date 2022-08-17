package fr.nemesis07.profile.manage;

import fr.nemesis07.profile.Profile;
import org.bukkit.Bukkit;

public class PlayTime {

    private long time;
    private boolean isConnected;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void start(){
        Bukkit.getScheduler().runTaskTimer(Profile.getInstance(), () -> {
            if (isConnected) {
                time++;
            }
        }, 0, 20);
    }
}
