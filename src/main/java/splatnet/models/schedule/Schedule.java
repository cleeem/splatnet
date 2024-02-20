package splatnet.models.schedule;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import splatnet.s3s.UtilitaryS3S;
import splatnet.s3s.classes.game.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Schedule {

    private static String URL_JSON_FILE = "https://splatoon3.ink/data/schedules.json";

    private String[] modes = {
            "regularSchedules",
            "bankaraSchedules",
            "xSchedules",
    };

    private ArrayList<Rotation> turfWarRotations;
    private ArrayList<Rotation> bankaraRotations;
    private ArrayList<Rotation> xRotations;

    public Schedule() {
        this.turfWarRotations = new ArrayList<>();
        this.bankaraRotations = new ArrayList<>();
        this.xRotations = new ArrayList<>();

        // we read the json file
        URL url = null;
        try {
            url = new URL(URL_JSON_FILE);
            InputStream is = url.openStream();
            String json = new String(is.readAllBytes());

            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            JsonArray nodes;
            JsonObject data;
            Rotation rotation;
            String typeKey;
            for (String mode : modes) {
                 nodes = jsonObject.getAsJsonObject("data")
                        .getAsJsonObject(mode)
                        .getAsJsonArray("nodes");

                for (JsonElement element : nodes) {
                    data = element.getAsJsonObject();
                    typeKey = getTypeKey(mode);
                    rotation = new Rotation(data, typeKey);
                    switch (mode) {
                        case "regularSchedules":
                            this.turfWarRotations.add(rotation);
                            break;
                        case "bankaraSchedules":
                            this.bankaraRotations.add(rotation);
                            break;
                        case "xSchedules":
                            this.xRotations.add(rotation);
                            break;
                    }
                }
            }


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getTypeKey(String mode) {
        switch (mode) {
            case "regularSchedules":
                return "regularMatchSetting";
            case "bankaraSchedules":
                return "bankaraMatchSettings";
            case "xSchedules":
                return "xMatchSetting";
            default:
                return null;
        }
    }

    public ArrayList<Rotation> getTurfWarRotations() {
        return turfWarRotations;
    }

    public ArrayList<Rotation> getBankaraRotations() {
        return bankaraRotations;
    }

    public ArrayList<Rotation> getxRotations() {
        return xRotations;
    }

//    public static void main(String[] args) {
//        InputStream is = null;
//        try {
//            URL url = new URL(URL_JSON_FILE);
//            is = url.openStream();
//            String json = new String(is.readAllBytes());
//
//            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
//
//            JsonObject data = jsonObject.getAsJsonObject("data");
//            JsonObject stages = data.getAsJsonObject("vsStages");
//            JsonArray nodes = stages.getAsJsonArray("nodes");
//
//            for (JsonElement node : nodes) {
//                JsonObject stage = node.getAsJsonObject();
//
//                String id = stage.get("id").getAsString();
//                String urlImage = stage.get("originalImage").getAsJsonObject().get("url").getAsString();
//                String type = "maps";
//                try {
//                    UtilitaryS3S.downloadImage(urlImage, id+"Small", type);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
