package splatnet.s3s.classes.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.util.ArrayList;

public class Game {

    private String id;

    /**
     * turf, zone, tower, rainmaker, clam
     */
    private String vsRule;

    /**
     * regular, anarchy, x, challenge, private, fest
     */
    private String vsMode;

    private Team myTeam;

    private ArrayList<Team> otherTeam;

    /**
     * win, lose, draw (EXEMPTED_LOSE)
     */
    private String status;

    private Stage vsStage;

    /**
     * only used when the game is an X game
     *  can be null
     */
    private String xPower;

    /** series / open --> win or lose pts */
    private String anarchyInfos;

    private int duration;

    /**
     * format : yyyy-mm-ddThh:mm:ssZ
     */
    private String playedTime;

    private JsonArray awards;

    private boolean isKO;

    public Game(String jsonData) {
        this(new JsonParser().parse(jsonData).getAsJsonObject());
    }

    public Game(JsonObject jsonData) {
        if (jsonData.has("data")) {
            jsonData = jsonData.get("data").getAsJsonObject();
        }
        if (jsonData.has("vsHistoryDetail") && !jsonData.get("vsHistoryDetail").isJsonNull()) {
            jsonData = jsonData.get("vsHistoryDetail").getAsJsonObject();
        }

        id = jsonData.get("id").getAsString();
        vsRule = jsonData.get("vsRule").getAsJsonObject().get("name").getAsString();
        vsMode = jsonData.get("vsMode").getAsJsonObject().get("mode").getAsString();
        myTeam = new Team(jsonData.get("myTeam").getAsJsonObject());

        otherTeam = new ArrayList<>();

        JsonArray otherTeamsData = jsonData.get("otherTeams").getAsJsonArray();

        for (JsonElement otherTeamData : otherTeamsData) {
            otherTeam.add(new Team(otherTeamData.getAsJsonObject()));
        }


        status = jsonData.get("judgement").getAsString();
        if (status.equals("EXEMPTED_LOSE")) {
            status = "DRAW";
        } else if (status.equals("DEEMED_LOSE")) {
            status = "LOSE";
        }


        vsStage = Stage.findStage(jsonData.get("vsStage").getAsJsonObject().get("id").getAsString());

        if (vsStage == null) {
            vsStage = new Stage(jsonData.get("vsStage").getAsJsonObject());
            Stage.addStage(vsStage);
        }

        if (jsonData.has("xPower") && !jsonData.get("xPower").isJsonNull()) {
            xPower = jsonData.get("xMatch").getAsJsonObject().get("lastXPower").getAsString();
        }

        duration = jsonData.get("duration").getAsInt();
        playedTime = jsonData.get("playedTime").getAsString();
        awards = jsonData.get("awards").getAsJsonArray();

        if (!jsonData.get("knockout").isJsonNull()) {
            isKO = !jsonData.get("knockout").getAsString().equals("NEITHER");
        } else {
            isKO = false;
        }
    }


    public String getId() {
        return id;
    }

    public String getVsRule() {
        return vsRule;
    }

    public String getVsMode() {
        return vsMode;
    }

    public Team getMyTeam() {
        return myTeam;
    }

    public ArrayList<Team> getOtherTeam() {
        return otherTeam;
    }

    public String getStatus() {
        return status;
    }

    public Stage getVsStage() {
        return vsStage;
    }

    public String getxPower() {
        return xPower;
    }

    public String getAnarchyInfos() {
        return anarchyInfos;
    }

    public int getDuration() {
        return duration;
    }

    public String getPlayedTime() {
        return playedTime;
    }

    public JsonArray getAwards() {
        return awards;
    }

    public boolean isKO() {
        return isKO;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Game ").append(id).append("\n\n").append(vsRule).append(" ").append(vsMode).append(" ").append(status).append("\n");

        return sb.toString();
    }
}
