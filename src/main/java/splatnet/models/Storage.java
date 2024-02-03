package splatnet.models;

import splatnet.s3s.classes.Friend;
import splatnet.s3s.classes.Game;
import splatnet.s3s.classes.Player;

import java.lang.reflect.Array;
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

    private Storage() {}

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
