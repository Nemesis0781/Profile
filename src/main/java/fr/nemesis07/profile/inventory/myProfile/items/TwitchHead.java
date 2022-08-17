package fr.nemesis07.profile.inventory.myProfile.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.nemesis07.profile.Profile;
import fr.nemesis07.profile.manage.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TwitchHead {

    public static ItemStack noAccount() {
        ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) it.getItemMeta();
        textures(meta);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("twitch.name")));
        List<String> lore = new ArrayList<>();
        for(String line: Profile.getInstance().getMyConfig().getStringList("twitch.noAccount.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);
        it.setItemMeta(meta);
        return it;
    }

    public static ItemStack hasAccount(User user) {
        ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) it.getItemMeta();
        textures(meta);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("twitch.name")));
        List<String> lore = new ArrayList<>();
        for (String line : Profile.getInstance().getMyConfig().getStringList("twitch.hasAccount.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        String description = user.getTwitch().getDescription();
        String[] finalDescription = description.split("\n");

        String onLive = ChatColor.RED + "Off";
        if(user.getTwitch().isOnLive())
            onLive = ChatColor.GREEN + "On";

        String finalOnLive = onLive;
        lore.replaceAll(x -> x.replace("%title%", user.getTwitch().getName())
                .replace("%viewCountLive%", user.getTwitch().getViewCount()+"")
                .replace("%viewCountTotal%", user.getTwitch().getViewCountTotal()+"")
                .replace("%isOnLive%", finalOnLive));

        for(String line: finalDescription) {
            lore.add(ChatColor.GRAY + line);
        }
        for(String line: Profile.getInstance().getMyConfig().getStringList("twitch.hasAccount.lore2")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        meta.setLore(lore);
        it.setItemMeta(meta);
        return it;
    }

    private static void textures(SkullMeta meta) {
        GameProfile gpro = new GameProfile(UUID.randomUUID(), null);
        gpro.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYxOGZhNDNkNGQ5Mzc4OTQ4YjU2Yjg1YjUzMTk3OTA3NDExOWMxMjUyMzJlNzE1Y2U0YmQ1Mjc4MGFjNGQ3NiJ9fX0="));
        Field gproField = null;
        try {
            gproField = meta.getClass().getDeclaredField("profile");
        } catch (SecurityException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        assert gproField != null;
        gproField.setAccessible(true);
        try {
            gproField.set(meta, gpro);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
