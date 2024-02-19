package splatnet.s3s.classes.game;

import com.google.gson.JsonObject;
import javafx.scene.image.ImageView;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Stage implements Comparable<Stage> {

    private String id;

    private String name;

    private String imageURL;

    private ImageView image;

    private static TreeSet<Stage> stages = new TreeSet<>();

    public static void addStage(Stage stage) {
        stages.add(stage);
    }

    public static Stage findStage(String id) {
        for (Stage stage : stages) {
            if (stage.getId().equals(id)) {
                return stage;
            }
        }
        System.out.println("Stage not found: " + id);
        return null;
    }

    public Stage(String id, String name, String imageURL) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
    }

    public Stage(JsonObject data) {
        this.id = data.get("id").getAsString();
        this.name = data.get("name").getAsString();
        if (data.has("image") && !data.get("image").isJsonNull()) {
            this.imageURL = data.getAsJsonObject("image").get("url").getAsString();

        } else if (data.has("originalImage") && !data.get("originalImage").isJsonNull()) {
            this.imageURL = data.getAsJsonObject("originalImage").get("url").getAsString();

        } else {
            this.imageURL = null;

        }

        String path = String.valueOf(Main.class.getResource("assets/maps/" + this.id + ".png"));
        if (path.equals("null")) {
            try {
                UtilitaryS3S.downloadImage(this.imageURL, this.id, "maps");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.image = new ImageView(String.valueOf(Main.class.getResource("assets/maps/" + this.id + ".png")));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public ImageView getImage() {
        return new ImageView(image.getImage());
    }

    @Override
    public int compareTo(Stage o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        return getName();
    }
}
