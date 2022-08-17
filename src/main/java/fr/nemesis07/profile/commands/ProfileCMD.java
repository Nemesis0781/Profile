package fr.nemesis07.profile.commands;

import fr.minuskube.inv.SmartInventory;
import fr.nemesis07.profile.Profile;
import fr.nemesis07.profile.inventory.myProfile.MyProfileProvider;
import fr.nemesis07.profile.network.YouTube;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.console")));
            return false;
        }
        Player p = (Player) sender;

        if(label.equalsIgnoreCase("profile") || label.equalsIgnoreCase("profil")) {
            if(args.length == 0) {
                openMyProfile(p);
            } else if(args.length == 1) {
                getProfile(args[0]);
                if(args[0].equalsIgnoreCase("reload")) {
                    if(!p.hasPermission("profile.reload")) return true;
                    Profile.getInstance().reloadConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReload effectué !"));
                } else if(args[0].equalsIgnoreCase("version")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "\n\n&aProfile version: " + Profile.getInstance().getDescription().getVersion() + "\n&aby Nemesis07\n\n"));
                    return true;
                }
            }
            youtube(args, p);

        }

        return false;
    }

    private void openMyProfile(Player p) {
        SmartInventory inv = SmartInventory.builder()
                .id("MyProfileInventory")
                .title(ChatColor.BLACK + "Mon profil")
                .size(6, 9)
                .provider(new MyProfileProvider())
                .closeable(true)
                .build();
        inv.open(p);
    }

    private void getProfile(String arg) {
        //GET PROFILE
        Player target = Bukkit.getPlayer(arg);
        if(target == null) ; //TODO getProfile
    }

    private void youtube(String[] args, Player p) {
        if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("youtube")) {
                if (args[1].equalsIgnoreCase("set")) {
                    YouTube ytb = YouTube.getAccount(args[2]);
                    Profile.getInstance().getUsers(p).ifPresent(user -> {
                        user.setYoutube(ytb.getChannelID());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.set")));
                    });
                } else if (args[1].equalsIgnoreCase("remove")) {
                    Profile.getInstance().getUsers(p).ifPresent(user -> {
                        user.setYoutubeAccount(false);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.remove")));
                    });
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.argument")));
                }
            }
        }
    }
}
