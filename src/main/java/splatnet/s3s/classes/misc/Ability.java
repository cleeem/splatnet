package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Ability {

    private static final String ABILITIES_URL = "assets/abilities/";

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

        // Chemin relatif à la racine du JAR
        String imagePath = ABILITIES_URL + this.id + ".png";
        // Obtenez un flux d'entrée vers la ressource
        InputStream inputStream = Main.class.getResourceAsStream(imagePath);

        if (inputStream == null) {
            try {
                // Si la ressource n'est pas trouvée, téléchargez-la
                UtilitaryS3S.downloadSmallImage(data.get("image").getAsJsonObject().get("url").getAsString(), this.name, "abilities");
                inputStream = Main.class.getResourceAsStream(imagePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Si la ressource est trouvée, créez un fichier temporaire pour la stocker localement
        try {
            this.image = File.createTempFile(this.name, ".png");
            // Copiez les données du flux d'entrée vers le fichier temporaire
            Files.copy(inputStream, this.image.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
