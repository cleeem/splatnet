package splatnet.s3s.classes.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.util.TreeSet;

public class Stuff implements Comparable<Stuff> {

    private String type;

    private String name;

    private int id;

    private String imageURL;

    private String imagePath;

    private Brand brand;

    private Ability mainAbility;

    private Ability[] subAbilities;

    private int rarity;

    private int exp;

    private static TreeSet<Stuff> stuffs = new TreeSet<>();

    public static void addStuff(Stuff stuff) {
        stuffs.add(stuff);
    }

    public static Stuff getStuffById(int id) {
        for (Stuff stuff : stuffs) {
            if (stuff.getId() == id) {
                return stuff;
            }
        }
        return null;
    }

    public Stuff(String type, String name, int id, String imageURL, Brand brand, Ability mainAbility, Ability[] subAbilities, int rarity, int exp) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.imageURL = imageURL;
        this.brand = brand;
        this.mainAbility = mainAbility;
        this.subAbilities = subAbilities;
        this.rarity = rarity;
        this.exp = exp;
    }

    public Stuff(JsonObject data) {
        this.type = data.get("__isGear").getAsString();
        this.name = data.get("name").getAsString()
                .replace("\\", "")
                .replace("/", "")
                .replace("%20", "")
                .replace(" ", "_");

        String idKey;
        if (this.type.equals("HeadGear")) {
            idKey = "headGearId";
        } else if (this.type.equals("ClothingGear")) {
            idKey = "clothingGearId";
        } else {
            idKey = "shoesGearId";
        }

        if (data.get(idKey) != null) {
            this.id = data.get(idKey).getAsInt();
        } else {
            this.id = -1;
        }

        if (data.get("image") != null) {
            this.imageURL = data.getAsJsonObject("image").get("url").getAsString();
        } else if (data.get("thumbnailImage") != null) {
            this.imageURL = data.getAsJsonObject("thumbnailImage").get("url").getAsString();
        } else {
            this.imageURL = null;
        }
        if (data.get("brand") != null) {
            this.brand = Brand.getBrand(
                    data.getAsJsonObject("brand").get("id").getAsString()
            );
        } else {
            this.brand = null;
        }

        this.mainAbility = Ability.getAbilityByName(
                data.getAsJsonObject("primaryGearPower").get("name").getAsString().replace(" ", "_")
        );

        JsonArray subAbilitiesData = data.getAsJsonArray("additionalGearPowers");
        this.subAbilities = new Ability[3];

        for (int i = 0; i < subAbilitiesData.size(); i++) {
            this.subAbilities[i] = Ability.getAbilityByName(
                    subAbilitiesData.get(i).getAsJsonObject().get("name").getAsString().replace(" ", "_")
            );
        }

        for (int i = subAbilitiesData.size(); i < 3; i++) {
            this.subAbilities[i] = Ability.getAbilityByName("Unknown");
        }

        String type = this.type.equals("HeadGear") ? "head" : this.type.equals("ClothingGear") ? "clothes" : "shoes";

        String path = String.valueOf(Main.class.getResource("assets/gears/" + type + "/" + this.name + ".png"));
        if (path.equals("null")) {
            try {
                UtilitaryS3S.downloadImage(this.imageURL, this.name, "gears/" + type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.imagePath = path;

        if (data.get("rarity") != null) {
            this.rarity = data.get("rarity").getAsInt();
        } else {
            this.rarity = 0;
        }
        if (data.getAsJsonObject("stats") != null
            && data.getAsJsonObject("stats").get("exp") != null) {
            this.exp = data.getAsJsonObject("stats").get("exp").getAsInt();
        } else {
            this.exp = 0;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImageURL() {
        return imageURL;
    }

    public ImageView getImage() {
        return new ImageView(imagePath);
    }

    public Brand getBrand() {
        return brand;
    }

    public Ability getMainAbility() {
        return mainAbility;
    }

    public Ability[] getSubAbilities() {
        return subAbilities;
    }

    public int getRarity() {
        return rarity;
    }

    public int getExp() {
        return exp;
    }

    @Override
    public int compareTo(Stuff o) {
        return this.id - o.id;
    }
}
