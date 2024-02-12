package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Weapon {

    private static final String WEAPONS_URL = "assets/";

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
        this.url = weapon.get("image").getAsJsonObject().get("url").getAsString();

        // Chemin relatif à la racine du JAR
        String imagePath = WEAPONS_URL + this.type + "/" + this.id + ".png";
        // Obtenez un flux d'entrée vers la ressource
        InputStream inputStream = Main.class.getResourceAsStream(imagePath);

        if (inputStream == null) {
            try {
                // Si la ressource n'est pas trouvée, téléchargez-la
                UtilitaryS3S.downloadSmallImage(this.url, this.id, this.type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Si la ressource est trouvée, créez un fichier temporaire pour la stocker localement
        try {
            this.image = File.createTempFile(this.id, ".png");
            // Copiez les données du flux d'entrée vers le fichier temporaire
            Files.copy(inputStream, this.image.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Fermez le flux d'entrée
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Weapon(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = new File(WEAPONS_URL + this.type + "/" + this.id + ".png");

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
