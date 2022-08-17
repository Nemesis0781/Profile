package fr.nemesis07.profile.inventory.myProfile;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.nemesis07.profile.Profile;
import fr.nemesis07.profile.inventory.myProfile.items.*;
import fr.nemesis07.profile.manage.User;
import fr.nemesis07.profile.network.Discord;
import fr.nemesis07.profile.network.Twitch;
import fr.nemesis07.profile.network.Twitter;
import fr.nemesis07.profile.network.YouTube;
import fr.nemesis07.profile.utils.AnvilGUI;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.regex.Pattern;

public class MyProfileProvider implements InventoryProvider {

    @Override
    public void init(Player p, InventoryContents contents) {
        Optional<User> user = Profile.getInstance().getUsers(p);
        if(user.isPresent()){
            PlayerAccount(user.get(), contents);
            if(user.get().hasYoutubeAccount()) {
                YoutubeAccount(user.get(), contents);
            } else {
                noYoutubeAccount(user.get(), contents);
            }
            if(user.get().hasTwitterAccount()) {
                TwitterAccount(user.get(), contents);
            } else {
                noTwitterAccount(user.get(), contents);
            }
            if(user.get().hasTwitchAccount()) {
                TwitchAccount(user.get(), contents);
            } else {
                noTwitchAccount(user.get(), contents);
            }
            if(user.get().hasDiscordAccount()) {
                DiscordAccount(user.get(), contents);
            } else {
                noDiscordAccount(user.get(), contents);
            }
        }
    }

    @Override
    public void update(Player p, InventoryContents contents) {
        Optional<User> user = Profile.getInstance().getUsers(p);
        if(user.isPresent()){
            if(user.get().hasYoutubeAccount()) {
                YoutubeAccount(user.get(), contents);
            }
            if(user.get().hasTwitterAccount()) {
                TwitterAccount(user.get(), contents);
            }
            if(user.get().hasTwitchAccount()) {
                TwitchAccount(user.get(), contents);
            }
            if(user.get().hasDiscordAccount()) {
                DiscordAccount(user.get(), contents);
            }
        }
    }

    private void PlayerAccount(User user, InventoryContents contents) {
        ClickableItem head = ClickableItem.of(PlayerInfo.account(user), e -> e.setCancelled(true));
        contents.set(1, 4, head);
    }

