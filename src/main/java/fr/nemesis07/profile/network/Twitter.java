package fr.nemesis07.profile.network;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2UsersByUsernameUsernameResponse;
import com.twitter.clientlib.model.Get2UsersIdResponse;
import com.twitter.clientlib.model.User;
import fr.nemesis07.profile.Profile;
import org.bukkit.Bukkit;

public class Twitter {

    private final String numericID;
    private final TwitterApi apiInstance;
    private final TwitterCredentialsOAuth2 credentials;
    private User user;

    public Twitter(String numericID) {
        this.numericID = numericID;
        this.credentials = new TwitterCredentialsOAuth2(Profile.getInstance().TWITTER_API_KEY,
                Profile.getInstance().TWITTER_API_SECRET_KEY,
                Profile.getInstance().TWITTER_ACCESS_TOKEN,
                Profile.getInstance().TWITTER_ACCESS_TOKEN_SECRET);
        this.apiInstance = new TwitterApi(credentials);

        try {
            Get2UsersIdResponse reponseId = apiInstance.users().findUserById(numericID).execute();

            Bukkit.getScheduler().runTaskTimer(Profile.getInstance(), () -> {
                this.user = reponseId.getData();
            }, 0, 20*30);
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println("" +
                    "aaa" +
                    "aaa" +
                    "aa" +
                    "");
            try {
                Get2UsersByUsernameUsernameResponse reponseUsername = apiInstance.users().findUserByUsername(numericID).execute();

                Bukkit.getScheduler().runTaskTimer(Profile.getInstance(), () -> {
                    this.user = reponseUsername.getData();
                }, 0, 20*30);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
                System.err.println("Exception when calling UsersApi#findUserById");
                System.err.println("Status code: " + e.getCode());
                System.err.println("Reason: " + e.getResponseBody());
                System.err.println("Response headers: " + e.getResponseHeaders());
            }
        }

    }

    public String getNumericID() {
        return numericID;
    }

    public static Twitter getAccount(String username) {
        return new Twitter(username);
    }

    public String getName() {
        return user.getName();
    }

    public String getDescription() {
        return user.getDescription();
    }

    public String getURL() {
        return user.getUrl();
    }

    public int getFollowersCount() {
        return user.getPublicMetrics().getFollowersCount();
    }

    public int getFollowingCount() {
        return user.getPublicMetrics().getFollowingCount();
    }

    public User getUser() {
        return user;
    }

    public int getTweetCount() {
        return user.getPublicMetrics().getTweetCount();
    }

    public String getUsername() {
        return user.getUsername();
    }
}
