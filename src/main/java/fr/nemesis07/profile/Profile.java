package fr.nemesis07.profile;

import fr.nemesis07.profile.commands.ProfileCMD;
import fr.nemesis07.profile.events.PlayerListeners;
import fr.nemesis07.profile.manage.User;
import fr.nemesis07.profile.storage.MySQL;
import fr.nemesis07.profile.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Profile extends JavaPlugin {

    private static Profile instance;
    private MySQL SQL;
    public Title title = new Title();
    private List<User> users;

    public String YOUTUBE_API_KEY = "AIzaSyDr74_MlHYIYkfuTj-p-OTa9ehTN5OX4rw";
    //ID: 148348190285-h6taelrbhvauceqb71oak6cl2kr5a0rn.apps.googleusercontent.com
    //Secret: iaeVd9vHEsFJdceGgF-HtLFL
    public String TWITCH_API_CLIENT_KEY = "wo1z5dwng0tg9hytk0xjw6p78b24ia";
    public String TWITCH_API_CLIENT_SECRET_KEY = "60dw7928wuu0yfvqg3reh1061hwiot";

    public String TWITTER_API_KEY = "Fkg0dIlhgGWEn8w5TkRCDqlQR";
    public String TWITTER_API_SECRET_KEY = "E0qM1iHQOSfgpr22vTz7AR8T8b39hLLrvGtJxlTt2xmdW9LlBh";
    public String TWITTER_ACCESS_TOKEN = "1418297183980466179-z5cVWAPntQm6NsSsvU8CcTFRQ20gYv";
    public String TWITTER_ACCESS_TOKEN_SECRET = "feOUi2dAj4rGbyfXwi6NPs7Lp2FOzzdwOT0jV9x7ePUzH";

    private File myProfile;
    private FileConfiguration myProfileConfig;
    private File hisProfile;
    private FileConfiguration hisProfileConfig;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        initConnection();
        registers();
        createFile();
    }

    @Override
    public void onDisable() {
        SQL.disconnect();
    }

    private void initConnection(){
        this.SQL = new MySQL(getConfig().getString("db.host"),
                getConfig().getString("db.port"),
                getConfig().getString("db.dbName"),
                getConfig().getString("db.username"),
                getConfig().getString("db.password"));
        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().warning("\n \nBase de donnée n'est pas connecté !\n");
        }

        if(SQL.isConnected()) {
            SQL.createTables();
        }
    }

    private void registers() {
        getCommand("profile").setExecutor(new ProfileCMD());
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListeners(), this);
        users = new ArrayList<>();
    }

    private void createFile() {
        myProfile = new File(getDataFolder(), "myProfile.yml");
        if(!myProfile.exists()) {
            myProfile.getParentFile().mkdirs();
            saveResource("myProfile.yml", false);
        }
        myProfileConfig = new YamlConfiguration();
        try {
            myProfileConfig.load(myProfile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        hisProfile = new File(getDataFolder(), "hisProfile.yml");
        if(!hisProfile.exists()) {
            hisProfile.getParentFile().mkdirs();
            saveResource("hisProfile.yml", false);
        }
        hisProfileConfig = new YamlConfiguration();
        try {
            hisProfileConfig.load(hisProfile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public MySQL getSQL() {return SQL;}

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> getUsers(Player p) {
        return new ArrayList<>(users).stream().filter(u -> u.getRealUUID().equals(p.getUniqueId())).findFirst();
    }

    public static Profile getInstance() {
        return instance;
    }

    public FileConfiguration getMyConfig() {
        return myProfileConfig;
    }

    public FileConfiguration getHisConfig() {
        return hisProfileConfig;
    }
}