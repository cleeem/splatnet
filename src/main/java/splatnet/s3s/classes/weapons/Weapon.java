package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;
import javafx.scene.image.ImageView;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.awt.*;
import java.io.File;

public class Weapon {

    private String id;

    private String name;

    private String imagePath;

    private String type;

    private String url;

    public Weapon() {}
    public Weapon(JsonObject weapon, String type) {
        this.id = weapon.get("id").getAsString();
        this.name = weapon.get("name").getAsString();
        this.type = type;
        this.url = weapon.get("image").getAsJsonObject().get("url").getAsString();

        String path = String.valueOf(Main.class.getResource("assets/" + this.type + "/" + this.id + ".png"));

        if (path.equals("null")) {
            try {
                UtilitaryS3S.downloadImage(
                        this.url,
                        this.id,
                        this.type
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.imagePath = path;
    }

    public Weapon(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;

        String path = String.valueOf(Main.class.getResource("assets/" + this.type + "/" + this.id + ".png"));
        if (path.equals("null")) {
            try {
                UtilitaryS3S.downloadImage(
                        this.url,
                        this.id,
                        this.type
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.imagePath = path;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ImageView getImage() {
        return new ImageView(imagePath);
    }

    public String getType() {
        return type;
    }
}
