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

public class DiscordHead {

    public static ItemStack noAccount() {
        ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) it.getItemMeta();
        textures(meta);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("discord.name")));
        List<String> lore = new ArrayList<>();
        for(String line: Profile.getInstance().getMyConfig().getStringList("discord.noAccount.lore")) {
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

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("discord.name")));
        List<String> lore = new ArrayList<>();
        for (String line : Profile.getInstance().getMyConfig().getStringList("discord.hasAccount.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        lore.replaceAll(x -> x.replace("%title%", user.getDiscord().getTag()));

        meta.setLore(lore);
        it.setItemMeta(meta);
        return it;
    }


    private static void textures(SkullMeta meta){
        GameProfile gpro = new GameProfile(UUID.randomUUID(), null);
        gpro.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNiMTgzYjE0OGI5YjRlMmIxNTgzMzRhZmYzYjViYjZjMmMyZGJiYzRkNjdmNzZhN2JlODU2Njg3YTJiNjIzIn19fQ=="));
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
