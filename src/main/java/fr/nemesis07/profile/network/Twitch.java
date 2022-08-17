package fr.nemesis07.profile.network;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import fr.nemesis07.profile.Profile;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class Twitch {

    private String username;

    private Stream stream;
    private User user;

    private TwitchClient client;

    private boolean b;

    public Twitch(String username) {
        this.username = username;
        Bukkit.getScheduler().runTaskTimer(Profile.getInstance(), () -> {
            this.b = false;
            client = TwitchClientBuilder.builder()
                    .withClientId(Profile.getInstance().TWITCH_API_CLIENT_KEY)
                    .withClientSecret(Profile.getInstance().TWITCH_API_CLIENT_SECRET_KEY)
                    .withEnableHelix(true).build();
            UserList userList = client.getHelix().getUsers(null, null, Arrays.asList(username)).execute();
            userList.getUsers().forEach(user -> {
                this.user = user;
            });
            StreamList streamList = client.getHelix().getStreams(null, null, null, null, null, null, null, Arrays.asList(username)).execute();
            streamList.getStreams().forEach(stream -> {
                this.stream = stream;
            });
            check(client);

            Bukkit.getScheduler().runTaskLater(Profile.getInstance(), () -> {
                client = TwitchClientBuilder.builder().build();
            }, 20);
        }, 0, 20*5);
    }

    private void check(TwitchClient client) {
        StreamList streamList = client.getHelix().getStreams(null, null, null, null, null, null, null, Arrays.asList(username)).execute();
        streamList.getStreams().forEach(stream -> {
            if(stream == null)
                this.stream = null;
        });
    }
    //TODO Check pour actualisé info twitch
    public static Twitch getAccount(String username) {
        return new Twitch(username);
    }

    public boolean isOnLive() {
        return stream != null;
    }

    public String getUsername() {
        return username;
    }

    public String getID() {
        return user.getId();
    }

    public String getName() {
        return user.getDisplayName();
    }

    public String getDescription() {
        return user.getDescription();
    }

    public int getViewCount() {
        if(stream != null)
            return stream.getViewerCount();
        else
            return 0;
    }

    public Integer getViewCountTotal() {
        return user.getViewCount();
    }
}