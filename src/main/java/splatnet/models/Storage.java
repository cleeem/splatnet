package splatnet.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import splatnet.s3s.UtilitaryS3S;
import splatnet.s3s.classes.game.Game;
import splatnet.s3s.classes.game.Player;
import splatnet.s3s.classes.misc.Ability;
import splatnet.s3s.classes.misc.Brand;
import splatnet.s3s.classes.misc.Friend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

//    private ArrayList<Game> salmonGames = new ArrayList<>();

    private ArrayList<Game> turfWarGames = new ArrayList<>();

    private ArrayList<Game> anarchyGames = new ArrayList<>();

    private HashMap<String, String> xPowers = new HashMap<>();

    private ArrayList<Friend> friendList = new ArrayList<>();

    private ArrayList<Ability> abilities = new ArrayList<>();

    private ArrayList<Brand> brands = new ArrayList<>();

    private Storage() {

        File file = new File("src/main/resources/splatnet/data/abilities.json");

        // file data is json type
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                String result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                fileReader.close();
                JsonObject obj = JsonParser.parseString(result).getAsJsonObject();

                JsonArray nodes = obj.getAsJsonObject("data")
                                     .getAsJsonObject("gearPowers")
                                     .getAsJsonArray("nodes");

                for (JsonElement node : nodes) {
                    String id = node.getAsJsonObject().get("gearPowerId").getAsString();

                    String url = node.getAsJsonObject().get("image").getAsJsonObject().get("url").getAsString();

                    try {
                        UtilitaryS3S.downloadSmallImage(url, id, "abilities");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Ability ability = new Ability(node.getAsJsonObject());
                    abilities.add(ability);
                    Ability.addAbility(ability);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        file = new File("src/main/resources/splatnet/data/brands.json");

        // file data is json type
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                String result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                fileReader.close();
                JsonObject obj = JsonParser.parseString(result).getAsJsonObject();

                JsonArray nodes = obj.getAsJsonObject("data")
                                     .getAsJsonObject("brands")
                                     .getAsJsonArray("nodes");

                for (JsonElement node : nodes) {
                    String id = node.getAsJsonObject().get("id").getAsString();

                    String url = node.getAsJsonObject().get("image").getAsJsonObject().get("url").getAsString();

                    try {
                        UtilitaryS3S.downloadSmallImage(url, id, "brands");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Brand brand = new Brand(node.getAsJsonObject());
                    brands.add(brand);
                    Brand.addBrand(brand);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public ArrayList<Friend> getFriendList() {
        return friendList;
    }

    public Player getPlayerData() {
        return playerData;
    }

    public Game getSelectedGame() {return selectedGame;}

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

    public void setFriendList(ArrayList<Friend> friendList) {
        this.friendList = friendList;
    }

    public void setPlayerData(Player playerData) {
        this.playerData = playerData;
    }

    public void setSelectedGame(Game game) {this.selectedGame = game;}
}
