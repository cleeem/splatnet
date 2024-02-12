package splatnet.s3s;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;
import splatnet.s3s.classes.misc.Friend;
import splatnet.s3s.classes.game.Game;
import splatnet.s3s.classes.game.Player;

public class S3SMain {

    private static final String PATH_TO_DATA_FILES = "src/main/java/splatnet/s3s/data/";

    public static ArrayList<Game> fetchLattestBattles() {

        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }

        String key = "LatestBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseVsHistory(UtilitaryS3S.gtoken, key, 50);

//        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchChallengeBattles() {
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }
        String key = "EventBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

//        writeToFile(key, data);

        return gameList(data);
    }

    public static HashMap<String, String> fetchXPowers() {
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }

        return Exploitation.getXPowers(UtilitaryS3S.gtoken);
    }

    public static ArrayList<Game> fetchXBattles() {
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }

        String key = "XBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

//        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchPrivateBattles() {
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }

        String key = "PrivateBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

//        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchAnarchyBattles() {
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }

        String key = "BankaraBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

//        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Game> fetchTurfWarBattles() {
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }

        String key = "RegularBattleHistoriesQuery";

        ArrayList<String> data = Exploitation.parseBattlesHistory(UtilitaryS3S.gtoken, key, 50);

//        writeToFile(key, data);

        return gameList(data);
    }

    public static ArrayList<Friend> fetchFriendList() {
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }

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

    public static void createTokens(String input, String authCode) {
        UtilitaryS3S.init(input, authCode);
    }


    public static void downloadBanner(JsonObject bannerObject) {
        String bannerUrl = bannerObject.get("image").getAsJsonObject().get("url").getAsString();
        String bannerName = bannerObject.get("id").getAsString();

        try {
            UtilitaryS3S.downloadSmallImage(bannerUrl, bannerName, "banners");
        } catch (IOException e) {
            System.out.println(e);
        }

    }



    public static void main(String[] args) {
        UtilitaryS3S.setup();
        try {
            UtilitaryS3S.checkTokens();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
            }
        }
        UtilitaryS3S.checkTokens();

        String key = "myOutfitCommonDataEquipmentsQuery";
        String data = Exploitation.customQuery(
                UtilitaryS3S.gtoken,
                key,
                null,
                null
        );
    }
}
