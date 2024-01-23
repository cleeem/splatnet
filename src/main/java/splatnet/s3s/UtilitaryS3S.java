package splatnet.s3s;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UtilitaryS3S {

    public static final String A_VERSION = "0.6.0";

    public static final boolean DEBUG = false;

    // SET HTTP HEADERS
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Linux; Android 11; Pixel 5) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/94.0.4606.61 Mobile Safari/537.36";

    public static final String APP_USER_AGENT = DEFAULT_USER_AGENT;

    public static Gson configData = new GsonBuilder().setPrettyPrinting().create();

    public static String userLang;
    public static String userCountry;
    public static String gtoken;
    public static String bulletToken;
    public static String sessionToken;
    public static String fGenUrl;

    private final static String CONFIG_PATH = System.getProperty("user.dir") + "/config.txt";

    public static void init(String userInput) {

        setup();
        createSessionToken(userInput);

    }

    private static void createSessionToken(String userInput) {
        ArrayList<String> data = Iksm.getConnectionUrl(Iksm.getNsoappVersion(), APP_USER_AGENT);

        String url = data.get(0);
        String authCodeVerifier = data.get(1);

        if (userInput == null || userInput.isEmpty()) {

            Scanner scanner = new Scanner(System.in);

            boolean inputValid = false;
            do {
                System.out.println("Please enter the code you got from the website:");
                System.out.print(url);
                System.out.println();

                userInput = scanner.nextLine();
                if (userInput.length() < 50) {
                    System.out.println("Invalid code");
                } else {
                    inputValid = true;
                }

            } while (!inputValid);
        } else {
            userInput = userInput.trim();
        }


        String sessionToken = Iksm.getSessionToken(userInput, authCodeVerifier);

        ArrayList<String> token = Iksm.getGtoken(getfGenUrl(), sessionToken, Iksm.getNsoappVersion());

        String webServiceToken = token.get(0);
        String userNickName = token.get(1);
        String userLang = token.get(2);
        String userCountry = token.get(3);

        String gBullet = Iksm.getBullet(webServiceToken, APP_USER_AGENT, userLang, userCountry);

        String accLoc = userLang + "|" + userCountry;

        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("gtoken",webServiceToken);
        tokens.put("bullettoken",gBullet);
        tokens.put("session_token",sessionToken);
        tokens.put("f_gen",getfGenUrl());
        tokens.put("acc_loc",accLoc);

        writeConfig(tokens);
    }

    public static void checkTokens() {

        // check if config file exists
        if (!setup()) {
            System.out.println("config file not found");
            throw new RuntimeException("config file not found");
        }

        try {
            Exploitation.test(gtoken);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("gtoken expired");
            if (setup()) {
                writeConfig(Iksm.getTokens(sessionToken));
            } else {
//                init(null);
                throw new RuntimeException("GTOKEN EXPIRED and session token invalid");
            }
        }

        System.out.println("gtoken valid");

    }

    public static boolean setup() {
        boolean setupComplete = false;

        // CONFIG.TXT CREATION
        try {
            File configFile = new File(CONFIG_PATH);


            JsonElement fileData;
            JsonObject returnData = null;

            FileReader reader;

            if (configFile.exists() && (new FileReader(configFile).read() != -1)) {
                // read config file
                System.out.println("Reading config file");

                reader = new FileReader(configFile);
                fileData = JsonParser.parseReader(reader);
                returnData = fileData.getAsJsonObject();

            } else {
                // create config file
                configFile.createNewFile();

                configData = new GsonBuilder().setPrettyPrinting().create();

                returnData = new JsonObject();
                returnData.add("acc_loc", new Gson().toJsonTree(""));
                returnData.add("gtoken", new Gson().toJsonTree(""));
                returnData.add("bullettoken", new Gson().toJsonTree(""));
                returnData.add("session_token", new Gson().toJsonTree(""));
                returnData.add("f_gen", new Gson().toJsonTree("https://api.imink.app/f"));

                // write config file
                FileWriter writer = new FileWriter(CONFIG_PATH);
                writer.write(configData.toJson(returnData));
                writer.close();

            }

            // set global variables
            int accLocLength = returnData.get("acc_loc").getAsString().length();

            if (!returnData.get("acc_loc").getAsString().isEmpty()) {
                userLang = returnData.get("acc_loc").getAsString().substring(0, 5);
            }
            if (accLocLength > 2) {
                userCountry = returnData.get("acc_loc").getAsString().substring(accLocLength - 2);
            }
            gtoken = returnData.get("gtoken").getAsString();
            bulletToken = returnData.get("bullettoken").getAsString();
            sessionToken = returnData.get("session_token").getAsString();
            fGenUrl = returnData.get("f_gen").getAsString();

            if (gtoken.isEmpty() || bulletToken.isEmpty() || sessionToken.isEmpty() || fGenUrl.isEmpty()) {
                return false;
            }

            setupComplete = true;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return setupComplete;
    }

    /**
     * Writes config file and updates the global variables.
     * @param tokens
     */
    public static void writeConfig(HashMap<String, String> tokens) {
        // CONFIG.TXT PATH

        File configFile = new File(CONFIG_PATH);

        try {
            Writer writer = new FileWriter(CONFIG_PATH);

            configData.toJson(tokens, writer);

            writer.close();

            // update global variables
            int accLocLength = tokens.get("acc_loc").length();

            userLang = tokens.get("acc_loc").substring(0, 5);
            userCountry = tokens.get("acc_loc").substring(accLocLength - 2);
            gtoken = tokens.get("gtoken");
            bulletToken = tokens.get("bullettoken");
            sessionToken = tokens.get("session_token");
            fGenUrl = tokens.get("f_gen");

            System.out.println("config file updated");

        } catch (IOException e) {
            System.out.println("config file update failed");
            throw new RuntimeException(e);
        }

    }

    /**
     * Returns a (dynamic!) header used for GraphQL requests.
     * @param forceLang
     * @return
     */
    public static HashMap<String, String> headbutt(String forceLang) {
        String lang;
        String country;

        if (forceLang != null) {
            lang = forceLang;
            country = forceLang.substring(forceLang.length() - 2);
        } else {
            lang = userLang;
            country = userCountry;
        }

        HashMap<String, String> graphqlHeader = new HashMap<>();

        graphqlHeader.put("Authorization", "Bearer " + bulletToken);
        graphqlHeader.put("Accept-Language", lang);
        graphqlHeader.put("User-Agent", APP_USER_AGENT);
        graphqlHeader.put("X-Web-View-Ver", Iksm.getWebViewVer());
        graphqlHeader.put("Content-Type", "application/json");
        graphqlHeader.put("Accept", "*/*");
        graphqlHeader.put("Origin", Iksm.SPLATNET3_URL);
        graphqlHeader.put("X-Requested-With", "com.nintendo.znca");
        graphqlHeader.put("Referer", Iksm.SPLATNET3_URL + "?lang=" + lang + "&na_country=" + country + "&na_lang=" + lang);
        graphqlHeader.put("Accept-Encoding", "gzip, deflate");

        return graphqlHeader;
    }



    public static String getfGenUrl() {
        return fGenUrl;
    }
}
