package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Ability implements Comparable<Ability> {
    private String name;
    private int id;
    private String imagePath;

    private static TreeSet<Ability> abilities = new TreeSet<>();

    public static Ability getAbilityById(int id) {
        for (Ability ability : abilities) {
            if (ability.getId() == id) {
                return ability;
            }
        }
        return null;
    }

    public static Ability getAbilityByName(String name) {
        for (Ability ability : abilities) {
            if (ability.getName().equals(name)) {
                return ability;
            }
        }
        System.out.println("Ability not found: " + name);
        return null;
    }

    public static void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public Ability(String name, int id, String path) {
        this.name = name;
        this.id = id;
        this.imagePath = path;
    }

    public Ability(JsonObject data) {
        this.name = data.get("name").getAsString()
                .replace("%20", " ")
                .replace(" ", "_");
        this.id = data.get("gearPowerId").getAsInt();

        String path = String.valueOf(Main.class.getResource("assets/abilities/" + this.name + ".png"));

        if (path.equals("null")) {
            try {
                UtilitaryS3S.downloadImage(
                        data.get("image").getAsJsonObject().get("url").getAsString(),
                        this.name,
                        "abilities"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.imagePath = path;
    }

    public static TreeSet<Ability> getAbilities() {
        return abilities;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ImageView getImage() {
        return new ImageView(imagePath);
    }

    @Override
    public int compareTo(Ability o) {
        return getId() - o.getId();
    }
}
