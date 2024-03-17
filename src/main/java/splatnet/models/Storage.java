package splatnet.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;
import splatnet.s3s.classes.game.Game;
import splatnet.s3s.classes.game.Player;
import splatnet.s3s.classes.misc.Ability;
import splatnet.s3s.classes.misc.Brand;
import splatnet.s3s.classes.misc.Friend;
import splatnet.s3s.classes.weapons.MainWeapon;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class Storage {

    private static Storage instance = new Storage();

    private Player playerData = null;

    private Game selectedGame = null;

    private ArrayList<Game> games = new ArrayList<>();

    private ArrayList<Game> latestGames = new ArrayList<>();

    private ArrayList<Game> challengeGames = new ArrayList<>();

    private ArrayList<Game> xGames = new ArrayList<>();

    private ArrayList<Game> privateGames = new ArrayList<>();

    private ArrayList<Game> turfWarGames = new ArrayList<>();

    private ArrayList<Game> anarchyGames = new ArrayList<>();

    private HashMap<String, String> xPowers = new HashMap<>();

    private Player player = null;

    private Storage() {

        try {
            InputStream abilitiesStream = Main.class.getResourceAsStream("data/abilities.json");
            JsonArray abilitiesArray = new JsonParser().parse(new InputStreamReader(abilitiesStream))
                    .getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonObject("gearPowers")
                    .getAsJsonArray("nodes");

            for (JsonElement abilityElement : abilitiesArray) {
                JsonObject abilityObject = abilityElement.getAsJsonObject();
                Ability ability = new Ability(abilityObject);
                Ability.addAbility(ability);
            }

            InputStream brandsStream = Main.class.getResourceAsStream("data/brands.json");
            JsonArray brandsArray = new JsonParser().parse(new InputStreamReader(brandsStream))
                    .getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonObject("brands")
                    .getAsJsonArray("nodes");

            for (JsonElement brandElement : brandsArray) {
                JsonObject brandObject = brandElement.getAsJsonObject();
                Brand brand = new Brand(brandObject);
                Brand.addBrand(brand);
            }

            InputStream weaponStream = Main.class.getResourceAsStream("data/weapons.json");
            JsonArray weaponsArray = new JsonParser().parse(new InputStreamReader(weaponStream))
                    .getAsJsonObject()
                    .getAsJsonObject("data")
                    .getAsJsonObject("weaponRecords")
                    .getAsJsonArray("nodes");

            for (JsonElement weaponElement : weaponsArray) {
                JsonObject weaponObject = weaponElement.getAsJsonObject();
                MainWeapon mainWeapon = new MainWeapon(weaponObject);
                MainWeapon.addMainWeapon(mainWeapon);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Storage getInstance() {
        return instance;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Game> getLatestGames() {
        return latestGames;
    }

    public ArrayList<Game> getChallengeGames() {
        return challengeGames;
    }

    public ArrayList<Game> getxGames() {
        return xGames;
    }

    public ArrayList<Game> getPrivateGames() {
        return privateGames;
    }

    public ArrayList<Game> getTurfWarGames() {
        return turfWarGames;
    }

    public ArrayList<Game> getAnarchyGames() {
        return anarchyGames;
    }

    public HashMap<String, String> getxPowers() {
        return xPowers;
    }

    public Player getPlayerData() {
        return playerData;
    }

    public Game getSelectedGame() {return selectedGame;}

    public Player getPlayer() {
        return player;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public void setLatestGames(ArrayList<Game> latestGames) {this.latestGames = latestGames;}

    public void setChallengeGames(ArrayList<Game> challengeGames) {
        this.challengeGames = challengeGames;
    }

    public void setxGames(ArrayList<Game> xGames) {
        this.xGames = xGames;
    }

    public void setPrivateGames(ArrayList<Game> privateGames) {
        this.privateGames = privateGames;
    }

    public void setTurfWarGames(ArrayList<Game> turfWarGames) {
        this.turfWarGames = turfWarGames;
    }

    public void setAnarchyGames(ArrayList<Game> anarchyGames) {
        this.anarchyGames = anarchyGames;
    }

    public void setxPowers(HashMap<String, String> xPowers) {
        this.xPowers = xPowers;
    }

    public void setPlayerData(Player playerData) {
        this.playerData = playerData;
    }

    public void setSelectedGame(Game game) {this.selectedGame = game;}

    public void setPlayer(Player player) {
        this.player = player;
    }
}
