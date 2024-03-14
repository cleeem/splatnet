package splatnet.s3s;

import com.google.gson.*;
import splatnet.Main;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilitaryS3S {

    private static final String ASSETS_URL = Main.class.getResource("assets/").getPath();

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

    public static void init(String userInput, String authCodeVerifier) {

        setup();
        createSessionToken(userInput, authCodeVerifier);

    }

    private static void createSessionToken(String userInput, String authCodeVerifier) {

        String url;

        // session_token_code = re.search('de=(.*)&', use_account_url)
        // use regex to get session token code
        Pattern pattern = Pattern.compile("de=(.*)&");
        Matcher matcher = pattern.matcher(userInput);
        String sessionTokenCode = null;
        if (matcher.find()) {
            sessionTokenCode = matcher.group(1);
        } else {
            throw new RuntimeException("Session token code not found");
        }

        String sessionToken = Iksm.getSessionToken(sessionTokenCode, authCodeVerifier);

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
        try {
            Exploitation.test(gtoken);
        } catch (Exception e) {

            System.out.println(e);

            if (e.getMessage().contains("Status=401")) {
                // gtoken expired
                throw new RuntimeException("gtoken expired");
            } else if (e.getMessage().contains("Status=400")) {
                // config file is invalid
                throw new RuntimeException("config file error");
            } else {
                // internet error
                throw new RuntimeException("internet error");
            }

        }

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

                setupComplete = true;
                return setupComplete;

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

                userLang = "";
                userCountry = "";
                gtoken = "";
                bulletToken = "";
                sessionToken = "";
                fGenUrl = "https://api.imink.app/f";

                return setupComplete;

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static void downloadImage(String objectUrl, String objectName, String imageType) throws IOException {
        System.out.println("Downloading " + objectName + " image type: " + imageType + " from " + objectUrl);
        URL url = new URL(objectUrl);
        InputStream in = url.openStream();
        // the output file will be saved in the assets folder
        // the one in src
        File file = new File("src/main/resources/splatnet/assets/" + imageType + "/" + objectName + ".png");
        file.createNewFile();
        OutputStream out1 = new FileOutputStream("src/main/resources/splatnet/assets/" + imageType + "/" + objectName + ".png");

        // the one in target
        File file2 = new File("target/classes/splatnet/assets/" + imageType + "/" + objectName + ".png");
        file2.createNewFile();
        OutputStream out2 = new FileOutputStream("target/classes/splatnet/assets/" + imageType + "/" + objectName + ".png");

        byte[] b = new byte[2048];
        int length;

        while ((length = in.read(b)) != -1) {
            out1.write(b, 0, length);
            out2.write(b, 0, length);
        }

        in.close();
        out1.close();
        out2.close();

    }

}
