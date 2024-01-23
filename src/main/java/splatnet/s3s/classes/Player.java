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

    private String bannerUrl;

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

    private JsonObject headGear;

    private JsonObject clothingGear;

    private JsonObject shoesGear;



    public Player(String jsonData) {
        this(new JsonParser().parse(jsonData).getAsJsonObject());
    }

    public Player(JsonObject jsonData) {

        name = jsonData.get("name").getAsString();

        if (jsonData.has("byname")) {
            quote = jsonData.get("byname").getAsString();
        }

        weapon = jsonData.get("weapon").getAsJsonObject();

        if (jsonData.has("species") && !jsonData.get("species").isJsonNull()) {
            species = jsonData.get("species").getAsString();
        }


        bannerUrl = jsonData.getAsJsonObject("nameplate").getAsJsonObject("background").getAsJsonObject("image").get("url").getAsString();

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

        headGear = jsonData.get("headGear").getAsJsonObject();

        clothingGear = jsonData.get("clothingGear").getAsJsonObject();

        shoesGear = jsonData.get("shoesGear").getAsJsonObject();

        if (jsonData.has("paint") && !jsonData.get("paint").isJsonNull()) {
            paint = jsonData.get("paint").getAsInt();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQuote() {
        return quote;
    }

    public JsonObject getWeapon() {
        return weapon;
    }

    public String getSpecies() {
        return species;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public JsonObject getNamePlate() {
        return namePlate;
    }

    public int getKills() {
        return kills;
    }

    public int getDeath() {
        return death;
    }

    public int getAssists() {
        return assists;
    }

    public int getSpecial() {
        return special;
    }

    public int getPaint() {
        return paint;
    }

    public boolean isCrown() {
        return crown;
    }

    public JsonObject getHeadGear() {
        return headGear;
    }

    public JsonObject getClothingGear() {
        return clothingGear;
    }

    public JsonObject getShoesGear() {
        return shoesGear;
    }
}
