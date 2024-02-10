package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import splatnet.s3s.UtilitaryS3S;

import java.io.File;
import java.util.ArrayList;

public class Brand {
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
        this.image = new File("src/main/java/splatnet/s3s/data/brands/" + this.id + ".png");
        if (!this.image.exists()) {
            try {
                UtilitaryS3S.downloadSmallImage(data.get("image").getAsJsonObject().get("url").getAsString(), this.id + "", "brands");
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

    public static ArrayList<Brand> getBrands() {
        return brands;
    }

}
