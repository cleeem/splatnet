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

public class Brand {

    private static final String BRANDS_URL = "assets/brands/";

    private String id;

    private String name;

    private File image;


    private static ArrayList<Brand> brands = new ArrayList<>();
    public static Brand getBrand(String id) {
        for (Brand brand : brands) {
            if (brand.id.equals(id)) {
                return brand;
            }
        }
        return null;
    }

    public static void addBrand(Brand brand) {
        brands.add(brand);
    }

    public Brand(String id, String name, File image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Brand(JsonObject data) {
        this.id = data.get("id").getAsString();
        this.name = data.get("name").getAsString();

        // Chemin relatif à la racine du JAR
        String imagePath = BRANDS_URL + this.id + ".png";
        // Obtenez un flux d'entrée vers la ressource
        InputStream inputStream = Main.class.getResourceAsStream(imagePath);

        if (inputStream == null) {
            try {
                // Si la ressource n'est pas trouvée, téléchargez-la
                UtilitaryS3S.downloadSmallImage(data.get("image").getAsJsonObject().get("url").getAsString(), this.id + "", "abilities");
                inputStream = Main.class.getResourceAsStream(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Si la ressource est trouvée, créez un fichier temporaire pour la stocker localement
        try {
            this.image = File.createTempFile("" + this.id, ".png");
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File getImage() {
        return image;
    }

    public static ArrayList<Brand> getBrands() {
        return brands;
    }

}
