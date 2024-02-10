package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.util.ArrayList;

public class Ability {
    private String name;
    private int id;
    private File image;

    private static ArrayList<Ability> abilities = new ArrayList<>();

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
        return null;
    }

    public static void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public Ability(String name, int id, File image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    public Ability(JsonObject data) {
        this.name = data.get("name").getAsString();
        this.id = data.get("gearPowerId").getAsInt();
        this.image = new File("src/main/resources/splatnet/assets/abilities/" + this.id + ".png");
        if (!this.image.exists()) {
            try {
                UtilitaryS3S.downloadSmallImage(data.get("image").getAsJsonObject().get("url").getAsString(), this.id + "", "abilities");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File getImage() {
        return image;
    }
}
