package splatnet.s3s.classes.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class Team {

    /**
     * a b g r color
     */
    private JsonObject color;

    private String score;

    /**
     * only in splatfest
     */
    private String teamName;

    /**
     * indicates wich one is on top of the screen
     * (1 or 2)
     */
    private int order;

    private ArrayList<Player> players;

    public Team(String jsonData) {
        this(new JsonParser().parse(jsonData).getAsJsonObject());
    }

    public Team(JsonObject jsonData) {
        color = jsonData.get("color").getAsJsonObject();

        if (!jsonData.get("result").isJsonNull()) {
            JsonObject result = jsonData.get("result").getAsJsonObject();

            if (result.has("score") && !result.get("score").isJsonNull()) {
                score = result.get("score").getAsString();
            }
            if (result.has("paintRatio") && !result.get("paintRatio").isJsonNull()) {
                score = result.get("paintRatio").getAsString();
            }

        } else {
            score = "DRAW";
        }

        if (jsonData.has("festTeamName") && !jsonData.get("festTeamName").isJsonNull()) {
            teamName = jsonData.get("festTeamName").getAsString();
        }

        JsonArray playersData = jsonData.get("players").getAsJsonArray();

        players = new ArrayList<>();
        for (JsonElement playerData : playersData) {
            players.add(new Player(playerData.getAsJsonObject()));
        }

    }

    public JsonObject getColor() {
        return color;
    }

    public String getScore() {
        return score;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getOrder() {
        return order;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
