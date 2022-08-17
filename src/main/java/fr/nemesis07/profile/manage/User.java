package fr.nemesis07.profile.manage;

import fr.nemesis07.profile.Profile;
import fr.nemesis07.profile.network.Discord;
import fr.nemesis07.profile.network.Twitch;
import fr.nemesis07.profile.network.Twitter;
import fr.nemesis07.profile.network.YouTube;
import org.bukkit.inventory.Inventory;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class User extends AbstractData {

    private boolean newPlayer;
    private final Coins coins;

    private YouTube youtube;
    private Twitter twitter;
    private Twitch twitch;
    private Discord discord;

    private Date createDate = new Date(new java.util.Date().getTime());
    private SimpleDateFormat format = new SimpleDateFormat(Profile.getInstance().getConfig().getString("dateFormat"));

    private PlayTime playTime = new PlayTime();

    public User(UUID uuid) {
        this.uuid = uuid;
        this.newPlayer = false;
        this.coins = new Coins(uuid);
    }

    private String[] getData() {
        String[] data = new String[5];
        Profile.getInstance().getSQL().query("SELECT * FROM Users WHERE uuid ='" + getUUID() + "'", rs -> {
            try {
                if (rs.next()) {
                    data[0] = rs.getString("resia");
                    data[1] = rs.getString("youtube");
                    data[2] = rs.getString("twitter");
                    data[3] = rs.getString("twitch");
                    data[4] = rs.getString("discord");
                    createDate = rs.getDate("createDate");
                    playTime.setTime(rs.getLong("playTime"));
                } else {
                    newPlayer = true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return data;
    }

    private void sendData() {
        if(newPlayer) {
            Profile.getInstance().getSQL().update("INSERT INTO Users (uuid, name, resia, createDate) VALUES ('"+getUUID()+"', '"+getPlayer().getName()+"', '"+coins.getCoins()+"', '"+ createDate +"')");
        } else {
            try {
                Profile.getInstance().getSQL().update("UPDATE Users SET resia='"+coins.getCoins()+"'");
                Profile.getInstance().getSQL().update("UPDATE Users SET playTime='"+playTime.getTime()+"'");
                if(youtube.getTitle() != null) {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET youtube='"+youtube.getChannelID()+
                                    "' WHERE uuid ='" + getUUID() + "'");
                } else {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET youtube='Null' WHERE uuid ='" + getUUID() + "'");
                }

                if(twitter.getName() != null) {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET twitter='"+twitter.getUsername()+
                                    "' WHERE uuid ='" + getUUID() + "'");
                } else {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET twitter='Null' WHERE uuid ='" + getUUID() + "'");
                }
                
                if(twitch.getName() != null) {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET twitch='"+twitch.getUsername()+
                                    "' WHERE uuid ='" + getUUID() + "'");
                } else {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET twitch='Null' WHERE uuid ='" + getUUID() + "'");
                }

                if(discord.getTag() != null) {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET discord='"+discord.getTag()+
                                    "' WHERE uuid ='" + getUUID() + "'");
                } else {
                    Profile.getInstance().getSQL().update(
                            "UPDATE Users SET discord='Null' WHERE uuid ='" + getUUID() + "'");
                }
            } catch (NullPointerException ignored) {}

        }
    }

    public boolean checkData(String name, String data) {
        Profile.getInstance().getSQL().query("SELECT * FROM Users WHERE "+name+"='"+data+"'", rs -> {
            try {
                return !rs.next();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return false;
        });
        return false;
    }

    public void onLogin() {
        Profile.getInstance().getUsers().add(this);

        playTime.setConnected(true);
        playTime.start();

        String[] data = getData();
        if(newPlayer) {
            coins.setCoins(0);
        } else {
            coins.setCoins(Long.parseLong(data[0]));
            if(data[1] != null) {
                setYoutube(data[1]);
                setYoutubeAccount(true);
            }
            if(data[2] != null) {
                twitter = Twitter.getAccount(data[2]);
                setTwitterAccount(true);
            }
            if(data[3] != null) {
                twitch = Twitch.getAccount(data[3]);
                setTwitchAccount(true);
            }
            if(data[4] != null) {
                discord = Discord.getAccount(data[4]);
                setDiscordAccount(true);
            }
        }
    }

    public void onLogout() {
        playTime.setConnected(false);
        sendData();
        Profile.getInstance().getUsers().remove(this);
    }

    public long getCoins() {
        return coins.getCoins();
    }

    public void setYoutube(String youtube) {
        this.youtube = YouTube.getAccount(youtube);
    }

    public void setTwitter(String twitter) {
        this.twitter = Twitter.getAccount(twitter);
    }

    public void setTwitch(String twitch) {
        this.twitch = Twitch.getAccount(twitch);
    }

    public void setDiscord(String discord) {
        this.discord = Discord.getAccount(discord);
    }

    public YouTube getYoutube() {
        return youtube;
    }

    public Discord getDiscord() {
        return discord;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public Twitch getTwitch() {
        return twitch;
    }

    public PlayTime getPlayTime() {
        return playTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

}
