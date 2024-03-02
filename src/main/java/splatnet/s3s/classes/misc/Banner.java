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

    private ImageView image;

    private HashMap<String, Integer> colors;

    private static TreeSet<Banner> badges = new TreeSet<>();

    public static Banner find(String id) {
        for (Banner badge : badges) {
            if (badge.getId().equals(id)) {
                return badge;
            }
        }
        return null;
    }

    public Banner(String id, ImageView image) {
        this.id = id;
        this.image = image;
    }

    public Banner(JsonObject data) {
        this.id = data.get("id").getAsString();
        this.imageURL = data.getAsJsonObject("image").get("url").getAsString();
        this.colors = new HashMap<>();

        JsonObject colors = data.getAsJsonObject("textColor");
        this.colors.put("r", colors.get("r").getAsInt());
        this.colors.put("g", colors.get("g").getAsInt());
        this.colors.put("b", colors.get("b").getAsInt());

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

        this.image = new ImageView(new Image(String.valueOf(Main.class.getResource("assets/banners/" + this.id + ".png"))));

    }

    public String getId() {
        return id;
    }

    public ImageView getImage() {
        return new ImageView(image.getImage());
    }

    public HashMap<String, Integer> getColors() {
        return colors;
    }

    @Override
    public int compareTo(Banner o) {
        return this.id.compareTo(o.id);
    }
}
