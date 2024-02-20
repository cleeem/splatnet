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

    private ImageView smallImage;

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

        try {
            this.smallImage = new ImageView(String.valueOf(Main.class.getResource("assets/maps/" + this.id + "Small.png")));
        } catch (Exception e) {
            // If the small image doesn't exist, we'll just use the normal image
        }
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

    public ImageView getSmallImage() {
        return new ImageView(smallImage.getImage());
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
