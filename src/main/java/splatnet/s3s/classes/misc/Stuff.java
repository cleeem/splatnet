package splatnet.s3s.classes.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Stuff {

    private static final String GEAR_URL = "assets/gears/";

    private String type;

    private String name;

    private int id;

    private String imageURL;

    private File image;

    private Brand brand;

    private Ability mainAbility;

    private Ability[] subAbilities;

    private int rarity;

    private int exp;

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
        this.name = data.get("name").getAsString().replace("\\", "").replace("/", "");

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
        } else if (data.get("thumbnailImage") != null){
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
                data.getAsJsonObject("primaryGearPower").get("name").getAsString()
        );

        JsonArray subAbilitiesData = data.getAsJsonArray("additionalGearPowers");
        this.subAbilities = new Ability[3];

        for (int i = 0; i < subAbilitiesData.size(); i++) {
            this.subAbilities[i] = Ability.getAbilityByName(
                    subAbilitiesData.get(i).getAsJsonObject().get("name").getAsString()
            );
        }

        for (int i = subAbilitiesData.size(); i < 3; i++) {
            this.subAbilities[i] = Ability.getAbilityByName("Unknown");
        }

        String type = this.type.equals("HeadGear") ? "head" : this.type.equals("ClothingGear") ? "clothes" : "shoes";
        // Chemin relatif à la racine du JAR
        String imagePath = GEAR_URL + type + "/" + this.name + ".png";
        // Obtenez un flux d'entrée vers la ressource
        InputStream inputStream = Main.class.getResourceAsStream(imagePath);

        if (inputStream == null) {
            try {
                // Si la ressource n'est pas trouvée, téléchargez-la
                UtilitaryS3S.downloadSmallImage(this.imageURL, this.name, "gears/" + type);
                inputStream = Main.class.getResourceAsStream(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Si la ressource est trouvée, créez un fichier temporaire pour la stocker localement
        try {
            this.image = File.createTempFile(this.name, ".png");
            // Copiez les données du flux d'entrée vers le fichier temporaire
            Files.copy(inputStream, this.image.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Fermez le flux d'entrée
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

    public File getImage() {
        return image;
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

}
