package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import javafx.scene.image.ImageView;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

public class Banner implements Comparable<Banner> {

    private String id;

    private String imageURL;

    private ImageView image;

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

    }

    public String getId() {
        return id;
    }

    public ImageView getImage() {
        return new ImageView(image.getImage());
    }

    @Override
    public int compareTo(Banner o) {
        return this.id.compareTo(o.id);
    }
}