    private void TwitterAccount(User user, InventoryContents contents) {
        Player p = Bukkit.getPlayer(user.getRealUUID());
        ClickableItem twitter = ClickableItem.of(TwitterHead.hasAccount(user), e -> {
            e.setCancelled(true);
            if(e.isRightClick()) {
                p.closeInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("twitch.hasAccount.deleted")));
                user.setTwitterAccount(false);
            }
        });
        contents.set(2, 1, twitter);
    }

    private void noTwitterAccount(User user, InventoryContents contents) {
        ClickableItem twitter = ClickableItem.of(TwitterHead.noAccount(), e -> {
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(user.getRealUUID());
            if(e.isLeftClick()) {
                for(String line: Profile.getInstance().getMyConfig().getStringList("twitter.click")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
                p.closeInventory();
                Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> new AnvilGUI(Profile.getInstance(), p, (menu, text) -> {
                    if(text.equalsIgnoreCase("Votre username"))
                        return false;

                    p.closeInventory();
                    // TODO Changer user par other user (on verif pas le data de ce joueur mais de tous les autres)
                    if(user.checkData("twitter", text)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance()
                                .getConfig().getString("messages.assigned")));
                        return false;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.wait")));

                    user.setTwitter(text);
                    user.setTwitterAccount(true);
                    Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> {
                        if(user.getTwitter().getName() == null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.errorAddAccount")));
                            user.setTwitterAccount(false);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.
                                    getInstance().getConfig().getString("messages.finishWait")));
                        }
                    }, 20*2);
                    return false;
                }).setInputName(Material.NAME_TAG,"Votre username").open(), 20L *Profile.getInstance().getConfig().getInt("waitTime.openAnvil"));
            }
        });
        contents.set(2,1, twitter);
    }

    private void DiscordAccount(User user, InventoryContents contents) {
        Player p = Bukkit.getPlayer(user.getRealUUID());
        ClickableItem discord = ClickableItem.of(DiscordHead.hasAccount(user), e -> {
            e.setCancelled(true);
            if(e.isRightClick()) {
                p.closeInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("discord.hasAccount.deleted")));
                user.setDiscordAccount(false);
            }
        });
        contents.set(4, 1, discord);
    }

    private void noDiscordAccount(User user, InventoryContents contents) {
        ClickableItem discord = ClickableItem.of(DiscordHead.noAccount(), e -> {
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(user.getRealUUID());
            if(e.isLeftClick()) {
                for(String line: Profile.getInstance().getMyConfig().getStringList("discord.click")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
                p.closeInventory();
                Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> new AnvilGUI(Profile.getInstance(), p, (menu, text) -> {
                    int length = text.length();
                    StringBuilder regex = new StringBuilder(".#[0-9][0-9][0-9][0-9]");
                    for(int i = 0; i<length; i++) {
                        regex.insert(0, '.');
                    }
                    System.out.println(regex);
                    if(text.equalsIgnoreCase("Votre tag") || !Pattern.matches(regex.toString(), text))
                        return false;

                    p.closeInventory();
                    // TODO Changer user par other user (on verif pas le data de ce joueur mais de tous les autres)
                    if(user.checkData("discord", text)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance()
                                .getConfig().getString("messages.assigned")));
                        return false;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.wait")));

                    Discord discord1 = Discord.getAccount(text);

                    user.setDiscord(discord1.getTag());
                    user.setDiscordAccount(true);
                    Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> {
                        if(user.getDiscord().getTag() == null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.errorAddAccount")));
                            user.setDiscordAccount(false);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.
                                    getInstance().getConfig().getString("messages.finishWait")));
                        }
                    }, 20*2);
                    return false;
                }).setInputName(Material.NAME_TAG,"Votre tag").open(), 20L *Profile.getInstance().getConfig().getInt("waitTime.openAnvil"));
            }
        });
        contents.set(4,1, discord);
    }

    private void TwitchAccount(User user, InventoryContents contents) {
        Player p = Bukkit.getPlayer(user.getRealUUID());
        ClickableItem twitch = ClickableItem.of(TwitchHead.hasAccount(user), e -> {
            e.setCancelled(true);
            if(e.isRightClick()) {
                p.closeInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("twitch.hasAccount.deleted")));
                user.setTwitchAccount(false);
            }
        });
        contents.set(3, 1, twitch);
    }

    private void noTwitchAccount(User user, InventoryContents contents) {
        ClickableItem twitch = ClickableItem.of(TwitchHead.noAccount(), e -> {
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(user.getRealUUID());
            if(e.isLeftClick()) {
                for(String line: Profile.getInstance().getMyConfig().getStringList("twitch.click")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }

                p.closeInventory();
                Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> new AnvilGUI(Profile.getInstance(), p, (menu, text) -> {
                    if(text.equalsIgnoreCase("Votre username"))
                        return false;

                    p.closeInventory();
                    // TODO Changer user par other user (on verif pas le data de ce joueur mais de tous les autres)
                    if(user.checkData("twitch", text)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance()
                                .getConfig().getString("messages.assigned")));
                        return false;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.wait")));

                    Twitch twitch1 = Twitch.getAccount(text);

                    user.setTwitch(twitch1.getUsername());
                    user.setTwitchAccount(true);

                    Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> {
                        if(user.getTwitch().getName() == null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.errorAddAccount")));
                            user.setTwitchAccount(false);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.
                                    getInstance().getConfig().getString("messages.finishWait")));
                        }
                    }, 20*2);
                    return false;
                }).setInputName(Material.NAME_TAG,"Votre username").open(), 20L *Profile.getInstance().getConfig().getInt("waitTime.openAnvil"));
            }
        });
        contents.set(3,1, twitch);
    }

    private void YoutubeAccount(User user, InventoryContents contents) {
        Player p = Bukkit.getPlayer(user.getRealUUID());
        ClickableItem youtube = ClickableItem.of(YouTubeHead.hasAccount(user), e -> {
           e.setCancelled(true);
           if(e.isRightClick()) {
               p.closeInventory();
               p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("youtube.hasAccount.deleted")));
               user.setYoutubeAccount(false);
           }
        });
        contents.set(1,1, youtube);
    }

    private void noYoutubeAccount(User user, InventoryContents contents) {
        ClickableItem youtube = ClickableItem.of(YouTubeHead.noAccount(), e -> {
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(user.getRealUUID());
            if(e.isLeftClick()) {
                for(String line: Profile.getInstance().getMyConfig().getStringList("youtube.click")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                }
                TextComponent click = new TextComponent(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getMyConfig().getString("youtube.shortcut")));
                click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.youtube.com/account_advanced"));
                click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("https://www.youtube.com/account_advanced").create()));
                p.spigot().sendMessage(click);
                p.closeInventory();
                Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> new AnvilGUI(Profile.getInstance(), p, (menu, text) -> {
                    if(text.equalsIgnoreCase("Votre Channel ID"))
                        return false;

                    p.closeInventory();
                    // TODO Changer user par other user (on verif pas le data de ce joueur mais de tous les autres)
                    if(user.checkData("youtube", text)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance()
                        .getConfig().getString("messages.assigned")));
                        return false;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.wait")));

                    YouTube ytb = YouTube.getAccount(text);

                    user.setYoutube(ytb.getChannelID());
                    user.setYoutubeAccount(true);

                    Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> {
                        if(user.getYoutube().getTitle() == null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.getInstance().getConfig().getString("messages.errorAddAccount")));
                            user.setYoutubeAccount(false);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Profile.
                                    getInstance().getConfig().getString("messages.finishWait")));
                        }
                    }, 20*2);
                    return false;
                }).setInputName(Material.NAME_TAG,"Votre Channel ID").open(), 20L *Profile.getInstance().getConfig().getInt("waitTime.openAnvil"));
            }
        });
        contents.set(1,1, youtube);
    }

}
