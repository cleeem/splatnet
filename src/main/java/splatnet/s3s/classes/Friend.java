package splatnet.s3s.classes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Friend {

    private String nickname;

    private String onlineStatus;

    private String playerName; // can be null

    /** web url to the image  */
    private String userIcon;

    private String vsMode; // can be null


    public Friend(String jsonData) {
        this(new JsonParser().parse(jsonData).getAsJsonObject());
    }

    public Friend(JsonObject jsonData) {

        onlineStatus = jsonData.get("onlineState").getAsString();
        nickname = jsonData.get("nickname").getAsString();
        userIcon = jsonData.get("userIcon").getAsJsonObject().get("url").getAsString();
        if (!jsonData.get("playerName").isJsonNull()) {
            playerName = jsonData.get("playerName").getAsString();
        }

        if (!jsonData.get("vsMode").isJsonNull()) {
            vsMode = jsonData.get("vsMode").getAsJsonObject().get("mode").getAsString();
        }

    }

    public String getNickname() {
        return nickname;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public String getVsMode() {
        return vsMode;
    }
}
