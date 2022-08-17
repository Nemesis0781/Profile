package fr.nemesis07.profile.network;

public class Discord {

    private String tag;
    
    public Discord(String tag) {
        this.tag = tag;
    }

    public static Discord getAccount(String tag) {
        return new Discord(tag);
    }

    public String getTag() {
        return tag;
    }
}
