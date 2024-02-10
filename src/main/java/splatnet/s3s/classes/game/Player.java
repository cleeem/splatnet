package splatnet.s3s.classes.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import splatnet.s3s.classes.misc.Stuff;
import splatnet.s3s.classes.weapons.MainWeapon;

public class Player {

    private String id;

    private boolean isMyself;

    private String nameId;

    private String name;

    private String quote;

    private MainWeapon weapon;

    /**
     * Inkling, Octoling
     */
    private String species;

    private JsonObject banner;

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

    private Stuff headGear;

    private Stuff clothingGear;

    private Stuff shoesGear;



    public Player(String jsonData) {
        this(new JsonParser().parse(jsonData).getAsJsonObject());
    }

    public Player(JsonObject jsonData) {

        if (jsonData.has("isMyself") && !jsonData.get("isMyself").isJsonNull()) {
            isMyself = jsonData.get("isMyself").getAsBoolean();
        }

        if (jsonData.has("id") && !jsonData.get("id").isJsonNull()) {
            id = jsonData.get("id").getAsString();
        }

        name = jsonData.get("name").getAsString();

        nameId = jsonData.get("nameId").getAsString();

        if (jsonData.has("byname")) {
            quote = jsonData.get("byname").getAsString();
        }

        weapon = new MainWeapon(jsonData.get("weapon").getAsJsonObject());

        if (jsonData.has("species") && !jsonData.get("species").isJsonNull()) {
            species = jsonData.get("species").getAsString();
        }


        banner = jsonData.getAsJsonObject("nameplate").getAsJsonObject("background");

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

        headGear = new Stuff(jsonData.get("headGear").getAsJsonObject());

        clothingGear = new Stuff(jsonData.get("clothingGear").getAsJsonObject());

        shoesGear = new Stuff(jsonData.get("shoesGear").getAsJsonObject());

        if (jsonData.has("paint") && !jsonData.get("paint").isJsonNull()) {
            paint = jsonData.get("paint").getAsInt();
        }
    }

    public String getId() {
        return id;
    }

    public boolean isMyself() {return isMyself;}

    public String getName() {
        return name;
    }

    public String getQuote() {
        return quote;
    }

    public MainWeapon getWeapon() {
        return weapon;
    }

    public String getSpecies() {
        return species;
    }

    public JsonObject getBanner() {
        return banner;
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

    public Stuff getHeadGear() {
        return headGear;
    }

    public Stuff getClothingGear() {
        return clothingGear;
    }

    public Stuff getShoesGear() {
        return shoesGear;
    }

    public String getNameId() {
        return nameId;
    }
}
