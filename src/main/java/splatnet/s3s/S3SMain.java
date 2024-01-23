package splatnet.s3s;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;
import splatnet.s3s.classes.Friend;
import splatnet.s3s.classes.Game;
import splatnet.s3s.classes.Player;

public class S3SMain {

    private static final String PATH_TO_DATA_FILES = "src/s3s/data/";

    public static ArrayList<Game> fetchLattestBattles() {

        UtilitaryS3S.checkTokens();

        String key = "LatestBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseVsHistory(UtilitaryS3S.gtoken, key, 50);

        System.out.println(data.get(0));

        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchChallengeBattles() {
        UtilitaryS3S.checkTokens();

        String key = "EventBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

        writeToFile(key, data);

        return gameList(data);
    }

    public static HashMap<String, String> fetchXPowers() {
        UtilitaryS3S.checkTokens();

        HashMap<String, String> data = Exploitation.getXPowers(UtilitaryS3S.gtoken);

        return data;
    }

    public static ArrayList<Game> fetchXBattles() {
        UtilitaryS3S.checkTokens();

        String key = "XBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchPrivateBattles() {
        UtilitaryS3S.checkTokens();

        String key = "PrivateBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchAnarchyBattles() {
        UtilitaryS3S.checkTokens();

        String key = "BankaraBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchTurfWarBattles() {
        UtilitaryS3S.checkTokens();

        String key = "RegularBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Friend> fetchFriendList() {
        UtilitaryS3S.checkTokens();

        JsonArray friendList = Exploitation.fetchFriendList(UtilitaryS3S.gtoken);

        return toFriends(friendList);

    }

    private static ArrayList<Friend> toFriends(JsonArray friendList) {
        ArrayList<Friend> friends = new ArrayList<>();

        for (JsonElement friendData : friendList) {
            friends.add(new Friend(friendData.getAsJsonObject()));
        }

        return friends;
    }

    private static ArrayList<Game> gameList(ArrayList<String> data) {
        ArrayList<Game> games = new ArrayList<>();

        for (String gameData : data) {
            games.add(new Game(gameData));
        }

        return games;
    }

    private static void writeToFile(String key, ArrayList<String> data) {
        if (true) {
            return;
        }

        String completePath = PATH_TO_DATA_FILES + key + ".json";

        File file = new File(completePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (String s : data) {
            JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
            result += gson.toJson(jsonObject) + "\n";
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(result);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Player fetchMyLastGameData() {
        UtilitaryS3S.checkTokens();

        return Exploitation.historyQuery(UtilitaryS3S.gtoken);

    }

    public static void main(String[] args) {
//        fetchMyLastGameData();
        UtilitaryS3S.checkTokens();

    }

}
