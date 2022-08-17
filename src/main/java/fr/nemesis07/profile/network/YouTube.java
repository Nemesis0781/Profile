package fr.nemesis07.profile.network;

import fr.nemesis07.profile.Profile;
import kong.unirest.Callback;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class YouTube {

    private final String channelID;

    private String title;
    private String description;
    private String URLpp;

    private String subscriberCount;
    private String viewCount;
    private String videoCount;

    public YouTube(String channelID) {
        this.channelID = channelID;
        Profile.getInstance().getServer().getScheduler().runTaskTimer(Profile.getInstance(), () -> {
            Unirest.get("https://www.googleapis.com/youtube/v3/channels?id="+channelID+"&key="+ Profile.getInstance().YOUTUBE_API_KEY+"&part=snippet,statistics").asJsonAsync(new Callback<JsonNode>() {
                @Override
                public void completed(HttpResponse<JsonNode> reponse) {
                    JSONObject channel = reponse.getBody().getObject();

                    if(channel.has("items")) {
                        JSONArray items = channel.getJSONArray("items");
                        JSONObject firstItem = items.getJSONObject(0);
                        JSONObject snippet = firstItem.getJSONObject("snippet");
                        JSONObject statistics = firstItem.getJSONObject("statistics");

                        viewCount = statistics.getString("viewCount");
                        subscriberCount = statistics.getString("subscriberCount");
                        videoCount = statistics.getString("videoCount");

                        title = snippet.getString("title");
                        description = snippet.getString("description");

                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject tinypp = thumbnails.getJSONObject("default");
                        URLpp = tinypp.getString("url");
                    }
                }
            });
        }, 0, 20*30);
    }

    public static YouTube getAccount(String channelID) {
        return new YouTube(channelID);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getURLpp() {
        return URLpp;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public String getChannelID() {return channelID; }

}
