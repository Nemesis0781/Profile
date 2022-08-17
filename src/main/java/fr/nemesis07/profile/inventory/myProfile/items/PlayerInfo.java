package fr.nemesis07.profile.inventory.myProfile.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.nemesis07.profile.Profile;
import fr.nemesis07.profile.manage.User;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerInfo {

    public static ItemStack account(User user) {
        ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) it.getItemMeta();
        Player p = Bukkit.getPlayer(user.getRealUUID());
        meta.setOwner(p.getName());

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("player.name").replace("%name%", p.getName()).replace("%displayname%", p.getDisplayName())));
        List<String> lore = new ArrayList<>();
        for (String line : Profile.getInstance().getMyConfig().getStringList("player.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        lore.replaceAll(x -> x.replace("%prefix%", PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%"))
                .replace("%coins%", user.getCoins()+"")
                .replace("%firstJoin%", user.getFormat().format(user.getCreateDate())+""));

        meta.setLore(lore);
        it.setItemMeta(meta);
        return it;
    }

}
