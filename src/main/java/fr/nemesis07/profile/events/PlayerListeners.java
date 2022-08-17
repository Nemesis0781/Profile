package fr.nemesis07.profile.events;

import fr.nemesis07.profile.Profile;
import fr.nemesis07.profile.manage.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        User user = new User(p.getUniqueId());
        user.onLogin();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        Profile.getInstance().getUsers(p).ifPresent(User::onLogout);
    }

}
