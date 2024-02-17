package splatnet.s3s;

import com.google.gson.*;
import org.jsoup.*;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Iksm {
    public static boolean USE_OLD_NSOAPP_VER   = false; // Change this to True if you're getting a "9403: Invalid token." error
    public static String S3S_VERSION           = "unknown";
    public static String NSOAPP_VERSION        = "unknown";
    public static String NSOAPP_VER_FALLBACK   = "2.8.1";
    public static String WEB_VIEW_VERSION      = "unknown";
    public static String WEB_VIEW_VER_FALLBACK = "6.0.0-daea5c11"; // fallback for current splatnet 3 ver
    public static String SPLATNET3_URL         = "https://api.lp1.av5ja.srv.nintendo.net";
    public static String GRAPHQL_URL           = SPLATNET3_URL + "/api/graphql";

//     functions in this file & call stack:
//     - get_nsoapp_version()
//     - get_web_view_ver()
//     - log_in() -> get_session_token()
//     - get_gtoken() -> call_f_api()
//     - get_bullet()
//     - enter_tokens()

    /**
     * Fetches the current Nintendo Switch Online app version from f API or the Apple App Store and sets it globally.
     * @return
     */
    public static String getNsoappVersion() {
        if (NSOAPP_VERSION != "unknown") {
            return NSOAPP_VERSION;
        }

        if (!NSOAPP_VERSION.equals("unknown")) { // already set
            return NSOAPP_VERSION;
        } else {
            try { // try to get NSO version from f API

                String fConfUrl = Paths.get(UtilitaryS3S.getfGenUrl(), "config").toString();
                HashMap<String, String> fConfHeader = new HashMap<>();
                fConfHeader.put("User-Agent", "s3s/" + S3S_VERSION);
                String fConfRsp = Jsoup.connect(fConfUrl).headers(fConfHeader).ignoreContentType(true).execute().body();
                JsonObject fConfJson = JsonParser.parseString(fConfRsp).getAsJsonObject();
                NSOAPP_VERSION = fConfJson.get("nso_version").getAsString();

                return NSOAPP_VERSION;

            } catch (Exception e) {
                return NSOAPP_VER_FALLBACK;

            }
        }

    }

    /**
     * Finds & parses the SplatNet 3 main.js file to fetch the current site version and sets it globally.
     * @return
     */
    public static String getWebViewVer() { // in the og code -> bhead=[] / gtoken=""

        if (!WEB_VIEW_VERSION.equals("unknown")) {
            return WEB_VIEW_VERSION;
        } else {

            HashMap<String, String> appHead = new HashMap<>();
            appHead.put("Upgrade-Insecure-Requests", "1");
            appHead.put("Accept", "*/*");
            appHead.put("DNT", "1");
            appHead.put("X-AppColorScheme", "DARK");
            appHead.put("X-Requested-With", "com.nintendo.znca");
            appHead.put("Sec-Fetch-Site", "none");
            appHead.put("Sec-Fetch-Mode", "navigate");
            appHead.put("Sec-Fetch-User", "?1");
            appHead.put("Sec-Fetch-Dest", "document");

            HashMap<String, String> appCookies = new HashMap<>();
            appCookies.put("_dnt", "1"); // Do Not Track

            /* never reached in the original code
            if bhead:
                app_head["User-Agent"]      = bhead.get("User-Agent")
                app_head["Accept-Encoding"] = bhead.get("Accept-Encoding")
                app_head["Accept-Language"] = bhead.get("Accept-Language")
            if gtoken:
                app_cookies["_gtoken"] = gtoken # X-GameWebToken
                */

            Connection.Response home = null;

            try {

                home = Jsoup.connect(SPLATNET3_URL).headers(appHead).cookies(appCookies).execute();

            } catch (Exception e) {
                // could not connect to network
//                System.exit(1);
            }

            if (home == null || home.statusCode() != 200) {
                System.out.println("Could not connect to SplatNet 3");
                return WEB_VIEW_VER_FALLBACK;
            }

            String mainJsUrl = null;
            Elements mainJs = null;

            try {
                mainJs = home.parse().select("script[src*='static']");

            } catch (IOException e) {
                System.out.println("Could not parse SplatNet 3");
                return WEB_VIEW_VER_FALLBACK;
            }

            if (mainJs != null) {
                mainJsUrl = SPLATNET3_URL + mainJs.attr("src");
            }

            appHead.clear();

            appHead.put("Accept", "*/*");
            appHead.put("X-Requested-With", "com.nintendo.znca");
            appHead.put("Sec-Fetch-Site", "same-origin");
            appHead.put("Sec-Fetch-Mode", "no-cors");
            appHead.put("Sec-Fetch-Dest", "script");
            appHead.put("Referer", SPLATNET3_URL); // sending w/o lang, na_country, na_lang params

            /* never reached in the original code
            if bhead:
                app_head["User-Agent"]      = bhead.get("User-Agent")
                app_head["Accept-Encoding"] = bhead.get("Accept-Encoding")
                app_head["Accept-Language"] = bhead.get("Accept-Language")
            */

            Connection.Response mainJsRsp = null;
            Elements mainJsBody = null;

            try {
                mainJsRsp = Jsoup.connect(mainJsUrl).headers(appHead).cookies(appCookies).ignoreContentType(true).execute();
            } catch (IOException e) {
                System.out.println("Could not connect to SplatNet 3 (2)");
                return WEB_VIEW_VER_FALLBACK;
            }

            if (mainJsRsp == null || mainJsRsp.statusCode() != 200) {
                System.out.println("Could not connect to SplatNet 3 (3)");
                return WEB_VIEW_VER_FALLBACK;
            }

            try {
                mainJsBody = mainJsRsp.parse().select("body");
            } catch (IOException e) {
                System.out.println("Could not parse SplatNet 3 (2)");
                return WEB_VIEW_VER_FALLBACK;
            }
            Pattern pattern = Pattern.compile("\\b(?<revision>[0-9a-fA-F]{40})\\b\\S*?void 0\\S*?\"revision_info_not_set\"\\},.*?=\\`(?<version>\\d+\\.\\d+\\.\\d+)-");
            Matcher match = pattern.matcher(mainJsBody.html());

            if (!match.find()) {
                return WEB_VIEW_VER_FALLBACK;
            }

            String revision, version, verString;

            try {
                revision = match.group(1);
                version = match.group(2);
            } catch (Exception e) {
                return WEB_VIEW_VER_FALLBACK;
            }

            System.out.println("SplatNet 3 version: " + version + "-" + revision);

            verString = version + "-" + revision.substring(0, 8);

            WEB_VIEW_VERSION = verString;

            return WEB_VIEW_VERSION;
        }
    }


    /**
     * Logs in to a Nintendo Account and returns a session_token.
     * @param ver
     * @param appUserAgent
     * @return
     */
    public static ArrayList<String> getConnectionUrl(String ver, String appUserAgent) {
        S3S_VERSION = ver;

        // random bytes
        SecureRandom random = new SecureRandom();

        // random 36 byte string
        byte[] randomBytes = new byte[36];
        random.nextBytes(randomBytes);
        // base64 encode
        String auth_state = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // random 32 byte string
        randomBytes = new byte[32];
        random.nextBytes(randomBytes);
        // base64 encode
        String authCodeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // sha256 hash
        MessageDigest sha256Digest = null;
        try {
            sha256Digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SHA-256 not supported");
//            System.exit(1);
        }
        // hash the verifier
        sha256Digest.update(authCodeVerifier.getBytes(StandardCharsets.UTF_8));
        byte[] hashedBytes = sha256Digest.digest();

        // base64 encode
        String auth_code_challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(hashedBytes);

        HashMap<String, String> appHead = new HashMap<>();
        appHead.put("Host", "accounts.nintendo.com");
        appHead.put("Connection", "keep-alive");
        appHead.put("Cache-Control", "max-age=0");
        appHead.put("Upgrade-Insecure-Requests", "1");
        appHead.put("User-Agent", appUserAgent);
        appHead.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8n");
        appHead.put("DNT", "1");
        appHead.put("Accept-Encoding", "gzip,deflate,br");

        HashMap<String, String> body = new HashMap<>();
        body.put("state", auth_state);
        body.put("redirect_uri", "npf71b963c1b7b6d119://auth");
        body.put("client_id", "71b963c1b7b6d119");
        body.put("scope", "openid user user.birthday user.mii user.screenName");
        body.put("response_type", "session_token_code");
        body.put("session_token_code_challenge", auth_code_challenge.replace("=", ""));
        body.put("session_token_code_challenge_method", "S256");
        body.put("theme", "login_form");

        // equivalent of urllib.parse.urlencode(body) in python
        StringBuilder parameters = new StringBuilder();
        for (Map.Entry<String, String> entry : body.entrySet()) {
            try {
                parameters.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                System.out.println("UTF-8 not supported");
//                System.exit(1);
            }
        }

        ArrayList<String> returnData = new ArrayList<>();

        returnData.add("https://accounts.nintendo.com/connect/1.0.0/authorize?" + parameters.toString());
        returnData.add(authCodeVerifier);

        return returnData;

    }

    public static String logIn(String inputCode, String authCodeVerifier) {
        return getSessionToken(inputCode, authCodeVerifier);
    }

    /**
     * Helper function for log_in().
     * @param input
     * @param authCodeVerifier
     * @return
     */
    public static String getSessionToken(String input, String authCodeVerifier) {
        String 	nsoappVersion = getNsoappVersion();

        HashMap<String, String> appHead = new HashMap<>();
        appHead.put("User-Agent", "OnlineLounge/" + nsoappVersion + " NASDKAPI Android");
        appHead.put("Accept-Language", "en-US");
        appHead.put("Accept", "application/json");
        appHead.put("Content-Type", "application/x-www-form-urlencoded");
        appHead.put("Content-Length", "540");
        appHead.put("Host", "accounts.nintendo.com");
        appHead.put("Connection", "Keep-Alive");
        appHead.put("Accept-Encoding", "gzip");


        HashMap<String, String> body = new HashMap<>();
        body.put("client_id", "71b963c1b7b6d119");
        body.put("session_token_code", input);
        body.put("session_token_code_verifier", authCodeVerifier.replace("=", ""));

        String url = "https://accounts.nintendo.com/connect/1.0.0/api/session_token";

        StringBuilder parameters = new StringBuilder();

        for (Map.Entry<String, String> entry : body.entrySet()) {
            parameters.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }

        String rsp = null;

        try {
            rsp = Jsoup.connect(url).headers(appHead).requestBody(parameters.toString()).ignoreContentType(true).post().body().text();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Could not connect to Nintendo Account (2)");
        }

        String sesssionToken = null;

        try {
            JsonObject rspJson = JsonParser.parseString(rsp).getAsJsonObject();
            sesssionToken = rspJson.get("session_token").getAsString();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Got non-JSON response from Nintendo (in api/session_token step). Please try again.");
        }

        return sesssionToken;


    }

    /**
     * Provided the session_token, returns a GameWebToken JWT and account info.
     * @param fGenUrl
     * @param sessionToken
     * @param ver
     * @return
     */
    public static ArrayList<String> getGtoken(String fGenUrl, String sessionToken, String ver) {
        String nsoappVersion = getNsoappVersion();

        S3S_VERSION = ver;

        HashMap<String, String> appHead = new HashMap<>();
        appHead.put("Host", "accounts.nintendo.com");
        appHead.put("Accept-Encoding", "gzip");
        appHead.put("Content-Type", "application/json");
        appHead.put("Content-Length", "436");
        appHead.put("Accept", "application/json");
        appHead.put("Connection", "Keep-Alive");
        appHead.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2)");

        HashMap<String, String> body = new HashMap<>();
        body.put("client_id", "71b963c1b7b6d119");
        body.put("session_token", sessionToken);
        body.put("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer-session-token");

        String url = "https://accounts.nintendo.com/connect/1.0.0/api/token";

        String rsp = null;

        try {
            rsp = Jsoup.connect(url).headers(appHead).requestBody(new Gson().toJson(body)).ignoreContentType(true).post().body().text();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Could not connect to Nintendo Account for idResponse");
        }

        JsonElement idResponse = null;

        try {
            idResponse = JsonParser.parseString(rsp).getAsJsonObject();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Got non-JSON response from Nintendo (in api/token step). Please try again.");
        }

        // get user info
        try {
            appHead.clear();
            appHead.put("User-Agent", "NASDKAPI; Android");
            appHead.put("Content-Type", "application/json");
            appHead.put("Accept", "application/json");
            appHead.put("Authorization", "Bearer " + idResponse.getAsJsonObject().get("access_token").getAsString());
            appHead.put("Host", "api.accounts.nintendo.com");
            appHead.put("Connection", "Keep-Alive");
            appHead.put("Accept-Encoding", "gzip");


        } catch (Exception e) {
            System.out.println("Not a valid authorization request. Please delete config.txt and try again.");
            System.out.println("Error from Nintendo (in api/token step):");
            System.out.println(idResponse);
//            System.exit(1);
        }

        url = "https://api.accounts.nintendo.com/2.0.0/users/me";

        try {
            // we can GET this url
            rsp = Jsoup.connect(url).headers(appHead).ignoreContentType(true).get().body().text();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Could not connect to Nintendo Account for userInfo");
        }

        JsonObject userInfo = null;

        try {
            userInfo = JsonParser.parseString(rsp).getAsJsonObject();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Got non-JSON response from Nintendo (in users/me step). Please try again.");
//            System.exit(1);
        }

        System.out.println("user info: " + userInfo);

        String userNickname, userLang, userCountry, userId;

        userNickname = userInfo.get("nickname").getAsString();
        userLang = userInfo.get("language").getAsString();
        userCountry = userInfo.get("country").getAsString();
        userId = userInfo.get("id").getAsString();

        System.out.println("userNickname: " + userNickname);
        System.out.println("userLang: " + userLang);
        System.out.println("userCountry: " + userCountry);
        System.out.println("userId: " + userId);

        // get id token
        String idToken = null;
        try {
            idToken = idResponse.getAsJsonObject().get("id_token").getAsString();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du token d'identification");
            System.out.print(e);
        }


        String f = null;
        String uuid = null;
        String timestamp = null;

        HashMap<String, Object> parameters = new HashMap<>();
        body.clear();

        ArrayList<String> data = callFApi(idToken, 1, fGenUrl, userId, null);
        f = data.get(0);
        uuid = data.get(1);
        timestamp = data.get(2);

        parameters.put("f", f);
        parameters.put("language", userLang);
        parameters.put("naBirthday", userInfo.get("birthday").getAsString());
        parameters.put("naCountry", userCountry);
        parameters.put("naIdToken", idToken);
        parameters.put("requestId", uuid);
        parameters.put("timestamp", timestamp);

//        body.put("parameter", new Gson().toJson(parameters));
        HashMap<String, HashMap> bodyTest = new HashMap<>();
        bodyTest.put("parameter", parameters);

        appHead.clear();
        appHead.put("X-Platform", "Android");
        appHead.put("X-ProductVersion", nsoappVersion);
        appHead.put("Content-Type", "application/json; charset=utf-8");
        appHead.put("Content-Length", "" + (990 + f.length()));
        appHead.put("Connection", "Keep-Alive");
        appHead.put("Accept-Encoding", "gzip");
        appHead.put("User-Agent", "com.nintendo.znca/" + nsoappVersion + "(Android/7.1.2)");

        url = "https://api-lp1.znc.srv.nintendo.net/v3/Account/Login";

        try {
            rsp = Jsoup.connect(url).headers(appHead).requestBody(new Gson().toJson(bodyTest)).ignoreContentType(true).post().body().text();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Could not connect to Nintendo Account for accessToken");
        }

        JsonObject splatoonToken = null;
        try {
            splatoonToken = JsonParser.parseString(rsp).getAsJsonObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Got non-JSON response from Nintendo (in Account/Login step). Please try again.");
//            System.exit(1);
        }

        if (splatoonToken == null) {
            System.out.println("Erreur lors de la récupération du token d'accès");
//            System.exit(1);
        }

        System.out.println("splatoonToken: \n" + splatoonToken);

        String accessToken = splatoonToken.get("result").getAsJsonObject().get("webApiServerCredential").getAsJsonObject().get("accessToken").getAsString();
        String coralUserId = splatoonToken.get("result").getAsJsonObject().get("user").getAsJsonObject().get("id").getAsString();



        // get web service token

        appHead.clear();

        appHead.put("X-Platform", "Android");
        appHead.put("X-ProductVersion", nsoappVersion);
        appHead.put("Authorization", "Bearer " + accessToken);
        appHead.put("Content-Type", "application/json; charset=utf-8");
        appHead.put("Content-Length", "391");
        appHead.put("Accept-Encoding", "gzip");
        appHead.put("User-Agent", "com.nintendo.znca/" + nsoappVersion + "(Android/7.1.2)");

        ArrayList<String> data2 = callFApi(accessToken, 2, fGenUrl, userId, coralUserId);
        f = data2.get(0);
        uuid = data2.get(1);
        timestamp = data2.get(2);

        parameters.clear();
	    parameters.put("f", f);
        parameters.put("id", 4834290508791808L);
        parameters.put("registrationToken", accessToken);
        parameters.put("requestId", uuid);
        parameters.put("timestamp", timestamp);

        bodyTest.clear();
        bodyTest.put("parameter", parameters);

      	url = "https://api-lp1.znc.srv.nintendo.net/v2/Game/GetWebServiceToken";

        String response = null;
        JsonObject webServiceToken = null;
        try {
            response = Jsoup.connect(url).headers(appHead).requestBody(new Gson().toJson(bodyTest)).ignoreContentType(true).post().body().text();
            webServiceToken = JsonParser.parseString(response).getAsJsonObject();

            if (webServiceToken.get("status").getAsInt() == 9403
                || webServiceToken.get("status").getAsInt() == 9599) {
                throw new RuntimeException("Invalid token.");
            }

        } catch (IOException e) {
            System.out.println("Got non-JSON response from Nintendo (in Game/GetWebServiceToken step). Please try again.");
            System.out.println(e);
        } catch (RuntimeException e) {

            System.out.println(e.getMessage());
            System.out.println("Trying again with old NSOAPP version...");

            data2 = callFApi(accessToken, 2, fGenUrl, userId, coralUserId);
            f = data2.get(0);
            uuid = data2.get(1);
            timestamp = data2.get(2);

            parameters.clear();
            parameters.put("f", f);
            parameters.put("id", 4834290508791808L);
            parameters.put("registrationToken", accessToken);
            parameters.put("requestId", uuid);
            parameters.put("timestamp", timestamp);

            url = "https://api-lp1.znc.srv.nintendo.net/v2/Game/GetWebServiceToken";

            bodyTest.clear();
            bodyTest.put("parameter", parameters);

            try {
                response = Jsoup.connect(url).headers(appHead).requestBody(new Gson().toJson(bodyTest)).ignoreContentType(true).post().body().text();
                webServiceToken = JsonParser.parseString(response).getAsJsonObject();
            } catch (IOException e2) {
                System.out.println("Got non-JSON response from Nintendo (in Game/GetWebServiceToken step). Please try again.");
                System.out.println(e2);
            }

        }

        String webServiceTokenString = null;

        if (webServiceToken == null) {
            System.out.println("Got non-JSON response from Nintendo (in Game/GetWebServiceToken step). Please try again.");
//            System.exit(1);
        } else {
            webServiceTokenString = webServiceToken.get("result").getAsJsonObject().get("accessToken").getAsString();
        }

        System.out.println("webServiceToken: \n" + webServiceTokenString);

        ArrayList<String> returnData = new ArrayList<>();
        returnData.add(webServiceTokenString);
        returnData.add(userNickname);
        returnData.add(userLang);
        returnData.add(userCountry);

        return returnData;
    }

    /**
     * Given a gtoken, returns a bulletToken.
     * @param webServiceToken
     * @param appUserAgent
     * @param userlang
     * @param userCountry
     * @return
     */
    public static String getBullet(String webServiceToken, String appUserAgent, String userlang, String userCountry) {

        HashMap<String, String> appHead = new HashMap<>();
        appHead.put("Content-Length", "0");
        appHead.put("Content-Type", "application/json");
        appHead.put("Accept-Language", userlang);
        appHead.put("User-Agent", appUserAgent);
        appHead.put("X-Web-View-Ver", WEB_VIEW_VER_FALLBACK);
        appHead.put("X-NACOUNTRY", userCountry);
        appHead.put("Accept", "*/*");
        appHead.put("Origin", SPLATNET3_URL);
        appHead.put("X-Requested-With", "com.nintendo.znca");

        HashMap<String, String> appCookies = new HashMap<>();
        appCookies.put("_gtoken", webServiceToken); // X-GameWebToken
        appCookies.put("_dnt", "1"); // Do Not Track

        System.out.println("cookies: " + new Gson().toJson(appCookies));


        String url = SPLATNET3_URL + "/api/bullet_tokens";

        String response = null;
        try {
            response = Jsoup.connect(url).headers(appHead).cookies(appCookies).ignoreContentType(true).post().body().text();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Could not connect to SplatNet 3 for bulletToken");
        }

        System.out.println("response: " + response);

        String bulletToken = null;
        JsonObject bulletTokenJson;

        try {
            bulletTokenJson = JsonParser.parseString(response).getAsJsonObject();
            bulletToken = bulletTokenJson.get("bulletToken").getAsString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Got non-JSON response from SplatNet 3 (in bullet_tokens step). Please try again.");
//            System.exit(1);
        }


        return bulletToken;
    }

    private static ArrayList<String> callFApi(String accessToken, int step, String fGenUrl,
                                              String userId, String coralUser) {
        try {
            HashMap<String, String> apiHead = new HashMap<>();
            apiHead.put("User-Agent", "s3s/" + S3S_VERSION);
            apiHead.put("Content-Type", "application/json; charset=utf-8");

            HashMap<String, String> apiBody = new HashMap<>();
            apiBody.put("token", accessToken);
            apiBody.put("hash_method", "" + step);
            apiBody.put("na_id", userId);

            if (step == 2 && coralUser == null) {
                apiBody.put("coral_user_id", coralUser);
            }

            // we request the f API using POST
            String apiResponse = Jsoup.connect(fGenUrl).headers(apiHead).requestBody(new Gson().toJson(apiBody)).ignoreContentType(true).post().body().text();

            Gson gson = new Gson();
            JsonObject apiJson = gson.fromJson(apiResponse, JsonObject.class);

            String f = apiJson.get("f").getAsString();
            String uuid = apiJson.get("request_id").getAsString();
            String timestamp = apiJson.get("timestamp").getAsString();

            System.out.println("f: " + f);
            System.out.println("uuid: " + uuid);
            System.out.println("timestamp: " + timestamp);


            ArrayList<String> data = new ArrayList<>();
            data.add(f);
            data.add(uuid);
            data.add(timestamp);

            return data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static HashMap<String, String> getTokens(String sessionToken) {
        HashMap<String, String> tokens = new HashMap<>();

        ArrayList<String> data = getGtoken(UtilitaryS3S.getfGenUrl(), sessionToken, S3S_VERSION);
        String webServiceToken = data.get(0);
        String userNickname = data.get(1);
        String userLang = data.get(2);
        String userCountry = data.get(3);

        String bullet = getBullet(webServiceToken, UtilitaryS3S.APP_USER_AGENT, userLang, userCountry);

        tokens.put("acc_loc", userLang + "-" + userCountry);
        tokens.put("gtoken", webServiceToken);
        tokens.put("bullettoken", bullet);
        tokens.put("session_token", sessionToken);
        tokens.put("f_gen", UtilitaryS3S.getfGenUrl());

        return tokens;
    }


}
