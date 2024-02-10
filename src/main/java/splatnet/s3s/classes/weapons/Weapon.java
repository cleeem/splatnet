package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;

public class Weapon {

    private String id;

    private String name;

    private File image;

    private String type;

    private String url;

    public Weapon() {}
    public Weapon(JsonObject weapon, String type) {
        this.id = weapon.get("id").getAsString();
        this.name = weapon.get("name").getAsString();
        this.type = type;
        this.image = new File("src/main/resources/splatnet/assets/" + this.type + "/" + this.id + ".png");
        this.url = weapon.get("image").getAsJsonObject().get("url").getAsString();

        if (!this.image.exists()) {
            try {
                UtilitaryS3S.downloadSmallImage(this.url, this.id, this.type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}

    public Weapon(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = new File("src/main/resources/images/"+ this.type + "/" + this.id + ".png");

        if (!this.image.exists()) {
            try {
                UtilitaryS3S.downloadSmallImage("https://app.splatoon2.nintendo.net" + this.image, this.id + "", this.type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File getImage() {
        return image;
    }

    public String getType() {
        return type;
    }
}
