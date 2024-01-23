package splatnet.s3s.classes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Player {

    private String id;

    private String name;

    private String quote;

    private JsonObject weapon;

    /**
     * Inkling, Octoling
     */
    private String species;

    private String bannerId;

    private JsonObject namePlate;

    private int kills;
    private int death;

    private int assists;

    private int special;

    private int paint;

    /**
     * if the player is in top 500
     */
    private boolean crown;


    public Player(String jsonData) {
        this(new JsonParser().parse(jsonData).getAsJsonObject());
    }

    public Player(JsonObject jsonData) {

        name = jsonData.get("name").getAsString();

        if (jsonData.has("byname")) {
            quote = jsonData.get("byname").getAsString();
        }

        weapon = jsonData.get("weapon").getAsJsonObject();

        species = jsonData.get("species").getAsString();

        if (jsonData.has("nameId")) {
            bannerId = jsonData.get("nameId").getAsString();
        }

        namePlate = jsonData.get("nameplate").getAsJsonObject();

        JsonObject results;
        if (jsonData.has("results") && !jsonData.get("results").isJsonNull()) {
            results = jsonData.get("results").getAsJsonObject();
            kills = results.get("kills").getAsInt();
            death = results.get("deaths").getAsInt();
            assists = results.get("assists").getAsInt();
            special = results.get("special").getAsInt();
        }


        if (jsonData.has("crown")) {
            crown = jsonData.get("crown").getAsBoolean();
        }

        paint = jsonData.get("paint").getAsInt();
    }
}
