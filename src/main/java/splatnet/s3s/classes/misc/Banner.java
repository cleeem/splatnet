package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

public class Banner implements Comparable<Banner> {

    private String id;

    private String imageURL;

    private String imagePath;

    private HashMap<String, Double> colors;

    private static TreeSet<Banner> badges = new TreeSet<>();

    public static Banner find(String id) {
        for (Banner badge : badges) {
            if (badge.getId().equals(id)) {
                return badge;
            }
        }
        return null;
    }

    public Banner(String id, String path) {
        this.id = id;
        this.imagePath = path;
    }

    public Banner(JsonObject data) {
        this.id = data.get("id").getAsString();
        this.imageURL = data.getAsJsonObject("image").get("url").getAsString();
        this.colors = new HashMap<>();

        JsonObject colors = data.getAsJsonObject("textColor");
        this.colors.put("r", colors.get("r").getAsDouble());
        this.colors.put("g", colors.get("g").getAsDouble());
        this.colors.put("b", colors.get("b").getAsDouble());

        String path = String.valueOf(Main.class.getResource("assets/banners/" + this.id + ".png"));
        if (path.equals("null")) {
            try {
                UtilitaryS3S.downloadImage(
                        this.imageURL,
                        this.id,
                        "banners"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        this.imagePath = path;
    }

    public String getId() {
        return id;
    }

    public ImageView getImage() {
        return new ImageView(imagePath);
    }

    public HashMap<String, Double> getColors() {
        return colors;
    }

    @Override
    public int compareTo(Banner o) {
        return this.id.compareTo(o.id);
    }
}
