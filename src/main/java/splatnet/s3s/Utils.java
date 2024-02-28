package splatnet.s3s;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class Utils {

    /** contains all the suported keys */
    private static final ArrayList<String> SUPPORTED_KEYS = new ArrayList<String>() {{
        add("ignore_private");
        add("ignore_private_jobs");
        add("app_user_agent");
        add("force_uploads");
        add("errors_pass_silently");
    }};

    /** contains all the supported values */
    public static final HashMap<String, String> translateRid = new HashMap<>(){{
        put("BankaraBattleHistoriesQuery", "9863ea4744730743268e2940396e21b891104ed40e2286789f05100b45a0b0fd");
        put("BankaraBattleHistoriesRefetchQuery", "7673fe37d5d5d81fa37d0b1cc02cffd7453a809ecc76b000c67d61aa51a39890");
        put("BattleHistoryCurrentPlayerQuery", "8b59e806751e4e74359b416472925bb405601a626743084d2158af72cc3e7716");
        put("CatalogQuery", "40b62e4734f22a6009f1951fc1d03366b14a70833cb96a9a46c0e9b7043c67ef");
        put("CatalogRefetchQuery", "c4f5474dfc5d7937618d8a38357ad1e78cc83d6019833b1b68d86a0ce8d4b9e5");
        put("ChallengeQuery", "4bab7a084fc42a2fe22d78498ce7e429dc9b0c994db87985bdf64a1566612b03");
        put("ChallengeRefetchQuery", "818b2bc11b278fad6dc16cf84a2030b70a506e8bc6e20f490c07d7934337a5cd");
        put("CheckinQuery", "6dfce83d02761395758ae21454cb46924e81c22c3f151f91330b0602278a060e");
        put("CheckinWithQRCodeMutation", "63a60eea7926b0f2600cfb64d8bf3b6736afc1e1040beabd5dfa40fbfdcb92d8");
        put("ConfigureAnalyticsQuery", "2a9302bdd09a13f8b344642d4ed483b9464f20889ac17401e993dfa5c2bb3607");
        put("CoopHistoryDetailQuery", "42262d241291d7324649e21413b29da88c0314387d8fdf5f6637a2d9d29954ae");
        put("CoopHistoryDetailRefetchQuery", "4bf516ccfd9a3f4efc32b215c59ae42c2a06dd2d8f73de95c2676dea6db74446");
        put("CoopHistoryQuery", "0f8c33970a425683bb1bdecca50a0ca4fb3c3641c0b2a1237aedfde9c0cb2b8f");
        put("CoopPagerLatestCoopQuery", "bc8a3d48e91d5d695ef52d52ae466920670d4f4381cb288cd570dc8160250457");
        put("CoopRecordBigRunRecordContainerPaginationQuery", "969a2318546e22dce98ddf83f5b0f3fe976f09b93dfe4bbb4c79d1de0b637b0c");
        put("CoopRecordPlayHistoryRefetchQuery", "a473c4d0bcb1bdd119e935dd64b90ee5fc97650f5fea6dbaca3ca8330df319e8");
        put("CoopRecordQuery", "56f989a59643642e0799c90d3f6d0457f5f5f72d4444dfae87043c4a23d13043");
        put("CoopRecordRefetchQuery", "b43e1e3fcfd8aeca37a32009c2f85d89d74b20cb566b68a846d50a7e9a494685");
        put("CreateMyOutfitMutation", "b5257c5a3840cb01556750cbb56881d758534dfd91e9aec7c0232098fd767bb9");
        put("DefeatEnemyRecordRefetchQuery", "f79466b7aaf0a4a94332791074e3eba9e0b4941d8ca3098ca22ef851b260dc2c");
        put("DetailFestRecordDetailQuery", "02946c9d6dec617425ed41ee9a9bf467ea2ddfb85e0a36b09e4c3ea2e0b9ac5b");
        put("DetailFestRefethQuery", "dc5c1890cec78094d919e71621e9b4bc1ee06cfa99812dcacb401b8116a1ccad");
        put("DetailFestVotingStatusRefethQuery", "4a24f9ff7b1c5a5c520872ce083c1623354c3ec092a0bf95c0dc1c12a1e3fb63");
        put("DetailRankingQuery", "2e1f603f6da371874a7473bb68418d9308f1fd2492e57fd2b7d9bbb80138f8c0");
        put("DetailTabViewWeaponTopsArRefetchQuery", "0d97601d58e0eba18ea83fcce9789e35e10413344ccda7f9bb83129a2d7949f4");
        put("DetailTabViewWeaponTopsClRefetchQuery", "42baca97f8038f51ffedc9bf837e820797d31c80cf4bac9b5b400fddb37ff3e1");
        put("DetailTabViewWeaponTopsGlRefetchQuery", "a5237b76a33b7ee3eb79a2fe83f297e0e1324a3bf42bea9182ea49a5396bb053");
        put("DetailTabViewWeaponTopsLfRefetchQuery", "2d23e55747f5365466b9563a89acb21851894b384fdbd33c80f8ee192b3d825b");
        put("DetailTabViewXRankingArRefetchQuery", "0dc7b908c6d7ad925157a7fa60915523dab4613e6902f8b3359ae96be1ba175f");
        put("DetailTabViewXRankingClRefetchQuery", "485e5decc718feeccf6dffddfe572455198fdd373c639d68744ee81507df1a48");
        put("DetailTabViewXRankingGlRefetchQuery", "6ab0299d827378d2cae1e608d349168cd4db21dd11164c542d405ed689c9f622");
        put("DetailTabViewXRankingLfRefetchQuery", "ca55206629f2c9fab38d74e49dda3c5452a83dd02a5a7612a2520a1fc77ae228");
        put("DetailVotingStatusQuery", "e2aafab18dab26ba1b6d40838c6842201f6480d62f8f3dffecad8dd4c5b102c1");
        put("DownloadSearchReplayQuery", "2805ee5182dd44c5114a1e6cfa57b2bcbbe9173c7e52069cc85a518de49c2191");
        put("EventBattleHistoriesQuery", "e47f9aac5599f75c842335ef0ab8f4c640e8bf2afe588a3b1d4b480ee79198ac");
        put("EventBattleHistoriesRefetchQuery", "a30281d08421b916902e4972f0d48d4d3346a92a68cbadcdb58b4e1a06273296");
        put("EventMatchRankingPeriodQuery", "ad4097d5fb900b01f12dffcb02228ef6c20ddbfba41f0158bb91e845335c708e");
        put("EventMatchRankingQuery", "875a827a6e460c3cd6b1921e6a0872d8b95a1fce6d52af79df67734c5cc8b527");
        put("EventMatchRankingRefetchQuery", "e9af725879a454fd3d5a191862ec3a544f552ae2d9bff6de6b212ac2676e8e14");
        put("EventMatchRankingSeasonRefetchQuery", "5b563e5fb86ff7e537cc1ed86485049a41a710ca79af9c38113d41dda1d54643");
        put("FestRecordQuery", "c8660a636e73dcbf55c12932bc301b1c9db2aa9a78939ff61bf77a0ea8ff0a88");
        put("FestRecordRefetchQuery", "87ed3300bdecdb51090398d43ee0957e69b7bd1370ac38d03f6c7cb160b4586a");
        put("FriendListQuery", "ea1297e9bb8e52404f52d89ac821e1d73b726ceef2fd9cc8d6b38ab253428fb3");
        put("FriendListRefetchQuery", "411b3fa70a9e0ff083d004b06cc6fad2638a1a24326cbd1fb111e7c72a529931");
        put("GesotownQuery", "d6f94d4c05a111957bcd65f8649d628b02bf32d81f26f1d5b56eaef438e55bab");
        put("GesotownRefetchQuery", "681841689c2d0f8d3355b71918d6c9aedf68484dfcb06b144407df1c4873dea0");
        put("HeroHistoryQuery", "71019ce4389463d9e2a71632e111eb453ca528f4f794aefd861dff23d9c18147");
        put("HeroHistoryRefetchQuery", "c6cb0b7cfd8721e90e3a85d3340d190c7f9c759b6b5e627900f5456fec61f6ff");
        put("HistoryRecordQuery", "0a62c0152f27c4218cf6c87523377521c2cff76a4ef0373f2da3300079bf0388");
        put("HistoryRecordRefetchQuery", "a5d80de05d1d4bfce67a1fb0801495d8bc6bba6fd780341cb90ddfeb1249c986");
        put("HomeQuery", "51fc56bbf006caf37728914aa8bc0e2c86a80cf195b4d4027d6822a3623098a8");
        put("JourneyChallengeDetailQuery", "ed634e52cd478ebc9d77d84831665aabfac14ac74bb343aa73c310539894169a");
        put("JourneyChallengeDetailRefetchQuery", "c7e4044cc4320e4ae44ccda1b7eb74897d213628c4e5d2f2863df5f8e8a9478d");
        put("JourneyQuery", "654ab98ebbb2057cb3e5d2c492cf03fd1acfba264108435f50b26295646db8ec");
        put("JourneyRefetchQuery", "10f2d2907537c27dd1e941bf74d2dd111a26acfd859d3ed8ef4735bef1928b11");
        put("LatestBattleHistoriesQuery", "b24d22fd6cb251c515c2b90044039698aa27bc1fab15801d83014d919cd45780");
        put("LatestBattleHistoriesRefetchQuery", "58bf17200ca97b55d37165d44902067b617d635e9c8e08e6721b97e9421a8b67");
        put("MyOutfitDetailQuery", "e2c9ea77f0469cb8109c54e93f3f35c930dfeb5b79cbf639397828a805ad9248");
        put("MyOutfitsQuery", "5b32bb88c47222522d2bc3643b92759644f890a70189a0884ea2d456a8989342");
        put("MyOutfitsRefetchQuery", "565bc1f16c0a5088d41b203775987c70756296747ba905c3e1c0ce8f3f27f925");
        put("PagerLatestVsDetailQuery", "73462e18d464acfdf7ac36bde08a1859aa2872a90ed0baed69c94864c20de046");
        put("PagerUpdateBattleHistoriesByVsModeQuery", "ac6561ff575363efcc9b876cf179929203dab17d3f25ab293a1123f4637e1dc7");
        put("PhotoAlbumQuery", "62383a0595fab69bf49a2a6877bc47acc081bfa065cb2eae28aa881980bb30b2");
        put("PhotoAlbumRefetchQuery", "0819c222d0b68fbcc7706f60b98e797da7d1fce637b45b3bdadca1ccdb692c86");
        put("PrivateBattleHistoriesQuery", "fef94f39b9eeac6b2fac4de43bc0442c16a9f2df95f4d367dd8a79d7c5ed5ce7");
        put("PrivateBattleHistoriesRefetchQuery", "3dd1b491b2b563e9dfc613e01f0b8e977e122d901bc17466743a82b7c0e6c33a");
        put("RankingHoldersFestTeamRankingHoldersPaginationQuery", "34460535ce2b699ed0617d67e22a7e3290fd30041559bf6f05d408d06f1c9938");
        put("RegularBattleHistoriesQuery", "2fe6ea7a2de1d6a888b7bd3dbeb6acc8e3246f055ca39b80c4531bbcd0727bba");
        put("RegularBattleHistoriesRefetchQuery", "e818519b50e877ac6aeaeaf19e0695356f28002ad4ccf77c1c4867ef0df9a6d7");
        put("ReplayModalReserveReplayDownloadMutation", "07e94ba8076b235d9b16c9e8d1714dfffbd4441c17225c36058b8a7ba44458b1");
        put("ReplayQuery", "3af48164d1176e8a88fb5321f5fb2daf9dde00b314170f1848a30e1825fc828e");
        put("ReplayUploadedReplayListRefetchQuery", "1e42b2238c385b5db29717b98d0df5934c75e8807545091d97200127ed1ecef0");
        put("SaleGearDetailOrderGesotownGearMutation", "bb716c3be6e85331741d7e2f9b36a6c0de92ca1b8382418744c1540fec7c8f57");
        put("SaleGearDetailQuery", "b42e70a6873aa716d089f2c5ea219083d30f0fff6ed15b8f5630c01ef7a32015");
        put("SettingQuery", "8473b5eb2c2048f74eb48b0d3e9779f44febcf3477479625b4dc23449940206b");
        put("StageRecordQuery", "c8b31c491355b4d889306a22bd9003ac68f8ce31b2d5345017cdd30a2c8056f3");
        put("StageRecordsRefetchQuery", "25dbf592793a590b6f8cfb0a62823aa02429b406a590333627d8ea703b190dfd");
        put("StageScheduleQuery", "d49fb6adffe15e3e43ca1167397debfc580eede3ad2232d7e32062bc5487e7eb");
        put("SupportButton_SupportChallengeMutation", "3165b76878d09ea55a7194e675397a5e030a2a89b98a0e81af77e346c625c4fd");
        put("UpdateMyOutfitMutation", "b83ed5a9b58252c088d3aac7f28a34a59acfbaa61b187ee3eebfe8506aa720f9");
        put("VotesUpdateFestVoteMutation", "b0830a3c3c9d8aa6ed83e200aed6b008f992acdba4550ab4399417c1f765e7e3");
        put("VsHistoryDetailPagerRefetchQuery", "973ca7012d8e94da97506cd39dfbb2a45eaae6e382607b650533d4f5077d840d");
        put("VsHistoryDetailQuery", "f893e1ddcfb8a4fd645fd75ced173f18b2750e5cfba41d2669b9814f6ceaec46");
        put("WeaponRecordQuery", "974fad8a1275b415c3386aa212b07eddc3f6582686e4fef286ec4043cdf17135");
        put("WeaponRecordsRefetchQuery", "7d7194a98cb7b0b235f15f98a622fab4945992fd268101e24443db82569dd25d");
        put("XBattleHistoriesQuery", "eb5996a12705c2e94813a62e05c0dc419aad2811b8d49d53e5732290105559cb");
        put("XBattleHistoriesRefetchQuery", "a175dc519f551c0bbeed04286194dc12b1a05e3117ab73f6743e5799e91f903a");
        put("è", "90932ee3357eadab30eb11e9d6b4fe52d6b35fde91b5c6fd92ba4d6159ea1cb7");
        put("XRankingDetailRefetchQuery", "00e8e962cc65795c6480d10caddaee7e0262d5cdf81e5958ff8f3359bd2f9743");
        put("XRankingQuery", "a5331ed228dbf2e904168efe166964e2be2b00460c578eee49fc0bc58b4b899c");
        put("XRankingRefetchQuery", "5a469004feb402a1d44a10820b647def2d4eb320436f6add4431194a34d0b497");
        put("myOutfitCommonDataEquipmentsQuery", "45a4c343d973864f7bb9e9efac404182be1d48cf2181619505e9b7cd3b56a6e8");
        put("myOutfitCommonDataFilteringConditionQuery", "ac20c44a952131cb0c9d00eda7bc1a84c1a99546f0f1fc170212d5a6bb51a426");
        put("refetchableCoopHistory_coopResultQuery", "bdb796803793ada1ee2ea28e2034a31f5c231448e80f5c992e94b021807f40f8");
        put("useCurrentFestQuery", "980af9d079ce2a6fa63893d2cd1917b70a229a222b24bbf8201f48d814ff48f0");
        put("useShareMyOutfitQuery", "5502b09121f5e18bec8fefbe80cce21e1641624b579c57c1992b30dcff612b44");}};

    /** contains all the supported abilities */
    public static final HashMap<String, String> translateGearAbilities = new HashMap<>(){{
        put("5c98cc37d2ce56291a7e430459dc9c44d53ca98b8426c5192f4a53e6dd6e4293", "ink_saver_main");
        put("11293d8fe7cfb82d55629c058a447f67968fc449fd52e7dd53f7f162fa4672e3", "ink_saver_sub");
        put("29b845ea895b931bfaf895e0161aeb47166cbf05f94f04601769c885d019073b", "ink_recovery_up");
        put("3b6c56c57a6d8024f9c7d6e259ffa2e2be4bdf958653b834e524ffcbf1e6808e", "run_speed_up");
        put("087ffffe40c28a40a39dc4a577c235f4cc375540c79dfa8ede1d8b63a063f261", "swim_speed_up");
        put("e8668a2af7259be74814a9e453528a3e9773435a34177617a45bbf79ad0feb17", "special_charge_up");
        put("e3154ab67494df2793b72eabf912104c21fbca71e540230597222e766756b3e4", "special_saver");
        put("fba267bd56f536253a6bcce1e919d8a48c2b793c1b554ac968af8d2068b22cab", "special_power_up");
        put("aaa9b7e95a61bfd869aaa9beb836c74f9b8d4e5d4186768a27d6e443c64f33ce", "quick_respawn");
        put("138820ed46d68bdf2d7a21fb3f74621d8fc8c2a7cb6abe8d7c1a3d7c465108a7", "quick_super_jump");
        put("9df9825e470e00727aa1009c4418cf0ace58e1e529dab9a7c1787309bb25f327", "sub_power_up");
        put("db36f7e89194ed642f53465abfa449669031a66d7538135c703d3f7d41f99c0d", "ink_resistance_up");
        put("664489b24e668ef1937bfc9a80a8cf9cf4927b1e16481fa48e7faee42122996d", "sub_resistance_up");
        put("1a0c78a1714c5abababd7ffcba258c723fefade1f92684aa5f0ff7784cc467d0", "intensify_action");
        put("85d97cd3d5890b80e020a554167e69b5acfa86e96d6e075b5776e6a8562d3d4a", "opening_gambit");
        put("d514787f65831c5121f68b8d96338412a0d261e39e522638488b24895e97eb88", "last_ditch_effort");
        put("aa5b599075c3c1d27eff696aeded9f1e1ddf7ae3d720268e520b260db5600d60", "tenacity");
        put("748c101d23261aee8404c573a947ffc7e116a8da588c7371c40c4f2af6a05a19", "comeback");
        put("2c0ef71abfb3efe0e67ab981fc9cd46efddcaf93e6e20da96980079f8509d05d", "ninja_squid");
        put("de15cad48e5f23d147449c70ee4e2973118959a1a115401561e90fc65b53311b", "haunt");
        put("56816a7181e663b5fedce6315eb0ad538e0aadc257b46a630fcfcc4a16155941", "thermal_ink");
        put("de0d92f7dfed6c76772653d6858e7b67dd1c83be31bd2324c7939105180f5b71", "respawn_punisher");
        put("0d6607b6334e1e84279e482c1b54659e31d30486ef0576156ee0974d8d569dbc", "ability_doubler");
        put("f9c21eacf6dbc1d06edbe498962f8ed766ab43cb1d63806f3731bf57411ae7b6", "stealth_jump");
        put("9d982dc1a7a8a427d74df0edcebcc13383c325c96e75af17b9cdb6f4e8dafb24", "object_shredder");
        put("18f03a68ee64da0a2e4e40d6fc19de2e9af3569bb6762551037fd22cf07b7d2d", "drop_roller");
        put("dc937b59892604f5a86ac96936cd7ff09e25f18ae6b758e8014a24c7fa039e91", null);
    }};


    /**
     * 	Given a URL, returns the gear ability string corresponding to the filename hash.
     * @param url
     */
    public static String translateGearAbility(String url) {
        for (String entry : translateGearAbilities.keySet()) {
            if (url.contains(entry)) {
                return translateGearAbilities.get(entry);
            }
        }
        return null;
    }

    /**
     * Returns the term to be used when referring to the type of results in question.
     * @param wich
     * @return
     */
    public static String setNoun(String wich) {
        if (wich.equals("both")) {
            return "battles/jobs";
        } else if (wich.equals("salmon")) {
            return "jobs";
        } else { // "ink"
            return "battles";
        }
    }

    /**
     * Given a dict of numbers from 0.0 - 1.0, converts these into a RGBA hex color format (without the leading #).
     * @param rgbadict
     * @return
     */
    public static String convertColor(HashMap rgbadict) {
        String r = Integer.toHexString((int) (255 * (double) rgbadict.get("r")));
        String g = Integer.toHexString((int) (255 * (double) rgbadict.get("g")));
        String b = Integer.toHexString((int) (255 * (double) rgbadict.get("b")));
        String a = Integer.toHexString((int) (255 * (double) rgbadict.get("a")));
        return String.format("%02x%02x%02x%02x", r, g, b, a);
    }

    /**
     * Given a SplatNet 3 Tricolor Turf War team role, convert it to the stat.ink string format.
     * @param value
     * @return
     */
    public static String convertTricolorRole(String value) {
        return value.equals("DEFENSE")
                ? "defender"
                : "attacker"; // ATTACK1 or ATTACK2
    }

    /**
     * Base64-decodes a string and cuts off the SplatNet prefix.
     * @param value
     * @return string or int
     */
    public static Object b64d (String value) {
        byte[] decodedBytes = Base64.getDecoder().decode(value);
        String thing_id = new String(decodedBytes, StandardCharsets.UTF_8);

        thing_id = thing_id.replace("VsStage-", "");
        thing_id = thing_id.replace("VsMode-", "");
        thing_id = thing_id.replace("CoopStage-", "");
        thing_id = thing_id.replace("CoopGrade-", "");
        thing_id = thing_id.replace("CoopEnemy-", "");
        thing_id = thing_id.replace("CoopEventWave-", "");
        thing_id = thing_id.replace("CoopUniform-", "");
        thing_id = thing_id.replace("SpecialWeapon-", "");

        if (thing_id.contains("Weapon-")) {
            thing_id = thing_id.replace("Weapon-", "");
            if (thing_id.length() == 5
                && thing_id.charAt(0) == '2'
                && thing_id.startsWith("900", 2)) { // grizzco weapon ID from a hacker
                return "";
            }
        }

        if (thing_id.startsWith("VsHistoryDetail")
            || thing_id.startsWith("CoopHistoryDetail")
            || thing_id.startsWith("VsPlayer")) {
            return thing_id;
        } else {
            return Integer.parseInt(thing_id);
        }

    }


    /**
     * Converts a playedTime string into an integer representing the epoch time.
     * @param time_string
     * @return
     */
    public static long epochTime(String time_string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(time_string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long epoch_time = date.getTime() / 1000;

        return (long) epoch_time;
    }

    /**
     * Generates a JSON dictionary, specifying information to retrieve, to send with GraphQL requests.
     * @param sha256hash
     * @param varname optional
     * @param varvalue optional
     * @return
     */
    public static String genGraphqlBody(String sha256hash, String varname, String varvalue) {

        HashMap<String, HashMap> great_passage = new HashMap<>();

        HashMap<String, HashMap> extensions = new HashMap<>();

        HashMap<String, Object> persistedQuery = new HashMap<>();

        persistedQuery.put("sha256Hash", sha256hash);

        persistedQuery.put("version", 1);

        extensions.put("persistedQuery", persistedQuery);

        great_passage.put("extensions", extensions);

        HashMap<String, Object> variables = new HashMap<>();

        if (varname != null && varvalue != null) {
            variables.put(varname, varvalue);
        }

        great_passage.put("variables", variables);

        return new Gson().toJson(great_passage);

    }

    /**
     * Checks if a given custom key exists in config.txt and is set to the specified value (true by default).
     * @param key
     * @param configData
     * @param value
     * @return
     */
    public static boolean customKeyExists(String key, HashMap configData, boolean value) {

        // https://github.com/frozenpandaman/s3s/wiki/config-keys
        if (!SUPPORTED_KEYS.contains(key)) {
            System.out.println("Unsupported custom key: " + key);
        }

        String keyValue = (String) configData.get(key);

        if (keyValue == null) {
            return false;
        }
        keyValue = keyValue.toLowerCase();

        return keyValue.equals(String.valueOf(value).toLowerCase());
    }

}
