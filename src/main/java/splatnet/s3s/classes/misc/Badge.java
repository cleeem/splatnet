package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import javafx.scene.image.ImageView;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

public class Badge implements Comparable<Badge> {

    private String id;

    private String imageURL;

    private ImageView image;

    private static TreeSet<Badge> badges = new TreeSet<>();
    public static void addBadge(Badge badge) {
        badges.add(badge);
    }

    public static Badge findBadge(String id) {
        for (Badge badge : badges) {
            if (badge.getId().equals(id)) {
                return badge;
            }
        }
        return null;
    }

    public Badge(String id, ImageView image) {
        this.id = id;
        this.image = image;
    }

    public Badge(JsonObject data) {
        this.id = data.get("id").getAsString();
        this.imageURL = data.getAsJsonObject("image").get("url").getAsString();

        String path = String.valueOf(Main.class.getResource("assets/badges/" + this.id + ".png"));
        if (path.equals("null")) {
            try {
                UtilitaryS3S.downloadImage(
                        this.imageURL,
                        this.id,
                        "badges"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

        this.image = new ImageView(String.valueOf(Main.class.getResource("assets/badges/" + this.id + ".png")));
    }

    public String getId() {
        return id;
    }

    public ImageView getImage() {
        return new ImageView(image.getImage());
    }

    @Override
    public int compareTo(Badge o) {
        return getId().compareTo(o.getId());
    }
}
